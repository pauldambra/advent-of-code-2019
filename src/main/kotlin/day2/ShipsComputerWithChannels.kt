package day2

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

class ShipsComputerWithChannels(
    program: MutableList<Long>,
    val inputs: ReceiveChannel<Long>,
    val outputs: SendChannel<Long>,
    val halts: SendChannel<Boolean>,
    val name: String = "unnamed"
) {
    companion object {
        fun parseProgram(s: String) =
            s.split(",").map { it.toLong() }.toMutableList()
    }

    var relativeBase: Long = 0
        private set

    val memory = program.map { it }.toMutableList() // copy the list?
    private var addressPointer: Long = 0

    var halted = false

    suspend fun run() {
//        println("$name: started")

        while (!halted) {
            val oci: OpCodeInstruction = readOpCode(addressPointer)
            var newPointer: Long? = null

//            println("$name: running with $oci at $addressPointer")
            when (oci.opCode) {
                1 -> processInstruction(oci, addressPointer, Long::plus)
                2 -> processInstruction(oci, addressPointer, Long::times)
                3 -> {
//                    println("waiting for input")
                    val input = inputs.receive() //blocks waiting for input?
                    writeFromInput(oci, addressPointer, input)
//                    println("$name: read $input from input channel")
                }
                4 -> {
                    val output = readFirstParameter(oci, addressPointer)
//                    println("output $output ready")
                    outputs.send(output)
//                    println("$name: sent $output to output channel")
                }
                5 -> {
                    newPointer = jumpIfTrue(oci, addressPointer)
                }
                6 -> {
                    newPointer = jumpIfFalse(oci, addressPointer)
                }
                7 -> testForLessThan(oci, addressPointer)
                8 -> testEquality(oci, addressPointer)
                9 -> alterRelativeBase(oci, addressPointer)
                99 -> {
                    halted = true
                }
            }

            if (newPointer != null) {
                addressPointer = newPointer
            } else {
                addressPointer += determinePointerJump(oci)
            }
        }

        println("$name: halting")
        inputs.cancel()
        outputs.close()
        halts.send(true)
    }

    private fun determinePointerJump(oci: OpCodeInstruction): Int {
        return when (oci.opCode) {
            3 -> 2
            4 -> 2
            5 -> 3
            6 -> 3
            9 -> 2
            else -> 4
        }
    }

    private fun alterRelativeBase(oci: OpCodeInstruction, addressPointer: Long) {
        val relativeBaseAdjustment = readFirstParameter(oci, addressPointer)
        relativeBase += relativeBaseAdjustment
//        println("$name: relative base is now $relativeBase")
    }

    private fun jumpIfTrue(oci: OpCodeInstruction, addressPointer: Long): Long? {
        val shouldSetPointer = readFirstParameter(oci, addressPointer) != 0L
        return when {
            shouldSetPointer -> readSecondParameter(oci, addressPointer)
            else -> null
        }
    }

    private fun jumpIfFalse(oci: OpCodeInstruction, addressPointer: Long): Long? {
        val shouldSetPointer = readFirstParameter(oci, addressPointer) == 0L
        return when {
            shouldSetPointer -> readSecondParameter(oci, addressPointer)
            else -> null
        }
    }

    private fun testForLessThan(oci: OpCodeInstruction, addressPointer: Long) {
        val targetAddress = readWriteTargetThree(oci, addressPointer)
        val a = readFirstParameter(oci, addressPointer)
        val b = readSecondParameter(oci, addressPointer)
        writeTo(targetAddress, if (a < b) 1 else 0)
    }

    private fun testEquality(oci: OpCodeInstruction, addressPointer: Long) {
        val targetAddress = readWriteTargetThree(oci, addressPointer)
        val a = readFirstParameter(oci, addressPointer)
        val b = readSecondParameter(oci, addressPointer)
        writeTo(targetAddress, if (a == b) 1 else 0)
    }

    /**
     * day 5 instructions said parameters that write will never be in IMMEDIATE mode
     */
    private fun writeFromInput(oci: OpCodeInstruction, addressPointer: Long, input: Long) {
        val targetAddress = readWriteTargetOne(oci, addressPointer)
        writeTo(targetAddress, input)
    }

    private fun readWriteTargetOne(oci: OpCodeInstruction, addressPointer: Long) =
        when (oci.firstParameterMode) {
            OpCodeInstruction.POSITIONAL -> memory[(addressPointer + 1).toInt()]
            OpCodeInstruction.IMMEDIATE -> throw Exception("write parameters should never be in immediate mode but got $oci at $addressPointer")
            OpCodeInstruction.RELATIVE -> (memory[(addressPointer + 1).toInt()] + relativeBase)
            else -> throw Exception("bad OpCodeInstruction for first parameter $oci")
        }

    private fun readWriteTargetThree(oci: OpCodeInstruction, addressPointer: Long) =
        when (oci.thirdParameterMode) {
            OpCodeInstruction.POSITIONAL -> memory[(addressPointer + 3).toInt()]
            OpCodeInstruction.IMMEDIATE -> throw Exception("write parameters should never be in immediate mode but got $oci at $addressPointer")
            OpCodeInstruction.RELATIVE -> (memory[(addressPointer + 3).toInt()] + relativeBase)
            else -> throw Exception("bad OpCodeInstruction for first parameter $oci")
        }

    private fun writeTo(targetAddress: Long, input: Long) {
        padMemory(targetAddress)
        memory[targetAddress.toInt()] = input
    }

    private fun processInstruction(oci: OpCodeInstruction, addressPointer: Long, operation: (Long, Long) -> Long) {
        padMemory(memory[(addressPointer + 3).toInt()])
        val firstParameter = readFirstParameter(oci, addressPointer)
        val secondParameter = readSecondParameter(oci, addressPointer)
        val targetAddress = readWriteTargetThree(oci, addressPointer)
        writeTo(targetAddress, operation(firstParameter, secondParameter))
    }

    private fun readFirstParameter(oci: OpCodeInstruction, addressPointer: Long) =
        try {
            when (oci.firstParameterMode) {
                OpCodeInstruction.POSITIONAL -> memory[memory[(addressPointer + 1).toInt()].toInt()]
                OpCodeInstruction.IMMEDIATE -> memory[(addressPointer + 1).toInt()]
                OpCodeInstruction.RELATIVE -> memory[(memory[(addressPointer + 1).toInt()] + relativeBase).toInt()]
                else -> throw Exception("bad OpCodeInstruction for first parameter $oci")
            }
        } catch (e: IndexOutOfBoundsException) {
            0L
        }

    private fun readSecondParameter(oci: OpCodeInstruction, addressPointer: Long) =
        try {
            when (oci.secondParameterMode) {
                OpCodeInstruction.POSITIONAL -> memory[memory[(addressPointer + 2).toInt()].toInt()]
                OpCodeInstruction.IMMEDIATE -> memory[(addressPointer + 2).toInt()]
                OpCodeInstruction.RELATIVE -> memory[(memory[(addressPointer + 2).toInt()] + relativeBase).toInt()]
                else -> throw Exception("bad OpCodeInstruction for second parameter $oci")
            }
        } catch (e: IndexOutOfBoundsException) {
            0L
        }

    private fun padMemory(pointer: Long) {
        if (pointer >= memory.size) {
            val toPad = pointer - memory.size + 1
            check(toPad <= Int.MAX_VALUE) { "value toPad must be an int but was $toPad" }
            val toAdd = arrayOfNulls<Long>(toPad.toInt()).map { 0L }.toMutableList()
            memory.plusAssign(toAdd)
        }
    }

    private fun readOpCode(pointer: Long): OpCodeInstruction =
        OpCodeInstruction(memory[pointer.toInt()])
}
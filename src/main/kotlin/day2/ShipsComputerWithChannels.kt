package day2

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel

class ShipsComputerWithChannels(
    program: MutableList<Int>,
    val inputs: ReceiveChannel<Int>,
    val outputs: SendChannel<Int>,
    val halts: SendChannel<Boolean>,
    val name: String = "unnamed"
) {
    companion object {
        fun parseProgram(s: String) =
            s.split(",").map { it.toInt() }.toMutableList()
    }

    val memory = program.map { it }.toMutableList() // copy the list?
    private var addressPointer: Int = 0

    var halted = false

    suspend fun run() {
//        println("$name: started")

        while (!halted) {
            val oci: OpCodeInstruction = readOpCode(addressPointer)
            var newPointer: Int? = null

            when (oci.opCode) {
                1 -> processInstruction(oci, addressPointer, Int::plus)
                2 -> processInstruction(oci, addressPointer, Int::times)
                3 -> {
                    val input = inputs.receive() //blocks waiting for input?
                    writeInput(addressPointer, input)
//                    println("$name: read $input from input channel")
                }
                4 -> {
                    val output = readFirstParameter(oci, addressPointer)
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

//        println("$name: halting")
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
            else -> 4
        }
    }

    private fun jumpIfTrue(oci: OpCodeInstruction, addressPointer: Int): Int? {
        val shouldSetPointer = readFirstParameter(oci, addressPointer) != 0
        return when {
            shouldSetPointer -> readSecondParameter(oci, addressPointer)
            else -> null
        }
    }

    private fun jumpIfFalse(oci: OpCodeInstruction, addressPointer: Int): Int? {
        val shouldSetPointer = readFirstParameter(oci, addressPointer) == 0
        return when {
            shouldSetPointer -> readSecondParameter(oci, addressPointer)
            else -> null
        }
    }

    private fun testForLessThan(oci: OpCodeInstruction, addressPointer: Int) {
        val targetAddress = memory[addressPointer + 3]
        val a = readFirstParameter(oci, addressPointer)
        val b = readSecondParameter(oci, addressPointer)
        writeTo(targetAddress, if (a < b) 1 else 0)
    }

    private fun testEquality(oci: OpCodeInstruction, addressPointer: Int) {
        val targetAddress = memory[addressPointer + 3]
        val a = readFirstParameter(oci, addressPointer)
        val b = readSecondParameter(oci, addressPointer)
        writeTo(targetAddress, if (a == b) 1 else 0)
    }

    private fun writeInput(addressPointer: Int, input: Int) {
        val targetAddress = memory[addressPointer + 1]
        writeTo(targetAddress, input)
    }

    private fun writeTo(targetAddress: Int, input: Int) {
        padMemory(targetAddress)
        memory[targetAddress] = input
    }

    private fun processInstruction(oci: OpCodeInstruction, addressPointer: Int, operation: (Int, Int) -> Int) {
        padMemory(memory[addressPointer + 3])
        val firstParameter = readFirstParameter(oci, addressPointer)
        val secondParameter = readSecondParameter(oci, addressPointer)
        memory[memory[addressPointer + 3]] = operation(firstParameter, secondParameter)
    }

    private fun readFirstParameter(oci: OpCodeInstruction, addressPointer: Int) =
        when (oci.firstParameterMode) {
            OpCodeInstruction.POSITIONAL -> memory[memory[addressPointer + 1]]
            OpCodeInstruction.IMMEDIATE -> memory[addressPointer + 1]
            else -> throw Exception("bad OpCodeInstruction for first parameter $oci")
        }

    private fun readSecondParameter(oci: OpCodeInstruction, addressPointer: Int) =
        when (oci.secondParameterMode) {
            OpCodeInstruction.POSITIONAL -> memory[memory[addressPointer + 2]]
            OpCodeInstruction.IMMEDIATE -> memory[addressPointer + 2]
            else -> throw Exception("bad OpCodeInstruction for second parameter $oci")
        }

    private fun padMemory(pointer: Int) {
        if (pointer >= memory.size) {
            val toPad = pointer - memory.size + 1
            val toAdd = arrayOfNulls<Int>(toPad).map { 0 }.toMutableList()
            memory.plusAssign(toAdd)
        }
    }

    private fun readOpCode(pointer: Int): OpCodeInstruction =
        OpCodeInstruction(memory[pointer])
}
package day2

data class ProgramHalted(
    val memory: String,
    val output: Int?,
    val addressPointer: Int,
    val state: String
)

class ShipsComputer {
    var memory = mutableListOf<Int>()
    var output: Int? = null

    fun runProgram(program: MutableList<Int>, inputs: Iterator<Int> = listOf(1).iterator(), pointerStart: Int = 0): ProgramHalted {
        memory = program

        var state = "continue"

        var addressPointer = pointerStart
//        println("Starting at pointer $addressPointer")

        while (state == "continue") {
//            println("step at pointer $addressPointer")
            val oci: OpCodeInstruction = readOpCode(addressPointer)
//            println("running with memory: $memory at address $addressPointer and oci: $oci")
            var newPointer: Int? = null

            when (oci.opCode) {
                1 -> processInstruction(oci, addressPointer, Int::plus)
                2 -> processInstruction(oci, addressPointer, Int::times)
                3 -> {
                    if(inputs.hasNext()) {
                        writeInput(addressPointer, inputs.next())
                    } else {
                        state = "pause"
                    }
                }
                4 -> {
                    output = readFirstParameter(oci, addressPointer)
                }
                5 -> {
                    newPointer = jumpIfTrue(oci, addressPointer)
                }
                6 -> {
                    newPointer = jumpIfFalse(oci, addressPointer)
                }
                7 -> testForLessThan(oci, addressPointer)
                8 -> testEquality(oci, addressPointer)
                99 -> state = "halt"
            }

            if (newPointer != null) {
                addressPointer = newPointer
            } else {
                addressPointer += determinePointerJump(oci)
            }
        }

        return ProgramHalted(memory.joinToString(","), output, addressPointer, state)
    }

    fun runProgram(program: String, inputs: Iterator<Int> = listOf(1).iterator(), pointerStart: Int = 0) =
        runProgram(
            program.split(",").map { it.toInt() }.toMutableList(),
            inputs,
            pointerStart
        )

    fun runProgram(program: String, input: Int, pointerStart: Int = 0) =
        runProgram(
            program,
            listOf(input).iterator(),
            pointerStart
        )

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

    private fun readOpCode(pointer: Int): OpCodeInstruction = OpCodeInstruction(memory[pointer].toLong())
}

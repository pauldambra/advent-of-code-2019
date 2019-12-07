package day2

data class ProgramResult(val memory: String, val output: Int?)

data class OpCodeInstruction(val instruction: Int) {

    private val parameters: List<Int>

    val opCode: Int
    val firstParameterMode: Int
        get() = parameters.getOrElse(0) { 0 }
    val secondParameterMode: Int
        get() = parameters.getOrElse(1) { 0 }
    val thirdParameterMode: Int
        get() = parameters.getOrElse(2) { 0 }
    val fourthParameterMode: Int
        get() = parameters.getOrElse(3) { 0 }

    init {
        val i = instruction.toString().padStart(2, '0')
        opCode = i.substring(i.length - 2).toInt()
        val ps = i.substring(0, i.length - 2)
        parameters = ps.reversed().map { it.toString().toInt() }
    }

    companion object {
        const val POSITIONAL = 0
        const val IMMEDIATE = 1
    }
}

object ShipsComputer {
    var memory = mutableListOf<Int>()
    var output: Int? = null

    fun runProgram(program: String, input: Int = 1): ProgramResult {
        memory = program.split(",").map { it.toInt() }.toMutableList()

        var addressPointer = 0
        var halt = false

        while (!halt) {

            val oci: OpCodeInstruction = readOpCode(addressPointer)
//            println("running with memory: $memory at address $addressPointer and oci: $oci")
            var newPointer: Int? = null

            when (oci.opCode) {
                1 -> processInstruction(oci, addressPointer, Int::plus)
                2 -> processInstruction(oci, addressPointer, Int::times)
                3 -> writeInput(addressPointer, input)
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
                99 -> halt = true
            }

            if (newPointer != null) {
                addressPointer = newPointer
            } else {
                addressPointer += determinePointerJump(oci)
            }
        }

        return ProgramResult(memory.joinToString(","), output)
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

    private fun readOpCode(pointer: Int): OpCodeInstruction = OpCodeInstruction(memory[pointer])
}

package day2

object ShipsComputer {
    var memory = mutableListOf<Int>()

    fun runProgram(program: String): String {
        memory = program.split(",").map { it.toInt() }.toMutableList()

        var addressPointer = 0
        var halt = false

        while (!halt) {

            when (readOpCode(addressPointer)) {
                1 -> processInstruction(addressPointer, Int::plus)
                2 -> processInstruction(addressPointer, Int::times)
                99 -> halt = true
            }

            addressPointer += 4
        }

        return memory.joinToString(",")
    }

    private fun processInstruction(addressPointer: Int, operation: (Int, Int) -> Int) {
        padMemory(addressPointer)
        memory[memory[addressPointer + 3]] = operation(memory[memory[addressPointer + 1]],(memory[memory[addressPointer + 2]]))
    }

    private fun padMemory(pointer: Int) {
        if (memory[pointer + 3] >= memory.size) {
            val toPad = memory[pointer + 3] - memory.size + 1
            val toAdd = arrayOfNulls<Int>(toPad).map { 0 }.toMutableList()
            memory.plusAssign(toAdd)
        }
    }

    private fun readOpCode(pointer: Int) = memory[pointer]
}

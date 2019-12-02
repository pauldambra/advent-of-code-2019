package day2

    fun processOpCode(instruction: String): String {
        val everyFour = generateSequence(0) { it + 1 }.map { it * 4 }.iterator()
        val x = instruction.split(",").map { it.toInt() }.toMutableList()

        var pointer = everyFour.next()
        var halt = false

        while (!halt) {

            when (x[pointer]) {
                1 -> {
                    if (x[pointer + 3] >= x.size) {
                        val toPad = x[pointer + 3] - x.size + 1
                        val toAdd = arrayOfNulls<Int>(toPad).map { 0 }.toMutableList()
                        x += toAdd
                    }
                    x[x[pointer + 3]] = x[x[pointer + 1]] + x[x[pointer + 2]]
                }
                2 -> {
                    if (x[pointer + 3] >= x.size) {
                        val toPad = x[pointer + 3] - x.size + 1
                        val toAdd = arrayOfNulls<Int>(toPad).map { 0 }.toMutableList()
                        x += toAdd
                    }
                    x[x[pointer + 3]] = x[x[pointer + 1]] * x[x[pointer + 2]]
                }
                99 -> halt = true
            }

            pointer = everyFour.next()
        }

        return x.joinToString(",")
    }

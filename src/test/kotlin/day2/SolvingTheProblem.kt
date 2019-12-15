package day2

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class SolvingTheProblem {
    private val puzzleInputPartOne = "1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,1,5,19,23,1,23,5,27,1,27,13,31,1,31,5,35,1,9,35,39,2,13,39,43,1,43,10,47,1,47,13,51,2,10,51,55,1,55,5,59,1,59,5,63,1,63,13,67,1,13,67,71,1,71,10,75,1,6,75,79,1,6,79,83,2,10,83,87,1,87,5,91,1,5,91,95,2,95,10,99,1,9,99,103,1,103,13,107,2,10,107,111,2,13,111,115,1,6,115,119,1,119,10,123,2,9,123,127,2,127,9,131,1,131,10,135,1,135,2,139,1,10,139,0,99,2,0,14,0"
    private fun puzzleInputPartTwo(noun:String, verb:String) = "1,$noun,$verb,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,1,5,19,23,1,23,5,27,1,27,13,31,1,31,5,35,1,9,35,39,2,13,39,43,1,43,10,47,1,47,13,51,2,10,51,55,1,55,5,59,1,59,5,63,1,63,13,67,1,13,67,71,1,71,10,75,1,6,75,79,1,6,79,83,2,10,83,87,1,87,5,91,1,5,91,95,2,95,10,99,1,9,99,103,1,103,13,107,2,10,107,111,2,13,111,115,1,6,115,119,1,119,10,123,2,9,123,127,2,127,9,131,1,131,10,135,1,135,2,139,1,10,139,0,99,2,0,14,0"
    @Test
    fun `running part one`() {
        val result = ShipsComputer().runProgram(puzzleInputPartOne).memory.split(",").first()
        Assertions.assertThat(result).isEqualTo("3562624")
    }

    @Test
    fun `running part two`() {

        for (noun in 0..99) {
            for (verb in 0..99) {
                val program = puzzleInputPartTwo(noun.toString(), verb.toString())
                val result = ShipsComputer().runProgram(program).memory.split(",").first()
                if (result == "19690720") {
                    println("19690720 is generated from ${(100*noun)+verb}")
                }
            }
        }

    }

    @Test
    fun `1,0,0,0,99 becomes 2,0,0,0,99 (1 + 1 = 2)`() {
        val input = "1,0,0,0,99"
        val finalMemory = "2,0,0,0,99"
        Assertions.assertThat(ShipsComputer().runProgram(input).memory).isEqualTo(finalMemory)
    }

    @Test
    fun `2,3,0,3,99 becomes 2,3,0,6,99 (3 * 2 = 6)`() {
        val input = "2,3,0,3,99"
        val finalMemory = "2,3,0,6,99"
        Assertions.assertThat(ShipsComputer().runProgram(input).memory).isEqualTo(finalMemory)
    }
    @Test
    fun `2,4,4,5,99,0 becomes 2,4,4,5,99,9801 (99 * 99 = 9801)`() {
        val input = "2,4,4,5,99,0"
        val finalMemory = "2,4,4,5,99,9801"
        Assertions.assertThat(ShipsComputer().runProgram(input).memory).isEqualTo(finalMemory)
    }
    @Test
    fun `1,1,1,4,99,5,6,0,99 becomes 30,1,1,4,2,5,6,0,99`() {
        val input = "1,1,1,4,99,5,6,0,99"
        val finalMemory = "30,1,1,4,2,5,6,0,99"
        Assertions.assertThat(ShipsComputer().runProgram(input).memory).isEqualTo(finalMemory)
    }
}
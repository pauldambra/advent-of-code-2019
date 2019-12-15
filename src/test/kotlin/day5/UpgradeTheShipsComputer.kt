package day5

import day2.OpCodeInstruction
import day2.ShipsComputer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UpgradeTheShipsComputer {
    @Test
    fun `OpCode 3 takes an input and writes to the given address`() {
        val program = "3,3,99"
        val finalMemory = "3,3,99,1"
        assertThat(ShipsComputer().runProgram(program).memory).isEqualTo(finalMemory)

    }

    @Test
    fun `OpCode 4 takes a value from an index and writes to the program output`() {
        val program = "4,3,99,201"
        val finalMemory = "4,3,99,201"
        val programResult = ShipsComputer().runProgram(program)

        assertThat(programResult.memory).isEqualTo(finalMemory)

        assertThat(programResult.output!!).isEqualTo(201)
    }

    @Test
    fun `3,0,4,0,99 takes whatever it is given as input and returns it as output`() {
        val program = "3,0,4,0,99"
        assertThat(ShipsComputer().runProgram(program).output!!).isEqualTo(1)
    }

    /*
    ABCDE
 1002

DE - two-digit opcode,      02 == opcode 2
 C - mode of 1st parameter,  0 == position mode
 B - mode of 2nd parameter,  1 == immediate mode
 A - mode of 3rd parameter,  0 == position mode,
                                  omitted due to being a leading zero
     */
    @Test
    fun `1002 as an opcode parameter`() {
        val x = OpCodeInstruction(1002)
        assertThat(x.opCode).isEqualTo(2)
        assertThat(x.firstParameterMode).isEqualTo(0)
        assertThat(x.secondParameterMode).isEqualTo(1)
        assertThat(x.thirdParameterMode).isEqualTo(0)
        assertThat(x.fourthParameterMode).isEqualTo(0)
    }

    @Test
    fun `first parameter switching program example`() {
        val program = "1002,4,3,4,33"
        assertThat(ShipsComputer().runProgram(program).memory).isEqualTo("1002,4,3,4,99")
    }

    @Test
    fun `second parameter example`() {
        val program = "1101,100,-1,4,0"
        assertThat(ShipsComputer().runProgram(program).memory).isEqualTo("1101,100,-1,4,99")
    }

    @Test
    fun `part 1 solution`() {
        val program = this::class.java.getResource("/day5/puzzleInput.txt").readText().trim()
        assertThat(ShipsComputer().runProgram(program).output).isEqualTo(15314507)
    }
}

package day5

import day2.ShipsComputer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/*
Opcode 7 is less than: if the first parameter is less than the second parameter,
it stores 1 in the position given by the third parameter. Otherwise, it stores 0.
 */
class OpCode7 {

    class WhenLessThan {
        @Test
        fun `(both positional)`() {
            val program = "7,5,6,3,99,1,2"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("7,5,6,1,99,1,2")
        }

        @Test
        fun `(first immediate)`() {
            val program = "107,2,6,3,99,1,3"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("107,2,6,1,99,1,3")
        }

        @Test
        fun `(second immediate)`() {
            val program = "1007,5,6,3,99,1,2"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("1007,5,6,1,99,1,2")
        }

        @Test
        fun `(both immediate)`() {
            val program = "1107,7,8,3,99"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("1107,7,8,1,99")
        }
    }

    class WhenEqualOrGreaterThan {
        @Test
        fun `(both positional)`() {
            val program = "7,5,6,3,99,2,1"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("7,5,6,0,99,2,1")
        }

        @Test
        fun `(first immediate)`() {
            val program = "107,2,6,3,99,1,2"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("107,2,6,0,99,1,2")
        }

        @Test
        fun `(second immediate)`() {
            val program = "1007,5,10,3,99,11,4"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("1007,5,10,0,99,11,4")
        }

        @Test
        fun `(both immediate)`() {
            val program = "1107,8,7,3,99"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("1107,8,7,0,99")
        }
    }

}
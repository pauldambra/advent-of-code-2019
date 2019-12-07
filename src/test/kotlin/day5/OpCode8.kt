package day5

import day2.ShipsComputer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/*
Opcode 8 is equals:
if the first parameter is equal to the second parameter, it stores 1 in the position given by the third parameter.
Otherwise, it stores 0.
 */
class OpCode8 {

    class WhenEqual {
        @Test
        fun `opcode 8 (both positional)`() {
            val program = "8,5,6,7,99,1,1"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("8,5,6,7,99,1,1,1")
        }

        @Test
        fun `opcode 8 (first immediate)`() {
            val program = "108,2,6,7,99,1,2"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("108,2,6,7,99,1,2,1")
        }

        @Test
        fun `opcode 8 (second immediate)`() {
            val program = "1008,5,1,3,99,1,2"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("1008,5,1,1,99,1,2")
        }

        @Test
        fun `opcode 8 (both immediate)`() {
            val program = "1108,5,5,3,99"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("1108,5,5,1,99")
        }
    }

    class WhenNotEqual {
        @Test
        fun `opcode 8 (both positional)`() {
            val program = "8,5,6,3,99,3,4"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("8,5,6,0,99,3,4")
        }

        @Test
        fun `opcode 8 (first immediate)`() {
            val program = "108,2,6,3,99,3,4"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("108,2,6,0,99,3,4")
        }

        @Test
        fun `opcode 8 (second immediate)`() {
            val program = "1008,5,1,3,99,3,4"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("1008,5,1,0,99,3,4")
        }

        @Test
        fun `opcode 8 (both immediate)`() {
            val program = "1108,3,4,3,99"
            assertThat(ShipsComputer.runProgram(program).memory).isEqualTo("1108,3,4,0,99")
        }
    }

}
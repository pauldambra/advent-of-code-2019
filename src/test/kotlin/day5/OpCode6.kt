package day5

import day2.ShipsComputer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/*
Opcode 6 is jump-if-false:
if the first parameter is zero, it sets the instruction pointer to the value from the second parameter.
Otherwise, it does nothing.
 */
class OpCode6 {

    class WhenFalse {
        @Test
        fun `both positional`() {
            val program = "0006,9,10,1101,1,1,8,0,99,0,8"
            assertThat(ShipsComputer().runProgram(program).memory).isEqualTo("6,9,10,1101,1,1,8,0,99,0,8")
        }
        @Test
        fun `first immediate`() {
            val program = "0106,0,10,1101,1,1,8,0,99,0,8"
            assertThat(ShipsComputer().runProgram(program).memory).isEqualTo("106,0,10,1101,1,1,8,0,99,0,8")
        }
    }

    class WhenTrue {
        @Test
        fun `both positional`() {
            val program = "0006,8,9,1101,1,1,10,99,1,8"
            assertThat(ShipsComputer().runProgram(program).memory).isEqualTo("6,8,9,1101,1,1,10,99,1,8,2")
        }
        @Test
        fun `first immediate`() {
            val program = "0106,1,10,1101,1,1,8,99,0,8,2"
            assertThat(ShipsComputer().runProgram(program).memory).isEqualTo("106,1,10,1101,1,1,8,99,2,8,2")
        }
    }

}
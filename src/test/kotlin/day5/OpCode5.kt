package day5

import day2.ShipsComputer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/*
Opcode 5 is jump-if-true:
if the first parameter is non-zero, it sets the instruction pointer to the value from the second parameter.
Otherwise, it does nothing.
 */
class OpCode5 {

    class WhenTrue {
        @Test
        fun `both positional`() {
            val program = "0005,9,10,1101,1,1,8,0,99,1,8"
            assertThat(ShipsComputer().runProgram(program).memory).isEqualTo("5,9,10,1101,1,1,8,0,99,1,8")
        }
    }

    class WhenFalse {
        @Test
        fun `both positional`() {
            val program = "0005,8,10,1101,1,1,10,99,0,7"
            assertThat(ShipsComputer().runProgram(program).memory).isEqualTo("5,8,10,1101,1,1,10,99,0,7,2")
        }
    }

}
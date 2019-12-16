package day5

import day2.ShipsComputer
import day2.ShipsComputerWithChannels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


/*
For example, here are several programs that take one input, compare it to the value 8, and then produce one output:

    3,9,8,9,10,9,4,9,99,-1,8 - Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).
    3,9,7,9,10,9,4,9,99,-1,8 - Using position mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not).
    3,3,1108,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).
    3,3,1107,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not).

Here are some jump tests that take an input, then output 0 if the input was zero or 1 if the input was non-zero:

    3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9 (using position mode)
    3,3,1105,-1,9,1101,0,0,12,4,12,99,1 (using immediate mode)

Here's a larger example:

3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99

The above example program uses an input instruction to ask for a single number. The program will then output 999 if the input value is below 8, output 1000 if the input value is equal to 8, or output 1001 if the input value is greater than 8.
 */
class PartTwoExamples  {
    @Test
    fun `3,9,8,9,10,9,4,9,99,-1,8 - Using position mode, consider whether the input is equal to 8 and output 1 (if it is) or 0 (if it is not)`() {
        val program = "3,9,8,9,10,9,4,9,99,-1,8"
        assertThat(ShipsComputer().runProgram(program, 8).output).isEqualTo(1)
        assertThat(ShipsComputer().runProgram(program, 7).output).isEqualTo(0)
    }

    @Test
    fun `less than 8 --- 3,9,7,9,10,9,4,9,99,-1,8 - Using position mode, consider whether the input is less than 8 and output 1 (if it is) or 0 (if it is not)`() {
        val program = "3,9,7,9,10,9,4,9,99,-1,8"

        testChannelComputer(program, { output ->
            assertThat(output).isEqualTo(1)
        }, 7)
    }
    @Test
    fun `less than 8 --- larger example - Using position mode, consider whether the input is less than 8 and output 999 (if it is) or 1001 (if it is not)`() {
        val program = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99"

        testChannelComputer(program, { output ->
            assertThat(output).isEqualTo(999)
        }, 7)
    }

    @Test
    fun `more than 8 --- larger example - Using position mode, consider whether the input is less than 8 and output 999 (if it is) or 1001 (if it is not)`() {
        val program = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99"

        testChannelComputer(program, { output ->
            assertThat(output).isEqualTo(1001)
        }, 198)
    }

    @Test
    fun `more than 8 --- 3,9,7,9,10,9,4,9,99,-1,8 - Using position mode, consider whether the input is less than 8 and output 1 (if it is) or 0 (if it is not)`() {
        val program = "3,9,7,9,10,9,4,9,99,-1,8"

        testChannelComputer(program, { output ->
            assertThat(output).isEqualTo(0)
        }, 9)
    }




        @Test
    fun `not equal to 8 --- 3,3,1108,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is equal to 8 and output 1 (if it is) or 0 (if it is not)`() {
        val program = "3,3,1108,-1,8,3,4,3,99"

        testChannelComputer(program, { output ->
            assertThat(output).isEqualTo(0)
        }, 7)
    }

    @Test
    fun `equal to 8 --- 3,3,1108,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is equal to 8 and output 1 (if it is) or 0 (if it is not)`() {
        val program = "3,3,1108,-1,8,3,4,3,99"

        testChannelComputer(program, { output ->
            assertThat(output).isEqualTo(1)
        }, 8)
    }

    @Test
    fun `less than 8 --- 3,3,1107,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is less than 8 and output 1 (if it is) or 0 (if it is not)`() {
        val program = "3,3,1107,-1,8,3,4,3,99"

        testChannelComputer(program, { output ->
            assertThat(output).isEqualTo(1)
        }, 7)
    }

    @Test
    fun `8 --- 3,3,1107,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is less than 8 and output 1 (if it is) or 0 (if it is not)`() {
        val program = "3,3,1107,-1,8,3,4,3,99"

        testChannelComputer(program, { output ->
            assertThat(output).isEqualTo(0)
        }, 8)
    }

    @Test
    fun `more than 8 --- 3,3,1107,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is less than 8 and output 1 (if it is) or 0 (if it is not)`() {
        val program = "3,3,1107,-1,8,3,4,3,99"

        testChannelComputer(program, { output ->
            assertThat(output).isEqualTo(0)
        }, 9)
    }

    @Test
    fun `3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9 (using position mode)`() {
        val program = "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"
        assertThat(ShipsComputer().runProgram(program, 0).output).isEqualTo(0)
        assertThat(ShipsComputer().runProgram(program, 18).output).isEqualTo(1)
    }
    @Test
    fun `3,3,1105,-1,9,1101,0,0,12,4,12,99,1 (using immediate mode)`() {
        val program = "3,3,1105,-1,9,1101,0,0,12,4,12,99,1"
        assertThat(ShipsComputer().runProgram(program, 0).output).isEqualTo(0)
        assertThat(ShipsComputer().runProgram(program, 2).output).isEqualTo(1)
    }
    @Test
    fun `larger example`() {
        val program = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99"
        assertThat(ShipsComputer().runProgram(program, 7).output).isEqualTo(999)
        assertThat(ShipsComputer().runProgram(program, 8).output).isEqualTo(1000)
        assertThat(ShipsComputer().runProgram(program, 9).output).isEqualTo(1001)
    }



    @Test
    fun `part 2 solution`() {
        val program = this::class.java.getResource("/day5/puzzleInput.txt").readText().trim()

        testChannelComputer(program, { output ->
            if (output != 0L) {
                assertThat(output).isEqualTo(652726)
            }
        }, 5)

    }

    private fun testChannelComputer(program: String, assertion: (Long) -> Unit, input: Long) {
        val inputs = Channel<Long>()
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(
            ShipsComputerWithChannels.parseProgram(program),
            inputs,
            outputs,
            halts
        )

        GlobalScope.launch {
            computer.run()
        }

        runBlocking {
            inputs.send(input)
            for (output in outputs) {
                assertion(output)

                println("closing channels")
                inputs.close()
                outputs.close()
                halts.close()
            }
        }
    }
}
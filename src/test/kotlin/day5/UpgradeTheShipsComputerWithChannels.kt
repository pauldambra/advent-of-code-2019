package day5

import day2.ShipsComputerWithChannels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

class UpgradeTheShipsComputerWithChannels {


    @Test
    fun `OpCode 4 takes a value from an index and writes to the program output`() {
        val program = "4,3,99,201"

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
            assertThat(outputs.receive()).isEqualTo(201)
            inputs.close()
            outputs.close()
            halts.close()
        }
    }

    @Test
    fun `3,0,4,0,99 takes whatever it is given as input and returns it as output`() {
        val program = "3,0,4,0,99"

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
            val input = Random.Default.nextLong()
            inputs.send(input)
            assertThat(outputs.receive()).isEqualTo(input)
            inputs.close()
            outputs.close()
            halts.close()
        }
    }

    @Test
    fun `part 1 solution`() {
        val program = this::class.java.getResource("/day5/puzzleInput.txt").readText().trim()

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
            inputs.send(1L)
            for (output in outputs) {
                if (output != 0L) {
                    assertThat(output).isEqualTo(15314507)
                    inputs.close()
                    outputs.close()
                    halts.close()
                }
            }
        }


    }
}

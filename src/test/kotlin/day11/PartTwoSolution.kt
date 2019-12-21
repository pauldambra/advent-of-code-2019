package day11

import day2.ShipsComputerWithChannels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PartTwoSolution {

    @Test
    fun `can solve part two`() {
        val program =
            ShipsComputerWithChannels.parseProgram(this::class.java.getResource("/day11/puzzleInput.txt").readText().trim())
        val inputs = Channel<Long>()
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(program, inputs, outputs, halts, "p1")
        val robot = Robot(outputs, inputs, 1)

        GlobalScope.launch { computer.run() }

        GlobalScope.launch { robot.run() }

        runBlocking {
            halts.receive()
            val squaresPaintedAtLeastOnce = robot.grid.values.sumBy { it.values.count() }
            println("which means the robot has painted $squaresPaintedAtLeastOnce")

            robot.printGrid()

            assertThat(squaresPaintedAtLeastOnce).isEqualTo(249)

        }
    }
}
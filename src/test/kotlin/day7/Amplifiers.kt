package day7

import day2.ShipsComputer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Amplifiers {

    @Test
    fun `seek example 1`() {
        val program = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"

        val maxThrusterSignal = seekMaxThrusterSignal(program)

        assertThat(maxThrusterSignal).isEqualTo(43210)
    }

    @Test
    fun `seek example 2`() {
        val program = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"

        val maxThrusterSignal = seekMaxThrusterSignal(program)

        assertThat(maxThrusterSignal).isEqualTo(54321)
    }

    @Test
    fun `seek part 1 solution`() {
        val program =
            "3,8,1001,8,10,8,105,1,0,0,21,46,59,80,105,122,203,284,365,446,99999,3,9,102,3,9,9,1001,9,5,9,102,2,9,9,1001,9,3,9,102,4,9,9,4,9,99,3,9,1002,9,2,9,101,2,9,9,4,9,99,3,9,101,5,9,9,1002,9,3,9,1001,9,3,9,1002,9,2,9,4,9,99,3,9,1002,9,4,9,1001,9,2,9,102,4,9,9,101,3,9,9,102,2,9,9,4,9,99,3,9,102,5,9,9,101,4,9,9,102,3,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,99"

        val maxThrusterSignal = seekMaxThrusterSignal(program)

        assertThat(maxThrusterSignal).isEqualTo(54321)
    }

    private fun seekMaxThrusterSignal(program: String) =
        Amplifier.possiblePhaseSettings.map { ps ->
            var thrusterSignal = 0
            ps.forEach { s ->
                thrusterSignal = Amplifier(program, s, thrusterSignal).run()!!
            }
            thrusterSignal
        }.max()
}

class Amplifier(val program: String, private val phaseSetting: Int, val input: Int) {
    companion object {
        val possiblePhaseSettings = listOf(
            listOf(0, 1, 2, 3, 4),
            listOf(1, 0, 2, 3, 4),
            listOf(2, 0, 1, 3, 4),
            listOf(0, 2, 1, 3, 4),
            listOf(1, 2, 0, 3, 4),
            listOf(2, 1, 0, 3, 4),
            listOf(2, 1, 3, 0, 4),
            listOf(1, 2, 3, 0, 4),
            listOf(3, 2, 1, 0, 4),
            listOf(2, 3, 1, 0, 4),
            listOf(1, 3, 2, 0, 4),
            listOf(3, 1, 2, 0, 4),
            listOf(3, 0, 2, 1, 4),
            listOf(0, 3, 2, 1, 4),
            listOf(2, 3, 0, 1, 4),
            listOf(3, 2, 0, 1, 4),
            listOf(0, 2, 3, 1, 4),
            listOf(2, 0, 3, 1, 4),
            listOf(1, 0, 3, 2, 4),
            listOf(0, 1, 3, 2, 4),
            listOf(3, 1, 0, 2, 4),
            listOf(1, 3, 0, 2, 4),
            listOf(0, 3, 1, 2, 4),
            listOf(3, 0, 1, 2, 4),
            listOf(4, 0, 1, 2, 3),
            listOf(0, 4, 1, 2, 3),
            listOf(1, 4, 0, 2, 3),
            listOf(4, 1, 0, 2, 3),
            listOf(0, 1, 4, 2, 3),
            listOf(1, 0, 4, 2, 3),
            listOf(1, 0, 2, 4, 3),
            listOf(0, 1, 2, 4, 3),
            listOf(2, 1, 0, 4, 3),
            listOf(1, 2, 0, 4, 3),
            listOf(0, 2, 1, 4, 3),
            listOf(2, 0, 1, 4, 3),
            listOf(2, 4, 1, 0, 3),
            listOf(4, 2, 1, 0, 3),
            listOf(1, 2, 4, 0, 3),
            listOf(2, 1, 4, 0, 3),
            listOf(4, 1, 2, 0, 3),
            listOf(1, 4, 2, 0, 3),
            listOf(0, 4, 2, 1, 3),
            listOf(4, 0, 2, 1, 3),
            listOf(2, 0, 4, 1, 3),
            listOf(0, 2, 4, 1, 3),
            listOf(4, 2, 0, 1, 3),
            listOf(2, 4, 0, 1, 3),
            listOf(3, 4, 0, 1, 2),
            listOf(4, 3, 0, 1, 2),
            listOf(0, 3, 4, 1, 2),
            listOf(3, 0, 4, 1, 2),
            listOf(4, 0, 3, 1, 2),
            listOf(0, 4, 3, 1, 2),
            listOf(0, 4, 1, 3, 2),
            listOf(4, 0, 1, 3, 2),
            listOf(1, 0, 4, 3, 2),
            listOf(0, 1, 4, 3, 2),
            listOf(4, 1, 0, 3, 2),
            listOf(1, 4, 0, 3, 2),
            listOf(1, 3, 0, 4, 2),
            listOf(3, 1, 0, 4, 2),
            listOf(0, 1, 3, 4, 2),
            listOf(1, 0, 3, 4, 2),
            listOf(3, 0, 1, 4, 2),
            listOf(0, 3, 1, 4, 2),
            listOf(4, 3, 1, 0, 2),
            listOf(3, 4, 1, 0, 2),
            listOf(1, 4, 3, 0, 2),
            listOf(4, 1, 3, 0, 2),
            listOf(3, 1, 4, 0, 2),
            listOf(1, 3, 4, 0, 2),
            listOf(2, 3, 4, 0, 1),
            listOf(3, 2, 4, 0, 1),
            listOf(4, 2, 3, 0, 1),
            listOf(2, 4, 3, 0, 1),
            listOf(3, 4, 2, 0, 1),
            listOf(4, 3, 2, 0, 1),
            listOf(4, 3, 0, 2, 1),
            listOf(3, 4, 0, 2, 1),
            listOf(0, 4, 3, 2, 1),
            listOf(4, 0, 3, 2, 1),
            listOf(3, 0, 4, 2, 1),
            listOf(0, 3, 4, 2, 1),
            listOf(0, 2, 4, 3, 1),
            listOf(2, 0, 4, 3, 1),
            listOf(4, 0, 2, 3, 1),
            listOf(0, 4, 2, 3, 1),
            listOf(2, 4, 0, 3, 1),
            listOf(4, 2, 0, 3, 1),
            listOf(3, 2, 0, 4, 1),
            listOf(2, 3, 0, 4, 1),
            listOf(0, 3, 2, 4, 1),
            listOf(3, 0, 2, 4, 1),
            listOf(2, 0, 3, 4, 1),
            listOf(0, 2, 3, 4, 1),
            listOf(1, 2, 3, 4, 0),
            listOf(2, 1, 3, 4, 0),
            listOf(3, 1, 2, 4, 0),
            listOf(1, 3, 2, 4, 0),
            listOf(2, 3, 1, 4, 0),
            listOf(3, 2, 1, 4, 0),
            listOf(3, 2, 4, 1, 0),
            listOf(2, 3, 4, 1, 0),
            listOf(4, 3, 2, 1, 0),
            listOf(3, 4, 2, 1, 0),
            listOf(2, 4, 3, 1, 0),
            listOf(4, 2, 3, 1, 0),
            listOf(4, 1, 3, 2, 0),
            listOf(1, 4, 3, 2, 0),
            listOf(3, 4, 1, 2, 0),
            listOf(4, 3, 1, 2, 0),
            listOf(1, 3, 4, 2, 0),
            listOf(3, 1, 4, 2, 0),
            listOf(2, 1, 4, 3, 0),
            listOf(1, 2, 4, 3, 0),
            listOf(4, 2, 1, 3, 0),
            listOf(2, 4, 1, 3, 0),
            listOf(1, 4, 2, 3, 0),
            listOf(4, 1, 2, 3, 0)
        )
    }

    fun run(): Int? {
        return ShipsComputer.runProgram(program, listOf(phaseSetting, input)).output
    }
}
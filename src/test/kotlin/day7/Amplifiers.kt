package day7

import day2.ProgramHalted
import day2.ShipsComputer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Amplifiers {

    @Test
    fun `seek example 1`() {
        val program = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"

        val maxThrusterSignal = Amplifier.seekMaxThrusterSignal(program)

        assertThat(maxThrusterSignal).isEqualTo(43210)
    }

    @Test
    fun `seek example 2`() {
        val program = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"

        val maxThrusterSignal = Amplifier.seekMaxThrusterSignal(program)

        assertThat(maxThrusterSignal).isEqualTo(54321)
    }

    @Test
    fun `seek part 1 solution`() {
        val program =
            "3,8,1001,8,10,8,105,1,0,0,21,46,59,80,105,122,203,284,365,446,99999,3,9,102,3,9,9,1001,9,5,9,102,2,9,9,1001,9,3,9,102,4,9,9,4,9,99,3,9,1002,9,2,9,101,2,9,9,4,9,99,3,9,101,5,9,9,1002,9,3,9,1001,9,3,9,1002,9,2,9,4,9,99,3,9,1002,9,4,9,1001,9,2,9,102,4,9,9,101,3,9,9,102,2,9,9,4,9,99,3,9,102,5,9,9,101,4,9,9,102,3,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,99"

        val maxThrusterSignal = Amplifier.seekMaxThrusterSignal(program)

        assertThat(maxThrusterSignal).isEqualTo(880726)
    }

    /**
     * Max thruster signal 139629729 (from phase setting sequence 9,8,7,6,5):

    3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,
    27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5
     */
    @Test
    fun `part 2 example 1`() {
        val program = "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"
        val expectedThrusterSignal = 18216

        val thrusterSignals = seekMaxThrusterSignalViaFeedback(program, listOf(listOf(9, 8, 7, 6, 5)))

        assertThat(thrusterSignals.map { it.last() }.max()).isEqualTo(expectedThrusterSignal)
    }

    /**
     * Max thruster signal 18216 (from phase setting sequence 9,7,8,5,6):

    3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,
    -5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,
    53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10
     */
    @Test
    fun `part 2 example 2`() {
        val program = "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"
        val expectedThrusterSignal = 18216

        val thrusterSignals = seekMaxThrusterSignalViaFeedback(program, listOf(listOf(9, 7, 8, 5, 6)))

        assertThat(thrusterSignals.map { it.last() }.max()).isEqualTo(expectedThrusterSignal)
    }

    @Test
    fun `seek part 2 solution`() {
        val program =
            "3,8,1001,8,10,8,105,1,0,0,21,46,59,80,105,122,203,284,365,446,99999,3,9,102,3,9,9,1001,9,5,9,102,2,9,9,1001,9,3,9,102,4,9,9,4,9,99,3,9,1002,9,2,9,101,2,9,9,4,9,99,3,9,101,5,9,9,1002,9,3,9,1001,9,3,9,1002,9,2,9,4,9,99,3,9,1002,9,4,9,1001,9,2,9,102,4,9,9,101,3,9,9,102,2,9,9,4,9,99,3,9,102,5,9,9,101,4,9,9,102,3,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,99"

        val maxThrusterSignal = seekMaxThrusterSignalViaFeedback(program, FeedbackAmplifier.possiblePhaseSettings)

        assertThat(maxThrusterSignal.map { it.last() }.max()).isGreaterThan(436)
        assertThat(maxThrusterSignal.map { it.last() }.max()).isEqualTo(-1)
    }

    private fun seekMaxThrusterSignalViaFeedback(
        program: String,
        phaseSettings: List<List<Int>>
    ): List<List<Int>> {
        return phaseSettings.map {
            println("starting for phase setting $it")

            val a = FeedbackAmplifier("a", program, it[0])
            val b = FeedbackAmplifier("b", program, it[1])
            val c = FeedbackAmplifier("c", program, it[2])
            val d = FeedbackAmplifier("d", program, it[3])
            val e = FeedbackAmplifier("e", program, it[4])

            val aSignals = mutableListOf<Int>()
            val bSignals = mutableListOf<Int>()
            val cSignals = mutableListOf<Int>()
            val dSignals = mutableListOf<Int>()
            val eSignals = mutableListOf(0)

            var loopCount = 0
            while (!e.halted()) {
                if (loopCount++ > 10000) {
                    throw Exception("oh bugger it")
                }
                aSignals.add(a.run(eSignals.last()))
                bSignals.add(b.run(aSignals.last()))
                cSignals.add(c.run(bSignals.last()))
                dSignals.add(d.run(cSignals.last()))
                eSignals.add(e.run(dSignals.last()))
            }

            eSignals
        }
    }
}

class FeedbackAmplifier(private val name: String, val program: String, private val phaseSetting: Int) {

    private var programState: ProgramHalted? = null

    fun halted(): Boolean {
        return (programState?.state ?: "unknown") == "halt"
    }

    fun run(input: Int): Int {
        println("$name: input $input")

        programState = ShipsComputer().runProgram(
            programState?.memory ?: program,
            listOf(phaseSetting, input).iterator(),
            programState?.addressPointer ?: 0
        )
        println("$name: output ${programState?.output ?: -1}")
        return programState!!.output!!
    }

    companion object {
        val possiblePhaseSettings = listOf(
            listOf(5, 6, 7, 8, 9),
            listOf(6, 5, 7, 8, 9),
            listOf(7, 5, 6, 8, 9),
            listOf(5, 7, 6, 8, 9),
            listOf(6, 7, 5, 8, 9),
            listOf(7, 6, 5, 8, 9),
            listOf(7, 6, 8, 5, 9),
            listOf(6, 7, 8, 5, 9),
            listOf(8, 7, 6, 5, 9),
            listOf(7, 8, 6, 5, 9),
            listOf(6, 8, 7, 5, 9),
            listOf(8, 6, 7, 5, 9),
            listOf(8, 5, 7, 6, 9),
            listOf(5, 8, 7, 6, 9),
            listOf(7, 8, 5, 6, 9),
            listOf(8, 7, 5, 6, 9),
            listOf(5, 7, 8, 6, 9),
            listOf(7, 5, 8, 6, 9),
            listOf(6, 5, 8, 7, 9),
            listOf(5, 6, 8, 7, 9),
            listOf(8, 6, 5, 7, 9),
            listOf(6, 8, 5, 7, 9),
            listOf(5, 8, 6, 7, 9),
            listOf(8, 5, 6, 7, 9),
            listOf(9, 5, 6, 7, 8),
            listOf(5, 9, 6, 7, 8),
            listOf(6, 9, 5, 7, 8),
            listOf(9, 6, 5, 7, 8),
            listOf(5, 6, 9, 7, 8),
            listOf(6, 5, 9, 7, 8),
            listOf(6, 5, 7, 9, 8),
            listOf(5, 6, 7, 9, 8),
            listOf(7, 6, 5, 9, 8),
            listOf(6, 7, 5, 9, 8),
            listOf(5, 7, 6, 9, 8),
            listOf(7, 5, 6, 9, 8),
            listOf(7, 9, 6, 5, 8),
            listOf(9, 7, 6, 5, 8),
            listOf(6, 7, 9, 5, 8),
            listOf(7, 6, 9, 5, 8),
            listOf(9, 6, 7, 5, 8),
            listOf(6, 9, 7, 5, 8),
            listOf(5, 9, 7, 6, 8),
            listOf(9, 5, 7, 6, 8),
            listOf(7, 5, 9, 6, 8),
            listOf(5, 7, 9, 6, 8),
            listOf(9, 7, 5, 6, 8),
            listOf(7, 9, 5, 6, 8),
            listOf(8, 9, 5, 6, 7),
            listOf(9, 8, 5, 6, 7),
            listOf(5, 8, 9, 6, 7),
            listOf(8, 5, 9, 6, 7),
            listOf(9, 5, 8, 6, 7),
            listOf(5, 9, 8, 6, 7),
            listOf(5, 9, 6, 8, 7),
            listOf(9, 5, 6, 8, 7),
            listOf(6, 5, 9, 8, 7),
            listOf(5, 6, 9, 8, 7),
            listOf(9, 6, 5, 8, 7),
            listOf(6, 9, 5, 8, 7),
            listOf(6, 8, 5, 9, 7),
            listOf(8, 6, 5, 9, 7),
            listOf(5, 6, 8, 9, 7),
            listOf(6, 5, 8, 9, 7),
            listOf(8, 5, 6, 9, 7),
            listOf(5, 8, 6, 9, 7),
            listOf(9, 8, 6, 5, 7),
            listOf(8, 9, 6, 5, 7),
            listOf(6, 9, 8, 5, 7),
            listOf(9, 6, 8, 5, 7),
            listOf(8, 6, 9, 5, 7),
            listOf(6, 8, 9, 5, 7),
            listOf(7, 8, 9, 5, 6),
            listOf(8, 7, 9, 5, 6),
            listOf(9, 7, 8, 5, 6),
            listOf(7, 9, 8, 5, 6),
            listOf(8, 9, 7, 5, 6),
            listOf(9, 8, 7, 5, 6),
            listOf(9, 8, 5, 7, 6),
            listOf(8, 9, 5, 7, 6),
            listOf(5, 9, 8, 7, 6),
            listOf(9, 5, 8, 7, 6),
            listOf(8, 5, 9, 7, 6),
            listOf(5, 8, 9, 7, 6),
            listOf(5, 7, 9, 8, 6),
            listOf(7, 5, 9, 8, 6),
            listOf(9, 5, 7, 8, 6),
            listOf(5, 9, 7, 8, 6),
            listOf(7, 9, 5, 8, 6),
            listOf(9, 7, 5, 8, 6),
            listOf(8, 7, 5, 9, 6),
            listOf(7, 8, 5, 9, 6),
            listOf(5, 8, 7, 9, 6),
            listOf(8, 5, 7, 9, 6),
            listOf(7, 5, 8, 9, 6),
            listOf(5, 7, 8, 9, 6),
            listOf(6, 7, 8, 9, 5),
            listOf(7, 6, 8, 9, 5),
            listOf(8, 6, 7, 9, 5),
            listOf(6, 8, 7, 9, 5),
            listOf(7, 8, 6, 9, 5),
            listOf(8, 7, 6, 9, 5),
            listOf(8, 7, 9, 6, 5),
            listOf(7, 8, 9, 6, 5),
            listOf(9, 8, 7, 6, 5),
            listOf(8, 9, 7, 6, 5),
            listOf(7, 9, 8, 6, 5),
            listOf(9, 7, 8, 6, 5),
            listOf(9, 6, 8, 7, 5),
            listOf(6, 9, 8, 7, 5),
            listOf(8, 9, 6, 7, 5),
            listOf(9, 8, 6, 7, 5),
            listOf(6, 8, 9, 7, 5),
            listOf(8, 6, 9, 7, 5),
            listOf(7, 6, 9, 8, 5),
            listOf(6, 7, 9, 8, 5),
            listOf(9, 7, 6, 8, 5),
            listOf(7, 9, 6, 8, 5),
            listOf(6, 9, 7, 8, 5),
            listOf(9, 6, 7, 8, 5)
        )
    }
}

class Amplifier(val program: String, private val phaseSetting: Int, val input: Int) {
    private val computer = ShipsComputer()

    fun run(): Int? {
        return computer.runProgram(program, listOf(phaseSetting, input).iterator()).output
    }

    companion object {
        fun seekMaxThrusterSignal(program: String) =
            possiblePhaseSettings.map { ps ->
                var thrusterSignal = 0
                ps.forEach { s ->
                    thrusterSignal = Amplifier(program, s, thrusterSignal).run()!!
                }
                thrusterSignal
            }.max()

        private val possiblePhaseSettings = listOf(
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
}

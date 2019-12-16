package day9

import day2.OpCodeInstruction
import day2.ShipsComputerWithChannels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class CompleteOpCodeComputer {
    @Test
    fun `202102 as an opcode parameter - can read relative parameters`() {
        val x = OpCodeInstruction(202102)
        assertThat(x.opCode).isEqualTo(2)
        println(x)
        assertThat(x.firstParameterMode).isEqualTo(OpCodeInstruction.IMMEDIATE)
        assertThat(x.secondParameterMode).isEqualTo(OpCodeInstruction.RELATIVE)
        assertThat(x.thirdParameterMode).isEqualTo(OpCodeInstruction.POSITIONAL)
        assertThat(x.fourthParameterMode).isEqualTo(OpCodeInstruction.RELATIVE)
    }

    @Test
    fun `OpCode 9 changes the relative base`() {
        val program = mutableListOf(109L,900L,99L)
        val inputs = Channel<Long>()
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(program, inputs, outputs, halts, "op9")
        assertThat(computer.relativeBase).isEqualTo(0)

        GlobalScope.launch {
            computer.run()
        }

        runBlocking {
            halts.receive()
        }
        assertThat(computer.relativeBase).isEqualTo(900)
    }

    @Test
    fun example1() {
        val program = ShipsComputerWithChannels.parseProgram("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99")
        val inputs = Channel<Long>()
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(program, inputs, outputs, halts, "ex1")

        GlobalScope.launch {
            computer.run()
        }

        val xs = mutableListOf<Long>()
        runBlocking {
            for (x in outputs) {
                xs.add(x)
            }
        }
        assertThat(xs).isEqualTo(program)
    }

    @Test
    fun example2() {
        val program = ShipsComputerWithChannels.parseProgram("1102,34915192,34915192,7,4,7,99,0")
        val inputs = Channel<Long>()
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(program, inputs, outputs, halts, "ex2")

        GlobalScope.launch {
            computer.run()
        }

        val xs = mutableListOf<Long>()
        runBlocking {
            for (x in outputs) {
                xs.add(x)
            }
        }
        assertThat(xs).hasSize(1)
        assertThat(xs.first().absoluteValue.toString().length).isEqualTo(16)
    }

    @Test
    fun example3() {
        val program = ShipsComputerWithChannels.parseProgram("104,1125899906842624,99")
        val inputs = Channel<Long>()
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(program, inputs, outputs, halts, "ex3")

        GlobalScope.launch {
            computer.run()
        }

        val xs = mutableListOf<Long>()
        runBlocking {
            for (x in outputs) {
                xs.add(x)
            }
        }
        assertThat(xs).hasSize(1)
        assertThat(xs.first()).isEqualTo(1125899906842624L)
    }

    @Test
    fun `solving part 1`() {
        val program = ShipsComputerWithChannels.parseProgram(this::class.java.getResource("/day9/puzzleInput.txt").readText().trim())
        val inputs = Channel<Long>()
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(program, inputs, outputs, halts, "p1")

        GlobalScope.launch {
            computer.run()
        }

        val xs = mutableListOf<Long>()
        runBlocking {
            inputs.send(1)
            for (x in outputs) {
                xs.add(x)
            }
        }
        assertThat(xs).hasSize(1)
        assertThat(xs.first()).isEqualTo(2453265701L)
    }
}
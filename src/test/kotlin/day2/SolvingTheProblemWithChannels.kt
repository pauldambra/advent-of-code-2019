package day2

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolvingTheProblemWithChannels {
    @ExperimentalCoroutinesApi
    private val infiniteChannelOfOnes = GlobalScope.produce {
        while (true) {
            send(1L)
        }
    }

    private val puzzleInputPartOne =
        "1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,1,5,19,23,1,23,5,27,1,27,13,31,1,31,5,35,1,9,35,39,2,13,39,43,1,43,10,47,1,47,13,51,2,10,51,55,1,55,5,59,1,59,5,63,1,63,13,67,1,13,67,71,1,71,10,75,1,6,75,79,1,6,79,83,2,10,83,87,1,87,5,91,1,5,91,95,2,95,10,99,1,9,99,103,1,103,13,107,2,10,107,111,2,13,111,115,1,6,115,119,1,119,10,123,2,9,123,127,2,127,9,131,1,131,10,135,1,135,2,139,1,10,139,0,99,2,0,14,0"

    private fun puzzleInputPartTwo(noun: String, verb: String) =
        "1,$noun,$verb,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,1,5,19,23,1,23,5,27,1,27,13,31,1,31,5,35,1,9,35,39,2,13,39,43,1,43,10,47,1,47,13,51,2,10,51,55,1,55,5,59,1,59,5,63,1,63,13,67,1,13,67,71,1,71,10,75,1,6,75,79,1,6,79,83,2,10,83,87,1,87,5,91,1,5,91,95,2,95,10,99,1,9,99,103,1,103,13,107,2,10,107,111,2,13,111,115,1,6,115,119,1,119,10,123,2,9,123,127,2,127,9,131,1,131,10,135,1,135,2,139,1,10,139,0,99,2,0,14,0"

    @ExperimentalCoroutinesApi
    @Test
    fun `running part one`() {
        val inputs = infiniteChannelOfOnes
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(
            ShipsComputerWithChannels.parseProgram(puzzleInputPartOne),
            inputs,
            outputs,
            halts
        )

        GlobalScope.launch {
            computer.run()
        }

        runBlocking {
            println("waiting for channels")
            for (halt in halts) {
                println("received $halt from halts")
                assertThat(halt).isTrue()

                val output = computer.memory.first()
                assertThat(output).isEqualTo(3562624)

                halts.close()
                outputs.close()
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `running part two`() {

        val results = mutableListOf<ResultSet>()

        for (noun in 0..99) {
            for (verb in 0..99) {
                val inputs = infiniteChannelOfOnes
                val outputs = Channel<Long>()
                val halts = Channel<Boolean>()

                val computer = ShipsComputerWithChannels(
                    ShipsComputerWithChannels.parseProgram(puzzleInputPartTwo(noun.toString(), verb.toString())),
                    inputs,
                    outputs,
                    halts
                )

                GlobalScope.launch {
                    computer.run()
                }

                runBlocking {

                    for (halt in halts) {
                        assertThat(halt).isTrue()
                        val output = computer.memory.first()

                        results.add(ResultSet(output, noun, verb))

                        halts.close()
                        outputs.close()
                    }
                }
            }
        }

        val matchingResults = results.filter { it.output == 19690720L }
        assertThat(matchingResults).hasSize(1)
        val matchingResult = matchingResults.first()

        val checksum = (100 * matchingResult.noun) + matchingResult.verb
        println("19690720 is generated from $checksum at noun: ${matchingResult.noun} and verb: ${matchingResult.verb}")
        assertThat(checksum).isEqualTo(8298)

    }
}

data class ResultSet(val output: Long, val noun: Int, val verb: Int)
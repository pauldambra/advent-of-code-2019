package day13

import day11.Coordinate
import day2.ShipsComputerWithChannels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CarePackage {

    @Test
    fun `parse output from the game`() {
        val xs = listOf(1L,2L,3L,6L,5L,4L)

        val game: List<GameTile> = GameTile.parse(xs)

        assertThat(game).containsExactly(
            GameTile(Coordinate(1, 2), GameTileType.HORIZONTAL_PADDLE),
            GameTile(Coordinate(6, 5), GameTileType.BALL)
        )
    }

    @Test
    fun `part 1`() {
        val program = this::class.java.getResource("/day13/puzzleInput.txt")
            .readText()
            .trim()
            .split(",")
            .map(String::toLong)
            .toMutableList()

        val inputs = Channel<Long>()
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(program, inputs = inputs, outputs = outputs, halts = halts)

        val programOutput = mutableListOf<Long>()

        GlobalScope.launch {
            computer.run()
        }

        GlobalScope.launch {
            for (o in outputs) {
                programOutput.add(o)
            }
        }

        runBlocking {
            halts.receive()
        }

        val tiles = GameTile.parse(programOutput)

        val blocks = tiles.filter { it.type == GameTileType.BLOCK }
        assertThat(blocks.size).isEqualTo(205)
    }
}

enum class GameTileType(val programValue: Long) {
    EMPTY(0),
    WALL(1),
    BLOCK(2),
    HORIZONTAL_PADDLE(3),
    BALL(4);

    companion object {
        private val reverseValues: Map<Long, GameTileType> = values().associateBy { it.programValue }
        fun from(i: Long): GameTileType = reverseValues[i]!!
    }
}

data class GameTile(val position: Coordinate, val type: GameTileType) {
companion object {
    fun parse(xs: List<Long>): List<GameTile> {
        check(xs.size % 3 == 0) { "input should come in groups of three but length is ${xs.size}" }

        return xs.chunked(3).map(::asTile)
    }

    private fun asTile(xs: List<Long>): GameTile {
        check(xs.size == 3) { "input should come as three numbers but length is ${xs.size}" }
        return GameTile(Coordinate(xs[0].toInt() ,xs[1].toInt()), GameTileType.from(xs[2]))
    }
}
}

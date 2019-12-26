package day13

import day11.Coordinate
import day2.ShipsComputerWithChannels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CarePackage {

    @Test
    fun `parse output from the game`() {
        val xs = listOf(1L, 2L, 3L, 6L, 5L, 4L)

        val game = Game()

        GlobalScope.launch {
            game.update(xs, Channel())

            assertThat(game.allTiles()).containsExactly(
                GameTile(Coordinate(1, 2), GameTileType.HORIZONTAL_PADDLE),
                GameTile(Coordinate(6, 5), GameTileType.BALL)
            )
        }
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

            val game = Game()
            game.update(programOutput, inputs)

            assertThat(game.blockCount()).isEqualTo(205)
        }
    }

    @Test
    fun `part 2`() {
        val program = this::class.java.getResource("/day13/puzzleInput.txt")
            .readText()
            .trim()
            .split(",")
            .map(String::toLong)
            .toMutableList()
        program[0] = 2

        val game = Game()
        val inputs = Channel<Long>(CONFLATED)
        val outputs = Channel<Long>()
        val halts = Channel<Boolean>()

        val computer = ShipsComputerWithChannels(program, inputs, outputs, halts)

        GlobalScope.launch {
            computer.run()
        }

        runBlocking {


            while (game.isRunning()) {
                val programOutput = mutableListOf<Long>()
                for (o in outputs) {
                    programOutput.add(o)
                    game.update(programOutput, inputs)
                }
            }
        }

        assertThat(game.score).isEqualTo(-1)
    }
}

enum class GameTileType(val programValue: Long, val draw: String) {
    EMPTY(0, " "),
    WALL(1, "▮"),
    BLOCK(2, "▩"),
    HORIZONTAL_PADDLE(3, "▬"),
    BALL(4, "●");

    companion object {
        private val reverseValues: Map<Long, GameTileType> = values().associateBy { it.programValue }
        fun from(i: Long): GameTileType = reverseValues[i] ?: error("tried to get a tile type that doesn't exist $i")
    }
}

@ExperimentalCoroutinesApi
class Game {
    private val tiles: MutableMap<Long, MutableMap<Long, GameTile>> = mutableMapOf()
    var score: Long = 0

    fun allTiles() = tiles.flatMap { it.value.values.map { t -> t.copy() } }

    fun blockCount() = allTiles().count { it.type == GameTileType.BLOCK }

    var hasHadAnyBlocks: Boolean = false
    fun isRunning(): Boolean {
        if (!hasHadAnyBlocks && blockCount() > 0) {
            hasHadAnyBlocks = true
        }
        return if (!hasHadAnyBlocks) {
            true
        } else {
            blockCount() > 0
        }
    }

    private val ballPositions = mutableListOf<Coordinate>()

    suspend fun update(
        xs: List<Long>,
        joystickChannel: Channel<Long>
    ) {
        if (xs.size % 3 != 0) {
            return
        }

        xs.chunked(3)
            .forEach {
                val x = it[0]
                val y = it[1]
                if (x == -1L) {
                    score = it[2]
                    println(drawGame())
                } else {
                    val row = tiles.getOrPut(y) { mutableMapOf() }
                    val tile = row.getOrPut(x) { GameTile.asTile(it) }
                    tile.update(it)

                    if (tile.type == GameTileType.BALL) {
                        ballPositions.add(tile.position)

                        val sideWays = sidewaysMotion()
                        joystickChannel.send(sideWays.toLong())
                    }

                    tiles[y]!![x] = tile

                }
            }
        println("now there are ${blockCount()} blocks left")
    }

    private fun sidewaysMotion() =
        ballPositions
            .map { bp -> bp.x }
            .takeLast(6)
            .zipWithNext()
            .fold(mutableListOf<Int>()) { acc, pair ->
                acc.add(pair.second.compareTo(pair.first))
                acc
            }
            .groupingBy { i -> i }
            .eachCount()
            .maxBy { j -> j.value }?.key ?: 0

    private fun drawGame(): String {

        var output = ""
        tiles.values.forEach { row ->
            row.values.forEach { tile ->
                output += tile.type.draw
            }
            output += "\n"
        }
        output += "score: $score"
        return "$output\n=========================================\n"
    }

    private suspend fun movePaddle(inputs: SendChannel<Long>) {
        val currentTiles = allTiles()
        val ball = currentTiles.find { it.type == GameTileType.BALL }
        val paddle = currentTiles.find { it.type == GameTileType.HORIZONTAL_PADDLE }

        ball?.let { b ->
            paddle?.let { p ->
                val direction = b.position.x.compareTo(p.position.x)
                if (!inputs.isClosedForSend) {
                    val nextDirection = direction.toLong()
                    println("setting joystick direction to $nextDirection")
                    inputs.send(nextDirection)
                }
            }
        }
    }
}

data class GameTile(val position: Coordinate, var type: GameTileType) {
    fun update(it: List<Long>) {
        type = GameTileType.from(it[2])
    }

    companion object {
        fun asTile(xs: List<Long>): GameTile {
            check(xs.size == 3) { "input should come as three numbers but length is ${xs.size}" }
            return GameTile(Coordinate(xs[0].toInt(), xs[1].toInt()), GameTileType.from(xs[2]))
        }
    }
}

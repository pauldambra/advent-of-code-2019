package day11

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel


class Robot(
    val instructions: ReceiveChannel<Long>,
    private val camera: SendChannel<Long>
) {

    suspend fun run() {
        var nextOperation = "paint"
        while (true) {
            println(grid)
            nextOperation = when (nextOperation) {
                "paint" -> {
                    sendingImageFromCamera()
                    val i = instructions.receive()
                    paint(i)
                    "move"
                }
                "move" -> {
                    val i = instructions.receive()
                    move(i)
                    "paint"
                }
                else -> throw Exception("wat operation is: $nextOperation")
            }
        }
    }

    private suspend fun sendingImageFromCamera() {
        val plateColour = plateColourAtCurrentLocation()
        println("Sending camera image $plateColour from $position")
        camera.send(plateColour)
    }

    private fun plateColourAtCurrentLocation(): Long {
        return plateColourAt(position)
    }

    private fun plateColourAt(coordinate: Coordinate): Long {
        val row = grid.getOrDefault(coordinate.y.toLong(), mutableMapOf())
        return row.getOrDefault(coordinate.x.toLong(), 0)
    }

    val grid = mutableMapOf<Long, MutableMap<Long, Long>>()

    var position = Coordinate(0, 0)
    private var direction: Direction = Direction.UP

    fun paint(colour: Long) {
        val row = grid.getOrPut(position.y.toLong()) { mutableMapOf() }
        row[position.x.toLong()] = colour
        println("painted $position colour: ${plateColourAtCurrentLocation()}")
    }

    private fun move(turn: Long) {
        direction = direction.turn(turn)
        position = position.move(direction)
        println("moved $direction to $position which is painted ${plateColourAtCurrentLocation()}")
    }

    override fun toString(): String {
        return "Robot(position=$position, direction=$direction)"
    }
}
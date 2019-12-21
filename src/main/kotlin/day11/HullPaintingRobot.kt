package day11

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel


class Robot(
    val instructions: ReceiveChannel<Long>,
    private val camera: SendChannel<Long>,
    startingPlateColour: Long = 0L
) {

    val grid = mutableMapOf<Long, MutableMap<Long, Long>>()
    var position = Coordinate(0, 0)
    private var direction: Direction = Direction.UP

    init {
        paint(startingPlateColour)
    }

    suspend fun run() {
        var nextOperation = "paint"
        while (true) {
//            println(grid)
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
//        println("Sending camera image $plateColour from $position")
        camera.send(plateColour)
    }

    private fun plateColourAtCurrentLocation(): Long {
        return plateColourAt(position)
    }

    private fun plateColourAt(coordinate: Coordinate): Long {
        val row = grid.getOrDefault(coordinate.y.toLong(), mutableMapOf())
        return row.getOrDefault(coordinate.x.toLong(), 0)
    }

    fun paint(colour: Long) {
        val row = grid.getOrPut(position.y.toLong()) { mutableMapOf() }
        row[position.x.toLong()] = colour
//        println("painted $position colour: ${plateColourAtCurrentLocation()}")
    }

    private fun move(turn: Long) {
        direction = direction.turn(turn)
        position = position.move(direction)
//        println("moved $direction to $position which is painted ${plateColourAtCurrentLocation()}")
    }

    override fun toString(): String {
        return "Robot(position=$position, direction=$direction)"
    }

    fun printGrid() {
        val padding = 1
        val yMin = (grid.keys.min() ?: 0) - padding
        val yMax = (grid.keys.max() ?: 0) + padding

        val xMin = (grid.minBy { it.value.keys.min() ?: 0 }?.value?.keys?.min() ?: 0) - padding
        val xMax = (grid.maxBy { it.value.keys.max() ?: 0 }?.value?.keys?.max() ?: 0) + padding

        for (y in yMax downTo yMin) {
            val row = grid.getOrDefault(y, mutableMapOf())

            for (x in xMin..xMax) {
                val i = row.getOrDefault(x, 0)
                print(if (i == 0L) " " else "#")
            }
            print("\n")
        }
    }
}
package day3

import kotlin.math.absoluteValue

data class Instruction(val direction: String, val count: Int) {
    companion object {
        fun parse(ss: List<String>) =
            ss.map { Instruction(
                it.substring(0, 1),
                it.substring(1).toInt()
            ) }
    }
}

data class GridCoord(val x: Int, val y: Int) {
    fun manhattanDistance() = x.absoluteValue + y.absoluteValue
}

class CurrentPosition(private var x: Int = 0, private var y: Int = 0) {

    fun move(direction: String): GridCoord = when (direction) {
        "R" -> GridCoord(++x, y)
        "U" -> GridCoord(x, ++y)
        "D" -> GridCoord(x, --y)
        "L" -> GridCoord(--x, y)
        else -> throw Exception("wat direction? $direction")
    }
}

fun shortestPathToIntersection(firstPath: String, secondPath: String): Int {
    val a = tracePath(firstPath)
    val b = tracePath(secondPath)
    val intersections = findIntersections(a, b)

    return intersections
        .map { a.indexOf(it) + b.indexOf(it) }
        .min()!!
}


fun closestIntersection(firstPath: String, secondPath: String) =
    findPathIntersections(firstPath, secondPath)
        .map { it.manhattanDistance() }
        .min()!!

fun findPathIntersections(firstPath: String, secondPath: String) =
    findIntersections(tracePath(firstPath), tracePath(secondPath))

private fun findIntersections(
    left: List<GridCoord>,
    right: List<GridCoord>
) = left.intersect(right).minus(GridCoord(0, 0))

fun tracePath(wirePath: String): List<GridCoord> {
    val instructions = wirePath.split(",")
    val parsedInstructions = Instruction.parse(instructions)

    val tracer = CurrentPosition()

    val path = parsedInstructions
        .flatMap { i ->
            (1..i.count).map { tracer.move(i.direction) }
        }

    return listOf(GridCoord(0, 0)) + path
}
package day11

data class Coordinate(val x: Int, val y: Int) {
    fun move(direction: Direction) =
        when (direction) {
            Direction.UP -> Coordinate(x, y + 1)
            Direction.RIGHT -> Coordinate(x + 1, y)
            Direction.DOWN -> Coordinate(x, y - 1)
            Direction.LEFT -> Coordinate(x - 1, y)
        }
}
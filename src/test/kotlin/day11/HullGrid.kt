package day11

import day11.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HullGrid {
    @Test
    fun `a robot knows when it is on a white square`() {
        val white = 1
        val robot = Robot()

        robot.paint(white)

        assertThat(robot.grid[0]?.get(0)).isEqualTo(1)
    }

    @Test
    fun `a robot knows when it is on a black square`() {
        val black = 0
        val robot = Robot()

        robot.paint(black)

        assertThat(robot.grid[0]?.get(0)).isEqualTo(0)
    }

    @Test
    fun `a robot can turn left`() {
        val left = 0
        val robot = Robot()

        robot.move(left)

        assertThat(robot.position).isEqualTo(Coordinate(-1, 0))
    }

    @Test
    fun `a robot can turn right`() {
        val right = 1
        val robot = Robot()

        robot.move(right)

        assertThat(robot.position).isEqualTo(Coordinate(1, 0))
    }

    /**
     *
     *      >-
     *
     *    then
     *
     *      *v
     *       |
     *
     *    ending
     *
     *      **
     *       v
     *
     */
    @Test
    fun `a robot can turn right twice`() {
        val right = 1
        val robot = Robot()

        robot.move(right)
        robot.move(right)

        assertThat(robot.position).isEqualTo(Coordinate(1, -1))
    }

    @Test
    fun `the grid records the journey`() {
        val robot = Robot()

        robot.paint(1)
        robot.move(0)

        robot.paint(1)
        robot.move(0)

        robot.paint(0)
        robot.move(1)

        assertThat(robot.grid).isEqualTo(
            mutableMapOf(
                0 to mutableMapOf(-1 to 1, 0 to 1),
                -1 to mutableMapOf(-1 to 0)
            )
        )
    }

    @Test
    fun `the grid records a different journey`() {
        val robot = Robot()

        robot.paint(1)
        robot.move(0)

        robot.paint(0)
        robot.move(1)

        robot.paint(0)
        robot.move(1)

        assertThat(robot.grid).isEqualTo(
            mutableMapOf(
                0 to mutableMapOf(-1 to 0, 0 to 1),
                1 to mutableMapOf(-1 to 0)
            )
        )
    }
}

data class Coordinate(val x: Int, val y: Int) {
    fun move(direction: Direction) =
        when (direction) {
            UP -> Coordinate(x, y + 1)
            RIGHT -> Coordinate(x + 1, y)
            DOWN -> Coordinate(x, y - 1)
            LEFT -> Coordinate(x - 1, y)
        }
}

enum class Direction {
    UP {
        override fun turn(turn: Int) =
            if (turn == 0) LEFT else RIGHT
    },
    RIGHT {
        override fun turn(turn: Int) =
            if (turn == 0) UP else DOWN
    },
    DOWN {
        override fun turn(turn: Int) =
            if (turn == 0) RIGHT else LEFT
    },
    LEFT {
        override fun turn(turn: Int) =
            if (turn == 0) DOWN else UP
    };

    abstract fun turn(turn: Int): Direction


}

class Robot {

    val grid = mutableMapOf<Int, MutableMap<Int, Int>>()

    var position = Coordinate(0, 0)
    var direction: Direction = UP

    fun paint(colour: Int) {
        val row = grid.getOrPut(position.y) { mutableMapOf() }
        row[position.x] = colour
    }

    fun move(turn: Int) {
        direction = direction.turn(turn)
        position = position.move(direction)
    }

    override fun toString(): String {
        return "Robot(position=$position, direction=$direction)"
    }


}
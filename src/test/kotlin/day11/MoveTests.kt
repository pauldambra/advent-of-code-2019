package day11

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MoveTests {
    @Test
    fun `move up from origin`() {
        val c = Coordinate(0, 0).move(Direction.UP)
        assertThat(c).isEqualTo(Coordinate(0, 1))
    }

    @Test
    fun `move down from origin`() {
        val c = Coordinate(0, 0).move(Direction.DOWN)
        assertThat(c).isEqualTo(Coordinate(0, -1))
    }

    @Test
    fun `move right from origin`() {
        val c = Coordinate(0, 0).move(Direction.RIGHT)
        assertThat(c).isEqualTo(Coordinate(1, 0))
    }

    @Test
    fun `move left from origin`() {
        val c = Coordinate(0, 0).move(Direction.LEFT)
        assertThat(c).isEqualTo(Coordinate(-1, 0))
    }

    @Test
    fun `move left and down from origin`() {
        val c = Coordinate(0, 0)
            .move(Direction.LEFT)
            .move(Direction.DOWN)
        assertThat(c).isEqualTo(Coordinate(-1, -1))
    }

    @Test
    fun `take several steps from the not origin`() {
        val c = Coordinate(3, 5)
            .move(Direction.LEFT)
            .move(Direction.DOWN)
            .move(Direction.DOWN)
            .move(Direction.RIGHT)
            .move(Direction.LEFT)
        assertThat(c).isEqualTo(Coordinate(2, 3))
    }
}
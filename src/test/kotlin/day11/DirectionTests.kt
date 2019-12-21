package day11

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DirectionTests {
    private val left = 0L
    private val right = 1L

    @Test
    fun `from up turn right`() {
        val d = Direction.UP.turn(right)
        assertThat(d).isEqualTo(Direction.RIGHT)
    }

    @Test
    fun `from up turn left`() {
        val d = Direction.UP.turn(left)
        assertThat(d).isEqualTo(Direction.LEFT)
    }

    @Test
    fun `from right turn right`() {
        val d = Direction.RIGHT.turn(right)
        assertThat(d).isEqualTo(Direction.DOWN)
    }

    @Test
    fun `from right turn left`() {
        val d = Direction.RIGHT.turn(left)
        assertThat(d).isEqualTo(Direction.UP)
    }

    @Test
    fun `from down turn right`() {
        val d = Direction.DOWN.turn(right)
        assertThat(d).isEqualTo(Direction.LEFT)
    }

    @Test
    fun `from down turn left`() {
        val d = Direction.DOWN.turn(left)
        assertThat(d).isEqualTo(Direction.RIGHT)
    }

    @Test
    fun `from left turn right`() {
        val d = Direction.LEFT.turn(right)
        assertThat(d).isEqualTo(Direction.UP)
    }

    @Test
    fun `from left turn left`() {
        val d = Direction.LEFT.turn(left)
        assertThat(d).isEqualTo(Direction.DOWN)
    }
}
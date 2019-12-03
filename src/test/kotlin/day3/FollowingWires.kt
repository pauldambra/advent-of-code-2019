package day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FollowingWires {

    data class GridCoord(val x: Int, val y: Int)

    @Test
    fun `just a straight line`() {
        val wirePath = "R2"
        val expected = listOf(
            GridCoord(0, 0),
            GridCoord(1, 0),
            GridCoord(2, 0)
        )
        assertThat(tracePath(wirePath)).isEqualTo(expected)
    }

    @Test
    fun `longer straight line`() {
        val wirePath = "R4"
        val expected = listOf(
            GridCoord(0, 0),
            GridCoord(1, 0),
            GridCoord(2, 0),
            GridCoord(3, 0),
            GridCoord(4, 0)
        )
        assertThat(tracePath(wirePath)).isEqualTo(expected)
    }

    @Test
    fun `straight line up`() {
        val wirePath = "U4"
        val expected = listOf(
            GridCoord(0, 0),
            GridCoord(0, 1),
            GridCoord(0, 2),
            GridCoord(0, 3),
            GridCoord(0, 4)
        )
        assertThat(tracePath(wirePath)).isEqualTo(expected)
    }

    @Test
    fun `straight line down`() {
        val wirePath = "D3"
        val expected = listOf(
            GridCoord(0, 0),
            GridCoord(0, -1),
            GridCoord(0, -2),
            GridCoord(0, -3)
        )
        assertThat(tracePath(wirePath)).isEqualTo(expected)
    }

    @Test
    fun `straight line left`() {
        val wirePath = "L2"
        val expected = listOf(
            GridCoord(0, 0),
            GridCoord(-1, 0),
            GridCoord(-2, 0)
        )
        assertThat(tracePath(wirePath)).isEqualTo(expected)
    }

    private fun tracePath(wirePath: String): List<GridCoord> {
        val instructions = wirePath.split(",")
        val instruction = parseInstruction(instructions)
        return when (instruction.direction) {
            "R" -> (0..instruction.count).map { GridCoord(it, 0) }
            "U" -> (0..instruction.count).map { GridCoord(0, it) }
            "D" -> (0..instruction.count).map { GridCoord(0, 0-it) }
            "L" -> (0..instruction.count).map { GridCoord(0-it, 0) }
            else -> emptyList()
        }

    }

    data class Instruction(val direction: String, val count: Int)

    private fun parseInstruction(instructions: List<String>): Instruction {
        val x = instructions.first().split("").subList(1, 3)
        return Instruction(x[0], x[1].toInt())
    }

    @Test
    fun `the first example`() {
        val wirePath = "R8,U5,L5,D3"
    }
}
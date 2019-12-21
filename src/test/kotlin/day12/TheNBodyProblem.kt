package day12

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TheNBodyProblem {
    private val puzzleInput = """
        <x=-1, y=-4, z=0>
        <x=4, y=7, z=-1>
        <x=-14, y=-10, z=9>
        <x=1, y=2, z=17>
    """.trimIndent()

    @Test
    fun `parsing input`() {
        val puzzleInput = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()

        val moons = Moon.parseInput(puzzleInput)
        assertThat(moons).containsExactlyInAnyOrder(
            Moon(-1, 0, 2),
            Moon(2, -10, -7),
            Moon(4, -8, 8),
            Moon(3, 5, -1)
        )
    }

    @Test
    fun `a new moon has velocity`() {
        val m = Moon(-1, 0, 2)
        val expected: Vector = Vector(0, 0, 0)
        assertThat(m.velocity).isEqualTo(expected)
    }

    @Test
    fun `can apply gravity`() {
        val a = Moon(3, 0, 2)
        val b = Moon(5, 0, 2)
        a.applyGravity(b)

        assertThat(a.velocity).isEqualTo(Vector(1, 0, 0))
    }

    @Test
    fun `can apply gravity for a different moon`() {
        val a = Moon(5, 0, 2)
        val b = Moon(3, 0, 2)
        a.applyGravity(b)

        assertThat(a.velocity).isEqualTo(Vector(-1, 0, 0))
        assertThat(b.velocity).isEqualTo(Vector(1, 0, 0))
    }

    @Test
    fun `can apply gravity and change more than one vector property`() {
        val a = Moon(5, 0, 3)
        val b = Moon(3, 1, 2)
        a.applyGravity(b)

        assertThat(a.velocity).isEqualTo(Vector(-1, 1, -1))
    }

    @Test
    fun `can apply velocity a positive velocity to each property of a moon's position`() {
        val a = Moon(5, 0, 3)
        a.velocity.x = 1
        a.velocity.y = 2
        a.velocity.z = 3

        a.applyVelocity()

        assertThat(a).isEqualTo(Moon(6, 2, 6, 1, 2, 3))
    }

    @Test
    fun `can parse a description`() {
        val s = "pos=<x= 5, y=-3, z=-1>, vel=<x= 3, y=-2, z=-2>"
        assertThat(Moon.parseDescription(s)).isEqualTo(Moon(5,-3,-1,3,-2,-2))
    }

    @Test
    fun `can apply a step to a moon system`() {
        val puzzleInput = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()
        val moons = Moon.parseInput(puzzleInput)

        Moon.applyStep(moons)

        assertThat(moons).containsExactly(
            Moon(2, -1, 1, 3, -1, -1),
            Moon(3, -7, -4, 1, 3, 3),
            Moon(1, -7, 5, -3, 1, -3),
            Moon(2, 2, 0, -1, -3, 1)
        )
    }

    @Test
    fun `can apply 2 steps to a moon system`() {
        val puzzleInput = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()
        val moons = Moon.parseInput(puzzleInput)

        Moon.applyStep(moons)
        Moon.applyStep(moons)

        val expectedMoons = """
            pos=<x= 5, y=-3, z=-1>, vel=<x= 3, y=-2, z=-2>
            pos=<x= 1, y=-2, z= 2>, vel=<x=-2, y= 5, z= 6>
            pos=<x= 1, y=-4, z=-1>, vel=<x= 0, y= 3, z=-6>
            pos=<x= 1, y=-4, z= 2>, vel=<x=-1, y=-6, z= 2>
        """.trimIndent()
            .split("\n")
            .map { Moon.parseDescription(it) }

        assertThat(moons).isEqualTo(expectedMoons)
    }

    @Test
    fun `can apply ten steps to a moon system`() {
        val puzzleInput = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()
        val moons = Moon.parseInput(puzzleInput)

        (0..9).forEach { _ -> Moon.applyStep(moons) }

        val expected = """
            pos=<x= 2, y= 1, z=-3>, vel=<x=-3, y=-2, z= 1>
            pos=<x= 1, y=-8, z= 0>, vel=<x=-1, y= 1, z= 3>
            pos=<x= 3, y=-6, z= 1>, vel=<x= 3, y= 2, z=-3>
            pos=<x= 2, y= 0, z= 4>, vel=<x= 1, y=-1, z=-1>
        """.trimIndent()
            .split("\n")
            .map { Moon.parseDescription(it) }

        assertThat(moons).isEqualTo(expected)
    }

    @Test
    fun `can calculate total energy after ten steps in the moon system`() {
        val puzzleInput = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()
        val moons = Moon.parseInput(puzzleInput)

        (0..9).forEach { _ -> Moon.applyStep(moons) }

        val totalEnergy = Moon.totalEnergy(moons)
        assertThat(totalEnergy).isEqualTo(179)
    }

    @Test
    fun `solve part 1`() {
        val moons = Moon.parseInput(puzzleInput)

        (0..999).forEach { _ -> Moon.applyStep(moons) }

        val totalEnergy = Moon.totalEnergy(moons)
        assertThat(totalEnergy).isEqualTo(179)
    }
}

package day12

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RepeatingStates {
    @Test
    fun `can find repeats`() {
        val puzzleInput = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()

        val originalState = Moon.parseInput(puzzleInput)

        val moons = Moon.parseInput(puzzleInput)

        val count = Moon.seekPeriod(moons)

        assertThat(count).isEqualTo(2772)
    }

//    @Test
//    fun `can find repeats in example 2`() {
//        val puzzleInput = """
//            <x=-8, y=-10, z=0>
//            <x=5, y=5, z=10>
//            <x=2, y=-7, z=3>
//            <x=9, y=-8, z=-3>
//        """.trimIndent()
//
//        val originalState = Moon.parseInput(puzzleInput)
//
//        val moons = Moon.parseInput(puzzleInput)
//
//         val count = Moon.seekPeriod(moons)
//
//
//        assertThat(count).isEqualTo(4686774924)
//    }
}
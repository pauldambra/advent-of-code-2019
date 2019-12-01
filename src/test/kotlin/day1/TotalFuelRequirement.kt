package day1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.Integer.parseInt

class TotalFuelRequirement {
    @Test
    fun `calculate total fuel requirement by summing fuel requirements`() {
        val masses = listOf(12, 14, 1969, 100756)
        val totalFuelRequirement: Int = calculateTotalFuelRequirement(masses)
        assertThat(totalFuelRequirement).isEqualTo(34241)
    }

    @Test
    fun `calculate total fuel requirement for part 1`() {
        val masses = this::class.java.getResource("/day1/puzzleInput.txt")
            .readText().split("\n")
            .filter { it.isNotEmpty() }
            .map { parseInt(it) }
        val totalFuelRequirement: Int = calculateTotalFuelRequirement(masses)
        assertThat(totalFuelRequirement).isEqualTo(3372463)
    }
}
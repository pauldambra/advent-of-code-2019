package day1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.floor

/**
 * Fuel required to launch a given module is based on its mass.
 * Specifically, to find the fuel required for a module,
 * take its mass, divide by three, round down, and subtract 2.
 */
class CalculateFuel {

    @Test
    fun `For a mass of 12 divide by 3 and round down to get 4 then subtract 2 to get 2`() {
        val fuelRequired = calculateFuelForMass(12)
        assertThat(fuelRequired).isEqualTo(2)
    }

    @Test
    fun `For a mass of 14 dividing by 3 and rounding down still yields 4 so the fuel required is also 2`() {
        val fuelRequired = calculateFuelForMass(14)
        assertThat(fuelRequired).isEqualTo(2)
    }
    @Test
    fun `For a mass of 1969 the fuel required is 654`() {
        val fuelRequired = calculateFuelForMass(1969)
        assertThat(fuelRequired).isEqualTo(654)
    }
    @Test
    fun `For a mass of 100756 the fuel required is 33583`() {
        val fuelRequired = calculateFuelForMass(100756)
        assertThat(fuelRequired).isEqualTo(33583)
    }
}
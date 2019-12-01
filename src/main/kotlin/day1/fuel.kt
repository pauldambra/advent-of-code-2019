package day1

import kotlin.math.floor

fun calculateFuelForMass(mass: Int) = (floor((mass / 3).toDouble()) - 2).toInt()

fun calculateTotalFuelRequirement(masses: List<Int>) =
    masses
        .map { x -> calculateFuelForMass(x) }
        .sum()

fun calculateFuelForMassTakingFuelIntoAccount(moduleMass: Int)
        = takeFuelIntoAccount(moduleMass, 0)

private fun takeFuelIntoAccount(nextMass: Int, currentTotal: Int): Int {
    val nextFuel = calculateFuelForMass(nextMass)
    return if (nextFuel > 0) {
        takeFuelIntoAccount(nextFuel, currentTotal + nextFuel)
    } else {
        currentTotal
    }
}
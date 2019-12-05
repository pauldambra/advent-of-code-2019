package day4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TwoAdjacentMatchingDigitsAreNotPartOfALargerGroupOfMatchingDigits {
    @Test
    fun `112233 meets these criteria because the digits never decrease and all repeated digits are exactly two digits long`() {
        val candidate = "112233"
        val actual = PartTwoPasswordValidator("000000", "300000").isValid(candidate)
        assertThat(actual).isEqualTo(true)
    }

    @Test
    fun `123444 no longer meets the criteria (the repeated 44 is part of a larger group of 444)`() {
        val candidate = "123444"
        val actual = PartTwoPasswordValidator("000000", "300000").isValid(candidate)
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `111122 meets the criteria (even though 1 is repeated more than twice, it still contains a double 22)`() {
        val candidate = "111122"
        val actual = PartTwoPasswordValidator("000000", "300000").isValid(candidate)
        assertThat(actual).isEqualTo(true)
    }

    @Test
    fun `can solve part 2`() {
        val validator = PartTwoPasswordValidator("197487", "673251")
        val validPasswordsInRange = (197487..673251)
            .map { it.toString() }
            .map { validator.isValid(it) }
            .count { it }
        assertThat(validPasswordsInRange).isLessThan(1151)
        assertThat(validPasswordsInRange).isGreaterThan(654)
        assertThat(validPasswordsInRange).isEqualTo(1126)
    }

    class PartTwoPasswordValidator(private val min: String, private val max: String) {
        fun isValid(candidate: String) =
            exactlyOneOfTheRepeatedDigitsRepeatsTwice(candidate) && PasswordValidator(min, max).isValid(candidate)

        private fun exactlyOneOfTheRepeatedDigitsRepeatsTwice(candidate: String): Boolean {
            val groups = mutableMapOf<String, Int>()
            var last: String? = null
            candidate.forEach { ch ->
                val s = ch.toString()

                if (s == last) {
                    val current = groups.getOrPut(s) { 1 }
                    groups[s] = current + 1
                }
                last = s

            }

            val repeatingGroups = groups.filter { x -> x.value == 2 }

            val atLeastOnePair = AtLeastOnePair(
                repeatingGroups.isNotEmpty()
            )
//            println(atLeastOnePair)
            return atLeastOnePair.isValid
        }

        data class AtLeastOnePair(
            val OnlyOnePair: Boolean
        ) {
            val isValid = OnlyOnePair
        }
    }
}
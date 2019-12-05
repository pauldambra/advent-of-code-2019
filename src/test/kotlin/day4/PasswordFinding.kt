package day4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PasswordFinding {

    @Test
    fun `can invalidate when less than 6 digit number`() {
        val candidate = "11"
        val actual = PasswordValidator("000000", "999999").isValid(candidate)
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `can invalidate when more than 6 digit number`() {
        val candidate = "1111111"
        val actual = PasswordValidator("000000", "999999").isValid(candidate)
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `can validate when exactly 6 digit number`() {
        val candidate = "111111"
        val actual = PasswordValidator("000000", "999999").isValid(candidate)
        assertThat(actual).isEqualTo(true)
    }

    @Test
    fun `can validate on presence of 2 repeating numbers`() {
        val candidate = "112222"
        val actual = PasswordValidator("000000", "999999").isValid(candidate)
        assertThat(actual).isEqualTo(true)
    }

    @Test
    fun `can invalidate when lack of 2 repeating numbers`() {
        val candidate = "123456"
        val actual = PasswordValidator("000000", "999999").isValid(candidate)
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `can invalidate if there are decreasing numbers from left to right`() {
        val candidate = "123400"
        val actual = PasswordValidator("000000", "999999").isValid(candidate)
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `can invalidate when numbers are outside of a given range`() {
        val candidate = "110000"
        val actual = PasswordValidator("000000", "100000").isValid(candidate)
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `can solve part 1`() {
        val validator = PasswordValidator("197487", "673251")
        val validPasswordsInRange = (197487..673251)
            .map { it.toString() }
            .map { validator.isValid(it) }
            .count { it }
        assertThat(validPasswordsInRange).isEqualTo(1640)
    }

}
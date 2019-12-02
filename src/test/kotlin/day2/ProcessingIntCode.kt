package day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProcessingIntCode {

    @Test
    fun `1,0,0,0,99 becomes 2,0,0,0,99 (1 + 1 = 2)`() {
        val input = "1,0,0,0,99"
        val output = "2,0,0,0,99"
        assertThat(runProgram(input)).isEqualTo(output)
    }

    private fun runProgram(input: String): String {
        return input
    }

    @Test
    fun `2,3,0,3,99 becomes 2,3,0,6,99 (3 * 2 = 6)`() {
        val input = "2,3,0,3,99"
        val output = "2,3,0,6,99"
        assertThat(runProgram(input)).isEqualTo(output)
    }
    @Test
    fun `2,4,4,5,99,0 becomes 2,4,4,5,99,9801 (99 * 99 = 9801)`() {
        val input = "2,4,4,5,99,0"
        val output = "2,4,4,5,99,9801"
        assertThat(runProgram(input)).isEqualTo(output)
    }
    @Test
    fun `1,1,1,4,99,5,6,0,99 becomes 30,1,1,4,2,5,6,0,99`() {
        val input = "1,1,1,4,99,5,6,0,99"
        val output = "30,1,1,4,2,5,6,0,99"
        assertThat(runProgram(input)).isEqualTo(output)
    }
}
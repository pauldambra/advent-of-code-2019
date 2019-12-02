package day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProcessingIntCode {

    private fun processOpCode(instruction: String): String {
        val x = instruction.split(",").map { it.toInt() }.toMutableList()
        val a = x[1]
        val b = x[2]
        val i = x[3]
        x[i] = a + b
        return x.joinToString(",")
    }

    @Test
    fun `process a single block of addition OpCode`() {
        val input = "1,10,20,2"
        val output = "1,10,30,2"
        assertThat(processOpCode(input)).isEqualTo(output)
    }

    @Test
    fun `process a single block of addition OpCode to a different output`() {
        val input = "1,10,20,1"
        val output = "1,30,20,1"
        assertThat(processOpCode(input)).isEqualTo(output)
    }

    @Test
    fun `process a single block of addition OpCode that requires adding padding`() {
        val input = "1,10,20,30"
        val output = "1,10,20,30,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,30"
    }
}
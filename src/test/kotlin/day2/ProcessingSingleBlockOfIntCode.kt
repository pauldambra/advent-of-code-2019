package day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProcessingSingleBlockOfIntCode {

    @Test
    fun `process a single block of addition OpCode`() {
        val input = "1,10,20,2,99"
        val output = "1,10,30,2,99"
        assertThat(processOpCode(input)).isEqualTo(output)
    }

    @Test
    fun `process a single block of addition OpCode to a different output`() {
        val input = "1,10,20,1,99"
        val output = "1,30,20,1,99"
        assertThat(processOpCode(input)).isEqualTo(output)
    }

    @Test
    fun `process a single block of multiplication OpCode`() {
        val input = "2,10,20,1,99"
        val output = "2,200,20,1,99"
        assertThat(processOpCode(input)).isEqualTo(output)
    }

    @Test
    fun `process a single block of addition OpCode that requires a single index padding`() {
        val input = "1,10,89,4"
        val output = "1,10,89,4,99"
        assertThat(processOpCode(input)).isEqualTo(output)
    }

    @Test
    fun `process a single block of OpCode that stops operation`() {
        val input = "99,10,20,4"
        val output = "99,10,20,4"
        assertThat(processOpCode(input)).isEqualTo(output)
    }

    @Test
    fun `process a single block of addition OpCode that requires adding padding`() {
        val input = "1,10,20,30,99"
        val output = "1,10,20,30,99,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,30"
        assertThat(processOpCode(input)).isEqualTo(output)
    }
}
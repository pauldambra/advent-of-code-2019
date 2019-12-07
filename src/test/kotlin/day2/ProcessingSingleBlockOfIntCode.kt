package day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProcessingSingleBlockOfIntCode {

    @Test
    fun `process a single block of addition OpCode`() {
        val input = "1,10,11,2,99,0,0,0,0,0,10,20"
        val finalMemory = "1,10,30,2,99,0,0,0,0,0,10,20"
        assertThat(ShipsComputer.runProgram(input).memory).isEqualTo(finalMemory)
    }

    @Test
    fun `process a single block of addition OpCode to a different output`() {
        val input = "1,5,6,1,99,10,20"
        val finalMemory = "1,30,6,1,99,10,20"
        assertThat(ShipsComputer.runProgram(input).memory).isEqualTo(finalMemory)
    }

    @Test
    fun `process a single block of multiplication OpCode`() {
        val input = "2,10,11,1,99,0,0,0,0,0,10,20"
        val finalMemory = "2,200,11,1,99,0,0,0,0,0,10,20"
        assertThat(ShipsComputer.runProgram(input).memory).isEqualTo(finalMemory)
    }

    @Test
    fun `process a single block of addition OpCode that requires a single index padding`() {
        val input = "1,0,3,3,99"
        val finalMemory = "1,0,3,4,99"
        assertThat(ShipsComputer.runProgram(input).memory).isEqualTo(finalMemory)
    }

    @Test
    fun `process a single block of OpCode that stops operation`() {
        val input = "99,10,20,4"
        val finalMemory = "99,10,20,4"
        assertThat(ShipsComputer.runProgram(input).memory).isEqualTo(finalMemory)
    }

    @Test
    fun `process a single block of addition OpCode that requires adding padding`() {
        val input = "1,5,6,30,99,10,20"
        val finalMemory = "1,5,6,30,99,10,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,30"
        assertThat(ShipsComputer.runProgram(input).memory).isEqualTo(finalMemory)
    }
}
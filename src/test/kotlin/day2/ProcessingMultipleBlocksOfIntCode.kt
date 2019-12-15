package day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProcessingMultipleBlocksOfIntCode {

    @Test
    fun `process two blocks of addition OpCode`() {
        val input = "1,5,6,1,1,10,11,2,99,0,10,20"
        val finalMemory = "1,21,30,1,1,10,11,2,99,0,10,20"
        assertThat(ShipsComputer().runProgram(input).memory).isEqualTo(finalMemory)
    }
}
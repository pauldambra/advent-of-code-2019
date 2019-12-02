package day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProcessingMultipleBlocksOfIntCode {

    @Test
    fun `process two blocks of addition OpCode`() {
        val input = "1,10,20,2,1,20,20,2,99"
        val output = "1,10,40,2,1,20,20,2,99"
        assertThat(processOpCode(input)).isEqualTo(output)
    }

//    @Test
//    fun `process a single block of addition OpCode to a different output`() {
//        val input = "1,10,20,1"
//        val output = "1,30,20,1"
//        assertThat(processOpCode(input)).isEqualTo(output)
//    }
//
//    @Test
//    fun `process a single block of multiplication OpCode`() {
//        val input = "2,10,20,1"
//        val output = "2,200,20,1"
//        assertThat(processOpCode(input)).isEqualTo(output)
//    }
//
//    @Test
//    fun `process a single block of addition OpCode that requires a single index padding`() {
//        val input = "1,10,20,4"
//        val output = "1,10,20,4,30"
//        assertThat(processOpCode(input)).isEqualTo(output)
//    }
//
//    @Test
//    fun `process a single block of addition OpCode that requires adding padding`() {
//        val input = "1,10,20,30"
//        val output = "1,10,20,30,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,30"
//        assertThat(processOpCode(input)).isEqualTo(output)
//    }
}
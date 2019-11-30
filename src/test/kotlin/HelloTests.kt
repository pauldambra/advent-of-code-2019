import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HelloTests {

    @Test
    fun `you can run a test with an assertion`() {
        assertThat(1).isEqualTo(1)
    }
}
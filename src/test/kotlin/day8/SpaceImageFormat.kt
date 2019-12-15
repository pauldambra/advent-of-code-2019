package day8

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpaceImageFormat {

    /**
     * For example, given an image 3 pixels wide and 2 pixels tall,
     * the image data 123456789012 corresponds to the following image layers:

    Layer 1: 123
    456

    Layer 2: 789
    012

     */
    @Test
    fun `the example 3 wide and 2 tall`() {
        val imageData = "123456789012"
        val layers = Layer.processSpaceImageFormat(imageData, 3, 2)

        val expectedLayers = listOf(
            Layer(listOf(1, 2, 3, 4, 5, 6), 3, 2),
            Layer(listOf(7, 8, 9, 0, 1, 2), 3, 2)
        )

        assertThat(layers).isEqualTo(expectedLayers)
    }

    @Test
    fun `solving part one`() {
        val imageData = this::class.java.getResource("/day8/puzzleInput.txt").readText().trim()
        val layers = Layer.processSpaceImageFormat(imageData, 25, 6)

        val checksum = checksumLayers(layers)

        assertThat(checksum).isEqualTo(2356)
    }

    @Test
    fun `example part two`() {
        val imageData = "0222112222120000"
        val layers = Layer.processSpaceImageFormat(imageData, 2, 2)

        val processedImage = printImage(layers, 2, 2)
        assertThat(processedImage).isEqualTo(
            """
            01
            10
        """.trimIndent()
        )
    }

    @Test
    fun `solve part two`() {
        val imageData = this::class.java.getResource("/day8/puzzleInput.txt").readText().trim()
        val layers = Layer.processSpaceImageFormat(imageData, 25, 6)
        val processedImage = printImage(layers, 25, 6)
        assertThat(processedImage).isEqualTo("""
            1110011110111101001011100
            1001000010100001010010010
            1001000100111001100011100
            1110001000100001010010010
            1000010000100001010010010
            1000011110111101001011100
        """.trimIndent())
        //replace the zeroes with spaces and that says PZEKB
    }
}

data class Layer(val pixels: List<Int>, val width: Int, val height: Int) {
    fun pixelAt(x: Int, y: Int): Int {
        return rows[y][x]
    }

    private val rows: List<List<Int>> = pixels.chunked(width)

    init {
        check(rows.size == height) { "there are ${rows.size} rows but should be $height" }
    }

    companion object {
        fun processSpaceImageFormat(imageData: String, width: Int, height: Int) =
            imageData
                .map { it.toString().toInt() }
                .chunked(width * height)
                .map { Layer(it, width, height) }
    }
}

fun checksumLayers(layers: List<Layer>): Int {
    val mostZeroes = layers.minBy { l -> l.pixels.count { x -> x == 0 } }!!
    val ones = mostZeroes.pixels.count { it == 1 }
    val twos = mostZeroes.pixels.count { it == 2 }
    return ones * twos
}

fun printImage(layers: List<Layer>, width: Int, height: Int): String {

    val collapsedLayer = mutableMapOf<Int, MutableList<Int>>()

    (0 until width).forEach { x ->
        (0 until height).forEach { y ->
            val pixelsAtCoordinate = layers.map { l -> l.pixelAt(x, y) }
            val visiblePixel = pixelsAtCoordinate.first { it != 2 }
            val row = collapsedLayer.getOrPut(y) { mutableListOf() }
            row.add(x, visiblePixel)
        }
    }

    return collapsedLayer.values
        .joinToString("\n") { it.joinToString("") }
}
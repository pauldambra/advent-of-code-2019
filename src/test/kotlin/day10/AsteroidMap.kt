package day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.atan2

class AsteroidMaps {

    @Test
    fun `can convert map input to asteroid coordinates`() {
        val s = """
            .#..#
            .....
            #####
            ....#
            ...##
        """.trimIndent()
        val map = AsteroidMap(s)

        assertThat(map.asteroids).containsExactlyInAnyOrder(
            Asteroid(x = 1, y = 0),
            Asteroid(x = 4, y = 0),
            Asteroid(x = 0, y = 2),
            Asteroid(x = 1, y = 2),
            Asteroid(x = 2, y = 2),
            Asteroid(x = 3, y = 2),
            Asteroid(x = 4, y = 2),
            Asteroid(x = 4, y = 3),
            Asteroid(x = 3, y = 4),
            Asteroid(x = 4, y = 4)
        )

        val bestLocation = map.linesOfSight.maxBy { it.value.size }!!
        assertThat(bestLocation.key).isEqualTo(Asteroid(3, 4))
        assertThat(bestLocation.value.size).isEqualTo(8)
    }

    @Test
    fun example2() {
        val s = """
            ......#.#.
            #..#.#....
            ..#######.
            .#.#.###..
            .#..#.....
            ..#....#.#
            #..#....#.
            .##.#..###
            ##...#..#.
            .#....####
        """.trimIndent()
        val map = AsteroidMap(s)

        val bestLocation = map.linesOfSight.maxBy { it.value.size }!!
        assertThat(bestLocation.key).isEqualTo(Asteroid(5, 8))
        assertThat(bestLocation.value.size).isEqualTo(33)
    }

    @Test
    fun example3() {
        val s = """
            #.#...#.#.
            .###....#.
            .#....#...
            ##.#.#.#.#
            ....#.#.#.
            .##..###.#
            ..#...##..
            ..##....##
            ......#...
            .####.###.
        """.trimIndent()
        val map = AsteroidMap(s)

        val bestLocation = map.linesOfSight.maxBy { it.value.size }!!
        assertThat(bestLocation.key).isEqualTo(Asteroid(1, 2))
        assertThat(bestLocation.value.size).isEqualTo(35)
    }

    @Test
    fun example4() {
        val s = """
            .#..#..###
            ####.###.#
            ....###.#.
            ..###.##.#
            ##.##.#.#.
            ....###..#
            ..#.#..#.#
            #..#.#.###
            .##...##.#
            .....#.#..
        """.trimIndent()
        val map = AsteroidMap(s)

        val bestLocation = map.linesOfSight.maxBy { it.value.size }!!
        assertThat(bestLocation.key).isEqualTo(Asteroid(6, 3))
        assertThat(bestLocation.value.size).isEqualTo(41)
    }

    @Test
    fun example5() {
        val s = """
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
        """.trimIndent()
        val map = AsteroidMap(s)

        val bestLocation = map.linesOfSight.maxBy { it.value.size }!!
        assertThat(bestLocation.key).isEqualTo(Asteroid(11, 13))
        assertThat(bestLocation.value.size).isEqualTo(210)
    }

    @Test
    fun part1() {
        val s = """
            ##.#..#..###.####...######
            #..#####...###.###..#.###.
            ..#.#####....####.#.#...##
            .##..#.#....##..##.#.#....
            #.####...#.###..#.##.#..#.
            ..#..#.#######.####...#.##
            #...####.#...#.#####..#.#.
            .#..#.##.#....########..##
            ......##.####.#.##....####
            .##.#....#####.####.#.####
            ..#.#.#.#....#....##.#....
            ....#######..#.##.#.##.###
            ###.#######.#..#########..
            ###.#.#..#....#..#.##..##.
            #####.#..#.#..###.#.##.###
            .#####.#####....#..###...#
            ##.#.......###.##.#.##....
            ...#.#.#.###.#.#..##..####
            #....#####.##.###...####.#
            #.##.#.######.##..#####.##
            #.###.##..##.##.#.###..###
            #.####..######...#...#####
            #..#..########.#.#...#..##
            .##..#.####....#..#..#....
            .###.##..#####...###.#.#.#
            .##..######...###..#####.#
        """.trimIndent()
        val map = AsteroidMap(s)

        val bestLocation = map.linesOfSight.maxBy { it.value.size }!!
        assertThat(bestLocation.key).isEqualTo(Asteroid(20, 19))
        assertThat(bestLocation.value.size).isEqualTo(284)
    }
}

class AsteroidMap(drawing: String) {
    var asteroids: MutableList<Asteroid> = mutableListOf()

    var linesOfSight: MutableMap<Asteroid, List<Double>> = mutableMapOf()

    init {
        val rows = drawing.split("\n")
        rows.forEachIndexed { y, r ->
            r.forEachIndexed { x, c ->
                if (c == '#') {
                    asteroids.add(Asteroid(x, y))
                }
            }
        }

        asteroids.forEach { a ->
            val slopes = asteroids.filter { it != a }
                .map { b -> atan2((a.x - b.x).toDouble(), (a.y - b.y).toDouble()) }
            linesOfSight[a] = slopes.distinct()
        }
    }
}

data class Asteroid(val x: Int, val y: Int)

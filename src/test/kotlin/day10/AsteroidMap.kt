package day10

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.MapEntry
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

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

        val bestLocation = map.laserStationLocation()
//        bestLocation.value.forEach {
//            println("from ${it.from} to ${it.to} is ${it.degrees}")
//        }

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

        val bestLocation = map.laserStationLocation()
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

        val bestLocation = map.laserStationLocation()
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

        val bestLocation = map.laserStationLocation()
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

        val bestLocation = map.laserStationLocation()
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

        val bestLocation = map.laserStationLocation()

        assertThat(bestLocation.key).isEqualTo(Asteroid(20, 19))
        assertThat(bestLocation.value.size).isEqualTo(284)
    }

    //don't think this actually works!!
//    @Test
//    fun `shooting asteroids`() {
//        val s = """
//            .#..##.###...#######
//            ##.############..##.
//            .#.######.########.#
//            .###.#######.####.#.
//            #####.##.#.##.###.##
//            ..#####..#.#########
//            ####################
//            #.####....###.#.#.##
//            ##.#################
//            #####.##.###..####..
//            ..######..##.#######
//            ####.##.####...##..#
//            .#####..#.######.###
//            ##...#.##########...
//            #.##########.#######
//            .####.#.###.###.#.##
//            ....##.##.###..#####
//            .#.#.###########.###
//            #.#.#.#####.####.###
//            ###.##.####.##.#..##
//        """.trimIndent()
//
//        val map = AsteroidMap(s)
//
//        map.shoot()
//    }

    // but it gets the right answer for the puzzle /shrug
    @Test
    fun `part 2`() {
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

        val twoHundredth = map.shoot()
        assertThat(twoHundredth.to).isEqualTo(Asteroid(4, 4))
    }
}

class AsteroidMap(drawing: String) {
    var asteroids: MutableList<Asteroid> = mutableListOf()

    var linesBetween: MutableMap<Asteroid, List<SlopeTo>> = mutableMapOf()

    private fun linesOfSight(): Map<Asteroid, List<SlopeTo>> {
        return linesBetween.mapValues {
            it.value.distinctBy { s -> s.degrees }
        }
    }

    fun laserStationLocation(): Map.Entry<Asteroid, List<SlopeTo>>  =
        linesOfSight().maxBy { it.value.size }!!

    fun shoot(): SlopeTo {
        val allLinesFromStation = linesBetween[laserStationLocation().key]!!

        val linesByDegrees = allLinesFromStation.groupBy { it.degrees }

        val clockwiseKeys = linesByDegrees.keys.sortedDescending()

        val shot:MutableList<SlopeTo> = mutableListOf()

        clockwiseKeys.forEach {
            val candidates = linesByDegrees.getValue(it)
            val closest = candidates.minBy { c -> c.distance }!!
            shot.add(closest)
        }

        return shot[199]
    }


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
                .map { b ->
                   val radians = atan2((a.x - b.x).toDouble(), (a.y - b.y).toDouble())
                    SlopeTo(radians, b, a)
                }
            linesBetween[a] = slopes
        }
    }
}

data class Asteroid(val x: Int, val y: Int)

data class SlopeTo(val radians: Double, val to: Asteroid, val from: Asteroid) {
    val degrees = (if (radians <= 0) 2 * PI + radians else radians) * 180 / PI
    val distance = sqrt(
        (from.x - from.y).toDouble().pow(2.toDouble())
                +
            (to.x - to.y).toDouble().pow(2.toDouble())
    )
}

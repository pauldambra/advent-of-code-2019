package day12

import kotlin.math.abs

class Vector(x: Int, y: Int, z: Int) {

    val vs = intArrayOf(x, y, z)

    fun totalEnergy() = abs(vs[0]) + abs(vs[1]) + abs(vs[2])

    operator fun plus(other: Vector) {
        vs[0] = vs[0] + other.vs[0]
        vs[1] = vs[1] + other.vs[1]
        vs[2] = vs[2] + other.vs[2]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector

        if (!vs.contentEquals(other.vs)) return false

        return true
    }

    override fun hashCode(): Int {
        return vs.contentHashCode()
    }


}

class Moon(x: Int, y: Int, z: Int, vx: Int = 0, vy: Int = 0, vz: Int = 0) {
    var position = Vector(x, y, z)
    val velocity = Vector(vx, vy, vz)

    fun applyGravity(b: Moon) {
        // println("before: $this and $b")
        applyGravityToX(b)
        applyGravityToY(b)
        applyGravityToZ(b)
    }

    private fun applyGravityTo(b: Moon, i: Int) {
        when {
            position.vs[i] > b.position.vs[i] -> {
                velocity.vs[i] -= 1
                b.velocity.vs[i] += 1
            }
            position.vs[i] < b.position.vs[i] -> {
                velocity.vs[i] += 1
                b.velocity.vs[i] -= 1
            }
        }
    }

    private fun applyGravityToX(b: Moon) {
        applyGravityTo(b, 0)
    }

    private fun applyGravityToY(b: Moon) {
        applyGravityTo(b, 1)
    }

    private fun applyGravityToZ(b: Moon) {
        applyGravityTo(b, 2)
    }

    fun applyVelocity() {
        position.plus(velocity)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Moon

        if (position != other.position) return false
        if (velocity != other.velocity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + velocity.hashCode()
        return result
    }

    override fun toString(): String {
        return "Moon(position=$position, velocity=$velocity)"
    }

    companion object {
        /**
         * input looks like <x=-1, y=0, z=2>
         */
        fun parseInput(s: String): List<Moon> {
            return s.split("\n")
                .map { it.trim('<', '>') }
                .map { it.split(",") }
                .map { it.map { x -> x.split("=") } }
                .map { Moon(it[0][1].toInt(), it[1][1].toInt(), it[2][1].toInt()) }
        }

        /**
         * descriptions look like pos=<x= 5, y=-3, z=-1>, vel=<x= 3, y=-2, z=-2>
         */
        fun parseDescription(s: String): Moon {

            val x =
                s.replace("pos=<", "")
                    .replace("vel=<", "")
                    .replace(">", "")
                    .split(",")
                    .map { it.split("= ", "=") }
                    .map { it[1] }
                    .map { it.toInt() }

            return Moon(x[0], x[1], x[2], x[3], x[4], x[5])
        }

        fun applyStep(moons: List<Moon>) {
            applyGravityToEachCombinationOfMoonPairs(moons)
            moons.forEach(Moon::applyVelocity)
        }

        private fun applyStepToX(moons: List<Moon>) {
            applyGravityToEachCombinationOfMoonPairsX(moons)
            moons.forEach(Moon::applyVelocity)
        }

        private fun applyStepToY(moons: List<Moon>) {
            applyGravityToEachCombinationOfMoonPairsY(moons)
            moons.forEach(Moon::applyVelocity)
        }

        private fun applyStepToZ(moons: List<Moon>) {
            applyGravityToEachCombinationOfMoonPairsY(moons)
            moons.forEach(Moon::applyVelocity)
        }

        private fun applyGravityToEachCombinationOfMoonPairs(moons: List<Moon>) {
            moons[0].applyGravity(moons[1])
            moons[0].applyGravity(moons[2])
            moons[0].applyGravity(moons[3])
            moons[1].applyGravity(moons[2])
            moons[1].applyGravity(moons[3])
            moons[2].applyGravity(moons[3])
        }

        private fun applyGravityToEachCombinationOfMoonPairsX(moons: List<Moon>) {
            moons[0].applyGravityToX(moons[1])
            moons[0].applyGravityToX(moons[2])
            moons[0].applyGravityToX(moons[3])
            moons[1].applyGravityToX(moons[2])
            moons[1].applyGravityToX(moons[3])
            moons[2].applyGravityToX(moons[3])
        }

        private fun applyGravityToEachCombinationOfMoonPairsY(moons: List<Moon>) {
            moons[0].applyGravityToY(moons[1])
            moons[0].applyGravityToY(moons[2])
            moons[0].applyGravityToY(moons[3])
            moons[1].applyGravityToY(moons[2])
            moons[1].applyGravityToY(moons[3])
            moons[2].applyGravityToY(moons[3])
        }

        private fun applyGravityToEachCombinationOfMoonPairsZ(moons: List<Moon>) {
            moons[0].applyGravityToZ(moons[1])
            moons[0].applyGravityToZ(moons[2])
            moons[0].applyGravityToZ(moons[3])
            moons[1].applyGravityToZ(moons[2])
            moons[1].applyGravityToZ(moons[3])
            moons[2].applyGravityToZ(moons[3])
        }

        fun totalEnergy(moons: List<Moon>) =
            moons.sumBy { it.position.totalEnergy() * it.velocity.totalEnergy() }

        fun seekPeriod(moons: List<Moon>): Int {

            val xPeriod = seekPeriodX(moons)
            println("xPeriod: $xPeriod")
            val yPeriod = seekPeriodY(moons)
            println("yPeriod: $yPeriod")
            val zPeriod = seekPeriodZ(moons)
            println("zPeriod: $zPeriod")


            return 0
        }

        private fun seekPeriodX(moons: List<Moon>): Int {
            val ms = mutableListOf<Moon>()
            ms.addAll(moons)
            val m = ms.toList()

            var shouldContinue = true
            var result = 0

            while (shouldContinue) {
                applyStepToX(m)
                result++
                if (
                    moonIsBackAtInitialStateX(m, moons, 0)
                    && moonIsBackAtInitialStateX(m, moons, 1)
                    && moonIsBackAtInitialStateX(m, moons, 2)
                    && moonIsBackAtInitialStateX(m, moons, 3)
                ) {
                    shouldContinue = false
                }
                if (result % 1000000 == 0) {
                    println("x count so far $result")
                }
            }

            return result
        }

        private fun seekPeriodY(moons: List<Moon>): Int {
            val ms = mutableListOf<Moon>()
            ms.addAll(moons)
            val m = ms.toList()

            var shouldContinue = true
            var result = 0

            while (shouldContinue) {
                applyStepToY(m)
                result++
                if (
                    moonIsBackAtInitialStateY(m, moons, 0)
                    && moonIsBackAtInitialStateY(m, moons, 1)
                    && moonIsBackAtInitialStateY(m, moons, 2)
                    && moonIsBackAtInitialStateY(m, moons, 3)
                ) {
                    shouldContinue = false
                }
            }

            return result
        }

        private fun moonIsBackAtInitialStateX(
            m: List<Moon>,
            moons: List<Moon>,
            index: Int
        ) = m[index].position.vs[0] == moons[index].position.vs[0] && m[index].velocity.vs[0] == index

        private fun moonIsBackAtInitialStateY(
            m: List<Moon>,
            moons: List<Moon>,
            index: Int
        ) = m[index].position.vs[1] == moons[index].position.vs[1] && m[index].velocity.vs[1] == index

        private fun moonIsBackAtInitialStateZ(
            m: List<Moon>,
            moons: List<Moon>,
            index: Int
        ) = m[index].position.vs[2] == moons[index].position.vs[2] && m[index].velocity.vs[2] == index

        private fun seekPeriodZ(moons: List<Moon>): Int {
            val ms = mutableListOf<Moon>()
            ms.addAll(moons)
            val m = ms.toList()

            var shouldContinue = true
            var result = 0

            while (shouldContinue) {
                applyStepToZ(m)
                result++
                if (
                    moonIsBackAtInitialStateZ(m, moons, 0)
                    && moonIsBackAtInitialStateZ(m, moons, 1)
                    && moonIsBackAtInitialStateZ(m, moons, 2)
                    && moonIsBackAtInitialStateZ(m, moons, 3)
                ) {
                    shouldContinue = false
                }
            }

            return result
        }
    }


}
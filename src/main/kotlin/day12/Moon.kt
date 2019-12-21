package day12

import kotlin.math.abs

data class Vector(var x: Int, var y: Int, var z: Int) {
    fun totalEnergy() = abs(x) + abs(y) + abs(z)

    operator fun plus(other: Vector): Vector {
        val x1 = x + other.x
        val y1 = y + other.y
        val z1 = z + other.z
        // println("combining $this")
        // println("and       $other")
        // println("gives $x1, $y1, $z1")
        return Vector(
            x1,
            y1,
            z1
        )
    }
}

class Moon(x: Int, y: Int, z: Int, vx: Int = 0, vy: Int = 0, vz: Int = 0) {
    var position = Vector(x, y, z)
    val velocity = Vector(vx, vy, vz)

    /**
     * To apply gravity, consider every pair of moons.
     * On each axis (x, y, and z), the velocity of each moon changes
     * by exactly +1 or -1 to pull the moons together.
     * For example, if Ganymede has an x position of 3, and Callisto has a x position of 5,
     * then Ganymede's x velocity changes by +1 (because 5 > 3)
     * and Callisto's x velocity changes by -1 (because 3 < 5).
     * However, if the positions on a given axis are the same,
     * the velocity on that axis does not change for that pair of moons.
     */
    fun applyGravity(b: Moon) {
        // println("before: $this and $b")
        when {
            position.x > b.position.x -> {
                velocity.x -= 1
                b.velocity.x += 1
            }
            position.x < b.position.x -> {
                velocity.x += 1
                b.velocity.x -= 1
            }
        }
        when {
            position.y > b.position.y -> {
                velocity.y -= 1
                b.velocity.y += 1
            }
            position.y < b.position.y -> {
                velocity.y += 1
                b.velocity.y -= 1
            }
        }
        when {
            position.z > b.position.z -> {
                velocity.z -= 1
                b.velocity.z += 1
            }
            position.z < b.position.z -> {
                velocity.z += 1
                b.velocity.z -= 1
            }
        }
    }

    fun applyVelocity() {
        // println("before: $position and $velocity")
        position = position.plus(velocity)
        // println("after: $position and $velocity")
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

        private fun applyGravityToEachCombinationOfMoonPairs(moons: List<Moon>) {
            moons[0].applyGravity(moons[1])
            moons[0].applyGravity(moons[2])
            moons[0].applyGravity(moons[3])
            moons[1].applyGravity(moons[2])
            moons[1].applyGravity(moons[3])
            moons[2].applyGravity(moons[3])
        }

        fun totalEnergy(moons: List<Moon>) =
            moons.sumBy { it.position.totalEnergy() * it.velocity.totalEnergy() }
    }


}
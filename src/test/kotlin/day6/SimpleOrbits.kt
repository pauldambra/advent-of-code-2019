package day6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class SimpleOrbits {

    @Test
    fun `can parse COM`() {
        val s = "COM)A"
        val op = parse(s)
        assertThat(op).isEqualTo(COM())
        assertThat(op!!.isOrbitedBy).isEqualTo(listOf(SpaceObject("A")))
    }

    @Test
    fun `space objects can have orbital relationships`() {
        val a = SpaceObject("A")
        val b = SpaceObject("B")
        a.orbits(b)

        assertThat(a.isInOrbitAround).isEqualTo(b)
        assertThat(b.isOrbitedBy).contains(a)
    }

    @Test
    fun `can parse several inputs`() {
        val s = """
            COM)A
            A)B
            B)C
            C)D
        """.trimIndent()
        val op = parse(s)

        assertThat(op).isEqualTo(COM())
        assertThat(op!!.isOrbitedBy.first()).isEqualTo(SpaceObject("A"))
        assertThat(op.isOrbitedBy.first().isOrbitedBy.first()).isEqualTo(SpaceObject("B"))
        assertThat(op.isOrbitedBy.first().isOrbitedBy.first().isOrbitedBy.first()).isEqualTo(SpaceObject("C"))
        assertThat(op.isOrbitedBy.first().isOrbitedBy.first().isOrbitedBy.first().isOrbitedBy.first()).isEqualTo(
            SpaceObject("D")
        )
    }


    /*
          G - H       J - K - L
       /           /
COM - B - C - D - E - F
               \
                I
     */
    @Test
    fun `the example wires up correctly`() {
        val s = """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
        """.trimIndent()
        val op = parse(s)

        assertThat(op).isEqualTo(COM())

        assertThat(op!!.isOrbitedBy).hasSize(1)
        assertThat(op.isOrbitedBy).containsExactlyInAnyOrder(SpaceObject("B"))
        val b = op.isOrbitedBy.first()

        assertThat(b.isOrbitedBy).containsExactlyInAnyOrder(SpaceObject("G"), SpaceObject("C"))

        val g = b.isOrbitedBy.first { it.name == "G" }
        val c = b.isOrbitedBy.first { it.name == "C" }

        assertThat(g.isOrbitedBy).containsExactlyInAnyOrder(SpaceObject("H"))
        assertThat(g.isOrbitedBy.first().isOrbitedBy).isEmpty()

        assertThat(c.isOrbitedBy).containsExactlyInAnyOrder(SpaceObject("D"))

        val d = c.isOrbitedBy.first()
        assertThat(d.isOrbitedBy).containsExactlyInAnyOrder(SpaceObject("E"), SpaceObject("I"))

        val i = d.isOrbitedBy.first { it.name == "I" }
        val e = d.isOrbitedBy.first { it.name == "E" }

        assertThat(i.isOrbitedBy).isEmpty()

        assertThat(e.isOrbitedBy).containsExactlyInAnyOrder(SpaceObject("F"), SpaceObject("J"))

        val j = e.isOrbitedBy.first { it.name == "J" }
        val f = e.isOrbitedBy.first { it.name == "F" }

        assertThat(f.isOrbitedBy).isEmpty()

        assertThat(j.isOrbitedBy).containsExactlyInAnyOrder(SpaceObject("K"))
        val k = j.isOrbitedBy.first()
        assertThat(k.isOrbitedBy).containsExactlyInAnyOrder(SpaceObject("L"))
        val l = k.isOrbitedBy.first()
        assertThat(l.isOrbitedBy).isEmpty()
    }

    /*
      G - H       J - K - L
   /           /
COM - B - C - D - E - F
           \
            I
 */
    @Test
    fun `the example can count its children`() {
        val s = """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
        """.trimIndent()
        val com = parse(s)

        val count = countDirectAndIndirectOrbits(com)

        assertThat(count).isEqualTo(42)
    }

    @Test
    fun `part 1 solution`() {
        val s = this::class.java.getResource("/day6/puzzleInput.txt").readText().trim()
        val com = parse(s)

        val count = countDirectAndIndirectOrbits(com)

        assertThat(count).isEqualTo(333679)
    }


    /*
                              YOU
                             /
            G - H       J - K - L
           /           /
    COM - B - C - D - E - F
                   \
                    I - SAN
     */
    @Test
    fun `part 2 example`() {
        val s = """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            K)YOU
            I)SAN
        """.trimIndent()

        val you = parse(s, "YOU")!!
        val san = parse(s, "SAN")!!

        val shortestRoute = findShortestRouteBetween(you, san)

        assertThat(shortestRoute).isEqualTo(4)
    }

    private fun findShortestRouteBetween(you: SpaceObject, san: SpaceObject): Int {
        val youPathToCOM: List<SpaceObject> = you.pathToCOM()

        val sanPathToCOM: List<SpaceObject> = san.pathToCOM()

        val intersection = sanPathToCOM.intersect(youPathToCOM).first()

        val youRouteToIntersection = youPathToCOM.takeWhile { it != intersection }
        val sanRouteToIntersection = sanPathToCOM.takeWhile { it != intersection }

        return youRouteToIntersection.size + sanRouteToIntersection.size
    }

    @Test
    fun `part 2 solution`() {
        val s = this::class.java.getResource("/day6/puzzleInput.txt").readText().trim()

        val you = parse(s, "YOU")!!
        val san = parse(s, "SAN")!!

        val shortestRoute = findShortestRouteBetween(you, san)

        assertThat(shortestRoute).isEqualTo(370)
    }

    private fun countDirectAndIndirectOrbits(com: SpaceObject?): Int {
        val queue: Queue<SpaceObject> = ArrayDeque<SpaceObject>()
        queue.add(com)

        var count = 0
        while (!queue.isEmpty()) {
            val x = queue.poll()
            x.isOrbitedBy.forEach {
                queue.add(it)
            }
            count += x.countStepsToCOM()
        }
        return count
    }
}

private fun parse(s: String, rootName: String = "COM"): SpaceObject? {
    val x = s
        .split("\n")
        .map { lines -> lines.split(")") }
        .map { ss ->
            Pair(
                spaceObjectOrCOM(ss[0]),
                spaceObjectOrCOM(ss[1])
            )
        }.fold(mutableMapOf<String, SpaceObject>()) { acc, pair ->
            val left = acc.getOrPut(pair.first.name) { pair.first }
            val right = acc.getOrPut(pair.second.name) { pair.second }
            right.orbits(left)

            acc
        }

    return x[rootName]!!
}

private fun spaceObjectOrCOM(s: String) =
    if (s == "COM") COM() else SpaceObject(s)

class COM : SpaceObject("COM") {
    override var isInOrbitAround: SpaceObject?
        get() = null
        set(o) = throw Exception("COM cannot orbit anything. you tried to add $o")
}

open class SpaceObject(val name: String) {
    open var isInOrbitAround: SpaceObject? = null
    open var isOrbitedBy: List<SpaceObject> = emptyList()

    fun orbits(b: SpaceObject) {
        isInOrbitAround = b
        b.isOrbitedBy += this
    }

    override fun toString(): String {
        return "SpaceObject(name='$name', isInOrbitAround=$isInOrbitAround, isOrbitedBy=${isOrbitedBy.size})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        return name == (other as SpaceObject).name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun countStepsToCOM(): Int {
        var x: SpaceObject? = this
        var count = 0
        while (x != null && x != COM()) {
            count++
            x = x.isInOrbitAround
        }
        return count
    }

    fun pathToCOM(): List<SpaceObject> {
        var x: SpaceObject? = this
        val sos = mutableListOf<SpaceObject>()
        while (x != null && x != COM()) {
            x = x.isInOrbitAround
            x?.let { sos.add(it) }
        }
        return sos
    }
}



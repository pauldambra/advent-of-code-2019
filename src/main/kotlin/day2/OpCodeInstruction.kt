package day2

data class OpCodeInstruction(val instruction: Int) {

    private val parameters: List<Int>

    val opCode: Int
    val firstParameterMode: Int
        get() = parameters.getOrElse(0) { 0 }
    val secondParameterMode: Int
        get() = parameters.getOrElse(1) { 0 }
    val thirdParameterMode: Int
        get() = parameters.getOrElse(2) { 0 }
    val fourthParameterMode: Int
        get() = parameters.getOrElse(3) { 0 }

    init {
        val i = instruction.toString().padStart(2, '0')
        opCode = i.substring(i.length - 2).toInt()
        val ps = i.substring(0, i.length - 2)
        parameters = ps.reversed().map { it.toString().toInt() }
    }

    companion object {
        const val POSITIONAL = 0
        const val IMMEDIATE = 1
    }
}
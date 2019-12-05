package day4


class PasswordValidator(min: String, max: String) {

    data class PasswordValidation(
        val withinRange: Boolean,
        val sixDigits: Boolean,
        val hasRepeatingNumber: Boolean,
        val hasOnlyIncreasingNumbersFromLeftToRight: Boolean
    ) {
        val isValid: Boolean = withinRange
                && sixDigits
                && hasRepeatingNumber
                && hasOnlyIncreasingNumbersFromLeftToRight
    }

    private val min: Int = min.toInt()
    private val max: Int = max.toInt()

    fun isValid(candidate: String): Boolean {
        val result = PasswordValidation(
            isWithinRange(candidate),
            isSixDigits(candidate),
            hasRepeatingNumber(candidate),
            hasOnlyIncreasingNumbersFromLeftToRight(candidate)
        )
//        println(result)
        return result.isValid
    }

    private fun isSixDigits(candidate: String) =
        candidate.length == 6

    private fun hasRepeatingNumber(candidate: String) =
        candidate.zipWithNext().any { it.first == it.second }

    private fun hasOnlyIncreasingNumbersFromLeftToRight(candidate: String) =
        candidate.zipWithNext().all {
            it.first.toString().toInt() <= it.second.toString().toInt()
        }

    private fun isWithinRange(candidate: String) =
        candidate.toInt() in min..max
}
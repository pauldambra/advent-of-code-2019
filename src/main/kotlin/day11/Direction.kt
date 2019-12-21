package day11

enum class Direction {
    UP {
        override fun turn(turn: Long) =
            if (turn == 0L) LEFT else RIGHT
    },
    RIGHT {
        override fun turn(turn: Long) =
            if (turn == 0L) UP else DOWN
    },
    DOWN {
        override fun turn(turn: Long) =
            if (turn == 0L) RIGHT else LEFT
    },
    LEFT {
        override fun turn(turn: Long) =
            if (turn == 0L) DOWN else UP
    };

    abstract fun turn(turn: Long): Direction
}
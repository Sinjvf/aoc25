package com.example.aoc25.util

/**
 *
 *
 * @author Тамара Синева on 16.12.2023
 */
enum class Direction {
    UP {
        override val opposite: Direction
            get() = DOWN

        override fun toString(): String {
            return "^"
        }

        override val orientation = Orientation.VERTICAL
    },
    DOWN {
        override val opposite: Direction
            get() = UP

        override fun toString(): String {
            return "v"
        }

        override val orientation = Orientation.VERTICAL
    },
    LEFT {
        override val opposite: Direction
            get() = RIGHT

        override fun toString(): String {
            return "<"
        }

        override val orientation = Orientation.HORIZONTAL
    },
    RIGHT {
        override val opposite: Direction
            get() = LEFT

        override fun toString(): String {
            return ">"
        }

        override val orientation = Orientation.HORIZONTAL
    };

    abstract val opposite: Direction
    abstract val orientation: Orientation
}

enum class Orientation {
    VERTICAL,
    HORIZONTAL
}

fun Orientation.opposite() =
    when (this) {
        Orientation.VERTICAL -> Orientation.HORIZONTAL
        Orientation.HORIZONTAL -> Orientation.VERTICAL
    }

fun Orientation.getDirs() = Direction.values().filter { it.orientation == this }

data class CountedDirection(val dir: Direction, var count: Int) {
    override fun toString(): String {
        return "$count$dir"
    }

    fun plus(a: CountedDirection): CountedDirection {
        if (dir != a.dir) return a
        else return CountedDirection(dir, count + a.count)
    }
}

fun Char.letterToDir(): Direction =
    when (this) {
        'L' -> Direction.LEFT
        'R' -> Direction.RIGHT
        'U' -> Direction.UP
        'D' -> Direction.DOWN
        else -> throw IllegalArgumentException("bad symbol $this")
    }
fun Char.symbolToDir(): Direction =
    when (this) {
        '<' -> Direction.LEFT
        '>' -> Direction.RIGHT
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        else -> throw IllegalArgumentException("bad symbol $this")
    }

fun Direction.toLeft(): Direction =
    when (this) {
        Direction.UP -> Direction.LEFT
        Direction.LEFT -> Direction.DOWN
        Direction.RIGHT -> Direction.UP
        Direction.DOWN -> Direction.RIGHT
    }

fun Direction.toRight(): Direction = this.toLeft().opposite

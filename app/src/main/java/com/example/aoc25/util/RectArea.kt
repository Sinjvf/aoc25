package com.example.aoc25.util

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 *
 *
 * @author Тамара Синева on 24.12.2023
 */
class RectArea(val x: LongRange, val y: LongRange) {
    constructor(p1: Point2D, p2: Point2D) : this(
        min(p1.x, p2.x).toLong()..max(p1.x, p2.x).toLong(),
        min(p1.y, p2.y).toLong()..max(p1.y, p2.y).toLong(),

        )

    fun contains(px: Double, py: Double) =
        px <= x.first && px <= x.last && py <= y.first && py <= y.last

    fun square(): Long = (x.last - x.first + 1) * (y.last - y.first + 1)
}

fun Point2D.inside(a: RectArea) =
    x in a.x && y in a.y

fun Point2D.inside(a: List<RectArea>) {
    a.any { this.inside(it) }
}


fun square(p1: Point2D, p2: Point2D): Long =
    (abs(p1.x - p2.x) + 1).toLong() * (abs(p1.y - p2.y) + 1)


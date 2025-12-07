package com.example.aoc25.util

/**
 *
 *
 * @author Тамара Синева on 24.12.2023
 */
class Area(val x: LongRange, val y: LongRange) {
    fun contains(px: Double, py: Double) =
        px <= x.first && px <= x.last && py <= y.first && py <= y.last
}

fun Point2D.inside(a: Area) =
    x in a.x && y in a.y

fun Point2D.inside(a: List<Area>){
    a.any { this.inside(it) }
}

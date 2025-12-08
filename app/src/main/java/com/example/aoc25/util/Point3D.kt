package com.example.aoc25.util

import kotlin.math.pow
import kotlin.math.sqrt

/**
 *
 *
 * @author Тамара Синева on 22.12.2023
 */
data class Point3D(val x: Double, val y: Double, val z: Double) : Comparable<Point3D> {
    constructor(x1: Long, y1: Long, z1: Long) : this(x1.toDouble(), y1.toDouble(), z1.toDouble())
    constructor(x1: Int, y1: Int, z1: Int) : this(x1.toDouble(), y1.toDouble(), z1.toDouble())

    override fun compareTo(other: Point3D): Int {
        return z.compareTo(other.z)
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }
}

fun Point3D.getOrient(other: Point3D): Orientation3D =
    when {
        y == other.y && z == other.z -> Orientation3D.STRAINT
        x == other.x && z == other.z -> Orientation3D.HORIZONTAL
        x == other.x && y == other.y -> Orientation3D.VERTICAL
        else -> throw IllegalStateException("Wrong orientation")

    }

fun Point3D.moveTo(dir: Direction, count: Int): Point3D =
    when (dir) {
        Direction.DOWN -> Point3D(x, y, z - count)
        Direction.UP -> Point3D(x, y, z + count)
        Direction.LEFT -> Point3D(x - count, y, z)
        Direction.RIGHT -> Point3D(x + count, y, z)
    }

fun Point3D.distanceTo(p: Point3D): Double =
    sqrt((p.x - x).pow(2) + (p.y - y).pow(2) + (p.z - z).pow(2))

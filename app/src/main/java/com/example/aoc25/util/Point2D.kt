package com.example.aoc25.util

import com.example.aoc25.util.Direction.DOWN
import com.example.aoc25.util.Direction.LEFT
import com.example.aoc25.util.Direction.RIGHT
import com.example.aoc25.util.Direction.UP
import kotlin.math.abs

/**
 * @author Тамара Синева on 11.12.2023
 */
data class Point2D(var x: Int, var y: Int) : Comparable<Point2D> {
    override fun compareTo(other: Point2D): Int {
        if (x != other.x) return x.compareTo(other.x)
        return y.compareTo(other.y)
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    fun plus(a: Point2D) = Point2D(x + a.x, y + a.y)

    fun toDirection(direction: Direction, size: Int = 1): Point2D =
        when (direction) {
            UP -> Point2D(x, y - size)
            DOWN -> Point2D(x, y + size)
            LEFT -> Point2D(x - size, y)
            RIGHT -> Point2D(x + size, y)
        }

    val allDirection = listOf(UP, DOWN, LEFT, RIGHT)
    fun getNei(matrix: Matrix<*>) =
        allDirection.map { this.toDirection(it) }.filter { it.inMatrix(matrix) }

    fun getDistansedNei(matrix: Matrix<*>, dist: Int): Set<Point2D> = buildSet {
        /* if (dist==0){
             Unit
         }else if (dist==1){
             val nei = getNei(matrix)
             addAll(nei)
         } else {
             val nei = getNei(matrix)
             addAll(nei)
             nei.forEach {
                 addAll(it.getDistansedNei(matrix, dist-1), )
             }
         }*/
        for (i in x - dist..x + dist) {
            for (j in y - dist..y + dist) {

                val newP = Point2D(i, j)
                if (!newP.inMatrix(matrix)) continue
                if (this@Point2D.distanceTo(Point2D(i, j)) > dist) continue
                add(newP)
            }
        }
    }
    fun <T> inMatrix(matrix: Matrix<T>): Boolean = matrix.pointInRange(this)


    fun inRange(xR: IntRange, yR: IntRange) =
        x in xR && y in yR

    fun maxAbs() = maxOf(abs(x), abs(y))

    fun distanceTo(p:Point2D):Int =
        abs(p.x-x) +abs(p.y-y)

    fun vectorTo(p: Point2D): List<Pair<Int, Direction>> {
        val list = mutableListOf<Pair<Int, Direction>>()
        val xDiff = p.x - x
        val yDiff = p.y - y
        if (xDiff > 0) list.add(abs(xDiff) to RIGHT) else if (xDiff < 0) list.add(abs(xDiff) to LEFT)
        if (yDiff > 0) list.add(abs(yDiff) to UP) else if (yDiff < 0) list.add(abs(yDiff) to DOWN)
        return list
    }
}


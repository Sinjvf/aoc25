package com.example.aoc25.util

import kotlin.math.max
import kotlin.math.min

class PolygonMatrix(list: List<Pair<Point2D, Point2D>>) : Matrix<Long>() {

    private val vertical = mutableMapOf<Int, MutableSet<IntRange>>()
    private val horizontal = mutableMapOf<Int, MutableSet<IntRange>>()

    private val points = list.flatMap { setOf(it.first, it.second) }

    init {

        list.forEach { (prev, curr) ->
            if (prev.x == curr.x) {
                val newVer = vertical.get(prev.x) ?: mutableSetOf()
                newVer.add(min(prev.y, curr.y)..max(prev.y, curr.y))
                vertical[prev.x] = newVer
            } else {
                val newH = horizontal.get(prev.y) ?: mutableSetOf()
                newH.add(min(prev.x, curr.x)..max(prev.x, curr.x))
                horizontal[prev.y] = newH
            }
        }
    }

    /**
     * На вход подается массив отрезков замкнутого многоугольника, после выполнения фуункции матрица
     * заполненна 0 и 1
     *
     * !!! для простоты считается что все отрезки либо вертикальные, либо горизонтальные
     * 0 - точка вне полигона
     * 1 - точка в внутри
     * */
    fun fillPolygonSect() {


    }

    /**
     * то же самое, только полигон задается списком вершин
     * */
    fun fillPolygonPoints(points: List<Point2D>, outPoint2D: Point2D) {
        var i = 0
        val list = buildList {
            while (i < points.size) {
                val prev = if (i == 0) points.last()
                else points[i - 1]
                val curr = points[i]
                add(prev to curr)
                i++
            }
        }

        bfsSimpleVisitTree(
            initNode = outPoint2D,
            getNext = { point ->
                getNei(point).filter { next ->
                    next.inMatrixCondition(this) { !next.onBorder() }
                }
            }
        )
    }

    private fun Point2D.onBorder(): Boolean {

        if (vertical[x]?.any { y in it } == true) return true
        if (horizontal[y]?.any { x in it } == true) return true

        return false
    }

}
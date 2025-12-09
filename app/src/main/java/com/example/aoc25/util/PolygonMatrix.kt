package com.example.aoc25.util

import com.example.Logger
import com.example.SystemLogger
import kotlin.math.max
import kotlin.math.min

class PolygonMatrix(xSize: Int, ySize: Int) : Matrix<Long>(xSize, ySize, { _ -> 0 }) {

    /**
     * На вход подается массив отрезков замкнутого многоугольника, после выполнения фуункции матрица
     * заполненна 0 и 1
     *
     * !!! для простоты считается что все отрезки либо вертикальные, либо горизонтальные
     * 0 - точка вне полигона
     * 1 - точка в внутри
     * */
    fun fillPolygonSect(list: List<Pair<Point2D, Point2D>>) {
        val vertical = sortedMapOf<Int, MutableSet<IntRange>>()
        val horizontal = sortedMapOf<Int, MutableSet<IntRange>>()
        val logger = SystemLogger()
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

        val iteratorVer = vertical.iterator()
        while (iteratorVer.hasNext()) {
            val currentList = iteratorVer.next()
            val x = currentList.key
            currentList.value.forEach { current ->
                for (y in current) {
                    put(Point2D(x, y), 1)
                    for (j in x + 1..xSize - 1) {
                        reverse(Point2D(j, y))
                    }
                }
            }
            logger.logD("\n${currentList.key}, ${currentList.value}")
            print(SystemLogger())
        }

    }

    /**
     * то же самое, только полигон задается списком вершин
     * */
    fun fillPolygonPoints(points: List<Point2D>) {
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
        fillPolygonSect(list)
    }

    // 0-> 1
    // else->0
    private fun reverse(p: Point2D) {
        if (get(p) == 0L) put(p, 1)
        else put(p, 0)
    }

}
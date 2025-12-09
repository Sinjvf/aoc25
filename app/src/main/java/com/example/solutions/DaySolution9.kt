package com.example.solutions

import com.example.ILogger
import com.example.aoc25.util.Matrix
import com.example.aoc25.util.Point2D
import java.util.SortedMap
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class DaySolution9(private val logger: ILogger, val start: Int = 0, val step: Int = 1) :
    DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Long = 0
        private val points = mutableListOf<Point2D>()


        override fun handleLine(inputStr: String, pos: Int) {
            val p = inputStr.split(',').map { it.toInt() }
            points.add(Point2D(p[0], p[1]))
        }

        override fun finish() {
            val sqr = mutableListOf<Pair<Point2D, Long>>()
            for (i in points.indices) {
                for (j in i + 1..points.size - 1) {
                    sqr.add(Point2D(i, j) to square(points[i], points[j]))
                    logger.logD("point = ${Point2D(i, j)}, area = ${square(points[i], points[j])}")
                }
            }
            intRes = sqr.maxOf { it.second }

        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0

        private val points = mutableListOf<Point2D>()
        private lateinit var matrix: Matrix<Char>

        //смещение
        private var offset: Point2D = Point2D(0, 0)

        override fun handleLine(inputStr: String, pos: Int) {
            val p = inputStr.split(',').map { it.toInt() }
            points.add(Point2D(p[0], p[1]))
        }

        override fun finish() {
            sortLines()
            val sqr = mutableListOf<Pair<Point2D, Long>>()
            for (i in points.indices) {
                for (j in i + 1..points.size - 1) {
                    //     if (checkInside(points[i], points[j])) {
                    sqr.add(Point2D(i, j) to square(points[i], points[j]))

                    //    }
                }
            }
            sqr.sortByDescending { it.second }
            var i = start
            //  logger.logD("${sqr.size}")
            while (i < sqr.size) {
                if (i % step == 0) {
                    logger.logD("\ncheck sqr[${i}]:${sqr[i].second} ${points[sqr[i].first.x]}, ${points[sqr[i].first.y]}")

                }
                if (checkInside(points[sqr[i].first.x], points[sqr[i].first.y])) {

                    intRes = sqr[i].second
                    return
                }
                i++
            }
        }


        lateinit var hor: SortedMap<Int, MutableList<IntRange>>
        lateinit var ver: SortedMap<Int, MutableList<IntRange>>
        fun sortLines() {
            var i = 0

            val horizontal = mutableMapOf<Int, MutableList<IntRange>>()
            val vertical = mutableMapOf<Int, MutableList<IntRange>>()
            while (i < points.size) {
                val prev = if (i == 0) points.last()
                else points[i - 1]
                val curr = points[i]
                if (prev.x == points[i].x) {
                    var newVer = vertical.get(prev.x) ?: mutableListOf()
                    newVer.add(min(prev.y, curr.y) + 1..max(prev.y, curr.y) - 1)
                    vertical[prev.x] = newVer
                } else {

                    var newH = horizontal.get(prev.y) ?: mutableListOf()
                    newH.add(min(prev.x, curr.x) + 1..max(prev.x, curr.x) - 1)
                    horizontal[prev.y] = newH
                }
                i++
            }
            hor = horizontal.toSortedMap()
            ver = vertical.toSortedMap()
        }

        fun checkInside(p1: Point2D, p2: Point2D): Boolean {
            for (i in min(p1.x, p2.x)..max(p1.x, p2.x)) {
                if (!checkVerInside(i, min(p1.y, p2.y)..max(p1.y, p2.y))) {
                    return false
                }
            }
            for (j in min(p1.y, p2.y)..max(p1.y, p2.y)) {
                if (!checkHorInside(min(p1.x, p2.x)..max(p1.x, p2.x), j)) {
                    return false
                }
            }
            return true
        }


        fun verCross(x: IntRange, y: Int, map: Map.Entry<Int, MutableList<IntRange>>): Boolean {
            return map.key in x && (map.value.any { y in it })
        }

        fun horCross(x: Int, y: IntRange, map: Map.Entry<Int, MutableList<IntRange>>): Boolean {
            return map.key in y && (map.value.any { x in it })
        }

        fun checkHorInside(x: IntRange, y: Int): Boolean {
            val entries = ver.entries.toList()
            var i = 0
            while (i < entries.size) {
                if (entries[i].key <= x.first) {
                    i++
                    continue
                }

                if (entries[i].key >= x.last) return true

                if (verCross(x, y, entries[i])) {
      //              return false
                    var j = 1
                    while (i + j < entries.size
                        && entries[i + j].key == entries[i].key + j
                        && verCross(x, y, entries[i + j])
                    ) {
                        j++
                    }
                    when {
                        entries[i].key == x.first + 1 -> {
                            i += j
                            continue
                        }

                        entries[i + j].key == x.last - 1 -> return true
                        j % 2 == 1 -> return false
                        else -> {
                            i += j

                            continue
                        }
                    }
                }
                i++
            }

            return true

        }

        fun checkVerInside(x: Int, y: IntRange): Boolean {

            val entries = hor.entries.toList()
            var i = 0
            while (i < entries.size) {
                if (entries[i].key <= y.first) {
                    i++
                    continue
                }

                if (entries[i].key >= y.last) return true

                if (horCross(x, y, entries[i])) {

               //     return false
                    var j = 1
                    while (i + j < entries.size
                        && entries[i + j].key == entries[i].key + j
                        && horCross(x, y, entries[i + j])
                    ) {
                        j++
                    }
                    when {
                        entries[i].key == y.first + 1 -> {
                            i += j
                            continue
                        }

                        entries[i + j].key == y.last - 1 -> return true
                        j % 2 == 1 -> return false
                        else -> {
                            i += j
                            continue
                        }
                    }
                }

                i++
            }
            return true

        }
        override fun obtainResult(): String = intRes.toString()
    }

    fun Matrix<Char>.put(point: Point2D, node: Char, offset: Point2D) {
        put(Point2D(point.x - offset.x, point.y - offset.y), node)
    }

    fun square(p1: Point2D, p2: Point2D): Long =
        (abs(p1.x - p2.x) + 1).toLong() * (abs(p1.y - p2.y) + 1)

}

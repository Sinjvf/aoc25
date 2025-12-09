package com.example.solutions

import com.example.ILogger
import com.example.aoc25.util.Point2D
import com.example.aoc25.util.PolygonMatrix
import com.example.aoc25.util.square

class DaySolution9_2(private val logger: ILogger, val start: Int = 0, val step: Int = 1) :
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
        private lateinit var matrix: PolygonMatrix


        override fun handleLine(inputStr: String, pos: Int) {
            val p = inputStr.split(',').map { it.toInt() }
            points.add(Point2D(p[0], p[1]))
        }

        override fun finish() {
            matrix = PolygonMatrix(points.maxBy { it.x }.x + 1, points.maxBy { it.y }.y + 1)
            matrix.fillPolygonPoints(points)
            matrix.print(logger)

        }

        override fun obtainResult(): String = intRes.toString()
    }

}

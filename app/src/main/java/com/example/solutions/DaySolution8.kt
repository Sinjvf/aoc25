package com.example.solutions

import com.example.ILogger
import com.example.aoc25.util.Point2D
import com.example.aoc25.util.Point3D
import com.example.aoc25.util.SearchResultType
import com.example.aoc25.util.bfsSimpleVisitTree
import com.example.aoc25.util.binarySearch
import com.example.aoc25.util.distanceTo

class DaySolution8(private val logger: ILogger, val count: Int) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Long = 0
        private val coord = mutableListOf<Point3D>()

        override fun handleLine(inputStr: String, pos: Int) {
            val c = inputStr.split(',').map { it.toLong() }
            coord.add(Point3D(c[0], c[1], c[2]))
        }

        override fun finish() {
            val closest = getClosestList(coord).take(count)
            logger.logD("\n" + closest.toString())

            val visited = mutableListOf<MutableSet<Int>>()
            for (i in closest) {
                if (visited.any { set -> set.contains(i.x) || set.contains(i.y) }) continue
                val visit = mutableSetOf<Int>()
                bfsSimpleVisitTree(
                    initNode = i.x,
                    getNext = { node ->
                        closest.filter { it.x == node || it.y == node }
                            .flatMap { listOf(it.x, it.y) }
                    },
                    onVisit = { node -> visit.add(node) }
                )
                visited.add(visit)
            }

            visited.sortByDescending { it.size }
            logger.logD(visited.toString())
            intRes = visited[0].size * visited[1].size * visited[2].size.toLong()
        }

        override fun obtainResult(): String = intRes.toString()
    }

    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0
        private val coord = mutableListOf<Point3D>()


        override fun handleLine(inputStr: String, pos: Int) {
            val c = inputStr.split(',').map { it.toLong() }
            coord.add(Point3D(c[0], c[1], c[2]))
        }

        override fun finish() {
            val closest = getClosestList(coord)
            logger.logD(closest.toString())

            val lastConId = binarySearch(
                start = count to SearchResultType.LESS,
                end = closest.size - 1 to SearchResultType.MORE_OR_EQUAL,
                compute = { id ->
                    val isAllConnected = allConnected(closest.take(id))
                    if (isAllConnected) SearchResultType.MORE_OR_EQUAL
                    else SearchResultType.LESS
                }
            ) - 1
            val lastCon = closest[lastConId]

            logger.logD("lastCon = $lastCon")

            intRes = coord[lastCon.x].x.toLong() * coord[lastCon.y].x.toLong()
        }

        fun allConnected(closest: List<Point2D>): Boolean {
            val visit = mutableSetOf<Int>()
            bfsSimpleVisitTree(
                initNode = closest.first().x,
                getNext = { node ->
                    closest.filter { it.x == node || it.y == node }
                        .flatMap { listOf(it.x, it.y) }
                },
                onVisit = { node -> visit.add(node) }
            )
            return visit.size == coord.size
        }

        override fun obtainResult(): String = intRes.toString()
    }

    fun getClosestList(coord: List<Point3D>): List<Point2D> {
        val dist = mutableListOf<Pair<Point2D, Double>>()
        for (i in 0..coord.size - 1) {
            for (j in i + 1..coord.size - 1) {
                dist.add(Point2D(i, j) to coord[i].distanceTo(coord[j]))
            }
        }
        dist.sortBy { it.second }
        return dist.map { it.first }
    }
}

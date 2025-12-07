package com.example.solutions

import com.example.ILogger
import com.example.aoc25.util.BFSTreeNode
import com.example.aoc25.util.Matrix
import com.example.aoc25.util.Point2D
import com.example.aoc25.util.bfsVisitTree

class DaySolution7(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Int = 0
        private val matrix = Matrix<Char>()
        private lateinit var initPos: Point2D
        val set = mutableSetOf<Point2D>()

        override fun handleLine(inputStr: String, pos: Int) {
            matrix.addRaw(pos, inputStr.toCharArray().toList())
            val idx = inputStr.indexOfFirst { it == 'S' }
            if (idx != -1) {
                initPos = Point2D(idx, pos)
                matrix.put(initPos, '.')
            }
        }

        override fun finish() {
            bfsVisitTree(
                initNode = BFSTreeNode(initPos, 1L),
                getNext = { node -> getNext(node, matrix) },
                onVisit = { node ->
                    if (node.pos.inMatrixVal(matrix, '^'))
                        set.add(node.pos)
                }
            )
            intRes = set.size
        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0
        private val matrix = Matrix<Char>()
        private lateinit var initPos: Point2D

        override fun handleLine(inputStr: String, pos: Int) {
            matrix.addRaw(pos, inputStr.toCharArray().toList())
            val idx = inputStr.indexOfFirst { it == 'S' }
            if (idx != -1) {
                initPos = Point2D(idx, pos)
                matrix.put(initPos, '.')
            }
        }

        override fun finish() {
            val visited = bfsVisitTree(
                initNode = BFSTreeNode(initPos, 1L),
                getNext = { node -> getNext(node, matrix) }
            )
            intRes = visited.filter { it.pos.y == matrix.ySize - 1 }.map { it.count }
                .fold(0, { a, b -> a + b })
        }

        override fun obtainResult(): String = intRes.toString()
    }


    fun getNext(node: BFSTreeNode, matrix: Matrix<Char>): List<BFSTreeNode> = buildList {
        val point2D = node.pos
        if (point2D.y == matrix.ySize - 1) return@buildList

        val char = matrix.get(point2D)

        if (char == '.') {
            if (Point2D(point2D.x, point2D.y + 1).inMatrix(matrix)) {
                add(BFSTreeNode(Point2D(point2D.x, point2D.y + 1), node.count))
            }
        } else {
            if (Point2D(point2D.x - 1, point2D.y + 1).inMatrix(matrix)) {
                add(BFSTreeNode(Point2D(point2D.x - 1, point2D.y + 1), node.count))
            }
            if (Point2D(point2D.x + 1, point2D.y + 1).inMatrix(matrix)) {
                add(BFSTreeNode(Point2D(point2D.x + 1, point2D.y + 1), node.count))
            }
        }
    }
}


package com.example.solutions

import com.example.ILogger
import com.example.aoc25.util.Matrix

class DaySolution6(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Long = 0
        val matrix = Matrix<Long>()
        lateinit var operations: List<String>


        override fun handleLine(inputStr: String, pos: Int) {
            if (!inputStr.contains("+") && !inputStr.contains('*')) {
                matrix.addRaw(
                    pos,
                    inputStr.split(' ').filter { it.isNotBlank() }.map { it.toLong() })
            } else {
                operations = inputStr.split(' ').filter { it.isNotBlank() }
            }
        }

        override fun finish() {
            for (i in 0..matrix.xSize - 1) {
                val nums = matrix.getCol(i)
                var res = if (operations[i] == "+") {
                    nums.fold(0L, { a1, a2 -> a1 + a2 })
                } else {
                    nums.fold(1L, { a1, a2 -> a1 * a2 })
                }
                intRes += res
            }

        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0

        val matrix = Matrix<Char>()
        lateinit var operations: List<String>

        override fun handleLine(inputStr: String, pos: Int) {

            if (!inputStr.contains("+") && !inputStr.contains('*')) {
                matrix.addRaw(
                    pos,
                    inputStr.toCharArray().toList()
                )
            } else {
                operations = inputStr.split(' ').filter { it.isNotBlank() }
            }
        }


        override fun finish() {
            var column = 0
            var res = if (operations[column] == "+") 0L else 1L
            for (i in 0..matrix.xSize - 1) {
                val strs = matrix.getCol(i)
                if (strs.all { it == ' ' }) {
                    column++

                    logger.logD("res = $res")
                    intRes += res
                    res = if (operations[column] == "+") 0 else 1
                    continue
                }

                var numStr = buildString {
                    for (j in 0..matrix.ySize - 1) {
                        append(matrix.get(i, j))
                    }
                }
                var num = numStr.trimIndent().trimEnd().replace(' ', '0').toLong()

                logger.logD("num = $num")
                if (operations[column] == "+") {
                    res += num
                } else {
                    res *= num
                }

            }

            intRes += res
        }

        override fun obtainResult(): String = intRes.toString()
    }
}

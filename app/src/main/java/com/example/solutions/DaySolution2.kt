package com.example.solutions

import com.example.ILogger

class DaySolution2(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Long = 0


        override fun handleLine(inputStr: String, pos: Int) {
            val ranges = inputStr.split(',').filter { !it.isNullOrEmpty() }.map { it.split('-') }
                .map { it[0].toLong()..it[1].toLong() }
            logger.logD(ranges.toString())
            for (range in ranges) {
                for (id in range) {
                    if (isIncorrect(id)) intRes += id
                }
            }
        }

        fun isIncorrect(id: Long): Boolean {
            val str = id.toString()
            if (str.length % 2 == 1) return false
            val half = str.length / 2
            return (str.substring(0..half - 1) == str.substring(half, str.length))
        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0

        override fun handleLine(inputStr: String, pos: Int) {
            val ranges = inputStr.split(',').filter { !it.isNullOrEmpty() }.map { it.split('-') }
                .map { it[0].toLong()..it[1].toLong() }
            logger.logD(ranges.toString())
            for (range in ranges) {
                for (id in range) {
                    if (isIncorrect(id)) intRes += id
                }
            }
        }

        fun isIncorrectBy(idStr: String, by: Int): Boolean {

            if (idStr.length % by != 0) return false
            val size = idStr.length / by
            for (i in 1..size - 1) {
                for (j in 0..by - 1) {
                    if (idStr[j] != idStr[i * by + j]) return false
                }
            }
            return true
        }

        fun isIncorrect(id: Long): Boolean {

            val str = id.toString()
            for (i in 1..str.length - 1) {
                if (isIncorrectBy(str, i)) return true
            }
            return false
        }

        override fun obtainResult(): String = intRes.toString()
    }
}

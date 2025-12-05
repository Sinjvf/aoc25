package com.example.solutions

import com.example.ILogger
import com.example.aoc25.util.orRanges

class DaySolution5(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Int = 0
        private val ranges = mutableListOf<LongRange>()
        private val nums = mutableListOf<Long>()
        var flag = true


        override fun handleLine(inputStr: String, pos: Int) {
            if (flag) {
                if (inputStr.isEmpty()) flag = false
                else {
                    val range = inputStr.split('-').map { it.toLong() }
                    ranges.add(range[0]..range[1])
                }

            } else {
                nums.add(inputStr.toLong())
            }
        }

        override fun finish() {
            for (n in nums) {
                for (r in ranges) {
                    if (n in r) {
                        intRes++
                        break
                    }
                }
            }
        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0
        private var ranges = mutableListOf<LongRange>()
        private val nums = mutableListOf<Long>()
        var flag = true

        override fun handleLine(inputStr: String, pos: Int) {
            if (flag) {
                if (inputStr.isEmpty()) flag = false
                else {
                    val range = inputStr.split('-').map { it.toLong() }
                    ranges.add(range[0]..range[1])
                }
            }
        }

        override fun finish() {
            orRanges(ranges)
            ranges.forEach {
                val size = it.last - it.first + 1
                intRes += size
            }

        }

        override fun obtainResult(): String = intRes.toString()
    }
}

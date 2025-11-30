package com.example.solutions

import com.example.ILogger
import kotlin.math.abs

class DaySolution1(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Int = 0
        private var l1 = mutableListOf<Int>()
        private var l2 = mutableListOf<Int>()

        override fun handleLine(inputStr: String, pos: Int) {
            if (inputStr.isEmpty()) return
            val l3 = inputStr.split(" ").filter { it.isNotEmpty() }

            l1.add(l3[0].toInt())
            l2.add(l3[1].toInt())
        }

        override fun obtainResult(): String {
            val l1S = l1.sorted()
            val l2S = l2.sorted()
            l1S.forEachIndexed { id, it ->
                intRes += abs(it - l2S[id])
            }
            return intRes.toString()
        }
    }
    override val part2 = object : DaySolutionPart {
        private var intRes: Int = 0
        private var l1 = mutableListOf<Int>()
        private var l2 = mutableListOf<Int>()

        override fun handleLine(inputStr: String, pos: Int) {
            if (inputStr.isEmpty()) return
            val l3 = inputStr.split(" ").filter { it.isNotEmpty() }

            l1.add(l3[0].toInt())
            l2.add(l3[1].toInt())
        }

        override fun finish() {
            l1.forEach { l1Elem ->
                val l2Size = l2.filter { l1Elem == it }.size
                intRes += l1Elem * l2Size
            }
        }

        override fun obtainResult(): String = intRes.toString()
    }
}

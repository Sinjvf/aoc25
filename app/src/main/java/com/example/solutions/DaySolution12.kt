package com.example.solutions

import com.example.ILogger

class DaySolution12(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Int = 0


        override fun handleLine(inputStr: String, pos: Int) {

        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Int = 0

        override fun handleLine(inputStr: String, pos: Int) {
        }

        override fun obtainResult(): String = intRes.toString()
    }
}

package com.example.solutions

import com.example.ILogger
import kotlin.math.abs

class DaySolution2(private val logger: ILogger) : DaySolution {

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

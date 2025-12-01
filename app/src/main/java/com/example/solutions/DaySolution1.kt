package com.example.solutions

import com.example.ILogger

class DaySolution1(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Int = 0
        private var position = 50

        override fun handleLine(inputStr: String, pos: Int) {
            val sign = if (inputStr[0] == 'R') 1 else -1
            val count = inputStr.substring(1..inputStr.length - 1).toInt()

            position += sign * count
            if (position < 0) position += 100
            position %= 100
     //       logger.logD("move $sign by $count new pos = $position")
            if (position == 0) intRes++
        }

        override fun obtainResult(): String = intRes.toString()
    }
    override val part2 = object : DaySolutionPart {
        private var intRes: Int = 0
        private var position = 50

        override fun handleLine(inputStr: String, pos: Int) {
            val sign = if (inputStr[0] == 'R') 1 else -1
            val count = inputStr.substring(1..inputStr.length - 1).toInt()

            for (i in 1..count){
                position+=sign
                if (position==100 || position==0) {
                    position = 0
                    intRes++
                }
                if (position==-1) position = 99
            }
        }

        override fun finish() {
        }

        override fun obtainResult(): String = intRes.toString()
    }
}

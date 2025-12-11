package com.example.solutions

import com.example.ILogger
import com.example.aoc25.util.Condition
import com.example.aoc25.util.SimpleGraph
import com.example.aoc25.util.countAll


class DaySolution11(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Long = 0
        val map = mutableMapOf<String, List<String>>()


        override fun handleLine(inputStr: String, pos: Int) {
            val split = inputStr.split(':')
            val split2 = split[1].split(' ').filter { it.isNotBlank() }
            map.put(split[0], split2)
        }

        override fun finish() {
            intRes = Graph11(map).countAll("you", "out", listOf(), mutableMapOf())
        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0
        val map = mutableMapOf<String, List<String>>()

        override fun handleLine(inputStr: String, pos: Int) {

            val split = inputStr.split(':')
            val split2 = split[1].split(' ').filter { it.isNotBlank() }
            map.put(split[0], split2)
        }

        override fun finish() {
            val conditions = listOf(cond("dac") to false, cond("fft") to false)
            intRes += Graph11(map).countAll("svr", "out", conditions, mutableMapOf())

        }

        fun cond(str: String) = object : Condition<String> {
            override fun condition(node: String): Boolean = node == str
        }

        override fun obtainResult(): String = intRes.toString()
    }

    class Graph11(val map: Map<String, List<String>>) : SimpleGraph<String>() {
        override fun next(node: String): List<String> {
            return map.get(node) ?: emptyList()
        }
    }
}


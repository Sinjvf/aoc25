package com.example.solutions

import com.example.ILogger
import com.example.aoc25.util.Matrix

class DaySolution12(private val logger: ILogger) : DaySolution {
    class Tree(val x: Int, val y: Int, val data: List<Int>, val str:String){
        override fun toString(): String {
            return str
        }
    }

    override val part1 = object : DaySolutionPart {
        private var intRes: Long = 0
        private var flag = false
        private val forms = mutableMapOf<Int, Matrix<Char>>()
        private val spaces = mutableMapOf<Int, Int>()

        private val trees = mutableListOf<Tree>()
        private var currForm = 0

        override fun handleLine(inputStr: String, pos: Int) {

            if (inputStr.isEmpty()) return
            if (flag || inputStr.contains("x")) {
                flag = true
                val s1 = inputStr.split(":")
                val s2 = s1[0].split("x").map { it.toInt() }
                trees.add(
                    Tree(
                        s2[0],
                        s2[1],
                        s1[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }, inputStr)
                )
            } else {
                if (inputStr.contains(":")) {
                    currForm = inputStr[0].toString().toInt()
                    forms[currForm] = Matrix()
                } else {
                    val m = forms[currForm]!!
                    forms[currForm]!!.addRaw(m.ySize, inputStr.toList())
                }
            }
        }

        override fun finish() {

            forms.forEach {
                val m = it.value
                val iterator = m.iterator()
                var size = 0
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next.data == '#') size++
                }
                spaces.put(it.key, size)
            }

            intRes = trees.count { enought(it) }.toLong()
        }

        private fun enought(tree: Tree):Boolean {
            val treeSize = tree.y * tree.x
            var giftSize = 0
             tree.data.forEachIndexed { index, i ->
                giftSize += spaces[index]!! * i
            }
            logger.logD("tree = $tree \n $treeSize, $giftSize")
            return treeSize > giftSize
        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0


        override fun handleLine(inputStr: String, pos: Int) {

        }

        override fun finish() {

        }

        override fun obtainResult(): String = intRes.toString()
    }
}

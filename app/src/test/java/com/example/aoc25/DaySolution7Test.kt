package com.example.aoc25

import com.example.solutions.DaySolution7
import org.junit.Assert.assertEquals
import org.junit.Test

class DaySolution7Test {



    @Test
    fun testPart1test() {
        //test case
        val solution = DaySolution7(TestLogger())
        test_input7_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(result1, solution.part1.obtainResult())
    }

    @Test
    fun testPart1() {
        // check
        val solution = DaySolution7(NoLogger())
        input7_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(null, solution.part1.obtainResult())
    }


    @Test
    fun testPart2test() {
        //test case
        val solution = DaySolution7(TestLogger())
        test_input7_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(result2, solution.part2.obtainResult())
    }

    @Test
    fun testPart2() {
        // check
        val solution = DaySolution7(NoLogger())
        input7_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(null, solution.part2.obtainResult())
    }

    private val test_input7_1 =
""".......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............""".trimIndent().split("\n")
    private val result1 = "21"

    private val test_input7_2 = test_input7_1
    private val result2 = "40"
}
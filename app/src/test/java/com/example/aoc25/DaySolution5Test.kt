package com.example.aoc25

import com.example.solutions.DaySolution1
import com.example.solutions.DaySolution5
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
class DaySolution5Test {
    private val solution = DaySolution5(TestLogger())

    @Test
    fun testPart1test() {
        //test case
        test_input5_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(result1, solution.part1.obtainResult())
    }

    @Test
    fun testPart1() {
        // check
        input5_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(null, solution.part1.obtainResult())
    }


    @Test
    fun testPart2test() {
        //test case
        test_input5_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(result2, solution.part2.obtainResult())
    }

    @Test
    fun testPart2() {
        // check
        input5_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(null, solution.part2.obtainResult())
    }

    private val test_input5_1 =
        """3-5
10-14
16-20
12-18
12-18
16-20

1
5
8
11
17
32""".trimIndent().split("\n")
    private val result1 = "3"

    private val test_input5_2 = test_input5_1
    private val result2 = "14"
}
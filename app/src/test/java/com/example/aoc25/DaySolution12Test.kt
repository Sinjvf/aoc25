package com.example.aoc25

import com.example.solutions.DaySolution1
import com.example.solutions.DaySolution12
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
class DaySolution12Test {

    @Test
    fun testPart1test() {
        //test case
        val solution = DaySolution12(TestLogger())
        test_input12_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(result1, solution.part1.obtainResult())
    }

    @Test
    fun testPart1() {
        // check
        val solution = DaySolution12(NoLogger())
        input12_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(null, solution.part1.obtainResult())
    }


    @Test
    fun testPart2test() {
        //test case
        val solution = DaySolution12(TestLogger())
        test_input12_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(result2, solution.part2.obtainResult())
    }

    @Test
    fun testPart2() {
        // check
        val solution = DaySolution12(NoLogger())
        input12_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(null, solution.part2.obtainResult())
    }

    private val test_input12_1 =
        """0:
###
##.
##.

1:
###
##.
.##

2:
.##
###
##.

3:
##.
###
##.

4:
###
#..
###

5:
###
.#.
###

4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2""".trimIndent().split("\n")
    private val result1 = "11"

    private val test_input12_2 = test_input12_1
    private val result2 = "31"
}
package com.example.aoc25

import com.example.solutions.DaySolution1
import org.junit.Assert.assertEquals
import org.junit.Test

class DaySolution1Test {
    private val solution = DaySolution1(TestLogger())

    @Test
    fun testPart1test() {
        //test case
        test_input1_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(result1, solution.part1.obtainResult())
    }

    @Test
    fun testPart1() {
        // check
        input1_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(null, solution.part1.obtainResult())
    }


    @Test
    fun testPart2test() {
        //test case
        test_input1_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(result2, solution.part2.obtainResult())
    }

    @Test
    fun testPart2() {
        // check
        input1_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(null, solution.part2.obtainResult())
    }

    private val test_input1_1 =
        """L68
L30
R48
L5
R60
L55
L1
L99
R14
L82""".trimIndent().split("\n")
    private val result1 = "3"

    private val test_input1_2 = test_input1_1
    private val result2 = "6"
}
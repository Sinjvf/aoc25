package com.example.aoc25

import com.example.solutions.DaySolution1
import com.example.solutions.DaySolution8
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
class DaySolution8Test {


    @Test
    fun testPart1test() {
        //test case
        val solution = DaySolution8(TestLogger())
        test_input8_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(result1, solution.part1.obtainResult())
    }

    @Test
    fun testPart1() {
        // check
        val solution = DaySolution8(NoLogger())
        input8_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(null, solution.part1.obtainResult())
    }

    @Test
    fun testPart2test() {
        //test case
        val solution = DaySolution8(TestLogger())
        test_input8_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(result2, solution.part2.obtainResult())
    }

    @Test
    fun testPart2() {
        // check
        val solution = DaySolution8(NoLogger())
        input8_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(null, solution.part2.obtainResult())
    }

    private val test_input8_1 =
        """""".trimIndent().split("\n")
    private val result1 = "11"

    private val test_input8_2 = test_input8_1
    private val result2 = "31"
}
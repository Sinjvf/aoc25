package com.example.aoc25

import com.example.solutions.DaySolution1
import com.example.solutions.DaySolution9
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
class DaySolution9Test {


    @Test
    fun testPart1test() {
        //test case
        val solution = DaySolution9(TestLogger())
        test_input9_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(result1, solution.part1.obtainResult())
    }

    @Test
    fun testPart1() {
        // check
        val solution = DaySolution9(NoLogger())
        input9_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(null, solution.part1.obtainResult())
    }


    @Test
    fun testPart2test() {
        //test case
        val solution = DaySolution9(TestLogger())
        test_input9_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(result2, solution.part2.obtainResult())
    }

    @Test
    fun testPart2test2() {
        //test case
        val solution = DaySolution9(TestLogger())
        test_input9_3.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(result2, solution.part2.obtainResult())
    }

    @Test
    fun testPart2() {
        // check
        val solution = DaySolution9(TestLogger(), start = 0, step = 1000)
        input9_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(null, solution.part2.obtainResult())
    }

    private val test_input9_1 =

        """7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3""".trimIndent().split("\n")
    private val result1 = "50"

    private val test_input9_2 = test_input9_1
    private val result2 = "26"

    private val test_input9_3 =

        """0,0
0,9
9,9
9,5
0,5
0,4
9,4
9,0""".trimIndent().split("\n")
}
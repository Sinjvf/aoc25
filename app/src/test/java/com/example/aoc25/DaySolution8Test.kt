package com.example.aoc25

import com.example.solutions.DaySolution8
import org.junit.Assert.assertEquals
import org.junit.Test

class DaySolution8Test {


    @Test
    fun testPart1test() {
        //test case
        val solution = DaySolution8(TestLogger(), 10)
        test_input8_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(result1, solution.part1.obtainResult())
    }

    @Test
    fun testPart1() {
        // check
        val solution = DaySolution8(TestLogger(), 1000)
        input8_1.forEachIndexed { id, str -> solution.part1.handleLine(str, id) }
        solution.part1.finish()
        assertEquals(null, solution.part1.obtainResult())
    }

    @Test
    fun testPart2test() {
        //test case
        val solution = DaySolution8(TestLogger(), 10)
        test_input8_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(result2, solution.part2.obtainResult())
    }

    @Test
    fun testPart2() {
        // check
        val solution = DaySolution8(NoLogger(), 1000)
        input8_2.forEachIndexed { id, str -> solution.part2.handleLine(str, id) }
        solution.part2.finish()
        assertEquals(null, solution.part2.obtainResult())
    }

    private val test_input8_1 =


        """162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689""".trimIndent().split("\n")
    private val result1 = "40"

    private val test_input8_2 = test_input8_1
    private val result2 = "25272"
}
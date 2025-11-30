package com.example.solutions

/**
 *
 *
 * @author Тамара Синева on 01.12.2023
 */
interface DaySolution {
    val part1: DaySolutionPart
    val part2: DaySolutionPart
}

interface DaySolutionPart {

    fun handleLine(inputStr: String, pos: Int)
    fun finish() = Unit
    fun obtainResult(): String
}
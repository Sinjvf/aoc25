package com.example.aoc25.util

import java.lang.Math.sqrt

/**
 * @author Тамара Синева on 11.12.2023
 */
fun gcd(_a: Long, _b: Long): Long {
    var a = _a
    var b = _b
    while (a != b) {
        if (a > b)
            a -= b
        else
            b -= a
    }
    return a
}

inline fun gcd(a: Int, b: Int): Int = gcd(a.toLong(), b.toLong()).toInt()

fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

inline fun lcm(a: Int, b: Int): Long = lcm(a.toLong(), b.toLong())

fun squareEx(a: Double, b: Double, c: Double): List<Double> {
    if (a == 0.0) return listOf(-c / b)
    val d = (b * b) - (4 * a * c)
    if (d < 0) return emptyList()
    if (d == 0.0) return listOf(-b / (2 * a))
    else return listOf((-b + sqrt(d)) / (2 * a), (-b - sqrt(d)) / (2 * a))
}

fun Int.toArr(): ByteArray = ByteArray(300).also { arr -> arr[this] = 1 }

fun Int.inArr(b: ByteArray): Boolean = b[this].toInt() == 1

fun Int.addToArr(b: ByteArray):ByteArray = b.also { arr-> arr[this] = 1 }



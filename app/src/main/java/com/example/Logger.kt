package com.example

import android.util.Log

/**
 * @author Тамара Синева on 04.12.2023
 */
class Logger(private val tag: String) : ILogger {
    override fun logD(str: String) {
        Log.d(tag, str)
    }
}
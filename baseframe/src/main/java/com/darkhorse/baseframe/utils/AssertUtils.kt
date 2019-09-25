package com.darkhorse.baseframe.utils

import java.io.BufferedReader
import java.io.InputStreamReader

object AssertUtils {

    fun readFile(fileName: String): String {
        val stringBuilder = StringBuilder()
        val inputStream = AppManager.getApplication().assets.open(fileName)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var line: String?
        line = bufferedReader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }
        return stringBuilder.toString()
    }
}


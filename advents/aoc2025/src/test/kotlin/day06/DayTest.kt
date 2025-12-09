package com.gilpereda.aoc2025.day06

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable

class DayTest : BaseTest() {
    override val example: String =
        """
        123 328  51 64 
         45 64  387 23 
          6 98  215 314
        *   +   *   +  
        """.trimIndent()

    override val resultExample1: String = "4277556"

    override val resultReal1: String = "5316572080628"

    override val resultExample2: String = "3263827"

    override val resultReal2: String = "11299263618107"

    override val input: String = "/day06/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask
}

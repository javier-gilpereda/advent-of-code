package com.gilpereda.aoc2025.day01

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable

class DayTest : BaseTest() {
    override val example: String =
        """
        L68
        L30
        R48
        L5
        R60
        L55
        L1
        L99
        R14
        L82
        """.trimIndent()

    override val resultExample1: String = "3"

    override val resultReal1: String = "1147"

    override val resultExample2: String = "6"

    override val resultReal2: String = "6789"

    override val input: String = "/day01/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask
}

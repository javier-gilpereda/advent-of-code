package com.gilpereda.aoc2025.day09

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable

class DayTest : BaseTest() {
    override val example: String =
        """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
        """.trimIndent()

    override val resultExample1: String = "50"

    override val resultReal1: String = "4782268188"

    override val resultExample2: String = "24"

    override val resultReal2: String = "1574717268"

    override val input: String = "/day09/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask
}

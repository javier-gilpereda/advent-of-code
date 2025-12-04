package com.gilpereda.aoc2025.day04

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable

class DayTest : BaseTest() {
    override val example: String =
        """
        ..@@.@@@@.
        @@@.@.@.@@
        @@@@@.@.@@
        @.@@@@..@.
        @@.@@@@.@@
        .@@@@@@@.@
        .@.@.@.@@@
        @.@@@.@@@@
        .@@@@@@@@.
        @.@.@@@.@.
        """.trimIndent()

    override val resultExample1: String = "13"

    override val resultReal1: String = "1437"

    override val resultExample2: String = "43"

    override val resultReal2: String = "8765"

    override val input: String = "/day04/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask
}

package com.gilpereda.aoc2025.day12

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable

class DayTest : BaseTest() {
    override val example: String =
        """
        0:
        ###
        ##.
        ##.

        1:
        ###
        ##.
        .##

        2:
        .##
        ###
        ##.

        3:
        ##.
        ###
        ##.

        4:
        ###
        #..
        ###

        5:
        ###
        .#.
        ###

        4x4: 0 0 0 0 2 0
        12x5: 1 0 1 0 2 2
        12x5: 1 0 1 0 3 2
        """.trimIndent()

    override val resultExample1: String = "2"

    override val resultReal1: String = ""

    override val resultExample2: String get() = TODO()

    override val resultReal2: String = ""

    override val input: String = "/day12/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask
}

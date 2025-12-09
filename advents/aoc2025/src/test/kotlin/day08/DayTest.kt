package com.gilpereda.aoc2025.day08

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable

class DayTest : BaseTest() {
    override val example: String =
        """
        10
        162,817,812
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
        425,690,689
        """.trimIndent()

    override val resultExample1: String = "40"

    override val resultReal1: String = "164475"

    override val resultExample2: String = "25272"

    override val resultReal2: String = "169521198"

    override val input: String = "/day08/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask
}

package com.gilpereda.aoc2025.day10

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class DayTest : BaseTest() {
    override val example: String =
        """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
        """.trimIndent()

    override val resultExample1: String = "7"

    override val resultReal1: String = "452"

    override val resultExample2: String = "33"

    override val resultReal2: String = ""

    override val input: String = "/day10/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask
}

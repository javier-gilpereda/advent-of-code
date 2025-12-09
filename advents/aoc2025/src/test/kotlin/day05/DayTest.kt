package com.gilpereda.aoc2025.day05

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
        3-5
        10-14
        16-20
        12-18

        1
        5
        8
        11
        17
        32
        """.trimIndent()

    override val resultExample1: String = "3"

    override val resultReal1: String = "567"

    override val resultExample2: String = "14"

    override val resultReal2: String = "354149806372909"

    override val input: String = "/day05/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask

    @ParameterizedTest
    @MethodSource("testCases")
    fun `should work in all cases`(
        lines: String,
        expected: String,
    ) {
        val result = secondTask(lines.splitToSequence("\n"))

        assertThat(result).isEqualTo(expected)
    }

    private fun testCases(): Stream<Arguments> =
        Stream.of(
            Arguments.of(
                """
                1-10
                7-15
                12-25
                """.trimIndent(),
                "25",
            ),
            Arguments.of(
                """
                1-10
                12-25
                """.trimIndent(),
                "24",
            ),
            Arguments.of(
                """
                1-10
                8-25
                """.trimIndent(),
                "25",
            ),
            Arguments.of(
                """
                1-25
                8-10
                """.trimIndent(),
                "25",
            ),
            Arguments.of(
                """
                8-10
                1-25
                """.trimIndent(),
                "25",
            ),
        )
}

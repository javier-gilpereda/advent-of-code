package com.gilpereda.aoc2025.day03

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DayTest : BaseTest() {
    override val example: String =
        """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
        """.trimIndent()

    override val resultExample1: String = "357"

    override val resultReal1: String = "17321"

    override val resultExample2: String = "3121910778619"

    override val resultReal2: String = "171989894144198"

    override val input: String = "/day03/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask

    @ParameterizedTest
    @CsvSource(
        textBlock =
            """
            Line            | Expected
            987654321111111 | 98
            811111111111119 | 89
            234234234234278 | 78
            818181911112111 | 92""",
        ignoreLeadingAndTrailingWhitespace = true,
        useHeadersInDisplayName = true,
        delimiterString = "|",
    )
    fun `should get the max voltage`(
        line: String,
        expected: Long,
    ) {
        val powerBank = PowerBank.from(line)
        assertThat(powerBank.maxPower2(2)).isEqualTo(expected)
    }
}

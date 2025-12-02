package com.gilpereda.aoc2025.day02

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class Day2Test : BaseTest() {
    override val example: String =
        """
        11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
        """.trimIndent()

    override val resultExample1: String = "1227775554"

    override val resultReal1: String = "12850231731"

    override val resultExample2: String = "4174379265"

    override val resultReal2: String = "24774350322"

    override val input: String = "/day02/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask

    @ParameterizedTest
    @ValueSource(
        strings = ["1188511885", "565656", "824824824", "2121212121"],
    )
    fun `should detect invalid values`(input: String) {
        assertThat(isInvalid2(input)).isTrue
    }
}

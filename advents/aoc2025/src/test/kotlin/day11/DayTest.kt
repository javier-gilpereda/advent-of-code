package com.gilpereda.aoc2025.day11

import com.gilpereda.aoc2025.BaseTest
import com.gilpereda.aoc2025.Executable

class DayTest : BaseTest() {
    override val example: String =
        """
        aaa: you hhh
        you: bbb ccc
        bbb: ddd eee
        ccc: ddd eee fff
        ddd: ggg
        eee: out
        fff: out
        ggg: out
        hhh: ccc fff iii
        iii: out
        """.trimIndent()

    override val example2: String =
        """
        svr: aaa bbb
        aaa: fft
        fft: ccc
        bbb: tty
        tty: ccc
        ccc: ddd eee
        ddd: hub
        hub: fff
        eee: dac
        dac: fff
        fff: ggg hhh
        ggg: out
        hhh: out
        """.trimIndent()

    override val resultExample1: String = "5"

    override val resultReal1: String = "470"

    override val resultExample2: String = "2"

    override val resultReal2: String = ""

    override val input: String = "/day11/input"

    override val run1: Executable = ::firstTask

    override val run2: Executable = ::secondTask
}

package com.gilpereda.aoc2025.day02

fun firstTask(input: Sequence<String>): String =
    input
        .first()
        .split(",")
        .flatMap { findIds(it) }
        .filter { isInvalid(it) }
        .sumOf { it.toLong() }
        .toString()

fun secondTask(input: Sequence<String>): String =
    input
        .first()
        .split(",")
        .flatMap { findIds(it) }
        .filter { isInvalid2(it) }
        .sumOf { it.toLong() }
        .toString()

private fun findIds(pair: String): List<String> {
    val (first, second) = pair.split("-").map(String::toLong)
    return LongRange(first, second + 1).toList().map(Long::toString)
}

private fun isInvalid(id: String): Boolean =
    if (id.length % 2 == 0) {
        val start = id.take((id.length / 2))
        val end = id.substring(id.length / 2)
        start == end
    } else {
        false
    }

fun isInvalid2(id: String): Boolean =
    (1..(id.length / 2))
        .any { id.chunked(it).toSet().size == 1 }

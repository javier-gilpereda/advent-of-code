package com.gilpereda.aoc2025.day07

import com.gilpereda.adventofcode.commons.geometry.Orientation
import com.gilpereda.adventofcode.commons.geometry.Point
import com.gilpereda.adventofcode.commons.map.parseToMap

fun firstTask(input: Sequence<String>): String {
    lateinit var start: Point
    val map =
        input.parseToMap { point, c ->
            when (c) {
                'S' -> {
                    start = point
                    Cell.START
                }
                '^' -> Cell.SPLIT
                else -> Cell.EMPTY
            }
        }

    tailrec fun go(
        acc: Int,
        rest: Set<Point>,
    ): Int =
        if (rest.isEmpty()) {
            acc
        } else {
            val next = rest.map { it.move(Orientation.SOUTH) }
            val newAcc = acc + next.count { map.getNullable(it) == Cell.SPLIT }
            val nextRest =
                next
                    .flatMap { point ->
                        when (map.getNullable(point)) {
                            null -> emptyList()
                            Cell.SPLIT ->
                                listOf(point.move(Orientation.EAST), point.move(Orientation.WEST))
                                    .filter { point in map }
                            else -> listOf(point)
                        }
                    }.toSet()
            go(newAcc, nextRest)
        }

    return go(0, setOf(start)).toString()
}

fun secondTask(input: Sequence<String>): String {
    lateinit var start: Point
    val map =
        input.parseToMap { point, c ->
            when (c) {
                'S' -> {
                    start = point
                    Cell.START
                }
                '^' -> Cell.SPLIT
                else -> Cell.EMPTY
            }
        }

    val pointToPaths = mutableMapOf<Point, Long>()

    fun pathsFrom(point: Point): Long =
        when (map.getNullable(point)) {
            null -> 1L
            Cell.SPLIT -> pointToPaths.getValue(point)
            else -> pathsFrom(point.move(Orientation.SOUTH))
        }

    map.indices.reversed().forEach { point ->
        val cell = map[point]
        if (cell == Cell.SPLIT) {
            pointToPaths[point] = pathsFrom(point.move(Orientation.WEST)) + pathsFrom(point.move(Orientation.EAST))
        }
    }

    return pathsFrom(start).toString()
}

enum class Cell {
    START,
    SPLIT,
    EMPTY,
}

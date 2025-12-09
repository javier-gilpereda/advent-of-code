package com.gilpereda.aoc2025.day09

import com.gilpereda.adventofcode.commons.geometry.Line
import com.gilpereda.adventofcode.commons.geometry.Point
import com.gilpereda.adventofcode.commons.geometry.Polygon
import kotlin.math.absoluteValue

fun firstTask(input: Sequence<String>): String {
    val points =
        input
            .map { line ->
                val (x, y) = line.split(",").map { it.toInt() }
                Point.from(x, y)
            }.toList()

    val result =
        points
            .flatMapIndexed { index, one ->
                points.drop(index + 1).map { other ->
                    Rectangle(one, other)
                }
            }.maxBy { it.area }
    return result.area.toString()
}

fun secondTask(input: Sequence<String>): String {
    val points =
        input
            .map { line ->
                val (x, y) = line.split(",").map { it.toInt() }
                Point.from(x, y)
            }.toList()

    val polygon = Polygon(points + points.first())
    return points
        .asSequence()
        .filter { polygon.pointIsInside(it, true) }
        .flatMapIndexed { index, one ->
            points.drop(index + 1).asSequence().mapNotNull { other ->
                Rectangle(one, other).let { rectangle ->
                    if (rectangle in polygon) rectangle.area else null
                }
            }
        }.maxOrNull()
        .toString()
}

data class Rectangle(
    private val first: Point,
    private val third: Point,
) {
    val second: Point = Point.from(first.x, third.y)
    val forth: Point = Point.from(third.x, first.y)
    val area = ((first.x - third.x).absoluteValue + 1) * ((first.y - third.y).absoluteValue + 1L)

    val sides: List<Line> =
        listOf(
            Line(first, second),
            Line(second, third),
            Line(third, forth),
            Line(forth, first),
        )

    val points: Sequence<Point>
        get() = (listOf(first, second, third, forth) + sides.flatMap(Line::points)).asSequence()
}

operator fun Polygon.contains(rectangle: Rectangle): Boolean = rectangle.points.all { pointIsInside(it, true) }

// 4782268188
// 2147481720

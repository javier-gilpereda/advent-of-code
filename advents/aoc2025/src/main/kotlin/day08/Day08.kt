package com.gilpereda.aoc2025.day08

import com.gilpereda.adventofcode.commons.geometry.Point3D

// 67158 too low
// 164475
fun firstTask(input: Sequence<String>): String {
    val inputList = input.toList()
    val count = inputList.first().toInt()
    val points =
        inputList
            .drop(1)
            .map(Point3D::from)

    val distances = getDistancesBetweenPoints(points)

    return getCircuits(points, distances, count)
        .map { it.size.toLong() }
        .sortedDescending()
        .take(3)
        .reduce { one, other -> one * other }
        .toString()
}

fun secondTask(input: Sequence<String>): String {
    val inputList = input.toList()
    val points =
        inputList
            .drop(1)
            .map(Point3D::from)

    val distances = getDistancesBetweenPoints(points)

    val (one, other, _) = getLastConnectingPoints(points, distances)

    return (one.x * other.x).toString()
}

private fun getDistancesBetweenPoints(points: List<Point3D>): List<Distances> {
    tailrec fun go(
        acc: List<Distances>,
        rest: List<Point3D>,
    ): List<Distances> =
        if (rest.size == 1) {
            acc.sortedBy { it.distance }
        } else {
            val next = rest.first()
            val newRest = rest.drop(1)
            val newAcc = acc + newRest.map { point -> Distances(next, point) }
            go(newAcc, newRest)
        }

    return go(emptyList(), points)
}

private fun getCircuits(
    points: List<Point3D>,
    distances: List<Distances>,
    count: Int,
): List<Set<Point3D>> {
    tailrec fun go(
        acc: List<MutableSet<Point3D>>,
        rest: List<Distances>,
        count: Int,
    ): List<Set<Point3D>> =
        if (count == 0 && rest.isNotEmpty()) {
            acc
        } else {
            val next = rest.first()
            val commonCircuit = acc.firstOrNull { next.one in it && next.other in it }
            if (commonCircuit != null) {
                // We have to do nothing
                go(acc, rest.drop(1), count - 1)
            } else {
                val circuitOne = acc.firstOrNull { next.one in it }
                val circuitTwo = acc.firstOrNull { next.other in it }
                val nextAcc =
                    if (circuitOne == null) {
                        if (circuitTwo == null) {
                            acc.plus<MutableSet<Point3D>>(mutableSetOf(next.one, next.other))
                        } else {
                            circuitTwo.addAll(next.points)
                            acc
                        }
                    } else {
                        if (circuitTwo == null) {
                            circuitOne.addAll(next.points)
                            acc
                        } else {
                            circuitOne.addAll(circuitTwo)
                            acc.minus<MutableSet<Point3D>>(circuitTwo)
                        }
                    }
                go(nextAcc, rest.drop(1), count - 1)
            }
        }

    return go(points.map { mutableSetOf(it) }, distances, count)
}

private fun getLastConnectingPoints(
    points: List<Point3D>,
    distances: List<Distances>,
): Distances {
    tailrec fun go(
        acc: List<MutableSet<Point3D>>,
        rest: List<Distances>,
    ): Distances {
        val next = rest.first()
        val commonCircuit = acc.firstOrNull { next.one in it && next.other in it }
        val nextAcc =
            if (commonCircuit != null) {
                // We have to do nothing
                acc
            } else {
                val circuitOne = acc.firstOrNull { next.one in it }
                val circuitTwo = acc.firstOrNull { next.other in it }
                if (circuitOne == null) {
                    if (circuitTwo == null) {
                        acc.plus<MutableSet<Point3D>>(mutableSetOf(next.one, next.other))
                    } else {
                        circuitTwo.addAll(next.points)
                        acc
                    }
                } else {
                    if (circuitTwo == null) {
                        circuitOne.addAll(next.points)
                        acc
                    } else {
                        circuitOne.addAll(circuitTwo)
                        acc.minus<MutableSet<Point3D>>(circuitTwo)
                    }
                }
            }

        return if (nextAcc.size == 1) {
            next
        } else {
            go(nextAcc, rest.drop(1))
        }
    }

    return go(points.map { mutableSetOf(it) }, distances)
}

data class Distances(
    val one: Point3D,
    val other: Point3D,
    val distance: Double = one.straightDistanceTo(other),
) {
    val points: Set<Point3D>
        get() = setOf(one, other)
}

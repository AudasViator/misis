package pro.prieran.misis.chm.second_homework.mnk

import pro.prieran.misis.Point
import java.lang.Math.abs
import java.lang.Math.pow

class Polynome {
    fun gramMatrix(points: List<Point>, m: Int): Array<Double> {
        val a = Array(m, { i -> Array<Double>(m + 1, { j -> 0.0 }) })
        for (i in 0..a.size - 1) {
            for (j in 0..a[i].size - 2) {
                for (k in 0..points.size - 1)
                    a[i][j] += pow(points[k].x, (i + j).toDouble())
            }
        }

        for (i in 0..a.size - 1) {
            for (j in 0..points.size - 1)
                a[i][a[i].size - 1] += pow(points[j].x, i.toDouble()) * points[j].y
        }
        return gauss(a)
    }

    fun gramMatrix(points: List<Point>, from: Double, to: Double): Array<Double> {
        val m = points.size

        val a = Array(m, { i -> Array<Double>(m + 1, { j -> 0.0 }) })

        for (i in 0..a.size - 1) {
            for (j in 0..a[i].size - 2) {
                a[i][j] = scalarProduct(from, to, newPolynomial(from, to, i), newPolynomial(from, to, j))
            }
        }

        for (i in 0..a.size - 1) {
            a[i][a[i].size - 1] = scalarProduct(points, newPolynomial(from, to, i))
        }
        return gauss(a)
    }

    fun newPolynomial(from: Double, to: Double, power: Int): (Double) -> Double {
        val zero = { x: Double -> 1.0 }
        val d1 = scalarProduct(from, to, { x -> x }, { x -> zero(x) }) /
                scalarProduct(from, to, { x -> zero(x) }, { x -> zero(x) })
        val first = { x: Double -> x - d1 }

        if (power == 0) {
            return zero
        } else if (power == 1) {
            return first
        } else {
            val currentMinusOne = zero
            val current = first
            val d2 = scalarProduct(from, to, { x -> x * current(x) }, { x -> current(x) }) /
                    scalarProduct(from, to, { x -> current(x) }, { x -> current(x) })
            val d3 = scalarProduct(from, to, { x -> x * current(x) }, { x -> currentMinusOne(x) }) /
                    scalarProduct(from, to, { x -> currentMinusOne(x) }, { x -> currentMinusOne(x) })
            var next = { x: Double -> x * current(x) - d2 * current(x) - d3 * currentMinusOne(x) }

            for (i in 2..power) {
                val currentMinusOne2 = current
                val current2 = next
                val d21 = scalarProduct(from, to, { x -> x * current2(x) }, { x -> current2(x) }) /
                        scalarProduct(from, to, { x -> current2(x) }, { x -> current2(x) })
                val d31 = scalarProduct(from, to, { x -> x * current2(x) }, { x -> currentMinusOne2(x) }) /
                        scalarProduct(from, to, { x -> currentMinusOne2(x) }, { x -> currentMinusOne2(x) })
                next = { x: Double -> x * current2(x) - d21 * current2(x) - d31 * currentMinusOne2(x) }
            }

            return next
        }
    }

    fun scalarProduct(from: Double, to: Double, func1: (Double) -> Double, func2: (Double) -> Double): Double {
        return integrate(from, to, func1, func2)
    }

    fun scalarProduct(points: List<Point>, func: (Double) -> Double): Double {
        var product = 0.0
        for (point in points) {
            product += point.y * func(point.x)
        }
        return product
    }

    fun gramMatrix(m: Int, func: Func, from: Double, to: Double): Array<Double> {
        val a = Array(m, { i -> Array<Double>(m + 1, { j -> 0.0 }) })

        for (i in 0..a.size - 1) {
            for (j in 0..a[i].size - 2) {
                a[i][j] = integrate(from, to, { x: Double -> pow(x, i.toDouble()) }, { x: Double -> pow(x, j.toDouble()) })
            }
        }

        for (i in 0..a.size - 1) {
            a[i][a[i].size - 1] = integrate(from, to, { x: Double -> pow(x, i.toDouble()) }, { x -> func.func(x) })
        }
        return gauss(a)
    }

    private fun integrate(from: Double, to: Double, func1: (Double) -> Double, func2: (Double) -> Double): Double {
        val step = 1E-4

        val func = { x: Double -> func1(x) * func2(x) }
        var integral = 0.0
        var current = from
        while (true) {
            current += step

            integral += 4.0 * func(current) + 2 * func(current + step)

            if (current > to) {
                break
            }
        }

        current *= step / 3.0

        return integral
    }

    private fun gauss(coefficients: Array<Array<Double>>): Array<Double> {
        val EPS = 1E-12
        val n = coefficients.size
        val m = coefficients[0].size - 1

        val a = Array(n, { i -> Array<Double>(coefficients[i].size, { j -> coefficients[i][j] }) })

        val ans = Array(m, { i -> 0.0 })
        val where = Array(m, { i -> -1 })

        var col = -1
        var row = 0
        while (col < m && row < n) {
            col++
            var sel = row
            for (i in row..n - 1) {
                if (abs(a[i][col]) > abs(a[sel][col])) {
                    sel = i
                }
            }
            if (abs(a[sel][col]) < EPS) {
                continue
            }
            for (i in col..m) {
                val temp = a[sel][i]
                a[sel][i] = a[row][i]
                a[row][i] = temp
            }
            where[col] = row

            for (i in 0..n - 1) {
                if (i != row) {
                    val c = a[i][col] / a[row][col]
                    for (j in col..m) {
                        a[i][j] -= a[row][j] * c
                    }
                }
            }
            row++
        }

        for (i in 0..m - 1) {
            if (where[i] != -1) {
                ans[i] = a[where[i]][m] / a[where[i]][i]
            }
        }

        for (i in 0..n - 1) {
            var sum = 0.0
            for (j in 0..m - 1) {
                sum += ans[j] * a[i][j]
            }

            if (abs(sum - a[i][m]) > EPS) {
                println("No, ${m - 1}")
                break
            }
        }

        for (i in 0..m - 1) {
            if (where[i] == -1) {
                println("Inf")
                break
            }
        }

        return ans
    }
}
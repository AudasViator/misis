package pro.prieran.misis.chm.second_homework.mnk

import pro.prieran.misis.Point
import java.lang.Math.abs
import java.lang.Math.pow

class Polynome {

    fun function(from: Double, to: Double, coefs: Array<Double>, x: Double): Double {
        var value = 0.0
        for (i in 0..coefs.size - 1) {
            value += coefs[i] * newPolynomial(from, to, i)(x)
        }
        return value
    }

    fun gramMatrix(points: List<Point>): Array<Double> {
        val m = points.size
        val from = 0.0
        val to = 6.0

        val a = Array(m, { i -> Array<Double>(m + 1, { j -> 0.0 }) })

        for (i in 0..a.size - 1) {
            for (j in 0..a[i].size - 2) {
                a[i][j] = scalarProduct(points, newPolynomial(from, to, j), newPolynomial(from, to, i))
            }
        }

        for (i in 0..a.size - 1) {
            a[i][a[i].size - 1] = scalarProduct(points, newPolynomial(from, to, i))
        }
        return gauss(a)
    }

    fun newPolynomial(from: Double, to: Double, power: Int): (Double) -> Double {
        return { x: Double -> pow(x, power.toDouble()) }
    }

    fun scalarProduct(points: List<Point>, func: (Double) -> Double): Double {
        var product = 0.0
        for (point in points) {
            product += point.y * func(point.x)
        }
        return product
    }

    fun scalarProduct(points: List<Point>, func1: (Double) -> Double, func2: (Double) -> Double): Double {
        var product = 0.0
        for (point in points) {
            product += func1(point.x) * func2(point.x)
        }
        return product
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
package pro.prieran.misis.mm.mnk

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

//        for (i in 0..n - 1) {
//            var sum = 0.0
//            for (j in 0..m - 1) {
//                sum += ans[j] * a[i][j]
//            }
//
//            if (abs(sum - a[i][m]) > EPS) {
//                println("No, ${m - 1}")
//                break
//            }
//        }
//
//        for (i in 0..m - 1) {
//            if (where[i] == -1) {
//                println("Inf")
//                break
//            }
//        }

        return ans
    }
}
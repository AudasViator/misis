package pro.prieran.misis.chm.second_homework.mnk

import pro.prieran.misis.Point
import java.lang.Math.abs

class Approximator(val points: List<Point>, maxPow: Int) {
    val coefs = gramMatrix(maxPow + 1)

    fun getValue(x: Double): Double {
        var value = 0.0
        for (i in 0..coefs.size - 1) {
            value += coefs[i] * newPolynomial(i)(x)
        }
        return value
    }

    private fun gramMatrix(maxPow: Int): Array<Double> {
        val a = Array(maxPow, { i -> Array<Double>(maxPow + 1, { j -> 0.0 }) })

        for (i in 0..a.size - 1) {
            for (j in 0..a[i].size - 2) {
                a[i][j] = scalarProduct(newPolynomial(j), newPolynomial(i))
            }
        }

        for (i in 0..a.size - 1) {
            a[i][a[i].size - 1] = scalarProduct(newPolynomial(i))
        }
        return gauss(a)
    }

    // Многочлены Чебышева первого рода
//    private fun newPolynomial(pow: Int): (Double) -> Double {
//        val zero = { x: Double -> 1.0 }
//        val first = { x: Double -> x }
//
//        if (pow == 0) {
//            return zero
//        } else if (pow == 1) {
//            return first
//        } else {
//            var current = first
//            var before = first
//            var before2 = zero
//            for (i in 2..pow) {
//                val curBefore = before
//                val curBefore2 = before2
//                current = { x: Double -> 2 * x * curBefore(x) - curBefore2(x) }
//                before2 = before
//                before = current
//            }
//
//            return current
//        }
//    }

    // Степенные функции
//    private fun newPolynomial(pow: Int): (Double) -> Double {
//        return { x: Double -> pow(x, pow.toDouble()) }
//    }

    // Ортогонализация Грама ― Шмидта (или нет?)
    private fun newPolynomial(pow: Int): (Double) -> Double {
        val zero = { x: Double -> 1.0 }
        val b1 = scalarProduct({ x -> x }, zero) /
                scalarProduct(zero, zero)
        val first = { x: Double -> x - b1 }

        if (pow == 0) {
            return zero
        } else if (pow == 1) {
            return first
        } else {
            var current = first
            var before = first
            var before2 = zero
            for (i in 2..pow) {
                val curBefore = before
                val curBefore2 = before2

                val b2 = scalarProduct({ x -> x * curBefore(x) }, curBefore) /
                        scalarProduct(curBefore, curBefore)
                val b3 = scalarProduct({ x -> x * curBefore(x) }, curBefore2) /
                        scalarProduct(curBefore2, curBefore2)

                current = { x: Double -> x * curBefore(x) - b2 * curBefore(x) - b3 * curBefore2(x) }
                before2 = before
                before = current
            }

            return current
        }
    }

    private fun scalarProduct(func: (Double) -> Double): Double {
        var product = 0.0
        for (point in points) {
            product += point.y * func(point.x)
        }
        return product
    }

    private fun scalarProduct(func1: (Double) -> Double, func2: (Double) -> Double): Double {
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
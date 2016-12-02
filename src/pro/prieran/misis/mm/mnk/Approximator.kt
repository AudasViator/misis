package pro.prieran.misis.mm.mnk

import java.lang.Math.abs
import java.lang.Math.pow
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class Approximator(func: (Double) -> Double, val from: Double, val to: Double, maxPow: Int) {
    val functions = Array(maxPow + 1, { i -> powerPolynomial(i) })
    val gramMatrix = gramMatrix(maxPow + 1, func, from, to)

    fun getFunction(pow: Int): (Double) -> Double = functions[pow]

    fun getCoefs(pow: Int): Array<Double> {
        val a = Array(pow + 1, { i -> Array<Double>(pow + 2, { j -> gramMatrix[i][j] }) })
        for (i in 0..a.size - 1) {
            a[i][a[i].size - 1] = gramMatrix[i][gramMatrix[i].size - 1]
        }
        return gauss(a)
    }

    private fun gramMatrix(m: Int, func: (Double) -> Double, from: Double, to: Double): Array<Array<Double>> {
        val a = Array(m, { i -> Array<Double>(m + 1, { j -> 0.0 }) })

        println("Let's go")
        val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        val left = AtomicInteger(a.size - 1)
        for (i in 0..a.size - 1) {
            executorService.submit {
                for (j in i..a[i].size - 2) {
                    a[i][j] = scalarProduct(from, to, functions[i], functions[j])
                }
                println("It remains to fill ${left.andDecrement} rows from ${a.size - 1}")
            }
        }

        executorService.submit {
            for (i in 0..a.size - 1) {
                a[i][a[i].size - 1] = scalarProduct(from, to, functions[i], { x -> func(x) })
            }
        }

        executorService.shutdown()
        executorService.awaitTermination(14, TimeUnit.DAYS)

        for (i in 0..a.size - 1) {
            for (j in 0..i) {
                a[i][j] = a[j][i]
            }
        }

        return a
    }

    private fun scalarProduct(from: Double, to: Double, func1: (Double) -> Double, func2: (Double) -> Double): Double {
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

    private fun powerPolynomial(pow: Int): (Double) -> Double = { x -> pow(x, pow.toDouble()) }

    //     Многочлены Чебышева первого рода
    private fun chebyshev(pow: Int): (Double) -> Double {
        val zero = { x: Double -> 1.0 }
        val first = { x: Double -> x }

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
                current = { x: Double -> 2 * x * curBefore(x) - curBefore2(x) }
                before2 = before
                before = current
            }

            return current
        }
    }

    // Ортогонализация Грама ― Шмидта (или нет?)
    private fun orthogonal(pow: Int): (Double) -> Double {
        val zero = { x: Double -> 1.0 }
        val b1 = scalarProduct(from, to, { x -> x }, zero) /
                scalarProduct(from, to, zero, zero)
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

                val b2 = scalarProduct(from, to, { x -> x * curBefore(x) }, curBefore) /
                        scalarProduct(from, to, curBefore, curBefore)
                val b3 = scalarProduct(from, to, { x -> x * curBefore(x) }, curBefore2) /
                        scalarProduct(from, to, curBefore2, curBefore2)

                current = { x: Double -> x * curBefore(x) - b2 * curBefore(x) - b3 * curBefore2(x) }
                before2 = before
                before = current
            }

            return current
        }
    }
}
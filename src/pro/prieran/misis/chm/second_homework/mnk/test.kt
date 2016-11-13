package pro.prieran.misis.chm.second_homework.mnk

import java.lang.Math.abs

fun main(args: Array<String>) {
    val a = arrayOf(
            arrayOf(65.0, -5.0, -11.0, -19.0, -9.0),
            arrayOf(1.0, 64.0, -11.0, -20.0, -8.0),
            arrayOf(5.0, -1.0, 57.0, -19.0, -7.0),
            arrayOf(11.0, 4.0, -5.0, 50.0, -6.0))
    for (value in gauss(a)) {
        println(value)
    }
}

//fun test(func: (Double, Double) -> Double): Double {
//    return func(16.0, 32.0)
//}

fun gauss(coefficients: Array<Array<Double>>): Array<Double> {
    val EPS = 1E-4
    val n = coefficients.size
    val m = coefficients[0].size - 1

    val a = Array(n, { i -> Array<Double>(coefficients[i].size, { i -> 0.0 }) })
    for (i in 0..coefficients.size - 1) {
        for (j in 0..coefficients[i].size - 1) {
            a[i][j] = coefficients[i][j]
        }
    }

    val ans = Array(m, { i -> 0.0 })
    val where = Array(m, { i -> -1 })

    var col = 0
    var row = 0
    while (col < m && row < n) {
        var sel = row
        for (i in row..n - 1) {
            if (abs(a[i][col]) > abs(a[sel][col])) {
                sel = i
            }
        }
        if (abs(a[sel][col]) < EPS) {
            continue
        }
        for (i in 0..n - 1) {
            val temp = a[sel][i]
            a[sel][i] = a[row][i]
            a[row][i] = temp
        }
        where[col] = row

        for (i in 0..n - 1) {
            if (i != row) {
                val c = a[i][col] / a[row][col]
                for (k in col..m) {
                    a[i][k] -= a[row][k] * c
                }
            }
        }
        row++
        col++
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

        if (abs(sum - a[i][m]) > EPS)
            throw IllegalStateException()
    }

    for (i in 0..m - 1) {
        if (where[i] == -1)
            throw IllegalStateException()
    }

    return ans
}
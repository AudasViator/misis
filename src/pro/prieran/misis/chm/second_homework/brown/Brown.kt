package pro.prieran.misis.chm.second_homework.brown

import java.lang.Math.pow

fun main(args: Array<String>) {
    val x_ = Array<Double>(3, { i -> 0.0 })
    x_[0] = 1.0
    x_[1] = 2.0
    x_[2] = 3.0

    for (i in 1..10000) {
        val x = X(x_[0], x_[1], x_[2])
        val y = Y(x, x_[0], x_[1], x_[2])
        val z = Z(x, y, x_[0], x_[1], x_[2])

        x_[0] = x
        x_[1] = y
        x_[2] = z
    }

    println("x=${x_[0]}")
    println("y=${x_[1]}")
    println("z=${x_[2]}")
    println()
}

fun Z(x: Double, y: Double, x0: Double, y0: Double, z0: Double): Double {
    return z0 + (x0 + 0.02 * y0 * y0 - 0.005 * z0 * z0 - 1 + x - x0 + 0.04 * y0 * (y - y0)) / (0.01 * z0)
}

fun Y(x: Double, x0: Double, y0: Double, z0: Double): Double {
    return y0 + (-y0 + 0.2 * x0 * x0 + 0.1 * Z(x0, y0, x0, y0, z0) + 1 + 0.4 * x0 * (x - x0) + 10 * (x - x0) / z0) / (1 - 0.4 * y0 / z0)
}

fun X(x0: Double, y0: Double, z0: Double): Double {
    return x0 + (0.2 * pow(Y(x0, x0, y0, z0), 4.0) + 0.0125 * x0 * x0 + 2 - Z(x0, y0, x0, y0, z0)) / (-0.0125 * x0 + 1.0 / 0.01 / z0 - 0.8 * pow(Y(x0, x0, y0, z0), 3.0) * (0.4 * x0 + 10 / z0) / (1 - 0.4 * y0 / z0))
}
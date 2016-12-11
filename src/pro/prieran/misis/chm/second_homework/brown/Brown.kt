package pro.prieran.misis.chm.second_homework.brown

val DIV_EPS = 1E-4 // Для производных
val ANS_EPS = 1E-8 // Для ответа

fun main(args: Array<String>) {
    var prevX = 0.0
    var prevY = 0.0
    var prevZ = 0.0

    var currX: Double
    var currY = prevY
    var currZ = prevZ

    for (i in 1..10000) {
        currX = x(currY, currZ, prevX, prevY, prevZ)
        currY = y(currX, currY, currZ, prevX, prevY, prevZ)
        currZ = z(currX, currY, currZ, prevX, prevY, prevZ)

        println("Итерация №$i")
        println("x=$currX")
        println("y=$currY")
        println("z=$currZ")
        println()

        val needToStop = (Math.abs(currX - prevX) < ANS_EPS)
                && (Math.abs(currY - prevY) < ANS_EPS)
                && (Math.abs(currY - prevY) < ANS_EPS)

        if (needToStop) {
            break
        }

        prevX = currX
        prevY = currY
        prevZ = currZ
    }
}

// Сами функции
fun f1(x: Double, y: Double, z: Double): Double {
    return x + 0.02 * y * y - 0.005 * z * z - 1
}

fun f2(x: Double, y: Double, z: Double): Double {
    return y - 0.1 * z - 0.2 * x * x - 1
}

fun f3(x: Double, y: Double, z: Double): Double {
    return z - 0.2 * y * y * y * y - 0.0125 * x * x - 2
}


// Производные первой функции
fun f1DivX(x: Double, y: Double, z: Double): Double {
    return (f1(x + DIV_EPS, y, z) - f1(x, y, z)) / DIV_EPS
}

fun f1DivY(x: Double, y: Double, z: Double): Double {
    return (f1(x, y + DIV_EPS, z) - f1(x, y, z)) / DIV_EPS
}

fun f1DivZ(x: Double, y: Double, z: Double): Double {
    return (f1(x, y, z + DIV_EPS) - f1(x, y, z)) / DIV_EPS
}


// Производные второй функции
fun f2DivX(x: Double, y: Double, z: Double): Double {
    return (f2(x + DIV_EPS, y, z) - f2(x, y, z)) / DIV_EPS
}

fun f2DivY(x: Double, y: Double, z: Double): Double {
    return (f2(x, y + DIV_EPS, z) - f2(x, y, z)) / DIV_EPS
}

fun f2DivZ(x: Double, y: Double, z: Double): Double {
    return (f2(x, y, z + DIV_EPS) - f2(x, y, z)) / DIV_EPS
}


// Производные третьей функции
fun f3DivX(x: Double, y: Double, z: Double): Double {
    return (f3(x + DIV_EPS, y, z) - f3(x, y, z)) / DIV_EPS
}

fun f3DivY(x: Double, y: Double, z: Double): Double {
    return (f3(x, y + DIV_EPS, z) - f3(x, y, z)) / DIV_EPS
}

fun f3DivZ(x: Double, y: Double, z: Double): Double {
    return (f3(x, y, z + DIV_EPS) - f3(x, y, z)) / DIV_EPS
}


// Аппроксимации
fun x(y: Double, z: Double, x0: Double, y0: Double, z0: Double): Double {
    return x0 - (1 / f1DivX(x0, y0, z0)) * ((y - y0) * f1DivY(x0, y0, z0) + (z - z0) * f1DivZ(x0, y0, z0) + f1(x0, y0, z0))
}

fun y(x: Double, y: Double, z: Double, x0: Double, y0: Double, z0: Double): Double {
    val f2DivY = f2DivX(x0, y0, z0) * xDivY(x, y, x0, y0, z0) + f2DivY(x0, y0, z0)
    val f2DivZ = f2DivX(x0, y0, z0) * xDivZ(x, y, x0, y0, z0) + f2DivZ(x0, y0, z0)
    return y0 - (1 / f2DivY) * ((z - z0) * f2DivZ + f2(x0, y0, z0))
}

fun z(x: Double, y: Double, z: Double, x0: Double, y0: Double, z0: Double): Double {
    val f3DivZ = f3DivX(x0, y0, z0) * xDivZ(y, z, x0, y0, z0) + f3DivY(x0, y0, z0) * yDivZ(x, y, z, x0, y0, z0) + f3DivZ(x0, y0, z0)
    return z0 - (1 / f3DivZ) * ((z - z0) * f3DivZ + f3(x0, y0, z0))
}


// Производные аппроксимаций
fun xDivY(y: Double, z: Double, x0: Double, y0: Double, z0: Double): Double {
    return (x(y + DIV_EPS, z, x0, y0, z0) - x(y, z, x0, y0, z0)) / DIV_EPS
}

fun xDivZ(y: Double, z: Double, x0: Double, y0: Double, z0: Double): Double {
    return (x(y, z + DIV_EPS, x0, y0, z0) - x(y, z, x0, y0, z0)) / DIV_EPS
}

fun yDivZ(x: Double, y: Double, z: Double, x0: Double, y0: Double, z0: Double): Double {
    return (y(x, y, z + DIV_EPS, x0, y0, z0) - y(x, y, z, x0, y0, z0)) / DIV_EPS
}
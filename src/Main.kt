import kotlin.math.pow

sealed interface Figure {
    val property: Double
}

data class Circle(override val property: Double): Figure {
}

data class Square(override val property: Double): Figure {
}

interface ConsoleService {
    fun work()
}

interface FigureService {
    fun addSquare(property: Double)
    fun addCircle(property: Double)
    fun getPerimeter(): Unit
    fun getArea(): Unit
}

enum class Operation {
    INSERT, EXIT, GET_PERIMETER, GET_AREA
}

enum class FigureType {
    CIRCLE, SQUARE
}

fun badPropertyException(invalidProperty: String): Nothing {
    println("Введено неверное значение параметра property: $invalidProperty")
    throw Exception()
}

fun wrongOperationTypeException(invalidOperation: String): Nothing {
    println("Введен неизвестный тип операции: $invalidOperation")
    throw Exception()
}

fun wrongFigureTypeException(invalidFigureType: String): Nothing {
    println("Введено неизвестное название фигуры: $invalidFigureType")
    throw Exception()
}


object FigureServiceImpl: FigureService {
    private val figureList: MutableList<Figure> = mutableListOf()
    override fun addSquare(property: Double) {
        figureList.add(Square(property))
        println(figureList.last())
    }

    override fun addCircle(property: Double) {
        figureList.add(Circle(property))
        println(figureList.last())
    }

    override fun getPerimeter() {
        figureList.forEachIndexed { index, figure ->
            print("Perimeter of figure with index $index is ")
            when(figure) {
                is Circle -> println("${figure.property * 3.14 * 2}")
                is Square -> println("${figure.property * 4}")
            }
        }
    }

    override fun getArea() {
        figureList.forEachIndexed { index, figure ->
            println("Perimeter of figure with index $index is ")
            when(figure) {
                is Circle -> println("${figure.property.pow(2) * 3.14}")
                is Square -> println("${figure.property.pow(2)}")
            }
        }
    }
}

object ConsoleServiceImpl: ConsoleService {
    private fun getPerimeter() {
        FigureServiceImpl.getPerimeter()
    }

    private fun getArea() {
        FigureServiceImpl.getArea()
    }

    private fun getOperation(operation: String): Operation {
        when (operation) {
            "addFigure" -> return Operation.INSERT
            "exit" -> return Operation.EXIT
            "getArea" -> return Operation.GET_AREA
            "getPerimeter" -> return Operation.GET_PERIMETER
            else -> wrongOperationTypeException(operation)
        }
    }

    private fun addFigure() {
        println("Введите название фигуры, которую вы хотите добавить(square / circle)")
        val enteredType = readln()
        val figureType = when (enteredType) {
            "square" -> FigureType.SQUARE
            "circle" -> FigureType.CIRCLE
            else -> wrongFigureTypeException(enteredType)
        }
        println("Введите обязательный параметр property")
        val property = readln()
        try {
            when(figureType) {
                FigureType.SQUARE -> FigureServiceImpl.addSquare(property.toDouble())
                FigureType.CIRCLE -> FigureServiceImpl.addCircle(property.toDouble())
            }
        } catch (ex: Exception) {
            badPropertyException(property)
        }
    }

    override fun work() {
        while(true) {
            println("""Введите тип операции, которую хотите исполнить:
                1) добавить фигуру (addFigure)
                2) получить площадь всех фигур (getArea)
                3) получить периметр всех фигур (getPerimeter)
                4) завершить выполнение (exit)"""
            )
            val operation = getOperation(readln())
            when (operation) {
                Operation.INSERT -> addFigure()
                Operation.GET_AREA -> getArea()
                Operation.GET_PERIMETER -> getPerimeter()
                Operation.EXIT -> break
            }
        }
    }
}

fun main() {
    ConsoleServiceImpl.work()
}
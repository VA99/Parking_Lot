package parking

import java.util.Scanner

data class Car(
    var reg: String = "",
    var color: String = "",
    var parkedAt: Int = 0
)

class ParkingLot {
    private val errMess1 = "Sorry, parking lot is not created."
    private val errMess2 = "Parking lot is empty."

    private var parkedCars = arrayOf<Car>()
    private var isCreated = false
    private var spots = 0

    private fun regByColor(car: Car, isFirst: Boolean) = if (isFirst) car.reg else car.color
    private fun spotByColor(car: Car, isFirst: Boolean) = if (isFirst) car.parkedAt else car.color
    private fun spotByReg(car: Car, isFirst: Boolean) = if (isFirst) car.parkedAt else car.reg
    private fun getCarInfo(option: String, car: Car, isFirst: Boolean): String {
        return when (option) {
            "reg_by_color" -> regByColor(car, isFirst)
            "spot_by_color" -> spotByColor(car, isFirst)
            "spot_by_reg" -> spotByReg(car, isFirst)
            else -> ""
        }.toString().toLowerCase()
    }

    fun create(spots: Int): String {
        isCreated = true
        this.spots = spots
        return "Created a parking lot with $spots spots."
    }
    fun park(car: Car): String {
        return if (!isCreated) errMess1
        else {
            val size = parkedCars.size
            return if (size == spots) "Sorry, parking lot is full."
            else {
                parkedCars += car
                parkedCars[size].parkedAt = size + 1
                "${car.color} car parked on the spot ${size + 1}."
            }
        }
    }
    fun leave(spot: Int): String {
        val size = parkedCars.size
        val lastIndex = parkedCars.lastIndex
        return if (!isCreated) errMess1
        else {
            if (spot in 1..this.spots) {
                if (spot in 1..size) {
                    for (index in spot..lastIndex)
                        parkedCars[index - 1] = parkedCars[index]
                    var newArr = arrayOf<Car>()
                    for (index in 0 until lastIndex)
                        newArr += parkedCars[index]
                    parkedCars = newArr
                    "Spot $spot is free."
                }
                else "There is no car in the spot $spot."
            } else ""
        }
    }
    fun status(): String {
        return when {
            !isCreated -> errMess1
            parkedCars.isEmpty() -> errMess2
            else -> {
                var text = ""
                for (index in parkedCars.indices) {
                    text += "${parkedCars[index].parkedAt}"+
                            " ${parkedCars[index].reg}"+
                            " ${parkedCars[index].color}"
                    if (index != parkedCars.lastIndex) text += "\n"
                }
                return text
            }
        }
    }
    fun getValue(option: String, value: String): String {
        val cars = parkedCars
        return when {
            cars.isEmpty() -> errMess1
            !isCreated -> errMess2
            else -> {
                var text = ""
                var equalValues = false

                for (index in cars.indices) {
                    val targetValue = getCarInfo(option, cars[index], false)
                    if (targetValue == value.toLowerCase()) {
                        if (equalValues && (index > 0)) text += ", "
                        text += getCarInfo(option, cars[index], true)
                        equalValues = true
                    }
                }

                val type = when (option) {
                    "reg_by_color", "spot_by_color" -> "color"
                    "spot_by_reg" -> "registration number"
                    else -> ""
                }

                if (!equalValues) "No cars with $type $value were found."
                else text
            }
        }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val parkingLot = ParkingLot()
    val car = Car()

    do {
        val lineScanner = Scanner(scanner.nextLine())
        val option = lineScanner.next()

        var message = if (option == "status") parkingLot.status() else ""
        if (lineScanner.hasNext()) {
            when (option) {
                "create" -> message = parkingLot.create(lineScanner.nextInt())
                "leave" -> message = parkingLot.leave(lineScanner.nextInt())
                "reg_by_color", "spot_by_color", "spot_by_reg" ->
                    message = parkingLot.getValue(option, lineScanner.next())
            }
            if (option == "park") {
                val reg = lineScanner.next()
                if (lineScanner.hasNext())
                    message = parkingLot.park(car.copy(reg = reg, color = lineScanner.next()))
            }
        }

        println(message)
    } while (option != "exit")
}
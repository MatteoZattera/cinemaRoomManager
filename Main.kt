import java.util.Locale

class CinemaRoom(private val rowsNumber: Int, private val seatsPerRow: Int) {

    private val seats = mutableListOf<MutableList<Seat>>()
    private val totalSeats = rowsNumber * seatsPerRow

    private var purchasedTickets = 0
    private var    currentIncome = 0
    private var      totalIncome = 0

    /** Creates the room and set the price of each seat */
    init {
        for (row in 0 until rowsNumber) {
            seats.add(mutableListOf())
            for (seat in 0 until seatsPerRow) {
                val priceOfCurrentSeat = setPriceOfSeat(rowPosition = row + 1)
                seats[row].add(Seat(priceOfCurrentSeat))
                totalIncome += priceOfCurrentSeat
            }
        }
    }

    /** Sets the price of the seat at given row position */
    private fun setPriceOfSeat(rowPosition: Int) = if (totalSeats <= 60 || rowPosition <= rowsNumber / 2) 10 else 8

    /** Returns the price of the seat at given position */
    fun getPriceOfSeatAt(rowPosition: Int, seatPosition: Int) = seats[rowPosition - 1][seatPosition - 1].getPrice()

    /** Buy seat at given position and returns false if it is already bought or do not exist */
    fun takeSeatAt(rowPosition: Int, seatPosition: Int): Boolean {
        try {
            val seat = seats[rowPosition - 1][seatPosition - 1]
            return if (seat.isTaken()) {
                println("That ticket has already been purchased!")
                false
            } else {
                seat.mark = "B"
                purchasedTickets++
                currentIncome += seat.getPrice()
                true
            }
        } catch (e: IndexOutOfBoundsException) {
            println("Wrong input") //The seat do not exist
            return false
        }
    }

    /** Prints the cinema room in the standard output */
    fun print() {
        print("Cinema:\n  ")
        (1..seatsPerRow).forEach { print(" $it") }
        println()
        for (rowPosition in 0 until rowsNumber) {
            print(" ${rowPosition + 1}")
            for (seatPosition in 0 until seatsPerRow) {
                print(" ${seats[rowPosition][seatPosition].mark}")
            }
            println()
        }
    }

    /** Prints cinema room statistics in the standard output */
    fun printStatistics() {
        println("Number of purchased tickets: $purchasedTickets\n" +
                "Percentage: ${"%.2f".format(Locale.US, 100.0 * purchasedTickets / totalSeats)}%\n" +
                "Current income: \$$currentIncome\n" +
                "Total income: \$$totalIncome")
    }

    private inner class Seat(private val price: Int) {
        private var taken = false
        var mark = "S"
            set(value) {
                field = value
                taken = true
            }
        fun getPrice() = price
        fun isTaken() = taken
    }
}

/** prints the menu in the standard output and returns the choice as an Integer */
fun menuChoice(): Int {
    println("1. Show the seats\n2. Buy a ticket\n3. Statistics\n0. Exit")
    return readln().toInt()
}

fun main() {
    println("Enter the number of rows:")
    val rowsNumber = readln().toInt()
    println("Enter the number of seats in each row:")
    val seatsPerRow = readln().toInt()

    val cinemaRoom = CinemaRoom(rowsNumber, seatsPerRow)

    while (true) {
        when (menuChoice()) {
            0 -> break
            1 -> cinemaRoom.print()
            2 -> {
                while (true) {
                    println("Enter a row number:")
                    val row = readln().toInt()
                    println("Enter a seat number in that row:")
                    val seat = readln().toInt()

                    if (cinemaRoom.takeSeatAt(row, seat)) {
                        println("Ticket price: $${cinemaRoom.getPriceOfSeatAt(row, seat)}")
                        break
                    } else continue
                }
            }
            3 -> cinemaRoom.printStatistics()
        }
    }
}

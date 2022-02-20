package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameofFifteen(initializer)

class GameofFifteen(private val initializer: GameOfFifteenInitializer): Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val values = initializer.initialPermutation
        for (i in 0 until lastIndex()) {
            board[board.getCell((i / board.width)+1, (i % board.width)+1)] = values[i]
        }
    }
    override fun canMove(): Boolean = true
    override fun hasWon(): Boolean {
        var seqNum = 1
        return board.all { it -> it == null || it == seqNum++ }
    }

    override fun processMove(direction: Direction) {
        with(board) {
            find { it == null }?.also { cell ->
                cell.getNeighbour(direction.reversed())?.also { neihbour ->
                    this[cell] = this[neihbour]
                    this[neihbour] = null
                }
            }
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run {
        val cell = getCell(i,j)
        this[cell]
    }

    private fun lastIndex() = board.width * board.width -1

}


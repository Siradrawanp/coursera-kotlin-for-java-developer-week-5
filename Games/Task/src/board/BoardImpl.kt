package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard  {
    val sq = SquareBoardImpl(width)
    return sq
}
open class SquareBoardImpl(val size: Int): SquareBoard{
    var cells : Array<Array<Cell>>

    init {
        cells = Array(size) { i ->
            Array(size) { j -> Cell(i+1, j+1) } }
    }

    override val width: Int
        get() = cells.size

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i <=0 || j <= 0 || i> width || j > width)
            return null
        return cells[i-1][j-1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i,j) ?: throw IllegalArgumentException()
    }

    override fun getAllCells(): Collection<Cell> = IntRange(1, width)
            .flatMap { i: Int -> IntRange(1, width)
                    .map { j:Int -> getCell(i,j) } }
            .toList()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
            when {
                jRange.last > width -> IntRange(jRange.first, width).map { j:Int -> getCell(i,j) }.toList()
                else -> jRange.map { j:Int -> getCell(i,j) }.toList()
            }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
            when {
                iRange.last > width -> IntRange(iRange.first, width).map { i:Int -> getCell(i,j) }.toList()
                else -> iRange.map { i:Int -> getCell(i,j) }.toList()
            }

    override fun Cell.getNeighbour(direction: Direction): Cell? =
            when (direction) {
                UP      -> getCellOrNull(i-1,j)
                DOWN    -> getCellOrNull(i+1,j)
                LEFT    -> getCellOrNull(i,j-1)
                RIGHT   -> getCellOrNull(i,j+1)
            }
}

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)
class GameBoardImpl<T>(override val width: Int): SquareBoardImpl(width), GameBoard<T> {
    private val values = mutableMapOf<Cell, T?>()

    init {
        getAllCells().forEach {
            values[it] = null
        }
    }

    override fun get(cell: Cell): T? {
        return values[cell]
    }

    override fun set(cell: Cell, value: T?) {
        values[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return values.filter { (_, value) -> predicate(value) }.map { (cells, _) -> cells }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return filter(predicate).firstOrNull()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return find(predicate) != null
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return filter(predicate).size == values.size
    }
}


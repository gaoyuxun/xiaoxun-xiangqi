package com.xiaoxun.xiangqi

/**
 * 象棋棋盘逻辑类
 */
class ChessBoard {
    companion object {
        const val ROWS = 10
        const val COLS = 9
    }

    val pieces = mutableListOf<ChessPiece>()
    private val moveHistory = mutableListOf<MoveRecord>()
    var currentTurn = PieceColor.RED

    init {
        initBoard()
    }

    /**
     * 初始化棋盘，设置初始布局
     */
    fun initBoard() {
        pieces.clear()
        moveHistory.clear()
        currentTurn = PieceColor.RED

        // 黑方（上方）
        pieces.add(ChessPiece(PieceType.CHE, PieceColor.BLACK, 0, 0))
        pieces.add(ChessPiece(PieceType.MA, PieceColor.BLACK, 0, 1))
        pieces.add(ChessPiece(PieceType.XIANG, PieceColor.BLACK, 0, 2))
        pieces.add(ChessPiece(PieceType.SHI, PieceColor.BLACK, 0, 3))
        pieces.add(ChessPiece(PieceType.JIANG, PieceColor.BLACK, 0, 4))
        pieces.add(ChessPiece(PieceType.SHI, PieceColor.BLACK, 0, 5))
        pieces.add(ChessPiece(PieceType.XIANG, PieceColor.BLACK, 0, 6))
        pieces.add(ChessPiece(PieceType.MA, PieceColor.BLACK, 0, 7))
        pieces.add(ChessPiece(PieceType.CHE, PieceColor.BLACK, 0, 8))

        pieces.add(ChessPiece(PieceType.PAO, PieceColor.BLACK, 2, 1))
        pieces.add(ChessPiece(PieceType.PAO, PieceColor.BLACK, 2, 7))

        pieces.add(ChessPiece(PieceType.BING, PieceColor.BLACK, 3, 0))
        pieces.add(ChessPiece(PieceType.BING, PieceColor.BLACK, 3, 2))
        pieces.add(ChessPiece(PieceType.BING, PieceColor.BLACK, 3, 4))
        pieces.add(ChessPiece(PieceType.BING, PieceColor.BLACK, 3, 6))
        pieces.add(ChessPiece(PieceType.BING, PieceColor.BLACK, 3, 8))

        // 红方（下方）
        pieces.add(ChessPiece(PieceType.CHE, PieceColor.RED, 9, 0))
        pieces.add(ChessPiece(PieceType.MA, PieceColor.RED, 9, 1))
        pieces.add(ChessPiece(PieceType.XIANG, PieceColor.RED, 9, 2))
        pieces.add(ChessPiece(PieceType.SHI, PieceColor.RED, 9, 3))
        pieces.add(ChessPiece(PieceType.JIANG, PieceColor.RED, 9, 4))
        pieces.add(ChessPiece(PieceType.SHI, PieceColor.RED, 9, 5))
        pieces.add(ChessPiece(PieceType.XIANG, PieceColor.RED, 9, 6))
        pieces.add(ChessPiece(PieceType.MA, PieceColor.RED, 9, 7))
        pieces.add(ChessPiece(PieceType.CHE, PieceColor.RED, 9, 8))

        pieces.add(ChessPiece(PieceType.PAO, PieceColor.RED, 7, 1))
        pieces.add(ChessPiece(PieceType.PAO, PieceColor.RED, 7, 7))

        pieces.add(ChessPiece(PieceType.BING, PieceColor.RED, 6, 0))
        pieces.add(ChessPiece(PieceType.BING, PieceColor.RED, 6, 2))
        pieces.add(ChessPiece(PieceType.BING, PieceColor.RED, 6, 4))
        pieces.add(ChessPiece(PieceType.BING, PieceColor.RED, 6, 6))
        pieces.add(ChessPiece(PieceType.BING, PieceColor.RED, 6, 8))
    }

    /**
     * 获取所有棋子
     */
    fun getPieces(): List<ChessPiece> = pieces.toList()

    /**
     * 获取指定位置的棋子
     */
    fun getPieceAt(row: Int, col: Int): ChessPiece? {
        return pieces.find { it.row == row && it.col == col }
    }

    /**
     * 检查移动是否合法
     */
    fun isValidMove(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        if (toRow < 0 || toRow >= ROWS || toCol < 0 || toCol >= COLS) {
            return false
        }

        val targetPiece = getPieceAt(toRow, toCol)
        if (targetPiece?.color == piece.color) {
            return false
        }

        return when (piece.type) {
            PieceType.JIANG -> isValidJiangMove(piece, toRow, toCol)
            PieceType.SHI -> isValidShiMove(piece, toRow, toCol)
            PieceType.XIANG -> isValidXiangMove(piece, toRow, toCol)
            PieceType.MA -> isValidMaMove(piece, toRow, toCol)
            PieceType.CHE -> isValidCheMove(piece, toRow, toCol)
            PieceType.PAO -> isValidPaoMove(piece, toRow, toCol)
            PieceType.BING -> isValidBingMove(piece, toRow, toCol)
        }
    }

    private fun isValidJiangMove(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        val palace = if (piece.color == PieceColor.RED) {
            (7..9) to (3..5)
        } else {
            (0..2) to (3..5)
        }

        if (toRow !in palace.first || toCol !in palace.second) return false

        val rowDiff = kotlin.math.abs(toRow - piece.row)
        val colDiff = kotlin.math.abs(toCol - piece.col)

        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)
    }

    private fun isValidShiMove(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        val palace = if (piece.color == PieceColor.RED) {
            (7..9) to (3..5)
        } else {
            (0..2) to (3..5)
        }

        if (toRow !in palace.first || toCol !in palace.second) return false

        val rowDiff = kotlin.math.abs(toRow - piece.row)
        val colDiff = kotlin.math.abs(toCol - piece.col)

        return rowDiff == 1 && colDiff == 1
    }

    private fun isValidXiangMove(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        val river = if (piece.color == PieceColor.RED) 5 else 4
        if ((piece.color == PieceColor.RED && toRow < river) ||
            (piece.color == PieceColor.BLACK && toRow > river)) {
            return false
        }

        val rowDiff = kotlin.math.abs(toRow - piece.row)
        val colDiff = kotlin.math.abs(toCol - piece.col)

        if (rowDiff != 2 || colDiff != 2) return false

        val midRow = (piece.row + toRow) / 2
        val midCol = (piece.col + toCol) / 2
        return getPieceAt(midRow, midCol) == null
    }

    private fun isValidMaMove(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        val rowDiff = kotlin.math.abs(toRow - piece.row)
        val colDiff = kotlin.math.abs(toCol - piece.col)

        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return false
        }

        val blockRow = if (rowDiff == 2) (piece.row + toRow) / 2 else piece.row
        val blockCol = if (colDiff == 2) (piece.col + toCol) / 2 else piece.col

        return getPieceAt(blockRow, blockCol) == null
    }

    private fun isValidCheMove(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        if (piece.row != toRow && piece.col != toCol) return false

        val minRow = minOf(piece.row, toRow)
        val maxRow = maxOf(piece.row, toRow)
        val minCol = minOf(piece.col, toCol)
        val maxCol = maxOf(piece.col, toCol)

        if (piece.row == toRow) {
            for (col in minCol + 1 until maxCol) {
                if (getPieceAt(piece.row, col) != null) return false
            }
        } else {
            for (row in minRow + 1 until maxRow) {
                if (getPieceAt(row, piece.col) != null) return false
            }
        }

        return true
    }

    private fun isValidPaoMove(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        if (piece.row != toRow && piece.col != toCol) return false

        val minRow = minOf(piece.row, toRow)
        val maxRow = maxOf(piece.row, toRow)
        val minCol = minOf(piece.col, toCol)
        val maxCol = maxOf(piece.col, toCol)

        var betweenCount = 0

        if (piece.row == toRow) {
            for (col in minCol + 1 until maxCol) {
                if (getPieceAt(piece.row, col) != null) betweenCount++
            }
        } else {
            for (row in minRow + 1 until maxRow) {
                if (getPieceAt(row, piece.col) != null) betweenCount++
            }
        }

        val targetPiece = getPieceAt(toRow, toCol)
        return if (targetPiece != null) {
            betweenCount == 1
        } else {
            betweenCount == 0
        }
    }

    private fun isValidBingMove(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        val river = if (piece.color == PieceColor.RED) 5 else 4
        val hasCrossedRiver = if (piece.color == PieceColor.RED) {
            piece.row < river
        } else {
            piece.row > river
        }

        val rowDiff = toRow - piece.row
        val colDiff = kotlin.math.abs(toCol - piece.col)

        if (piece.color == PieceColor.RED) {
            if (!hasCrossedRiver) {
                return rowDiff == -1 && colDiff == 0
            } else {
                return (rowDiff == -1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)
            }
        } else {
            if (!hasCrossedRiver) {
                return rowDiff == 1 && colDiff == 0
            } else {
                return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)
            }
        }
    }

    /**
     * 执行移动
     */
    fun movePiece(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        if (!isValidMove(piece, toRow, toCol)) {
            return false
        }

        val capturedPiece = getPieceAt(toRow, toCol)
        val move = MoveRecord(piece.copy(), toRow, toCol, capturedPiece?.copy())

        capturedPiece?.let { pieces.remove(it) }

        piece.row = toRow
        piece.col = toCol

        moveHistory.add(move)
        currentTurn = if (currentTurn == PieceColor.RED) PieceColor.BLACK else PieceColor.RED

        return true
    }

    /**
     * 悔棋
     */
    fun undo(): Boolean {
        if (moveHistory.isEmpty()) return false

        val lastMove = moveHistory.removeAt(moveHistory.size - 1)
        val piece = pieces.find { it == lastMove.piece ||
            (it.type == lastMove.piece.type && it.color == lastMove.piece.color &&
             it.row == lastMove.toRow && it.col == lastMove.toCol) }

        piece?.let {
            it.row = lastMove.piece.row
            it.col = lastMove.piece.col
        }

        lastMove.capturedPiece?.let {
            pieces.add(it)
        }

        currentTurn = if (currentTurn == PieceColor.RED) PieceColor.BLACK else PieceColor.RED

        return true
    }

    /**
     * 检查游戏是否结束
     */
    fun checkGameOver(): PieceColor? {
        val hasRedJiang = pieces.any { it.type == PieceType.JIANG && it.color == PieceColor.RED }
        val hasBlackJiang = pieces.any { it.type == PieceType.JIANG && it.color == PieceColor.BLACK }

        return when {
            !hasRedJiang -> PieceColor.BLACK
            !hasBlackJiang -> PieceColor.RED
            else -> null
        }
    }

    /**
     * 获取某位置可以移动到的所有位置
     */
    fun getValidMoves(piece: ChessPiece): List<Pair<Int, Int>> {
        val validMoves = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until ROWS) {
            for (col in 0 until COLS) {
                if (isValidMove(piece, row, col)) {
                    validMoves.add(Pair(row, col))
                }
            }
        }
        return validMoves
    }
}

/**
 * 移动记录
 */
data class MoveRecord(
    val piece: ChessPiece,
    val toRow: Int,
    val toCol: Int,
    val capturedPiece: ChessPiece?
)

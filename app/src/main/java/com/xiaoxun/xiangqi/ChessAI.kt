package com.xiaoxun.xiangqi

import kotlin.math.max
import kotlin.math.min

/**
 * 象棋AI引擎 - 基于极小化极大算法
 */
class ChessAI(private val difficulty: AIDifficulty = AIDifficulty.MEDIUM) {

    companion object {
        // 棋子价值表
        private val PIECE_VALUES = mapOf(
            PieceType.JIANG to 10000,
            PieceType.CHE to 600,
            PieceType.MA to 300,
            PieceType.PAO to 300,
            PieceType.SHI to 200,
            PieceType.XIANG to 200,
            PieceType.BING to 100
        )

        // 位置价值表 - 兵
        private val BING_POSITION_VALUE = arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, -2, 0, 4, 0, -2, 0, 0),
            intArrayOf(2, 0, 8, 0, 8, 0, 8, 0, 2),
            intArrayOf(6, 12, 18, 18, 20, 18, 18, 12, 6),
            intArrayOf(10, 20, 30, 34, 40, 34, 30, 20, 10),
            intArrayOf(14, 26, 42, 60, 80, 60, 42, 26, 14),
            intArrayOf(18, 36, 56, 80, 120, 80, 56, 36, 18),
            intArrayOf(0, 3, 6, 9, 12, 9, 6, 3, 0)
        )

        // 位置价值表 - 马
        private val MA_POSITION_VALUE = arrayOf(
            intArrayOf(0, -3, 5, 4, 2, 4, 5, -3, 0),
            intArrayOf(-3, 2, 4, 6, 10, 6, 4, 2, -3),
            intArrayOf(4, 6, 10, 12, 14, 12, 10, 6, 4),
            intArrayOf(2, 6, 8, 6, 10, 6, 8, 6, 2),
            intArrayOf(4, 12, 16, 14, 12, 14, 16, 12, 4),
            intArrayOf(6, 16, 14, 18, 16, 18, 14, 16, 6),
            intArrayOf(8, 24, 18, 24, 20, 24, 18, 24, 8),
            intArrayOf(12, 14, 16, 20, 18, 20, 16, 14, 12),
            intArrayOf(4, 10, 28, 16, 8, 16, 28, 10, 4),
            intArrayOf(4, 8, 16, 12, 4, 12, 16, 8, 4)
        )

        // 位置价值表 - 车
        private val CHE_POSITION_VALUE = arrayOf(
            intArrayOf(14, 14, 12, 18, 16, 18, 12, 14, 14),
            intArrayOf(16, 20, 18, 24, 26, 24, 18, 20, 16),
            intArrayOf(12, 12, 12, 18, 18, 18, 12, 12, 12),
            intArrayOf(12, 18, 16, 22, 22, 22, 16, 18, 12),
            intArrayOf(12, 14, 12, 18, 18, 18, 12, 14, 12),
            intArrayOf(12, 16, 14, 20, 20, 20, 14, 16, 12),
            intArrayOf(6, 10, 8, 14, 14, 14, 8, 10, 6),
            intArrayOf(4, 8, 6, 14, 12, 14, 6, 8, 4),
            intArrayOf(8, 4, 8, 16, 8, 16, 8, 4, 8),
            intArrayOf(-2, 10, 6, 14, 12, 14, 6, 10, -2)
        )

        // 位置价值表 - 炮
        private val PAO_POSITION_VALUE = arrayOf(
            intArrayOf(0, 0, 2, 6, 6, 6, 2, 0, 0),
            intArrayOf(0, 2, 4, 6, 6, 6, 4, 2, 0),
            intArrayOf(4, 0, 8, 6, 10, 6, 8, 0, 4),
            intArrayOf(0, 0, 0, 2, 4, 2, 0, 0, 0),
            intArrayOf(-2, 0, 4, 2, 6, 2, 4, 0, -2),
            intArrayOf(0, 0, 0, 2, 8, 2, 0, 0, 0),
            intArrayOf(0, 0, -2, 4, 10, 4, -2, 0, 0),
            intArrayOf(2, 2, 0, -10, -8, -10, 0, 2, 2),
            intArrayOf(2, 2, 0, -4, -14, -4, 0, 2, 2),
            intArrayOf(6, 4, 0, -10, -12, -10, 0, 4, 6)
        )
    }

    /**
     * 计算最佳移动
     */
    fun getBestMove(board: ChessBoard): Pair<ChessPiece, Pair<Int, Int>>? {
        val depth = when (difficulty) {
            AIDifficulty.EASY -> 2
            AIDifficulty.MEDIUM -> 3
            AIDifficulty.HARD -> 4
        }

        var bestScore = Int.MIN_VALUE
        var bestMove: Pair<ChessPiece, Pair<Int, Int>>? = null

        val pieces = board.getPieces().filter { it.color == board.currentTurn }

        for (piece in pieces) {
            val validMoves = board.getValidMoves(piece)

            for ((toRow, toCol) in validMoves) {
                // 模拟移动
                val simulatedBoard = simulateMove(board, piece, toRow, toCol)
                val score = minimax(simulatedBoard, depth - 1, Int.MIN_VALUE, Int.MAX_VALUE, false)

                if (score > bestScore) {
                    bestScore = score
                    bestMove = Pair(piece, Pair(toRow, toCol))
                }
            }
        }

        return bestMove
    }

    /**
     * 极小化极大算法（带Alpha-Beta剪枝）
     */
    private fun minimax(
        board: ChessBoard,
        depth: Int,
        alpha: Int,
        beta: Int,
        isMaximizing: Boolean
    ): Int {
        // 终止条件
        if (depth == 0 || board.checkGameOver() != null) {
            return evaluateBoard(board)
        }

        var newAlpha = alpha
        var newBeta = beta

        if (isMaximizing) {
            var maxScore = Int.MIN_VALUE
            val pieces = board.getPieces().filter { it.color == board.currentTurn }

            for (piece in pieces) {
                val validMoves = board.getValidMoves(piece)

                for ((toRow, toCol) in validMoves) {
                    val simulatedBoard = simulateMove(board, piece, toRow, toCol)
                    val score = minimax(simulatedBoard, depth - 1, newAlpha, newBeta, false)
                    maxScore = max(maxScore, score)
                    newAlpha = max(newAlpha, score)

                    // Alpha-Beta剪枝
                    if (newBeta <= newAlpha) {
                        break
                    }
                }

                if (newBeta <= newAlpha) {
                    break
                }
            }

            return maxScore
        } else {
            var minScore = Int.MAX_VALUE
            val opponentColor = if (board.currentTurn == PieceColor.RED) PieceColor.BLACK else PieceColor.RED
            val pieces = board.getPieces().filter { it.color == opponentColor }

            for (piece in pieces) {
                val validMoves = board.getValidMoves(piece)

                for ((toRow, toCol) in validMoves) {
                    val simulatedBoard = simulateMove(board, piece, toRow, toCol)
                    val score = minimax(simulatedBoard, depth - 1, newAlpha, newBeta, true)
                    minScore = min(minScore, score)
                    newBeta = min(newBeta, score)

                    // Alpha-Beta剪枝
                    if (newBeta <= newAlpha) {
                        break
                    }
                }

                if (newBeta <= newAlpha) {
                    break
                }
            }

            return minScore
        }
    }

    /**
     * 评估棋盘局势
     */
    private fun evaluateBoard(board: ChessBoard): Int {
        val winner = board.checkGameOver()
        if (winner != null) {
            return if (winner == board.currentTurn) 100000 else -100000
        }

        var score = 0

        for (piece in board.getPieces()) {
            val pieceValue = PIECE_VALUES[piece.type] ?: 0
            val positionValue = getPositionValue(piece)

            val totalValue = pieceValue + positionValue

            if (piece.color == board.currentTurn) {
                score += totalValue
            } else {
                score -= totalValue
            }
        }

        // 添加随机性（简单难度）
        if (difficulty == AIDifficulty.EASY) {
            score += (-20..20).random()
        }

        return score
    }

    /**
     * 获取棋子位置价值
     */
    private fun getPositionValue(piece: ChessPiece): Int {
        val row = if (piece.color == PieceColor.BLACK) piece.row else 9 - piece.row
        val col = piece.col

        val positionTable = when (piece.type) {
            PieceType.BING -> BING_POSITION_VALUE
            PieceType.MA -> MA_POSITION_VALUE
            PieceType.CHE -> CHE_POSITION_VALUE
            PieceType.PAO -> PAO_POSITION_VALUE
            else -> return 0
        }

        return if (row in positionTable.indices && col in positionTable[row].indices) {
            positionTable[row][col]
        } else {
            0
        }
    }

    /**
     * 模拟移动（创建新棋盘）
     */
    private fun simulateMove(
        board: ChessBoard,
        piece: ChessPiece,
        toRow: Int,
        toCol: Int
    ): ChessBoard {
        val newBoard = ChessBoard()

        // 复制棋盘状态
        newBoard.getPieces().clear()
        for (p in board.getPieces()) {
            newBoard.getPieces().add(p.copy())
        }
        newBoard.currentTurn = board.currentTurn

        // 执行移动
        val movingPiece = newBoard.getPieces().find {
            it.type == piece.type && it.color == piece.color && it.row == piece.row && it.col == piece.col
        }

        movingPiece?.let {
            val captured = newBoard.getPieceAt(toRow, toCol)
            captured?.let { newBoard.getPieces().remove(it) }
            it.row = toRow
            it.col = toCol
            newBoard.currentTurn = if (newBoard.currentTurn == PieceColor.RED) PieceColor.BLACK else PieceColor.RED
        }

        return newBoard
    }

    // 添加访问pieces的方法
    private fun ChessBoard.getPieces(): MutableList<ChessPiece> {
        return this.javaClass.getDeclaredField("pieces").apply {
            isAccessible = true
        }.get(this) as MutableList<ChessPiece>
    }
}

/**
 * AI难度等级
 */
enum class AIDifficulty {
    EASY,    // 简单 - 搜索深度2
    MEDIUM,  // 中等 - 搜索深度3
    HARD     // 困难 - 搜索深度4
}

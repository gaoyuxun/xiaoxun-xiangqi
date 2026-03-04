package com.xiaoxun.xiangqi

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 象棋棋盘自定义View
 */
class ChessBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val chessBoard = ChessBoard()

    // 画笔
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.board_line)
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 50f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val selectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.selected)
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    private val movablePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.movable)
        style = Paint.Style.FILL
        alpha = 100
    }

    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        alpha = 50
        maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
    }

    // 棋盘参数
    private var cellSize = 0f
    private var boardLeft = 0f
    private var boardTop = 0f

    // 选中的棋子
    private var selectedPiece: ChessPiece? = null
    private var validMoves: List<Pair<Int, Int>> = emptyList()

    // 回调
    var onMoveListener: ((String) -> Unit)? = null
    var onGameOverListener: ((PieceColor) -> Unit)? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        // 计算合适的棋盘大小
        val size = minOf(width, (height * 0.9f).toInt())
        cellSize = size / ChessBoard.COLS.toFloat()

        boardLeft = (width - cellSize * ChessBoard.COLS) / 2
        boardTop = (height - cellSize * ChessBoard.ROWS) / 2

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制渐变背景
        drawGradientBackground(canvas)

        // 绘制棋盘线
        drawBoard(canvas)

        // 绘制可移动位置
        drawValidMoves(canvas)

        // 绘制棋子
        drawPieces(canvas)

        // 绘制选中效果
        selectedPiece?.let { drawSelectedPiece(canvas, it) }
    }

    private fun drawGradientBackground(canvas: Canvas) {
        val gradient = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            context.getColor(R.color.board_bg),
            Color.parseColor("#E8D4A0"),
            Shader.TileMode.CLAMP
        )
        val bgPaint = Paint().apply {
            shader = gradient
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
    }

    private fun drawBoard(canvas: Canvas) {
        // 横线
        for (i in 0..ChessBoard.ROWS) {
            val y = boardTop + i * cellSize
            canvas.drawLine(
                boardLeft,
                y,
                boardLeft + cellSize * (ChessBoard.COLS - 1),
                y,
                linePaint
            )
        }

        // 竖线
        for (i in 0 until ChessBoard.COLS) {
            val x = boardLeft + i * cellSize

            // 边上两条竖线通顶
            if (i == 0 || i == ChessBoard.COLS - 1) {
                canvas.drawLine(
                    x,
                    boardTop,
                    x,
                    boardTop + cellSize * ChessBoard.ROWS,
                    linePaint
                )
            } else {
                // 其他竖线分两段（楚河汉界）
                canvas.drawLine(
                    x,
                    boardTop,
                    x,
                    boardTop + cellSize * 4,
                    linePaint
                )
                canvas.drawLine(
                    x,
                    boardTop + cellSize * 5,
                    x,
                    boardTop + cellSize * ChessBoard.ROWS,
                    linePaint
                )
            }
        }

        // 绘制九宫格斜线
        // 上方九宫
        canvas.drawLine(
            boardLeft + cellSize * 3,
            boardTop,
            boardLeft + cellSize * 5,
            boardTop + cellSize * 2,
            linePaint
        )
        canvas.drawLine(
            boardLeft + cellSize * 5,
            boardTop,
            boardLeft + cellSize * 3,
            boardTop + cellSize * 2,
            linePaint
        )

        // 下方九宫
        canvas.drawLine(
            boardLeft + cellSize * 3,
            boardTop + cellSize * 7,
            boardLeft + cellSize * 5,
            boardTop + cellSize * 9,
            linePaint
        )
        canvas.drawLine(
            boardLeft + cellSize * 5,
            boardTop + cellSize * 7,
            boardLeft + cellSize * 3,
            boardTop + cellSize * 9,
            linePaint
        )

        // 绘制"楚河汉界"文字
        textPaint.textSize = 40f
        textPaint.color = context.getColor(R.color.board_line)
        val riverY = boardTop + cellSize * 4.6f
        canvas.drawText("楚河", boardLeft + cellSize * 2, riverY, textPaint)
        canvas.drawText("汉界", boardLeft + cellSize * 6, riverY, textPaint)
        textPaint.textSize = 50f
    }

    private fun drawPieces(canvas: Canvas) {
        for (piece in chessBoard.getPieces()) {
            val x = boardLeft + piece.col * cellSize
            val y = boardTop + piece.row * cellSize
            val radius = cellSize * 0.4f

            // 绘制阴影
            canvas.drawCircle(x + 3, y + 3, radius, shadowPaint)

            // 绘制棋子圆圈
            val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                style = Paint.Style.FILL
            }
            canvas.drawCircle(x, y, radius, bgPaint)

            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = if (piece.color == PieceColor.RED) {
                    context.getColor(R.color.red_piece)
                } else {
                    context.getColor(R.color.black_piece)
                }
                style = Paint.Style.STROKE
                strokeWidth = 4f
            }
            canvas.drawCircle(x, y, radius, borderPaint)

            // 绘制棋子文字
            textPaint.color = if (piece.color == PieceColor.RED) {
                context.getColor(R.color.red_piece)
            } else {
                context.getColor(R.color.black_piece)
            }

            val metrics = textPaint.fontMetrics
            val textY = y - (metrics.ascent + metrics.descent) / 2
            canvas.drawText(piece.getDisplayText(), x, textY, textPaint)
        }
    }

    private fun drawSelectedPiece(canvas: Canvas, piece: ChessPiece) {
        val x = boardLeft + piece.col * cellSize
        val y = boardTop + piece.row * cellSize
        val radius = cellSize * 0.45f
        canvas.drawCircle(x, y, radius, selectedPaint)
    }

    private fun drawValidMoves(canvas: Canvas) {
        for ((row, col) in validMoves) {
            val x = boardLeft + col * cellSize
            val y = boardTop + row * cellSize
            val radius = cellSize * 0.15f
            canvas.drawCircle(x, y, radius, movablePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event)
        }

        val touchX = event.x
        val touchY = event.y

        // 转换为棋盘坐标
        val col = ((touchX - boardLeft + cellSize / 2) / cellSize).toInt()
        val row = ((touchY - boardTop + cellSize / 2) / cellSize).toInt()

        if (row < 0 || row >= ChessBoard.ROWS || col < 0 || col >= ChessBoard.COLS) {
            return true
        }

        handleTouch(row, col)
        return true
    }

    private fun handleTouch(row: Int, col: Int) {
        val clickedPiece = chessBoard.getPieceAt(row, col)

        if (selectedPiece == null) {
            // 选中棋子
            if (clickedPiece?.color == chessBoard.currentTurn) {
                selectedPiece = clickedPiece
                validMoves = chessBoard.getValidMoves(clickedPiece)
                invalidate()
            }
        } else {
            // 尝试移动
            if (validMoves.contains(Pair(row, col))) {
                val success = chessBoard.movePiece(selectedPiece!!, row, col)
                if (success) {
                    selectedPiece = null
                    validMoves = emptyList()
                    invalidate()

                    // 检查游戏是否结束
                    chessBoard.checkGameOver()?.let { winner ->
                        onGameOverListener?.invoke(winner)
                    } ?: run {
                        // 通知回合变化
                        val turn = if (chessBoard.currentTurn == PieceColor.RED) {
                            context.getString(R.string.red_turn)
                        } else {
                            context.getString(R.string.black_turn)
                        }
                        onMoveListener?.invoke(turn)
                    }
                }
            } else if (clickedPiece?.color == chessBoard.currentTurn) {
                // 选中另一个己方棋子
                selectedPiece = clickedPiece
                validMoves = chessBoard.getValidMoves(clickedPiece)
                invalidate()
            } else {
                // 取消选中
                selectedPiece = null
                validMoves = emptyList()
                invalidate()
            }
        }
    }

    /**
     * 重新开始游戏
     */
    fun newGame() {
        chessBoard.initBoard()
        selectedPiece = null
        validMoves = emptyList()
        invalidate()
        onMoveListener?.invoke(context.getString(R.string.red_turn))
    }

    /**
     * 悔棋
     */
    fun undo() {
        if (chessBoard.undo()) {
            selectedPiece = null
            validMoves = emptyList()
            invalidate()

            val turn = if (chessBoard.currentTurn == PieceColor.RED) {
                context.getString(R.string.red_turn)
            } else {
                context.getString(R.string.black_turn)
            }
            onMoveListener?.invoke(turn)
        }
    }

    /**
     * 获取棋盘对象（供AI使用）
     */
    fun getChessBoard(): ChessBoard = chessBoard

    /**
     * 获取当前回合
     */
    fun getCurrentTurn(): PieceColor = chessBoard.currentTurn

    /**
     * 执行移动（供AI使用）
     */
    fun makeMove(piece: ChessPiece, toRow: Int, toCol: Int): Boolean {
        val actualPiece = chessBoard.getPieceAt(piece.row, piece.col)
        if (actualPiece != null && chessBoard.movePiece(actualPiece, toRow, toCol)) {
            invalidate()

            // 检查游戏是否结束
            chessBoard.checkGameOver()?.let { winner ->
                onGameOverListener?.invoke(winner)
            } ?: run {
                // 通知回合变化
                val turn = if (chessBoard.currentTurn == PieceColor.RED) {
                    context.getString(R.string.red_turn)
                } else {
                    context.getString(R.string.black_turn)
                }
                onMoveListener?.invoke(turn)
            }
            return true
        }
        return false
    }
}

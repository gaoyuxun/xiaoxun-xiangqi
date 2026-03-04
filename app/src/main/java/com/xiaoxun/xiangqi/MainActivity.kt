package com.xiaoxun.xiangqi

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var chessBoardView: ChessBoardView
    private lateinit var tvGameStatus: TextView
    private lateinit var btnNewGame: Button
    private lateinit var btnUndo: Button
    private lateinit var rgGameMode: RadioGroup
    private lateinit var rbTwoPlayer: RadioButton
    private lateinit var rbAI: RadioButton
    private lateinit var spinnerDifficulty: Spinner

    private var isAIMode = false
    private var aiDifficulty = AIDifficulty.MEDIUM
    private var chessAI: ChessAI? = null
    private var isAIThinking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
        setupDifficultySpinner()
    }

    private fun initViews() {
        chessBoardView = findViewById(R.id.chessBoardView)
        tvGameStatus = findViewById(R.id.tvGameStatus)
        btnNewGame = findViewById(R.id.btnNewGame)
        btnUndo = findViewById(R.id.btnUndo)
        rgGameMode = findViewById(R.id.rgGameMode)
        rbTwoPlayer = findViewById(R.id.rbTwoPlayer)
        rbAI = findViewById(R.id.rbAI)
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty)
    }

    private fun setupDifficultySpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.difficulty_levels,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDifficulty.adapter = adapter
        spinnerDifficulty.setSelection(1) // 默认中等难度

        spinnerDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                aiDifficulty = when (position) {
                    0 -> AIDifficulty.EASY
                    1 -> AIDifficulty.MEDIUM
                    2 -> AIDifficulty.HARD
                    else -> AIDifficulty.MEDIUM
                }
                chessAI = ChessAI(aiDifficulty)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupListeners() {
        // 游戏模式切换
        rgGameMode.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbTwoPlayer -> {
                    isAIMode = false
                    spinnerDifficulty.visibility = View.GONE
                    chessBoardView.newGame()
                }
                R.id.rbAI -> {
                    isAIMode = true
                    spinnerDifficulty.visibility = View.VISIBLE
                    chessAI = ChessAI(aiDifficulty)
                    chessBoardView.newGame()
                }
            }
        }

        // 棋盘移动监听
        chessBoardView.onMoveListener = { status ->
            tvGameStatus.text = status

            // 人机模式下，黑方由AI控制
            if (isAIMode && chessBoardView.getCurrentTurn() == PieceColor.BLACK && !isAIThinking) {
                makeAIMove()
            }
        }

        // 游戏结束监听
        chessBoardView.onGameOverListener = { winner ->
            val winnerText = if (winner == PieceColor.RED) {
                if (isAIMode) "恭喜！你赢了！" else getString(R.string.red_win)
            } else {
                if (isAIMode) "很遗憾，AI获胜！" else getString(R.string.black_win)
            }

            tvGameStatus.text = winnerText

            // 弹出游戏结束对话框
            AlertDialog.Builder(this)
                .setTitle("游戏结束")
                .setMessage(winnerText)
                .setPositiveButton("再来一局") { _, _ ->
                    chessBoardView.newGame()
                }
                .setNegativeButton("退出") { _, _ ->
                    finish()
                }
                .setCancelable(false)
                .show()
        }

        // 新游戏按钮
        btnNewGame.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("新游戏")
                .setMessage("确定要开始新游戏吗？")
                .setPositiveButton("确定") { _, _ ->
                    chessBoardView.newGame()
                }
                .setNegativeButton("取消", null)
                .show()
        }

        // 悔棋按钮
        btnUndo.setOnClickListener {
            if (isAIMode) {
                // 人机模式下悔两步（玩家和AI各一步）
                chessBoardView.undo()
                chessBoardView.undo()
            } else {
                chessBoardView.undo()
            }
        }
    }

    /**
     * AI走棋
     */
    private fun makeAIMove() {
        if (isAIThinking) return

        isAIThinking = true
        tvGameStatus.text = getString(R.string.ai_thinking)

        lifecycleScope.launch {
            try {
                // 在后台线程计算AI移动
                val aiMove = withContext(Dispatchers.Default) {
                    delay(500) // 模拟思考时间
                    chessAI?.getBestMove(chessBoardView.getChessBoard())
                }

                // 在主线程执行移动
                aiMove?.let { (piece, position) ->
                    val (toRow, toCol) = position
                    chessBoardView.makeMove(piece, toRow, toCol)
                }

                isAIThinking = false
            } catch (e: Exception) {
                e.printStackTrace()
                isAIThinking = false
                Toast.makeText(this@MainActivity, "AI思考出错", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

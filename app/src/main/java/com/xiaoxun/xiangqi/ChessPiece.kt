package com.xiaoxun.xiangqi

/**
 * 棋子类型
 */
enum class PieceType {
    JIANG,   // 将/帅
    SHI,     // 士
    XIANG,   // 象
    MA,      // 马
    CHE,     // 车
    PAO,     // 炮
    BING     // 兵/卒
}

/**
 * 棋子颜色
 */
enum class PieceColor {
    RED,     // 红方
    BLACK    // 黑方
}

/**
 * 棋子数据类
 */
data class ChessPiece(
    val type: PieceType,
    val color: PieceColor,
    var row: Int,
    var col: Int
) {
    /**
     * 获取棋子显示文字
     */
    fun getDisplayText(): String {
        return when (type) {
            PieceType.JIANG -> if (color == PieceColor.RED) "帅" else "将"
            PieceType.SHI -> "士"
            PieceType.XIANG -> if (color == PieceColor.RED) "相" else "象"
            PieceType.MA -> "马"
            PieceType.CHE -> "车"
            PieceType.PAO -> "炮"
            PieceType.BING -> if (color == PieceColor.RED) "兵" else "卒"
        }
    }

    /**
     * 复制棋子
     */
    fun copy(): ChessPiece {
        return ChessPiece(type, color, row, col)
    }
}

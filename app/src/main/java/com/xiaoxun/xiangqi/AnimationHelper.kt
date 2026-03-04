package com.xiaoxun.xiangqi

import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator

/**
 * 动画辅助类
 */
class AnimationHelper {

    companion object {
        /**
         * 创建棋子移动动画
         */
        fun createMoveAnimation(
            fromRow: Int,
            fromCol: Int,
            toRow: Int,
            toCol: Int,
            duration: Long = 300,
            onUpdate: (Float, Float) -> Unit
        ): ValueAnimator {
            return ValueAnimator.ofFloat(0f, 1f).apply {
                this.duration = duration
                interpolator = DecelerateInterpolator()

                addUpdateListener { animator ->
                    val progress = animator.animatedValue as Float
                    val currentRow = fromRow + (toRow - fromRow) * progress
                    val currentCol = fromCol + (toCol - fromCol) * progress
                    onUpdate(currentRow, currentCol)
                }
            }
        }

        /**
         * 创建选中缩放动画
         */
        fun createScaleAnimation(
            onUpdate: (Float) -> Unit
        ): ValueAnimator {
            return ValueAnimator.ofFloat(1f, 1.1f, 1f).apply {
                duration = 500
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE

                addUpdateListener { animator ->
                    val scale = animator.animatedValue as Float
                    onUpdate(scale)
                }
            }
        }

        /**
         * 创建淡入淡出动画
         */
        fun createFadeAnimation(
            onUpdate: (Int) -> Unit
        ): ValueAnimator {
            return ValueAnimator.ofInt(255, 100, 255).apply {
                duration = 1000
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE

                addUpdateListener { animator ->
                    val alpha = animator.animatedValue as Int
                    onUpdate(alpha)
                }
            }
        }
    }
}

package com.xiaoxun.xiangqi

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

/**
 * 音效管理器
 */
class SoundManager(context: Context) {

    private val soundPool: SoundPool
    private val sounds = mutableMapOf<SoundType, Int>()
    private var enabled = true

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        // 注意：这里使用程序生成的音效，实际项目中应该使用真实音频文件
        // 可以将音频文件放在 res/raw/ 目录下，然后使用：
        // sounds[SoundType.MOVE] = soundPool.load(context, R.raw.move, 1)
    }

    /**
     * 播放音效
     */
    fun play(soundType: SoundType) {
        if (!enabled) return

        sounds[soundType]?.let { soundId ->
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    /**
     * 设置是否启用音效
     */
    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    /**
     * 释放资源
     */
    fun release() {
        soundPool.release()
    }
}

/**
 * 音效类型
 */
enum class SoundType {
    MOVE,      // 移动棋子
    CAPTURE,   // 吃子
    CHECK,     // 将军
    WIN        // 获胜
}

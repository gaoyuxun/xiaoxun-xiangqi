# 音效资源指南

## 音效文件位置

将音效文件放置在: `app/src/main/res/raw/` 目录下

## 需要的音效文件

创建以下音频文件（建议格式：OGG或MP3）：

1. **move.ogg** - 移动棋子音效
   - 建议：短促的"啪"声
   - 时长：~0.2秒

2. **capture.ogg** - 吃子音效
   - 建议：较重的"咚"声
   - 时长：~0.3秒

3. **check.ogg** - 将军提示音
   - 建议：警告音"叮"
   - 时长：~0.5秒

4. **win.ogg** - 获胜音效
   - 建议：胜利音乐
   - 时长：~1-2秒

## 获取音效资源的方法

### 方法1: 使用免费音效网站

- [Freesound](https://freesound.org) - 搜索 "chess move", "click"
- [Zapsplat](https://www.zapsplat.com) - 游戏音效分类
- [Mixkit](https://mixkit.co/free-sound-effects/) - 免费音效库

### 方法2: 使用在线生成工具

- [sfxr](http://www.drpetter.se/project_sfxr.html) - 8bit游戏音效生成器
- [Bfxr](https://www.bfxr.net/) - 浏览器版音效生成器

### 方法3: 使用AI生成

使用文字描述生成音效：
- [ElevenLabs Sound Effects](https://elevenlabs.io/sound-effects)
- 提示词示例：
  - "wooden chess piece moving on board"
  - "chess piece capture impact"
  - "warning bell for check"

## 临时替代方案

如果暂时没有音效文件，可以：

1. **使用系统音效**
   ```kotlin
   // 在代码中使用系统默认提示音
   val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
   val ringtone = RingtoneManager.getRingtone(context, notification)
   ringtone.play()
   ```

2. **使用ToneGenerator**
   ```kotlin
   val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
   toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
   ```

3. **先关闭音效功能**
   - 在设置中添加"音效开关"选项
   - 用户可以选择静音模式

## 集成步骤

1. 创建 `res/raw` 目录（如果不存在）
   ```bash
   mkdir -p app/src/main/res/raw
   ```

2. 将音效文件复制到该目录

3. 更新 `SoundManager.kt`
   ```kotlin
   init {
       sounds[SoundType.MOVE] = soundPool.load(context, R.raw.move, 1)
       sounds[SoundType.CAPTURE] = soundPool.load(context, R.raw.capture, 1)
       sounds[SoundType.CHECK] = soundPool.load(context, R.raw.check, 1)
       sounds[SoundType.WIN] = soundPool.load(context, R.raw.win, 1)
   }
   ```

4. 在合适的地方调用音效
   ```kotlin
   soundManager.play(SoundType.MOVE)
   ```

## 音效设计建议

- **移动音效**: 短促、干脆，不刺耳
- **吃子音效**: 比移动音稍重，有打击感
- **将军音效**: 明显的提示音，引起注意
- **获胜音效**: 欢快、积极的音乐

## 音量控制

在 `SoundManager` 中调整音量：
```kotlin
soundPool.play(soundId,
    0.7f,  // 左声道音量 (0.0-1.0)
    0.7f,  // 右声道音量 (0.0-1.0)
    1,     // 优先级
    0,     // 循环次数 (0=不循环)
    1f     // 播放速率 (0.5-2.0)
)
```

---

**提示**: 音效文件总大小建议控制在500KB以内，以减小APK体积。

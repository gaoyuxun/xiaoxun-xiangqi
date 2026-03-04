# 小勋象棋 (Xiaoxun Xiangqi)

一款完整的中国象棋Android应用，支持双人对弈、悔棋等功能。

## 📱 功能特点

- ✅ 完整的中国象棋规则实现
- ✅ 精美的棋盘界面设计
- ✅ 双人对弈模式
- ✅ 悔棋功能
- ✅ 智能走子提示（显示可移动位置）
- ✅ 游戏结束判定（将/帅被吃）
- ✅ 流畅的动画和交互体验

## 🎮 游戏规则

### 棋子介绍

1. **帅/将** - 只能在九宫内移动，每次走一格（横或竖）
2. **士** - 只能在九宫内斜走，每次走一格
3. **相/象** - 在己方半场走"田"字，不能过河，且中心点不能有子（象眼）
4. **马** - 走"日"字，可以被"蹩马腿"
5. **车** - 直线移动，不限距离
6. **炮** - 直线移动，吃子时需要隔一个棋子（打炮）
7. **兵/卒** - 过河前只能向前走一格，过河后可以左右移动

### 游戏目标

吃掉对方的将/帅即获胜。

## 🛠️ 技术栈

- **语言**: Kotlin
- **最低SDK**: Android 7.0 (API 24)
- **目标SDK**: Android 14 (API 34)
- **UI**: 自定义View + Canvas绘图
- **架构**: MVVM模式

## 📦 项目结构

```
xiangqi/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/xiaoxun/xiangqi/
│   │       │   ├── MainActivity.kt          # 主Activity
│   │       │   ├── ChessBoardView.kt        # 棋盘自定义View
│   │       │   ├── ChessBoard.kt            # 棋盘逻辑
│   │       │   └── ChessPiece.kt            # 棋子数据类
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml    # 主界面布局
│   │       │   └── values/
│   │       │       ├── colors.xml           # 颜色定义
│   │       │       ├── strings.xml          # 字符串资源
│   │       │       └── themes.xml           # 主题样式
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## 🚀 编译运行

### 前置要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 34

### 编译步骤

1. **克隆项目**
   ```bash
   cd /private/tmp/chaogu/xiangqi
   ```

2. **打开Android Studio**
   - File → Open → 选择 `xiangqi` 目录

3. **同步Gradle**
   - Android Studio会自动提示同步，点击 "Sync Now"

4. **运行应用**
   - 连接Android设备或启动模拟器
   - 点击工具栏的 "Run" 按钮（绿色三角形）
   - 或使用快捷键: `Shift + F10` (Windows/Linux) / `Control + R` (Mac)

### 命令行编译

```bash
# 调试版本
./gradlew assembleDebug

# 发布版本
./gradlew assembleRelease

# 安装到设备
./gradlew installDebug

# 运行测试
./gradlew test
```

## 🎯 使用说明

1. **开始游戏**
   - 打开应用后自动进入游戏界面
   - 红方先走

2. **移动棋子**
   - 点击要移动的棋子（必须是当前回合的颜色）
   - 棋子会高亮显示，可移动的位置会显示绿色小圆点
   - 再次点击目标位置完成移动

3. **悔棋**
   - 点击底部"悔棋"按钮
   - 可以连续悔棋多步

4. **新游戏**
   - 点击底部"新游戏"按钮
   - 确认后重置棋盘

## 🎨 界面预览

- 棋盘背景: 米黄色仿木纹
- 红方棋子: 红色文字 + 红色边框
- 黑方棋子: 深灰色文字 + 深灰色边框
- 选中效果: 金色高亮圆圈
- 可移动位置: 半透明绿色圆点
- 楚河汉界: 棋盘中间文字标识

## 📝 待开发功能

- [ ] AI对弈模式（简单/中等/困难）
- [ ] 棋谱保存和加载
- [ ] 音效和背景音乐
- [ ] 在线对战
- [ ] 残局练习模式
- [ ] 走棋计时功能
- [ ] 历史对局回放

## 🐛 已知问题

- 暂无

## 📄 开源协议

MIT License

## 👨‍💻 作者

小勋象棋开发团队

## 📮 反馈

如有问题或建议，欢迎提Issue！

---

**享受象棋的乐趣！** ♟️

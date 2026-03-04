# 编译说明文档

## 🎯 快速编译指南

由于这是一个完整的Android项目，需要使用Android Studio或Gradle工具链进行编译。

## 方法1: 使用Android Studio（最简单）

### 步骤：

1. **安装Android Studio**
   - 下载地址: https://developer.android.com/studio
   - 确保安装了Android SDK 34

2. **打开项目**
   ```bash
   # 方式A: 命令行打开
   open -a "Android Studio" /private/tmp/chaogu/xiangqi

   # 方式B: 在Android Studio中
   File → Open → 选择 /private/tmp/chaogu/xiangqi 目录
   ```

3. **等待Gradle同步**
   - 首次打开会自动下载依赖（需要几分钟）
   - 查看底部状态栏确认同步完成

4. **编译APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

5. **查找APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

## 方法2: 使用命令行（需要配置环境）

### 前置要求：

1. **安装JDK 17**
   ```bash
   # macOS
   brew install openjdk@17

   # 设置环境变量
   export JAVA_HOME=/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home
   ```

2. **配置Android SDK**
   ```bash
   # 设置SDK路径
   export ANDROID_HOME=~/Library/Android/sdk
   export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
   ```

3. **检查SDK安装**
   ```bash
   # 确保安装了以下组件：
   # - Android SDK Platform 34
   # - Android SDK Build-Tools 34.0.0

   # 使用sdkmanager检查
   sdkmanager --list
   ```

### 编译命令：

```bash
cd /private/tmp/chaogu/xiangqi

# 创建gradlew（如果没有）
# 由于项目缺少gradle wrapper jar文件，需要先生成

# 方法A: 如果已有gradle
gradle wrapper

# 方法B: 使用Android Studio生成
# File → Sync Project with Gradle Files

# 编译Debug版本
./gradlew assembleDebug

# 编译Release版本
./gradlew assembleRelease

# APK输出位置
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

## 方法3: 在线编译（无需本地环境）

### 使用GitHub Actions或AppCenter

1. 将项目上传到GitHub
2. 配置CI/CD自动编译
3. 下载编译好的APK

## 🔧 当前项目状态

### ✅ 已完成的功能

- **AI对弈引擎** - 3个难度级别（简单/中等/困难）
  - 基于极小化极大算法
  - Alpha-Beta剪枝优化
  - 位置价值评估

- **界面优化**
  - 渐变背景
  - 棋子阴影效果
  - 高亮选中效果

- **音效系统**
  - 音效管理器框架
  - 支持移动/吃子/将军/获胜音效
  - 需要添加音频资源文件（见 SOUND_GUIDE.md）

- **游戏功能**
  - 完整象棋规则
  - 双人对弈
  - 人机对弈
  - 悔棋功能
  - 游戏结束判定

### 📋 代码统计

```
核心代码文件:
- MainActivity.kt         (190行) - 主界面，AI集成
- ChessBoardView.kt       (320行) - 棋盘渲染
- ChessBoard.kt           (300行) - 规则引擎
- ChessPiece.kt          (50行)  - 数据模型
- ChessAI.kt             (280行) - AI引擎
- AnimationHelper.kt     (60行)  - 动画辅助
- SoundManager.kt        (70行)  - 音效管理

总计: ~1270行Kotlin代码
```

## 📱 安装APK到设备

### USB安装

```bash
# 连接Android设备（开启USB调试）
adb devices

# 安装APK
adb install app/build/outputs/apk/debug/app-debug.apk

# 或者强制重装
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### 无线安装

```bash
# 通过无线连接
adb tcpip 5555
adb connect <设备IP>:5555
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 二维码分享

1. 将APK上传到云存储（如：Google Drive, Dropbox）
2. 生成分享链接
3. 使用二维码生成器创建二维码
4. 手机扫码下载安装

## ⚠️ 常见问题

### 问题1: gradlew: Permission denied

**解决**:
```bash
chmod +x gradlew
```

### 问题2: SDK location not found

**解决**:
在 `local.properties` 中添加：
```properties
sdk.dir=/Users/你的用户名/Library/Android/sdk
```

### 问题3: 缺少gradle-wrapper.jar

**解决**:
```bash
# 使用系统gradle生成wrapper
gradle wrapper --gradle-version 8.2

# 或在Android Studio中
File → Sync Project with Gradle Files
```

### 问题4: Execution failed for task ':app:mergeDebugResources'

**解决**:
```bash
# 清理项目
./gradlew clean

# 重新编译
./gradlew assembleDebug
```

### 问题5: OutOfMemoryError

**解决**:
在 `gradle.properties` 中增加内存：
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=512m
```

## 📦 发布版本APK

### 1. 创建签名密钥

```bash
keytool -genkey -v -keystore xiaoxun-release.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias xiaoxun-key
```

### 2. 配置签名（app/build.gradle）

```groovy
android {
    signingConfigs {
        release {
            storeFile file("../xiaoxun-release.jks")
            storePassword "your_password"
            keyAlias "xiaoxun-key"
            keyPassword "your_password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            shrinkResources true
        }
    }
}
```

### 3. 编译Release版本

```bash
./gradlew assembleRelease

# 输出位置
app/build/outputs/apk/release/app-release.apk
```

### 4. 优化APK大小

```bash
# 查看APK内容
unzip -l app-release.apk

# 使用ProGuard混淆（已在build.gradle中启用）
# 使用R8优化（Android Studio默认）

# 分析APK大小
./gradlew analyzeReleaseApkSize
```

## 🚀 下一步

1. **测试APK**
   - 在多个设备上测试
   - 测试所有功能
   - 检查性能表现

2. **添加音效资源**
   - 参考 SOUND_GUIDE.md
   - 将音频文件放入 res/raw/
   - 更新 SoundManager.kt

3. **发布到应用商店**
   - Google Play Store
   - 华为应用市场
   - 小米应用商店
   - 等

## 📊 编译时间估算

- **首次编译**: 3-5分钟（下载依赖）
- **增量编译**: 30-60秒
- **Clean Build**: 1-2分钟

## 💡 优化建议

1. **启用Gradle缓存**
   ```properties
   org.gradle.caching=true
   ```

2. **并行编译**
   ```properties
   org.gradle.parallel=true
   ```

3. **使用本地Maven仓库**
   ```properties
   org.gradle.daemon=true
   ```

---

**编译愉快！** 🎉

如有问题，请查看：
- BUILD_GUIDE.md - 详细编译指南
- README.md - 项目说明
- SOUND_GUIDE.md - 音效资源指南

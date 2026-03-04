# 小勋象棋 - 编译指南

## 快速开始

### 方法一: 使用Android Studio（推荐）

1. **打开Android Studio**
   ```bash
   # 如果已安装Android Studio
   open -a "Android Studio" /private/tmp/chaogu/xiangqi
   ```

2. **等待Gradle同步完成**
   - 首次打开会自动下载依赖，需要几分钟

3. **运行应用**
   - 连接Android设备（USB调试模式）或启动模拟器
   - 点击绿色运行按钮 ▶️
   - 或按快捷键: `Ctrl + R` (Mac) / `Shift + F10` (Windows)

### 方法二: 命令行编译

```bash
cd /private/tmp/chaogu/xiangqi

# 首次运行需要下载Gradle Wrapper
chmod +x gradlew

# 编译Debug版本
./gradlew assembleDebug

# 编译成功后APK位置:
# app/build/outputs/apk/debug/app-debug.apk

# 直接安装到连接的设备
./gradlew installDebug
```

## 环境要求

### 必需软件

- ✅ **Java Development Kit (JDK) 17**
  ```bash
  # 检查Java版本
  java -version

  # 应显示: openjdk version "17.x.x" 或更高
  ```

- ✅ **Android SDK**
  - compileSdk: 34
  - minSdk: 24
  - targetSdk: 34

- ✅ **Android Studio** (推荐)
  - 版本: Hedgehog (2023.1.1) 或更高

### Android设备要求

- Android 7.0 (API 24) 或更高版本
- 建议屏幕尺寸: 5英寸以上

## 安装Android Studio

### macOS
```bash
# 使用Homebrew安装
brew install --cask android-studio

# 或从官网下载
# https://developer.android.com/studio
```

### Windows
下载安装包: https://developer.android.com/studio

### Linux
```bash
# Ubuntu/Debian
sudo snap install android-studio --classic
```

## 配置SDK路径

如果Android Studio未自动检测到SDK，需要手动配置：

1. **修改 `local.properties`**
   ```properties
   sdk.dir=/Users/你的用户名/Library/Android/sdk
   ```

2. **或设置环境变量**
   ```bash
   export ANDROID_HOME=$HOME/Library/Android/sdk
   export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
   ```

## 常见问题

### 1. Gradle同步失败

**问题**: "Failed to resolve: com.android.tools.build:gradle:8.1.0"

**解决**:
```bash
# 检查网络连接
# 如在中国大陆，可配置镜像源
# 在 build.gradle 中添加:
maven { url 'https://maven.aliyun.com/repository/public/' }
maven { url 'https://maven.aliyun.com/repository/google/' }
```

### 2. JDK版本不匹配

**问题**: "Unsupported Java version"

**解决**:
```bash
# 安装JDK 17
brew install openjdk@17

# 设置JAVA_HOME
export JAVA_HOME=/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home
```

### 3. 设备未识别

**问题**: 设备无法连接

**解决**:
```bash
# 检查设备连接
adb devices

# 如果显示 "unauthorized"，在手机上允许USB调试
# 如果显示 "no devices"，检查USB线和驱动
```

### 4. 编译内存不足

**问题**: "Out of memory"

**解决**:
在 `gradle.properties` 中增加内存:
```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

## 生成签名APK

### 1. 创建密钥库

```bash
keytool -genkey -v -keystore xiaoxun-xiangqi.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias xiaoxun
```

### 2. 配置签名

在 `app/build.gradle` 中添加:
```groovy
android {
    signingConfigs {
        release {
            storeFile file("../xiaoxun-xiangqi.jks")
            storePassword "your_password"
            keyAlias "xiaoxun"
            keyPassword "your_password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### 3. 编译发布版本

```bash
./gradlew assembleRelease

# APK位置:
# app/build/outputs/apk/release/app-release.apk
```

## 调试技巧

### 查看日志

```bash
# 实时查看应用日志
adb logcat | grep "xiaoxun"

# 或在Android Studio中打开Logcat窗口
```

### 性能分析

在Android Studio中:
1. View → Tool Windows → Profiler
2. 选择应用进程
3. 查看CPU、内存、网络使用情况

## 项目清理

```bash
# 清理所有编译文件
./gradlew clean

# 删除.gradle缓存（慎用）
rm -rf .gradle
rm -rf app/build
```

## 版本发布检查清单

- [ ] 修改版本号 (`app/build.gradle`)
  ```groovy
  versionCode 2
  versionName "1.1"
  ```
- [ ] 运行所有测试
  ```bash
  ./gradlew test
  ```
- [ ] 生成签名APK
- [ ] 在多个设备上测试
- [ ] 更新README.md
- [ ] 创建Git tag
  ```bash
  git tag -a v1.0 -m "Release version 1.0"
  ```

## 技术支持

如遇到问题:
1. 查看 [Android开发者文档](https://developer.android.com)
2. 搜索 [Stack Overflow](https://stackoverflow.com)
3. 提交Issue到项目仓库

---

**祝编译顺利！** 🎉

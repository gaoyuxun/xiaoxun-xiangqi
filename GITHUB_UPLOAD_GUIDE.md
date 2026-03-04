# 📤 GitHub上传和自动编译完整指南

## 🎯 目标

将小勋象棋项目上传到GitHub，利用GitHub Actions自动编译生成APK文件。

---

## 📋 前置准备

### 1. 注册GitHub账号

如果还没有GitHub账号：
- 访问 https://github.com
- 点击 "Sign up" 注册
- 验证邮箱

### 2. 安装Git

```bash
# macOS - 使用Homebrew
brew install git

# 或检查是否已安装
git --version
```

### 3. 配置Git

```bash
# 设置用户名和邮箱
git config --global user.name "你的名字"
git config --global user.email "你的邮箱@example.com"
```

---

## 🚀 步骤一：创建GitHub仓库

### 方法A：网页创建（推荐）

1. 登录GitHub
2. 点击右上角 "+" → "New repository"
3. 填写信息：
   - **Repository name**: `xiaoxun-xiangqi`
   - **Description**: `小勋象棋 - 完整功能的中国象棋Android应用，带AI对手`
   - **Public** 或 **Private** (选择公开或私有)
   - ❌ **不要勾选** "Initialize this repository with a README"
4. 点击 "Create repository"
5. 记下仓库地址，例如：`https://github.com/你的用户名/xiaoxun-xiangqi.git`

### 方法B：命令行创建

```bash
# 使用GitHub CLI (需要先安装: brew install gh)
gh repo create xiaoxun-xiangqi --public --source=. --remote=origin --push
```

---

## 📦 步骤二：上传项目到GitHub

### 完整命令（复制执行）

```bash
# 1. 进入项目目录
cd /private/tmp/chaogu/xiangqi

# 2. 初始化Git仓库
git init

# 3. 添加所有文件
git add .

# 4. 查看要提交的文件
git status

# 5. 创建首次提交
git commit -m "🎮 初始提交: 小勋象棋 v1.0

✨ 功能特性:
- 完整的中国象棋规则引擎
- 双人对弈模式
- 人机对弈模式（简单/中等/困难）
- 智能AI引擎（Minimax + Alpha-Beta剪枝）
- 精美UI界面（渐变背景、阴影效果）
- 音效系统框架
- 完整开发文档

📊 技术栈:
- Kotlin
- Android Custom View
- Canvas绘图
- 协程
- MVC架构

🎯 代码量: ~1500行
📱 支持: Android 7.0+
"

# 6. 关联远程仓库（替换为你的仓库地址！）
git remote add origin https://github.com/你的用户名/xiaoxun-xiangqi.git

# 7. 设置主分支名称
git branch -M main

# 8. 推送到GitHub
git push -u origin main
```

### ⚠️ 重要提示

**第6步中的URL必须替换**为你自己的仓库地址！

例如：
- 你的用户名是 `zhangsan`
- 那么URL应该是：`https://github.com/zhangsan/xiaoxun-xiangqi.git`

---

## 🔐 身份验证

推送时GitHub会要求验证身份：

### 选项1：使用Personal Access Token（推荐）

1. 访问 https://github.com/settings/tokens
2. 点击 "Generate new token" → "Generate new token (classic)"
3. 设置：
   - Note: `xiaoxun-xiangqi-upload`
   - Expiration: `90 days`
   - 勾选: `repo` (所有权限)
4. 点击 "Generate token"
5. **立即复制token**（只显示一次！）
6. 在推送时输入：
   - Username: 你的GitHub用户名
   - Password: 刚才复制的token

### 选项2：使用SSH Key

```bash
# 生成SSH key
ssh-keygen -t ed25519 -C "your_email@example.com"

# 复制公钥
cat ~/.ssh/id_ed25519.pub | pbcopy

# 访问 https://github.com/settings/keys
# 点击 "New SSH key"，粘贴公钥

# 修改远程仓库地址为SSH格式
git remote set-url origin git@github.com:你的用户名/xiaoxun-xiangqi.git
git push -u origin main
```

---

## ⚙️ 步骤三：查看自动编译

上传成功后，GitHub Actions会自动开始编译：

### 1. 访问Actions页面

```
https://github.com/你的用户名/xiaoxun-xiangqi/actions
```

### 2. 查看编译进度

- 点击最新的workflow运行记录
- 查看实时日志
- 等待编译完成（约3-5分钟）

### 3. 编译过程说明

```
✓ 检出代码
✓ 设置JDK 17
✓ 授予gradlew执行权限
✓ 设置Gradle Wrapper
✓ 编译Debug APK        (约2分钟)
✓ 编译Release APK      (约1分钟)
✓ 上传Artifacts
```

---

## 📥 步骤四：下载APK

### 编译成功后：

1. 在Actions页面找到成功的编译任务（绿色✓）
2. 点击进入详情页
3. 向下滚动到 **"Artifacts"** 区域
4. 你会看到两个下载项：

```
📦 xiaoxun-xiangqi-debug.zip     (Debug版本，约4-6MB)
📦 xiaoxun-xiangqi-release.zip   (Release版本，约2-4MB)
```

5. 点击下载（需要登录GitHub）
6. 解压zip文件得到APK

---

## 📱 步骤五：安装APK到手机

### 方法A：直接传输安装

```bash
# 1. 解压下载的zip文件
unzip xiaoxun-xiangqi-debug.zip

# 2. 把APK传到手机（通过微信/QQ/AirDrop等）

# 3. 在手机上打开APK文件安装
#    可能需要允许"安装未知来源应用"
```

### 方法B：通过ADB安装

```bash
# 1. 手机开启USB调试
#    设置 → 关于手机 → 连续点击版本号7次 → 开发者选项 → USB调试

# 2. 连接手机到电脑

# 3. 检查连接
adb devices

# 4. 安装APK
adb install app-debug.apk

# 5. 打开应用
adb shell am start -n com.xiaoxun.xiangqi/.MainActivity
```

### 方法C：生成二维码分享

1. 将APK上传到云盘（百度网盘/Google Drive等）
2. 生成分享链接
3. 使用二维码生成器：https://www.qrcode-monkey.com
4. 手机扫码下载

---

## 🔄 更新项目代码

以后修改代码后，推送更新：

```bash
cd /private/tmp/chaogu/xiangqi

# 查看修改
git status

# 添加修改的文件
git add .

# 提交修改
git commit -m "✨ 新增功能: 描述你的修改"

# 推送到GitHub
git push

# GitHub Actions会自动编译新的APK
```

---

## 🎨 优化：美化README

为了让GitHub页面更美观，可以添加徽章和说明：

在仓库创建 `README.md` 文件（如果还没有）：

```markdown
# 🎮 小勋象棋

完整功能的中国象棋Android应用，带智能AI对手

[![Android CI](https://github.com/你的用户名/xiaoxun-xiangqi/actions/workflows/android-build.yml/badge.svg)](https://github.com/你的用户名/xiaoxun-xiangqi/actions)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](https://android.com)

## ✨ 特性

- ✅ 完整象棋规则
- 🤖 AI对手（3个难度）
- 🎨 精美UI界面
- 🔄 悔棋功能
- 📱 适配所有屏幕

## 📥 下载

在 [Releases](https://github.com/你的用户名/xiaoxun-xiangqi/releases) 页面下载最新APK

或查看 [Actions](https://github.com/你的用户名/xiaoxun-xiangqi/actions) 获取最新构建
```

---

## 🏷️ 创建Release版本（可选）

让用户更方便地下载：

1. 访问仓库页面
2. 点击右侧 "Releases" → "Create a new release"
3. 填写：
   - **Tag version**: `v1.0`
   - **Release title**: `小勋象棋 v1.0`
   - **Description**: 功能说明
4. 从Actions下载APK，上传到Release
5. 点击 "Publish release"

现在用户可以直接从Releases页面下载APK！

---

## 📊 监控编译状态

### 启用通知

Settings → Notifications → Actions:
- ✅ Send notifications for failed workflows

### 添加状态徽章

在README.md中添加：
```markdown
![Build Status](https://github.com/你的用户名/xiaoxun-xiangqi/actions/workflows/android-build.yml/badge.svg)
```

---

## ❓ 常见问题

### Q1: 推送时要求输入密码

**A**: 使用Personal Access Token代替密码（见上文"身份验证"部分）

### Q2: 编译失败怎么办？

**A**:
1. 查看Actions日志找到错误
2. 检查是否所有文件都已提交
3. 确认gradle配置正确

### Q3: 无法下载Artifacts

**A**:
- 必须登录GitHub账号
- Artifacts保留30天
- 或创建Release让所有人下载

### Q4: APK无法安装

**A**:
- 允许"安装未知来源应用"
- 确认Android版本 >= 7.0
- 尝试卸载旧版本再安装

### Q5: 编译时间过长

**A**:
- 首次编译需要下载依赖（3-5分钟）
- 后续编译使用缓存（1-2分钟）
- 免费账户每月2000分钟限额

---

## 🎉 完成！

现在你的项目已经：
- ✅ 上传到GitHub
- ✅ 自动编译APK
- ✅ 可随时下载安装
- ✅ 代码永久保存

每次推送代码，GitHub都会自动编译新的APK！

---

## 📞 需要帮助？

- GitHub文档: https://docs.github.com
- Git教程: https://git-scm.com/book/zh/v2
- Android开发: https://developer.android.com

---

**祝你成功上传并获得APK！** 🎊

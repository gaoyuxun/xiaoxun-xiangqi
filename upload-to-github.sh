#!/bin/bash

# 小勋象棋 - GitHub上传脚本
# 使用方法: ./upload-to-github.sh

echo "╔══════════════════════════════════════════════════════════╗"
echo "║                                                          ║"
echo "║     🎮 小勋象棋 - GitHub自动上传脚本                      ║"
echo "║                                                          ║"
echo "╚══════════════════════════════════════════════════════════╝"
echo ""

# 检查Git是否安装
if ! command -v git &> /dev/null; then
    echo "❌ 未找到Git，请先安装："
    echo "   brew install git"
    exit 1
fi

echo "✓ Git已安装"
echo ""

# 获取用户GitHub信息
echo "📝 请输入你的GitHub信息:"
echo ""
read -p "GitHub用户名: " GITHUB_USERNAME
read -p "GitHub仓库名 (默认: xiaoxun-xiangqi): " REPO_NAME
REPO_NAME=${REPO_NAME:-xiaoxun-xiangqi}

echo ""
echo "📦 准备上传到: https://github.com/$GITHUB_USERNAME/$REPO_NAME"
echo ""
read -p "确认继续? (y/n) " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "❌ 已取消"
    exit 0
fi

# 检查是否已初始化Git
if [ ! -d ".git" ]; then
    echo "🔧 初始化Git仓库..."
    git init
    echo "✓ Git仓库已初始化"
fi

# 配置用户信息（如果未配置）
if [ -z "$(git config user.name)" ]; then
    echo ""
    read -p "请输入你的名字 (用于Git提交): " GIT_NAME
    git config user.name "$GIT_NAME"
fi

if [ -z "$(git config user.email)" ]; then
    echo ""
    read -p "请输入你的邮箱 (用于Git提交): " GIT_EMAIL
    git config user.email "$GIT_EMAIL"
fi

echo ""
echo "📦 添加文件到Git..."
git add .

echo "✓ 文件已添加"
echo ""

echo "💾 创建提交..."
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

echo "✓ 提交已创建"
echo ""

# 设置远程仓库
REMOTE_URL="https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"
if git remote | grep -q "origin"; then
    echo "🔄 更新远程仓库地址..."
    git remote set-url origin "$REMOTE_URL"
else
    echo "🔗 添加远程仓库..."
    git remote add origin "$REMOTE_URL"
fi

echo "✓ 远程仓库已配置: $REMOTE_URL"
echo ""

# 设置主分支
echo "🌿 设置主分支为 main..."
git branch -M main

echo "✓ 分支已设置"
echo ""

# 推送到GitHub
echo "🚀 推送到GitHub..."
echo "   (如果是首次推送，需要输入GitHub密码或Personal Access Token)"
echo ""

if git push -u origin main; then
    echo ""
    echo "╔══════════════════════════════════════════════════════════╗"
    echo "║                                                          ║"
    echo "║              🎉 上传成功！                                ║"
    echo "║                                                          ║"
    echo "╚══════════════════════════════════════════════════════════╝"
    echo ""
    echo "📍 GitHub仓库地址:"
    echo "   https://github.com/$GITHUB_USERNAME/$REPO_NAME"
    echo ""
    echo "⚙️  GitHub Actions正在自动编译APK..."
    echo "   查看进度: https://github.com/$GITHUB_USERNAME/$REPO_NAME/actions"
    echo ""
    echo "📥 编译完成后，在Actions页面下载APK:"
    echo "   1. 点击最新的workflow运行记录"
    echo "   2. 向下滚动到 Artifacts 区域"
    echo "   3. 下载 xiaoxun-xiangqi-debug.zip 或 xiaoxun-xiangqi-release.zip"
    echo ""
    echo "⏱️  预计编译时间: 3-5分钟"
    echo ""
    echo "🎊 完成！"
else
    echo ""
    echo "❌ 推送失败"
    echo ""
    echo "💡 可能的原因:"
    echo "   1. GitHub仓库不存在 - 请先在GitHub上创建仓库"
    echo "   2. 认证失败 - 请使用Personal Access Token作为密码"
    echo "   3. 网络问题 - 检查网络连接"
    echo ""
    echo "📖 详细说明请查看: GITHUB_UPLOAD_GUIDE.md"
    exit 1
fi

# GitHub Actions 自动编译配置

## 📦 功能说明

本配置文件会在以下情况自动编译APK：

1. **推送代码到main/master分支** - 自动触发编译
2. **创建Pull Request** - 自动编译检查
3. **手动触发** - 在GitHub Actions页面手动运行

## 🚀 使用步骤

### 第一步：上传项目到GitHub

```bash
cd /private/tmp/chaogu/xiangqi

# 初始化Git仓库
git init

# 添加所有文件
git add .

# 创建首次提交
git commit -m "Initial commit: 小勋象棋 v1.0

功能特性:
- 完整的中国象棋规则引擎
- 双人对弈模式
- 人机对弈模式（3个难度级别）
- AI引擎（Minimax + Alpha-Beta剪枝）
- 精美UI界面（渐变背景、阴影效果）
- 音效系统框架
- 完整文档
"

# 关联远程仓库（替换为你的GitHub仓库地址）
git remote add origin https://github.com/你的用户名/xiaoxun-xiangqi.git

# 推送到GitHub
git branch -M main
git push -u origin main
```

### 第二步：在GitHub上查看编译结果

1. 访问你的GitHub仓库
2. 点击顶部的 **"Actions"** 标签
3. 找到最新的编译任务
4. 等待编译完成（约3-5分钟）
5. 点击编译任务，找到 **"Artifacts"** 区域
6. 下载APK文件

## 📥 下载APK

编译完成后，你会看到两个下载选项：

1. **xiaoxun-xiangqi-debug.zip**
   - Debug版本，用于测试
   - 文件大小较大
   - 包含调试信息

2. **xiaoxun-xiangqi-release.zip**
   - Release版本，用于发布
   - 文件大小较小
   - 已优化性能
   - 未签名（需要手动签名才能发布到应用商店）

## 🔧 手动触发编译

1. 访问仓库的 Actions 页面
2. 选择 "Android CI - 自动编译APK" workflow
3. 点击右侧的 "Run workflow" 按钮
4. 选择分支
5. 点击 "Run workflow" 开始编译

## 📱 安装APK

下载APK后：

```bash
# 解压下载的zip文件
unzip xiaoxun-xiangqi-debug.zip

# 通过ADB安装到设备
adb install app-debug.apk

# 或者直接把APK文件传到手机上安装
```

## ⚠️ 常见问题

### 问题1：编译失败

**解决方法**：
- 查看Actions日志找到错误信息
- 检查gradle配置是否正确
- 确保所有文件都已提交

### 问题2：无法下载Artifacts

**原因**：
- 需要登录GitHub账号
- Artifacts保留时间为30天

### 问题3：Release版本无法安装

**原因**：
- Release版本未签名
- 需要先签名才能安装

**签名方法**：
```bash
# 创建密钥
keytool -genkey -v -keystore my-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias

# 签名APK
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore my-release-key.jks app-release-unsigned.apk my-key-alias

# 优化APK
zipalign -v 4 app-release-unsigned.apk app-release.apk
```

## 🔐 配置自动签名（可选）

如果想让GitHub Actions自动签名Release版本：

1. 创建签名密钥并转换为base64：
   ```bash
   base64 my-release-key.jks > keystore.txt
   ```

2. 在GitHub仓库设置中添加Secrets：
   - `KEYSTORE_FILE`: keystore.txt的内容
   - `KEYSTORE_PASSWORD`: 密钥库密码
   - `KEY_ALIAS`: 密钥别名
   - `KEY_PASSWORD`: 密钥密码

3. 修改workflow文件添加签名步骤（见advanced示例）

## 📊 编译时间

- **首次编译**: 3-5分钟（下载依赖）
- **后续编译**: 1-2分钟（使用缓存）

## 💡 提示

- 每次推送代码都会触发编译
- 可以在Settings → Actions中配置通知
- Artifacts会自动保留30天
- 免费账户有每月2000分钟的编译时间限制

---

**祝编译顺利！** 🎉

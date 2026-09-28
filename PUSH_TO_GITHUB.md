# 推送到 GitHub 指南

## 快速开始

### 第一步：在 GitHub 创建仓库

1. 访问 https://github.com/new
2. 填写信息：
   - **Repository name**: `NeteaseMusicTranslator`
   - **Description**: `LSPosed module for translating Netease Cloud Music to Vietnamese`
   - **Public** (选择公开，这样可以免费使用 GitHub Actions)
   - **不要勾选** "Initialize this repository with..."
3. 点击 "Create repository"

### 第二步：推送代码

#### 方法 1: 使用脚本（推荐）

```bash
# 1. 编辑脚本，替换你的 GitHub 用户名
# 打开 push_to_github.sh，将 YOUR_GITHUB_USERNAME 替换为你的用户名

# 2. 运行脚本
cd "D:\cd\wyyyy\software-development\NeteaseMusicTranslator"
bash push_to_github.sh
```

#### 方法 2: 手动推送

```bash
cd "D:\cd\wyyyy\software-development\NeteaseMusicTranslator"

# 添加远程仓库（替换 YOUR_USERNAME）
git remote add origin https://github.com/YOUR_USERNAME/NeteaseMusicTranslator.git

# 推送代码
git push -u origin master
```

### 第三步：验证和构建

1. **查看代码**: 访问 `https://github.com/YOUR_USERNAME/NeteaseMusicTranslator`
2. **查看自动构建**: 点击 "Actions" 标签
   - GitHub Actions 会自动开始构建 APK
   - 构建通常需要 3-5 分钟
3. **下载 APK**: 构建完成后
   - 前往 "Releases" 页面
   - 或在 Actions 页面点击构建任务，在 "Artifacts" 中下载

## GitHub Actions 自动构建

我已经配置了自动构建工作流（`.github/workflows/build.yml`），每次推送代码时会自动：

1. ✅ 设置 Java 11 环境
2. ✅ 下载依赖并编译项目
3. ✅ 构建 Debug APK
4. ✅ 上传 APK 作为构建产物
5. ✅ 创建 Release 并附加 APK（仅主分支）

### 查看构建状态

访问仓库的 Actions 页面：
```
https://github.com/YOUR_USERNAME/NeteaseMusicTranslator/actions
```

### 下载构建的 APK

**选项 1: 从 Releases 下载**（推荐）
```
https://github.com/YOUR_USERNAME/NeteaseMusicTranslator/releases
```

**选项 2: 从 Actions 构建产物下载**
1. 进入 Actions 页面
2. 点击最新的构建任务
3. 在页面底部的 "Artifacts" 区域找到 `NeteaseMusicTranslator-debug`
4. 点击下载

## 认证问题

如果推送时要求认证，你需要使用 Personal Access Token (PAT)：

### 创建 Personal Access Token

1. 访问 https://github.com/settings/tokens
2. 点击 "Generate new token" → "Generate new token (classic)"
3. 填写信息：
   - **Note**: `NeteaseMusicTranslator`
   - **Expiration**: 选择有效期
   - **Scopes**: 勾选 `repo` (全部子选项)
4. 点击 "Generate token"
5. **复制 token**（只显示一次，务必保存）

### 使用 Token 推送

当 git push 要求输入密码时：
- **Username**: 你的 GitHub 用户名
- **Password**: 粘贴刚才生成的 Personal Access Token（不是你的 GitHub 密码）

### 保存凭据（可选）

避免每次都输入：
```bash
# Windows 凭据管理器会自动保存
git config --global credential.helper wincred
```

## 更新代码

以后修改代码后推送更新：

```bash
cd "D:\cd\wyyyy\software-development\NeteaseMusicTranslator"

# 查看修改
git status

# 添加修改
git add .

# 提交
git commit -m "描述你的修改"

# 推送
git push
```

每次推送后，GitHub Actions 会自动构建新的 APK。

## 故障排查

### 推送失败：remote rejected

```
error: failed to push some refs
```

**原因**: 远程仓库已经初始化（有 README 等文件）

**解决**:
```bash
git pull origin master --allow-unrelated-histories
git push -u origin master
```

### 认证失败

```
remote: Support for password authentication was removed
```

**原因**: GitHub 不再支持密码认证

**解决**: 使用 Personal Access Token（见上文）

### Actions 构建失败

1. 访问 Actions 页面查看错误日志
2. 常见问题：
   - Gradle 依赖下载失败（网络问题，重新运行即可）
   - 代码编译错误（检查本地是否能编译）

## 下一步

代码推送成功后，你可以：

1. ✅ 在 GitHub 上查看代码
2. ✅ 等待 Actions 自动构建 APK（3-5分钟）
3. ✅ 从 Releases 下载 APK
4. ✅ 安装到 Android 设备测试
5. ✅ 分享仓库链接给其他人

---

需要帮助？在仓库中提交 Issue 或查看构建日志排查问题。

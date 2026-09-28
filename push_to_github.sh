#!/bin/bash

# 网易云音乐翻译模块 - GitHub 推送脚本
# 使用说明：
# 1. 在 GitHub 上创建新仓库（https://github.com/new）
# 2. 获取仓库 URL（例如：https://github.com/username/NeteaseMusicTranslator.git）
# 3. 替换下面的 YOUR_GITHUB_USERNAME
# 4. 运行此脚本： bash push_to_github.sh

# ========== 配置区域 ==========
GITHUB_USERNAME="YOUR_GITHUB_USERNAME"  # 替换为你的 GitHub 用户名
REPO_NAME="NeteaseMusicTranslator"
# ==============================

REPO_URL="https://github.com/${GITHUB_USERNAME}/${REPO_NAME}.git"

echo "================================================"
echo "推送到 GitHub 仓库"
echo "================================================"
echo "目标仓库: ${REPO_URL}"
echo ""

# 检查是否已经设置了远程仓库
if git remote | grep -q "^origin$"; then
    echo "✓ 检测到已存在的 origin，将更新 URL"
    git remote set-url origin "${REPO_URL}"
else
    echo "✓ 添加远程仓库 origin"
    git remote add origin "${REPO_URL}"
fi

# 显示当前分支
CURRENT_BRANCH=$(git branch --show-current)
echo "✓ 当前分支: ${CURRENT_BRANCH}"
echo ""

# 推送到 GitHub
echo "开始推送..."
if git push -u origin "${CURRENT_BRANCH}"; then
    echo ""
    echo "================================================"
    echo "✓ 推送成功！"
    echo "================================================"
    echo ""
    echo "仓库地址: https://github.com/${GITHUB_USERNAME}/${REPO_NAME}"
    echo "GitHub Actions 将自动构建 APK"
    echo ""
    echo "下一步："
    echo "1. 访问仓库查看代码"
    echo "2. 前往 Actions 标签查看自动构建状态"
    echo "3. 构建完成后在 Releases 页面下载 APK"
else
    echo ""
    echo "================================================"
    echo "✗ 推送失败"
    echo "================================================"
    echo ""
    echo "可能的原因："
    echo "1. GitHub 用户名不正确"
    echo "2. 仓库尚未在 GitHub 上创建"
    echo "3. 没有推送权限"
    echo ""
    echo "请确认："
    echo "- 已在 GitHub 上创建仓库: https://github.com/new"
    echo "- GITHUB_USERNAME 已正确设置"
    echo "- 已登录 GitHub（可能需要输入用户名和 Personal Access Token）"
fi

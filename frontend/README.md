# 个人博客系统 npm 前端

这是与 Java Spring Boot 后端对应的独立前端工程，使用 npm 和 Vite 管理、启动及构建。页面继续沿用原始首页和文章详情页的布局与视觉样式。

## 运行前提

1. IDEA 中的 Spring Boot 后端已经启动。
2. 后端接口可以通过以下地址访问：

```text
http://127.0.0.1:8080/api/articles
```

3. 已安装 Node.js 18 或更高版本。推荐 Node.js 20 LTS。

检查 Node.js：

```bash
node -v
npm -v
```

## 使用 VS Code 运行

1. 使用 VS Code 打开本前端文件夹。
2. 打开 VS Code 终端。
3. 第一次运行时安装依赖：

```bash
npm install
```

4. 启动开发服务器：

```bash
npm run dev
```

5. 浏览器打开：

```text
http://127.0.0.1:5173
```

请保持 IDEA 后端持续运行。开发环境会通过 Vite 将 `/api` 请求代理到 `http://127.0.0.1:8080`，不需要修改后端端口或额外设置跨域。

## 演示账号

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin123` |
| 普通用户 | `user` | `user123` |

页面地址：

```text
首页：http://127.0.0.1:5173/
文章详情：http://127.0.0.1:5173/article-detail.html?id=1
登录：http://127.0.0.1:5173/login.html
个人资料：http://127.0.0.1:5173/profile.html
管理后台：http://127.0.0.1:5173/admin.html
```

## 与后端接口的对应关系

| 前端功能 | 后端接口 |
| --- | --- |
| 首页文章和搜索 | `GET /api/articles` |
| 分类、标签筛选 | `GET /api/categories`、`GET /api/tags` |
| 文章详情与阅读量 | `GET /api/articles/{id}` |
| 评论列表与提交 | `GET/POST /api/articles/{id}/comments` |
| 登录、当前用户、退出 | `/api/auth/*` |
| 修改个人资料 | `PUT /api/profile` |
| 邮件订阅 | `POST /api/subscriptions` |
| 后台文章管理 | `/api/admin/articles/*` |
| 后台评论审核 | `/api/admin/comments/*` |
| 后台分类标签管理 | `/api/admin/categories/*`、`/api/admin/tags/*` |
| 后台统计 | `GET /api/admin/stats` |

## 生产构建

```bash
npm run build
```

生成的文件位于 `dist` 目录。预览构建结果：

```bash
npm run preview
```

## 常见问题

### npm 不是内部或外部命令

安装 Node.js 后重新打开 VS Code。安装时勾选 `Add to PATH`。

### 请求失败，请确认后端已启动

先在浏览器访问：

```text
http://127.0.0.1:8080/api/articles
```

如果无法看到 JSON，说明 IDEA 后端没有成功启动或后端端口不是 8080。

### 5173 端口被占用

关闭占用端口的前端进程，或者修改 `vite.config.js` 中的 `server.port`。如果修改端口，不影响 `/api` 代理。

### 登录后仍提示未登录

前后端地址要统一使用 `127.0.0.1`，不要一个使用 `localhost`、另一个使用 `127.0.0.1`。

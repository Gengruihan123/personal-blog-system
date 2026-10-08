# 个人博客系统

个人博客系统课程实训项目。仓库包含 Java Spring Boot 后端与 HTML / JavaScript / Vite 前端。

## 功能

- 文章列表、搜索、分类和标签筛选、文章详情、阅读量统计。
- 登录、退出、个人资料修改。
- 评论提交与后台审核、邮件订阅信息登记。
- 管理后台：文章、分类、标签管理和统计。

## 技术栈

| 部分 | 技术 |
| --- | --- |
| 后端 | Java 17、Spring Boot 3.3.5、Spring Security、Spring Data JPA |
| 数据库 | MySQL；后端测试使用 H2 |
| 前端 | HTML、JavaScript、Vite 5 |
| 页面资源 | Tailwind CSS、Font Awesome、highlight.js 和外部图片资源 |
| 构建 | Maven、npm |

## 项目目录

| 路径 | 内容 |
| --- | --- |
| `backend/` | 后端源代码、Maven 配置、已有测试和 Dockerfile |
| `frontend/` | 前端页面、JavaScript、npm 锁文件和 Vite 配置 |
| `database/init.sql` | 本地演示数据库和数据库用户初始化脚本 |
| `scripts/` | Windows 和 Linux/macOS 启动脚本 |
| `.gitignore` | 排除依赖、编译产物、IDE 配置和本地敏感配置 |

## 本地运行

先安装 JDK 17、Maven 3.9、Node.js 22 和 MySQL 8，并确认 `java`、`mvn`、`node`、`npm`、`mysql` 可在终端使用。

### 1. 初始化数据库

在项目根目录执行：

```bash
mysql -u root -p
```

在 MySQL 命令行中执行（将路径替换为你的实际路径，Windows 路径可使用 `/`）：

```sql
SOURCE /absolute/path/personal-blog/database/init.sql;
EXIT;
```

脚本创建 `personal_blog` 数据库以及本地演示用户 `blog_user`，演示密码为 `blog_password`。若该用户已经存在，脚本不会重置其密码；请确认密码与后端配置一致。

无需额外导入业务表 SQL：后端启动时，JPA 自动创建/更新表结构；当用户表为空时，`DataInitializer` 自动生成演示账号、分类、标签、文章和评论。

后端配置位于 `backend/src/main/resources/application.yml`，支持环境变量 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 和 `SERVER_PORT`。修改数据库连接时，在运行后端的终端设置相应变量。

例如 Windows PowerShell：

```powershell
$env:DB_USERNAME = "your_database_user"
$env:DB_PASSWORD = "your_database_password"
```

Linux/macOS：

```bash
export DB_USERNAME='your_database_user'
export DB_PASSWORD='your_database_password'
```

### 2. 启动后端

在第一个终端执行：

```bash
cd backend
mvn spring-boot:run
```

或在 IDEA 中将 `backend/pom.xml` 导入为 Maven 项目，使用 JDK 17，运行 `com.group3.blog.BlogApplication`。

接口验证地址：`http://127.0.0.1:8080/api/articles`。

### 3. 启动前端

保持后端运行，在第二个终端执行：

```bash
cd frontend
npm ci
npm run dev
```

访问 `http://127.0.0.1:5173/`。开发服务器将 `/api` 请求代理到 `http://127.0.0.1:8080`；前后端地址统一使用 `127.0.0.1`。

Windows 也可依次运行 `scripts/start-backend.bat` 和 `scripts/start-frontend.bat`。Linux/macOS 可在两个终端分别执行 `bash scripts/start-backend.sh` 与 `bash scripts/start-frontend.sh`。这些脚本仍需要预先安装运行环境并初始化数据库。

### 演示账号

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin123` |
| 普通用户 | `user` | `user123` |

这些是原项目的本地演示账号。公开部署前应调整初始化账号和数据库凭据；不要提交真实密码或个人数据库备份。

## 测试与构建

后端已有两项集成测试，覆盖文章列表和管理员登录，测试配置使用 H2，不需要本地 MySQL：

```bash
cd backend
mvn test
mvn clean package
```

前端构建：

```bash
cd frontend
npm ci
npm run build
npm run preview
```

前端生成 `frontend/dist/`，后端生成 `backend/target/personal-blog-backend-1.0.0.jar`。生成目录不用提交 GitHub。

`npm run preview` 默认使用 `127.0.0.1:4173`，仍需启动本地后端。页面样式、图标、语法高亮和部分图片依赖外部网站，需要网络连接。

本项目保留原有运行方式。上传 GitHub 仅保存源代码，不会启动 Java 后端或 MySQL；GitHub Pages 无法独立运行完整系统。远程部署需要单独配置前后端地址、代理、跨域与数据库。后端 Dockerfile 仅构建后端，不包含前端和 MySQL。



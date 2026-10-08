# 整理验证记录

- 对比原始压缩包，后端业务源码、测试、pom.xml、Dockerfile 均保留。
- 前端页面、JavaScript、Vite 配置与 npm 锁文件均保留；Windows 启动脚本增加切换至自身目录的处理。
- 排除后端编译产物与 IDE 私有配置。
- Node.js 检查前端 JavaScript 语法；Bash 检查 shell 启动脚本语法。
- 未安装 Maven，未执行 Java 编译与已有集成测试；未进行 MySQL 联调。
- 前端 `npm ci --ignore-scripts` 安装成功，`npm run build` 构建成功（Vite 5.4.21，5 个页面入口）。

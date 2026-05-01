## 开发

```bash
# 进入前端目录
cd platform-ui

# 安装依赖
npm install

# 可选：使用国内镜像加速
npm install --registry=https://registry.npmmirror.com

# 启动开发服务
npm run dev
```

浏览器访问控制台提示的本地地址（默认端口见 `vue.config.js`）。

## 发布

```bash
npm run build:stage
npm run build:prod
```

import { defineConfig } from 'vite';
import { resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const rootDir = fileURLToPath(new URL('.', import.meta.url));

export default defineConfig({
  server: {
    host: '127.0.0.1',
    port: 5173,
    strictPort: true,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        secure: false
      }
    }
  },
  build: {
    rollupOptions: {
      input: {
        index: resolve(rootDir, 'index.html'),
        article: resolve(rootDir, 'article-detail.html'),
        login: resolve(rootDir, 'login.html'),
        profile: resolve(rootDir, 'profile.html'),
        admin: resolve(rootDir, 'admin.html')
      }
    }
  }
});

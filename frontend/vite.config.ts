// frontend/vite.config.ts
import { defineConfig } from 'vite';
import { resolve } from 'node:path';

export default defineConfig({
    root: './public',
    server: {
        fs: {
            // permite importar arquivos fora do root (./public), como ../js
            allow: [
                resolve(__dirname, '.'),
                resolve(__dirname, 'js'),
                resolve(__dirname, 'assets')
            ]
        }
    },
    build: {
        outDir: '../dist',
        emptyOutDir: true,
    },
});

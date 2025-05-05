/// <reference types="react/experimental" />
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { ThemeProvider } from "@/components/theme-provider"
import { Layout } from './Layout.tsx'

createRoot(document.querySelector('#root')!).render(
    <StrictMode>
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
            <Layout>
                <App />
            </Layout>
        </ThemeProvider>
    </StrictMode>
)

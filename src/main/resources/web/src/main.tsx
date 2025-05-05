import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { ThemeProvider } from "@/components/theme-provider"
import { RouterProvider } from 'react-router'
import router from './routes.tsx'

createRoot(document.querySelector('#root')!).render(
    <StrictMode>
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">

            <RouterProvider router={router} />

        </ThemeProvider>
    </StrictMode>
)

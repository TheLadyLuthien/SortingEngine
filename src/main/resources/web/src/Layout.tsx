import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar"
import { AppSidebar } from "@/components/AppSidebar"

interface Props
{
    children: React.ReactNode
}

export function Layout(props: Props)
{
    return (
        <SidebarProvider>
            <AppSidebar />
            <main>
                <SidebarTrigger />
                {props.children}
            </main>
        </SidebarProvider>
    )
}

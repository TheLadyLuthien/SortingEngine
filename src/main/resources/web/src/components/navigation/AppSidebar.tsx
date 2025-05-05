// import { Home, Inbox, Calendar, Search, Settings, Sidebar } from "lucide-react"
import { SidebarContent, SidebarGroup, SidebarGroupLabel, SidebarGroupContent, SidebarMenu, SidebarMenuItem, SidebarMenuButton, SidebarHeader, SidebarRail, Sidebar, SidebarFooter } from "@/components/ui/sidebar"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "../ui/tabs"
import { ModePicker } from "./ModePicker"

import { unstable_ViewTransition as ViewTransition } from 'react';
import { SettingsSidebarFooter } from "../SettingsSidebarFooter";
import { Separator } from "../ui/separator";
import { matchPath, NavLink, useLocation, useMatch } from "react-router";

// Menu items.

const data = {
    user: {
        name: "Laggy",
        email: "laggy.lan",
        avatar: "/avatars/shadcn.jpg",
    },
    versions: ["1.0.1", "1.1.0-alpha", "2.0.0-beta1"],
    navMain: [
        {
            title: "Getting Started",
            url: "/",
            items: [
                {
                    title: "Home",
                    url: "/",
                },
                {
                    title: "Test",
                    url: "/test",
                },
            ],
        },
        {
            title: "Building Your Application",
            url: "#",
            items: [
                {
                    title: "Routing",
                    url: "#",
                },
                {
                    title: "Data Fetching",
                    url: "#",
                },
                {
                    title: "Rendering",
                    url: "#",
                },
                {
                    title: "Caching",
                    url: "#",
                },
                {
                    title: "Styling",
                    url: "#",
                },
                {
                    title: "Optimizing",
                    url: "#",
                },
                {
                    title: "Configuring",
                    url: "#",
                },
                {
                    title: "Testing",
                    url: "#",
                },
                {
                    title: "Authentication",
                    url: "#",
                },
                {
                    title: "Deploying",
                    url: "#",
                },
                {
                    title: "Upgrading",
                    url: "#",
                },
                {
                    title: "Examples",
                    url: "#",
                },
            ],
        },
        {
            title: "API Reference",
            url: "#",
            items: [
                {
                    title: "Components",
                    url: "#",
                },
                {
                    title: "File Conventions",
                    url: "#",
                },
                {
                    title: "Functions",
                    url: "#",
                },
                {
                    title: "next.config.js Options",
                    url: "#",
                },
                {
                    title: "CLI",
                    url: "#",
                },
                {
                    title: "Edge Runtime",
                    url: "#",
                },
            ],
        },
        {
            title: "Architecture",
            url: "#",
            items: [
                {
                    title: "Accessibility",
                    url: "#",
                },
                {
                    title: "Fast Refresh",
                    url: "#",
                },
                {
                    title: "Next.js Compiler",
                    url: "#",
                },
                {
                    title: "Supported Browsers",
                    url: "#",
                },
                {
                    title: "Turbopack",
                    url: "#",
                },
            ],
        },
    ],
}

export function AppSidebar()
{
    const location = useLocation();

    return (
        <Sidebar>
            <SidebarHeader className="pb-0">
                <ModePicker />
                {/* <Separator orientation="horizontal"/> */}
            </SidebarHeader>

            <ViewTransition>
                <SidebarContent>
                    {data.navMain.map((item) => (
                        <SidebarGroup key={item.title}>
                            <SidebarGroupLabel>{item.title}</SidebarGroupLabel>
                            <SidebarGroupContent>
                                <SidebarMenu>
                                    {item.items.map((item) => (
                                        <SidebarMenuItem key={item.title}>
                                            
                                            <SidebarMenuButton asChild isActive={matchPath(location.pathname, item.url) != null}>
                                                <NavLink to={item.url}>{item.title}</NavLink>
                                            </SidebarMenuButton>
                                        </SidebarMenuItem>
                                    ))}
                                </SidebarMenu>
                            </SidebarGroupContent>
                        </SidebarGroup>
                    ))}
                </SidebarContent>
            </ViewTransition>
            <SidebarFooter>
                <SettingsSidebarFooter user={data.user}></SettingsSidebarFooter>
            </SidebarFooter>
            <SidebarRail />
        </Sidebar >
    )
}
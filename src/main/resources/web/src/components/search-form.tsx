import { Search } from "lucide-react"

import { Label } from "@/components/ui/label"
import
{
    SidebarGroup,
    SidebarGroupContent,
    SidebarInput,
} from "@/components/ui/sidebar"
import { Input } from "./ui/input"

export function SearchForm({ ...props }: React.ComponentProps<"form">)
{
    return (
        <form {...props} className="w-full">
            <div className="relative">
                <Label htmlFor="search" className="sr-only">
                    Search
                </Label>
                <Input
                    id="search"
                    placeholder="Search Everything"
                    className="pl-8"
                />
                <Search className="pointer-events-none absolute top-1/2 left-2 size-4 -translate-y-1/2 opacity-50 select-none" />
            </div>
        </form>
    )
}

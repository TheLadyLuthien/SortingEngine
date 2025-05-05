import { Check, ChevronsUpDown, GalleryVerticalEnd, Pencil } from "lucide-react";
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "../ui/dropdown-menu";
import { SidebarMenu, SidebarMenuAction, SidebarMenuButton, SidebarMenuItem } from "../ui/sidebar";
import { TabsList, TabsTrigger } from "../ui/tabs";


// export function ModePickerButtons()
// {
//     return (
//         <SidebarMenu>
//             <SidebarMenuItem>
//                 {/* <DropdownMenu> */}
//                     {/* <DropdownMenuTrigger asChild> */}
//                     {/* <SidebarMenuButton size="lg" className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"> */}
//                         {/* <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-sidebar-primary text-sidebar-primary-foreground">
//                                 <GalleryVerticalEnd className="size-4" />
//                             </div>
//                             <div className="flex flex-col gap-0.5 leading-none">
//                                 <span className="font-semibold">Documentation</span>
//                                 <span className="">Test</span>
//                             </div>
//                             <ChevronsUpDown className="ml-auto" /> */}

//                         <TabsList className="w-full grid grid-cols-2 h-12 text-sm group-data-[collapsible=icon]:p-0!">
//                             <TabsTrigger value="sort">Sort</TabsTrigger>
//                             <TabsTrigger value="browse">Browse</TabsTrigger>
//                         </TabsList>
//                     {/* </SidebarMenuButton> */}
//                     {/* </DropdownMenuTrigger> */}
//                     {/* <DropdownMenuContent
//                         className="w-[--radix-dropdown-menu-trigger-width]"
//                         align="start"
//                     >
//                         <DropdownMenuItem>
//                             Foo
//                         </DropdownMenuItem>
//                         <DropdownMenuItem>
//                             bar
//                         </DropdownMenuItem>
//                     </DropdownMenuContent> */}
//                 {/* </DropdownMenu> */}

//             </SidebarMenuItem>
//         </SidebarMenu>
//         // <TabsList className="w-full grid grid-cols-2">
//         //     <TabsTrigger value="sort">Sort</TabsTrigger>
//         //     <TabsTrigger value="browse">Browse</TabsTrigger>
//         // </TabsList>
//     )
// }

export function ModePicker()
{
    return (
        <SidebarMenu>
            <SidebarMenuItem>
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <SidebarMenuButton
                            size="lg"
                            className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
                        >
                            <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-primary text-sidebar-primary-foreground">
                                <Pencil className="size-4" />
                            </div>
                            <div className="flex flex-col gap-0.5 leading-none">
                                <span className="font-semibold">Sorting Engine</span>
                                <span className="">Browse</span>
                            </div>
                            <ChevronsUpDown className="ml-auto" />
                        </SidebarMenuButton>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent
                        className="w-full"
                        align="center"
                    >
                        <DropdownMenuItem className="w-full">
                            Sort <Check className="ml-auto" />
                        </DropdownMenuItem>
                        <DropdownMenuItem className="w-full">
                            Browse
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>

            </SidebarMenuItem>
        </SidebarMenu>
        // <TabsList className="w-full grid grid-cols-2">
        //     <TabsTrigger value="sort">Sort</TabsTrigger>
        //     <TabsTrigger value="browse">Browse</TabsTrigger>
        // </TabsList>
    )
}
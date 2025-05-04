import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";

// interface Props
// {

// }

export function Sidebar(/* props: Props */)
{
    return (
        <div id="sidebar" className="w-64 bg-slate-500">
            <Tabs defaultValue="browse" className="w-full">
                <TabsList className='grid w-full grid-cols-2'>
                    <TabsTrigger value="sort">Sort</TabsTrigger>
                    <TabsTrigger value="browse">Browse</TabsTrigger>
                </TabsList>
                <TabsContent value="sort">Make changes to your account here.</TabsContent>
                <TabsContent value="browse">Change your password here.</TabsContent>
            </Tabs>
        </div>
    )
}
// import { useState } from 'react'
import React, { startTransition, useState } from 'react'
import { Button } from '../components/ui/button'
// import { Button } from './components/ui/button'
// import { ModeToggle } from './components/UiModeToggle'
// import Layout from './Layout'
// import { AppSidebar } from './components/AppSidebar'


import { unstable_ViewTransition as ViewTransition } from 'react';
import { Layout } from '../Layout';
import { Link } from 'react-router';
import { ModeToggle } from '@/components/UiModeToggle';

function AnotherPage()
{
    const [state, setState] = useState(false)

    return (
        <div>
            <h1>TEST PAGE</h1>
            <Link to="/">Go TO: Home</Link>
            <p>Another PAGE!!!</p>
            <ModeToggle></ModeToggle>
            <Button onClick={() => startTransition(() => setState(!state))}>State {state}</Button>
            {(state) && (
                <ViewTransition>
                    <Button>TADA</Button>
                </ViewTransition>
            )}
        </div>
    )
}


// const ListItem = React.forwardRef<
//   React.ElementRef<"a">,
//   React.ComponentPropsWithoutRef<"a">
// >(({ className, title, children, ...props }, ref) => {
//   return (
//     <li>
//       <NavigationMenuLink asChild>
//         <a
//           ref={ref}
//           className={cn(
//             "block select-none space-y-1 rounded-md p-3 leading-none no-underline outline-none transition-colors hover:bg-accent hover:text-accent-foreground focus:bg-accent focus:text-accent-foreground",
//             className
//           )}
//           {...props}
//         >
//           <div className="text-sm font-medium leading-none">{title}</div>
//           <p className="line-clamp-2 text-sm leading-snug text-muted-foreground">
//             {children}
//           </p>
//         </a>
//       </NavigationMenuLink>
//     </li>
//   )
// })
// ListItem.displayName = "ListItem"


export default AnotherPage
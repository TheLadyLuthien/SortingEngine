import { useEffect } from "react";
import { matchPath, useLocation } from "react-router";


export function ModeThemeProvider()
{
    const location = useLocation();
    useEffect((() => {
        const root = window.document.documentElement
    
        root.classList.remove("mode-soprt", "mode-browse")
    
        if (matchPath(location.pathname, "/browse") != null)
        {
            root.classList.add("mode-browse")
        }
        else if (matchPath(location.pathname, "/sort") != null)
        {
            root.classList.add("mode-sort")
        }
    }), [location])
}

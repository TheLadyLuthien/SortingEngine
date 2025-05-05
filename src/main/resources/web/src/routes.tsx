import
{
    createBrowserRouter,
} from "react-router";
import App from "./App";
import AnotherPage from "./Page2";

const router = createBrowserRouter([
    {
        path: "/",
        Component: App,
    },
    {
        path: "test",
        Component: AnotherPage,
    },
]);

export default router;

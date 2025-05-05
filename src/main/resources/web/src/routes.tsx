import
{
    createBrowserRouter,
} from "react-router";
import App from "./pages/App";
import AnotherPage from "./pages/Page2";

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

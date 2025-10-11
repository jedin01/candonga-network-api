import { ViewConfig } from "@vaadin/hilla-file-router/types.js";
import { useEffect } from "react";
import { useNavigate } from "react-router";

export const config: ViewConfig = {
  menu: {
    exclude: true,
  },
};

export default function MainView() {
  const navigate = useNavigate();

  useEffect(() => {
    // Redirect to dashboard on load
    navigate("/dashboard");
  }, [navigate]);

  return null;
}

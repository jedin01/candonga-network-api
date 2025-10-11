import { Outlet, useLocation, useNavigate } from "react-router";
import "@vaadin/icons";
import {
  AppLayout,
  Icon,
  ProgressBar,
  Scroller,
  SideNav,
  SideNavItem,
} from "@vaadin/react-components";
import { Suspense, useMemo } from "react";
import { createMenuItems } from "@vaadin/hilla-file-router/runtime.js";
import "./dashboard.css";

function Header() {
  return (
    <div className="flex p-m gap-m items-center" slot="drawer">
      <Icon icon="vaadin:road" className="text-primary icon-l" />
      <span className="font-semibold text-l">Sistema Indika</span>
    </div>
  );
}

function MainMenu() {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <SideNav
      className="mx-m"
      onNavigate={({ path }) => path != null && navigate(path)}
      location={location}
    >
      {createMenuItems().map(({ to, icon, title }) => (
        <SideNavItem path={to} key={to}>
          {icon && <Icon icon={icon} slot="prefix" />}
          {title}
        </SideNavItem>
      ))}
    </SideNav>
  );
}

export default function MainLayout() {
  return (
    <AppLayout
      primarySection="drawer"
      style={
        {
          "--vaadin-app-layout-drawer-width": "280px",
          "--vaadin-app-layout-drawer-overlay": "false",
        } as React.CSSProperties
      }
    >
      <Header />
      <Scroller
        slot="drawer"
        style={{
          width: "280px",
          minWidth: "280px",
          maxWidth: "280px",
          flexShrink: 0,
        }}
      >
        <MainMenu />
      </Scroller>
      <Suspense fallback={<ProgressBar indeterminate={true} className="m-0" />}>
        <div
          style={{
            width: "100%",
            height: "100%",
            overflow: "auto",
            display: "flex",
            flexDirection: "column",
          }}
        >
          <Outlet />
        </div>
      </Suspense>
    </AppLayout>
  );
}

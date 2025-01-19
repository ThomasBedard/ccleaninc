import NavBar from "./Navbar";
import { PageFooter } from "./page-footer";
import { ReactNode } from "react";

interface PageLayoutProps {
  children: ReactNode;
}

export const PageLayout = ({ children }: PageLayoutProps) => {
  return (
    <div className="page-layout">
      <NavBar />
      <div className="page-layout__content">{children}</div>
      <PageFooter />
    </div>
  );
};

import {
  withAuthenticationRequired,
  WithAuthenticationRequiredOptions,
} from "@auth0/auth0-react";
import React, { ComponentType } from "react";
import { PageLoader } from "./page-loader";

// Define props for the AuthenticationGuard
interface AuthenticationGuardProps {
  component: ComponentType; // The component to protect
}

export const AuthenticationGuard: React.FC<AuthenticationGuardProps> = ({
  component,
}) => {
  const Component = withAuthenticationRequired(component, {
    onRedirecting: () => (
      <div className="page-layout">
        <PageLoader />
      </div>
    ),
  } as WithAuthenticationRequiredOptions);

  return <Component />;
};

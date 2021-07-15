import React from "react";
import { Route } from "react-router-dom";
import { Burndown } from "./Burndown";

export const BurndownRoute: React.VFC = () => (
  <Route exact path="/projects/:projectId/burndown">
    <Burndown />
  </Route>
);

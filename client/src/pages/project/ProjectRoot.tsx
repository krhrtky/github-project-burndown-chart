import React from "react";
import { Route, Switch } from "react-router-dom";
import { ProjectList } from "@/pages/project/ProjectList";

export const ProjectRoot: React.VFC = () => (
  <Switch>
    <Route exact path="/project/select">
      <ProjectList />
    </Route>
  </Switch>
);

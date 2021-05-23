import React from "react";
import { Route, Switch } from "react-router-dom";
import { ProjectList } from "@/pages/projects/ProjectList";

export const ProjectsRoot = () => (
  <Switch>
    <Route exact path="/projects">
      <ProjectList />
    </Route>
  </Switch>
)

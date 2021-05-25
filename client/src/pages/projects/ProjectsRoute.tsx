import React from "react";
import { Route } from "react-router-dom";
import { ProjectDetail } from "@/pages/projects/ProjectDetail";

export const ProjectsRoot = () => (
  <Route exact path="/projects/:projectId">
    <ProjectDetail />
  </Route>
);

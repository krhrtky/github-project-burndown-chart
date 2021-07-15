import React, { useEffect } from "react";

import { useDispatch } from "react-redux";
import { push } from "connected-react-router";
import { Route, useParams, useRouteMatch } from "react-router-dom";
import { Tabs, useTabs } from "@geist-ui/react";
import { BurndownRoute } from "./Burndown/BurndownRoute";
import { ProjectsDetailRoot } from "./ProjectDetailRoot";

const Container: React.VFC = () => {
  const dispatch = useDispatch();
  const { projectId } = useParams<{ projectId: string }>();
  const { setState, bindings } = useTabs("detail");

  const isBurndownPage = useRouteMatch({
    path: "/projects/:projectId/burndown",
    strict: true,
  });

  useEffect(() => {
    setState(isBurndownPage ? "burndown" : "detail");
  }, [isBurndownPage]);

  return (
    <>
      <Tabs
        {...bindings}
        onChange={(val) => {
          dispatch(push(val === "detail" ? `/projects/${projectId}` : `/projects/${projectId}/burndown`));
        }}
      >
        <Tabs.Item label="Projects detail" value="detail">
          Projects detail & Task list
        </Tabs.Item>
        <Tabs.Item label="Burndown" value="burndown">
          Project burndown chart
        </Tabs.Item>
      </Tabs>
      <div>
        <ProjectsDetailRoot />
        <BurndownRoute />
      </div>
    </>
  );
};

export const ProjectsRoot: React.VFC = () => {
  return (
    <Route path="/projects/:projectId">
      <Container />
    </Route>
  );
};

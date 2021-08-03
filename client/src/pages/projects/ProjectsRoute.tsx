import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import { push } from "connected-react-router";
import { Route, useParams, useRouteMatch } from "react-router-dom";
import { Button, Tabs, useTabs } from "@geist-ui/react";
import { ChevronLeft } from "@geist-ui/react-icons";
import { ProjectsDetailRoot } from "./ProjectDetailRoot";
import { BurndownRoute } from "./Burndown/BurndownRoute";

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
  }, [isBurndownPage, setState]);

  return (
    <div>
      <Button auto size="small" icon={<ChevronLeft />} onClick={() => dispatch(push("/project/select"))}>
        Projects
      </Button>
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
    </div>
  );
};

export const ProjectsRoot: React.VFC = () => {
  return (
    <Route path="/projects/:projectId">
      <Container />
    </Route>
  );
};

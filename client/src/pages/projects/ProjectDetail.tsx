import React, { useEffect, useState } from "react";
import { getApp } from "@firebase/app";
import { useParams } from "react-router-dom";
import { doc, getFirestore, onSnapshot } from "@firebase/firestore";
import { useProjectCardsQuery } from "@/generated/graphql/graphql";
import { useSelector } from "react-redux";
import { selectUser } from "@/store/store";
import { Col, Loading, Row, Text } from "@geist-ui/react";
import { Column } from "@/pages/projects/Column";

type Project = {
  organization: string;
  projectNumber: number;
  tasks: Array<string>;
};

export const ProjectDetail: React.FC = () => {
  const user = useSelector(selectUser);
  const { projectId } = useParams<{ projectId: string }>();
  const [project, setProject] = useState<Project | null>(null);
  const { loading, data } = useProjectCardsQuery({
    variables: {
      login: project?.organization ?? "",
      projectNumber: project?.projectNumber ?? 0,
      columnFirst: 100,
    },
    context: {
      headers: {
        Authorization: `Bearer ${user.authenticated ? user.accessToken : ""}`,
      },
    },
  });
  const [currentColumnCursor, setCurrentColumn] = useState<string | null>("");

  if (!user.authenticated) {
    return null;
  }

  useEffect(() => {
    const unsubscribe = onSnapshot<Project>(doc(getFirestore(getApp()), "project", projectId), (snapShot) => {
      setProject(snapShot.data() || null);
    });
    return () => unsubscribe();
  }, []);

  return loading ? (
    <Loading size="large" />
  ) : (
    <Row gap={1}>
      <Col span={5}>
        {data?.viewer.organization?.project?.columns.edges?.map((edge) => (
          <Text
            key={edge?.node?.id ?? ""}
            style={{ cursor: "pointer" }}
            onClick={() => setCurrentColumn(edge?.node?.id ?? "")}
            type={currentColumnCursor === edge?.node?.id ? "success" : undefined}
            b={currentColumnCursor === edge?.node?.id ?? ""}
            p
          >
            {edge?.node?.name}
          </Text>
        ))}
      </Col>
      <Col span={19}>
        {currentColumnCursor?.length === 0 ? (
          <div>Select Column</div>
        ) : (
          <Column columnId={currentColumnCursor ?? ""} taskIds={project?.tasks ?? []} />
        )}
      </Col>
    </Row>
  );
};

import React, {useEffect, useState} from "react";
import { getApp } from "@firebase/app";
import {
  Redirect,
  useParams
} from "react-router-dom";
import {
  doc,
  getDocs,
  getFirestore,
  collection,
  where,
  onSnapshot,
  query,
  Timestamp,
  documentId,
} from "@firebase/firestore";
import { useProjectCardsQuery } from "@/generated/graphql/graphql";
import { useSelector } from "react-redux";
import { selectUser } from "@/store/user/userSlice";
import {Col, Loading, Row, Text} from "@geist-ui/react";
import {Column} from "@/pages/projects/Column";

type Project = {
  organization: string;
  projectNumber: number;
  tasks: Array<string>;
};

type UnFinishedTask = {
  estimateStoryPoint: number;
  finishedAt: Timestamp;
  projectCardId: null;
  resultStoryPoint: null;
};

type FinishedTask = {
  estimateStoryPoint: number;
  finishedAt: Timestamp;
  projectCardId: number;
  resultStoryPoint: number;
};

type Task = UnFinishedTask | FinishedTask;

export const ProjectDetail: React.FC = () => {
  const user = useSelector(selectUser);
  const { projectId } = useParams<{ projectId: string; }>();
  const [project, setProject] = useState<Project | null>(null);
  const [tasks, setTasks] = useState<Array<Task>>([]);
  const { loading, data } = useProjectCardsQuery({
    variables: {
      login: project?.organization ?? "",
      projectNumber: project?.projectNumber ?? 0,
      columnFirst: 100,
    },
    context: {
      headers: {
        Authorization: `Bearer ${user.authenticated ? user.accessToken : ""}`
      }
    }
  });
  const [currentColumnCursor, setCurrentColumn] = useState<string | null>("");

  if (!user.authenticated) {
    return <Redirect to="/" />;
  }

  useEffect(() => {
    const unsubscribe = onSnapshot<Project>(
      doc(getFirestore(getApp()), "project", projectId),
      doc => {
        setProject(doc.data() || null);
      }
    );
    return () => unsubscribe();
  }, []);

  useEffect(() => {
    if (project == null || project.tasks.length === 0) {
      return;
    }
    getDocs<Task>(
      query(
        collection(getFirestore(getApp()), "task"),
        where(documentId(), "in", project.tasks)
      ),
    ).then(querySnapshot => {
      const tasks = querySnapshot.docs.map(documentSnapshot => documentSnapshot.data())
      setTasks(tasks);
    });
  }, [project]);

  return loading ? (
    <Loading size="large" />
  ) : (
    <Row gap={1}>
      <Col span={5}>
        {data?.viewer.organization?.project?.columns.edges?.map(edge => (
          <Text
            key={edge?.node?.id ?? ""}
            onClick={() => setCurrentColumn(edge?.node?.id ?? "")}
            b={currentColumnCursor === edge?.cursor ?? ""}
            p
          >
            {edge?.node?.name}
          </Text>
        ))}
      </Col>
      <Col span={19}>
        {currentColumnCursor?.length == 0 ? (
          <div>Select Column</div>
        ) : (
          <Column
            columnId={currentColumnCursor ?? ""}
          />
        )}
      </Col>
    </Row>
  )
}

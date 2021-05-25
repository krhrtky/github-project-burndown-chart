import React, {useEffect, useState} from "react";
import { getApp } from "@firebase/app";
import {
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
  const { projectId } = useParams<{ projectId: string; }>();
  const [taskIds, setTaskIds] = useState<Array<string>>([]);
  const [tasks, setTasks] = useState<Array<Task>>([]);

  useEffect(() => {
    const unsubscribe = onSnapshot<{tasks: Array<string>}>(
      doc(getFirestore(getApp()), "project", projectId),
      doc => {
        const project = doc.data();
        setTaskIds(project ? project.tasks : []);
      }
    );
    return () => unsubscribe();
  }, []);

  useEffect(() => {
    if (taskIds.length === 0) {
      return;
    }
    getDocs<Task>(
      query(
        collection(getFirestore(getApp()), "task"),
        where(documentId(), "in", taskIds)
      ),
    ).then(querySnapshot => {
      const tasks = querySnapshot.docs.map(documentSnapshot => documentSnapshot.data())
      setTasks(tasks);
    });
  }, [taskIds]);

  return (
    <div>
      {
        tasks.map(task => (
          <div key={task.projectCardId}>{task.projectCardId}</div>
        ))
      }
    </div>
  )
}

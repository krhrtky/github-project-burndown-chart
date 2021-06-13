import {useSelector} from "react-redux";
import {selectUser} from "@/store/user/userSlice";
import React, {useEffect, useState} from "react";
import {
  Issue,
  Maybe,
  ProjectCard,
  PullRequest,
  useProjectColumnQuery
} from "@/generated/graphql/graphql";
import { getApp } from "@firebase/app";
import {
  onSnapshot,
  getFirestore,
  collection,
  where,
  query,
  documentId,
} from "@firebase/firestore";
import {Badge, Card, Grid, Loading, useTheme } from "@geist-ui/react";
import ReactMarkdown from "react-markdown";
import {CreateTaskModal} from "@/pages/projects/Task/CreateTaskModal";
import {FinishTaskModal} from "@/pages/projects/Task/FinishTaskModal";
import {FinishedTask, Task} from "./Task/type";
import {FinishedTaskModal} from "@/pages/projects/Task/FinishedTaskModal";

type Props = {
  columnId: string;
  taskIds: Array<string>;
};

export const Column: React.FC<Props> = ({
  columnId,
  taskIds,
}) => {

  const user = useSelector(selectUser);
  const theme = useTheme();
  const { loading, data } = useProjectColumnQuery({
    variables: {
      columnId,
      cardFirst: 100,
    },
    context: {
      headers: {
        Authorization: `Bearer ${user.authenticated ? user.accessToken : ""}`
      }
    }
  });
  const [modalOpening, setModalOpening] = useState<boolean>(false);
  const closeModal = () => setModalOpening(false);
  const [selectedCardId, selectCardId] = useState<string>("");
  const [tasks, setTasks] = useState<Array<Task>>([]);
  const openModal = (cardId: string) => {
    selectCardId(cardId);
    setModalOpening(true);
  }

  useEffect(() => {
    if (taskIds.length === 0) {
      return;
    }

    const unsubscribe = onSnapshot<Task>(
      query(
        collection(getFirestore(getApp()), "task"),
        where(documentId(), "in", taskIds)
      ),
      doc => {
        const tasks = doc.docs.map(documentSnapshot => ({
          ...(documentSnapshot.data()),
          id: documentSnapshot.id,
        }));
        setTasks(tasks);
      }
    );

    return () => unsubscribe();

  }, [taskIds]);

  if (loading) {
    return <Loading size="large" />;
  }

  if (data == null || data.node?.__typename !== "ProjectColumn") {
    throw new Error();
  }

  const storedTask = tasks.find(task => task.projectCardId === selectedCardId);

  const badge = (card: Card) => {
    switch (card?.content?.__typename) {
      case "Issue":
        return (
          <Badge type="warning">Issue</Badge>
        )
      case "PullRequest":
        return (
          <Badge
            style={{ backgroundColor: theme.palette.alert }}
          >
            PullRequest
          </Badge>
        );
      default:
        return (
          <Badge
            style={{ backgroundColor: theme.palette.cyanLighter, color: theme.palette.foreground }}
          >
            Note
          </Badge>
        );
    }
  }

  return (
    <>
      <Grid.Container gap={1} justify="flex-start" style={{ overflowY: "auto",
        maxHeight: "calc(100vh - (16pt * 10) - 65px)",}}>
        {(data?.node?.cards.nodes?.length ?? 0) === 0 ? (
          <div>card not exists</div>
        ) : data?.node?.cards?.nodes?.map(card => (
          <Grid xs={12} key={card?.id}>
            <Card
              key={card?.id}
              style={{ overflowWrap: "break-word" }}
              onClick={() => openModal(card?.id ?? "")}
              hoverable
            >
              {badge(card)}
              <ReactMarkdown>
                {title(card) || ""}
              </ReactMarkdown>
            </Card>
          </Grid>
        ))
        }
      </Grid.Container>
      {
        storedTask != null && isFinishedTask(storedTask) ? (
          <FinishedTaskModal
            open={modalOpening}
            onClose={closeModal}
            task={storedTask}
          />
        ) : null
      }
      <FinishTaskModal
        open={modalOpening && storedTask != null && !isFinishedTask(storedTask)}
        onClose={closeModal}
        taskId={tasks.find(task => task.projectCardId === selectedCardId)?.id ?? ""}
      />
      <CreateTaskModal open={modalOpening && storedTask == null} onClose={closeModal} projectCardId={selectedCardId} />
    </>
  );
}

const isFinishedTask = (task: Task): task is FinishedTask  => task.finishedAt !== null && task.resultStoryPoint !== null;

type Card = Maybe<{ __typename: 'ProjectCard' }
  & Pick<ProjectCard, 'id' | 'resourcePath' | 'databaseId' | 'note' | 'isArchived'>
  & { content?: Maybe<(
    { __typename: 'Issue' }
    & Pick<Issue, 'id' | 'title'>
    ) | (
    { __typename: 'PullRequest' }
    & Pick<PullRequest, 'id' | 'title'>
    )> }>;

const title = (card: Card) => {
  switch (card?.content?.__typename) {
    case "Issue":
    case "PullRequest":
      return card.content?.title
    default:
      return card?.note
  }
}


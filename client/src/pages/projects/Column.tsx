import {useSelector} from "react-redux";
import {selectUser} from "@/store/user/userSlice";
import React, {useState} from "react";
import {
  Issue,
  Maybe,
  ProjectCard,
  PullRequest,
  useProjectColumnQuery
} from "@/generated/graphql/graphql";
import {Badge, Card, Grid, Loading, useTheme } from "@geist-ui/react";
import ReactMarkdown from "react-markdown";
import {CreateTaskModal} from "@/pages/projects/Task/CreateTaskModal";

type Props = {
  columnId: string;
};

export const Column: React.FC<Props> = ({
  columnId,
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
  const openModal = (cardId: string) => {
    selectCardId(cardId);
    setModalOpening(true);
  }

  if (loading) {
    return <Loading size="large" />;
  }

  if (data == null || data.node?.__typename !== "ProjectColumn") {
    throw new Error();
  }

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
      <CreateTaskModal open={modalOpening} onClose={closeModal} projectCardId={selectedCardId} />
    </>
  );
}

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


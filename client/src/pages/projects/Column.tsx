import {useSelector} from "react-redux";
import {selectUser} from "@/store/user/userSlice";
import React from "react";
import {
  Issue,
  Maybe,
  ProjectCard,
  PullRequest,
  useProjectColumnQuery
} from "@/generated/graphql/graphql";
import {Badge, Card, Grid, Loading, useTheme } from "@geist-ui/react";
import ReactMarkdown from "react-markdown";

type Props = {
  login: string;
  projectNumber: number;
  cursor: string | null;
};

export const Column: React.FC<Props> = ({
  login,
  projectNumber,
  cursor
}) => {

  const user = useSelector(selectUser);
  const theme = useTheme();
  const { loading, data } = useProjectColumnQuery({
    variables: {
      login,
      projectNumber,
      cursor,
      cardFirst: 100,
    },
    context: {
      headers: {
        Authorization: `Bearer ${user.authenticated ? user.accessToken : ""}`
      }
    }
  });

  const nodes = data?.viewer.organization?.project?.columns.nodes ?? []
  const node = nodes[0] ?? null;

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

  return loading ? (
    <Loading size="large" />
  ) : (
    <Grid.Container gap={1} justify="flex-start" style={{ overflowY: "auto",
      maxHeight: "calc(100vh - (16pt * 10) - 65px)",}}>
      {node == null ? (
        <div>card not exists</div>
      ) : node?.cards.nodes?.map(card => (
        <Grid xs={12} key={card?.id}>
          <Card
            key={card?.id}
            style={{ overflowWrap: "break-word" }}
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


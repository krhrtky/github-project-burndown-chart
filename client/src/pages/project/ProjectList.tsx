import React, { useEffect, useState } from "react";
import { selectUser } from "@/store/store";
import { useSelector } from "react-redux";
import { useOrganizationsLazyQuery } from "@/generated/graphql/graphql";
import { Col, Loading, Row } from "@geist-ui/react";
import { ProjectSelect } from "@/pages/project/ProjectSelect";

export const ProjectList: React.VFC = () => {
  const user = useSelector(selectUser);
  const [currentOrg, setCurrentOrg] = useState<string | null>(null);
  const [fetchOrgs, { loading, data }] = useOrganizationsLazyQuery({
    context: {
      headers: {
        Authorization: `Bearer ${user.authenticated ? user.accessToken : ""}`,
      },
    },
  });

  useEffect(() => {
    fetchOrgs({
      variables: {
        first: 10,
        after: null,
      },
    });
  }, [fetchOrgs]);

  if (!user.authenticated) {
    return null;
  }

  return loading ? (
    <Loading size="large" />
  ) : (
    <div>
      <Row gap={0.8}>
        <Col span={5}>
          <div>
            {data?.viewer.organizations.edges?.map((org) => (
              <div
                key={JSON.stringify(org)}
                role="presentation"
                onClick={() => setCurrentOrg(org?.node?.login ?? "")}
                onKeyDown={() => setCurrentOrg(org?.node?.login ?? "")}
              >
                {org?.node?.name ?? ""}
              </div>
            ))}
          </div>
        </Col>
        <Col span={10}>
          {currentOrg == null ? <div>Select Organization</div> : <ProjectSelect login={currentOrg} user={user} />}
        </Col>
      </Row>
    </div>
  );
};

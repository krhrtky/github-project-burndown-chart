import React, {useEffect, useState} from "react";
import { selectUser } from "@/store/user/userSlice";
import { useSelector } from "react-redux";
import { Redirect } from "react-router-dom";
import {useOrganizationsLazyQuery} from "@/generated/graphql/graphql";
import {Col, Loading, Row} from "@geist-ui/react";
import {ProjectSelect} from "@/pages/project/ProjectSelect";

export const ProjectList = () => {
  const user = useSelector(selectUser);
  const [currentOrg, setCurrentOrg] = useState<string | null>(null);
  const [fetchOrgs, { loading, data } ] = useOrganizationsLazyQuery({
    context: {
      headers: {
        Authorization: `Bearer ${user.authenticated ? user.accessToken : ""}`
      }
    }
  });

  if (!user.authenticated) {
    return <Redirect to="/" />;
  }

  useEffect(() => {
    fetchOrgs({
      variables: {
        first: 10,
        after: null,
      }
    })

  }, []);

  return loading ? (
    <Loading size="large" />
  ) : (
    <div>
      <Row gap={0.8}>
        <Col span={5}>
          <div>
            {data?.viewer.organizations.edges?.map(org => (
              <div key={JSON.stringify(org)} onClick={() => setCurrentOrg(org?.node?.login ?? "")}>{
                org?.node?.name ?? ""
              }</div>
            ))}
          </div>
        </Col>
        <Col span={10}>
          {currentOrg == null ? (
            <div>Select Organization</div>
          ) : (
            <ProjectSelect login={currentOrg} user={user} />
          )}
        </Col>
      </Row>
    </div>
  )
}



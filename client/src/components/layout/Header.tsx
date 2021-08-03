import React from "react";
import { Button, Col, Row } from "@geist-ui/react";
import { useAuth } from "@/libs/firebase/auth";

export const Header: React.VFC = () => {
  const { loading, user, signOut } = useAuth();
  return (
    <Row gap={0}>
      <Col span={22}>
        <h2>GitHub Project Burndown</h2>
      </Col>
      <Col span={2} style={{ display: "flex", alignItems: "center" }}>
        {user.authenticated ? (
          <Button loading={loading} onClick={signOut} size="small" auto>
            Logout
          </Button>
        ) : null}
      </Col>
    </Row>
  );
};

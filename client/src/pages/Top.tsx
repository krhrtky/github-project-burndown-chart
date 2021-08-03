import React from "react";
import { Button, Loading, Grid } from "@geist-ui/react";
import { push } from "connected-react-router";
import { useDispatch } from "react-redux";
import { useAuth } from "@/libs/firebase/auth";
import { Github } from "@geist-ui/react-icons";

export const Top: React.VFC = () => {
  const dispatch = useDispatch();
  const { user, signIn, loading } = useAuth();

  if (user.authenticated) {
    dispatch(push("/project/select"));
  }

  return (
    <Grid.Container justify="center" alignItems="center" style={{ height: "70vh" }}>
      <Grid xs />
      <Grid xs>
        {loading ? (
          <Loading size="large" />
        ) : (
          <Button icon={<Github />} onClick={signIn} type="secondary">
            Login
          </Button>
        )}
      </Grid>
      <Grid xs />
    </Grid.Container>
  );
};

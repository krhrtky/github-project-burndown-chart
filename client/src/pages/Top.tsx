import React from "react";
import {useAuth} from "@/libs/firebase/auth";
import {Link} from "react-router-dom";

export const Top = () => {
  const { user, signIn, signOut, loading } = useAuth();

  if (loading) {
    return <div>loading...</div>
  }

  return (
    <>
      <h2>Hello, Everyone.</h2>
      <p>This is a simulated page, you can click anywhere to close it.</p>
      <Link to="/projects">projects</Link>
      {
        user.authenticated ? (
          <button onClick={signOut}>signOut</button>
        ) : (
          <button onClick={signIn}>signIn</button>
        )
      }
    </>
  );
}

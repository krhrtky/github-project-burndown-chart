import React from "react";
import { selectUser } from "@/store/user/userSlice";
import { useSelector } from "react-redux";
import { Redirect } from "react-router-dom";

export const ProjectList = () => {
  const user = useSelector(selectUser)
  return user.authenticated ? (
    <div>Project List</div>
  ) : (
    <Redirect to="/" />
  )
}



import React, {useCallback, useEffect, useState} from "react";
import { useProjectQuery } from "@/generated/graphql/graphql";
import {Authenticated} from "@/store/user/userSlice";
import {Spinner} from "@geist-ui/react";
import { getApp } from "@firebase/app";
import { getFirestore, collection, where, onSnapshot, query } from "@firebase/firestore";
import { useDispatch } from "react-redux";
import  { push } from "connected-react-router";
import { Configuration, ProjectControllerApi } from "@/generated/openapi";

type Props = {
  login: string;
  user: Authenticated;
};

export const ProjectSelect: React.FC<Props> = ({ login, user }) => {
  const [projects, setProjects] = useState<Array<{
    projectNumber: number;
    id: string;
  }>>([]);
  const {loading, data} = useProjectQuery({
    variables: {
      login,
      first: 10,
    },
    context: {
      headers: {
        Authorization: `Bearer ${user.accessToken}`
      }
    }
  });
  const dispatch = useDispatch();
  const goToProjectDetail = useCallback(async (projectNumber: number) => {
    const maybeProject = projects.find(project => project.projectNumber === projectNumber)
    const api = new ProjectControllerApi(new Configuration({
      headers: {
        Authorization: `Bearer ${user.authenticated ? user.token : ""}`
      }
    }));
    const projectId = maybeProject == null
      ? await api
        .create({ createProjectRequestBody: {
            organization: login,
            projectNumber
          }})
        .then(resp => resp.projectId)
      :  maybeProject.id

    dispatch(push(`/projects/${projectId}`));
  }, [user.token]);

  useEffect(() => {
    const unsubscribe = onSnapshot<{projectNumber: number}>(
      query(collection(getFirestore(getApp()), "project"), where("organization", "==", login)),
      querySnapshot => {
        const registeredProjectNumbers = querySnapshot.docs.map(documentSnapshot => {
          const { projectNumber } = documentSnapshot.data();
          return {
            id: documentSnapshot.id,
            projectNumber,
          };
        });
        setProjects(registeredProjectNumbers);
      });

    return () => unsubscribe();

  }, []);

  return loading ? (
    <Spinner size="large" />
  ) : (
    <div>
      {data?.organization?.projects.edges?.map((project => (
        <div
          key={JSON.stringify(project)}
          onClick={
            () => goToProjectDetail(project?.node?.number ?? 0)
          }
        >
          {project?.node?.name}
        </div>
      )))}
    </div>
  )
}

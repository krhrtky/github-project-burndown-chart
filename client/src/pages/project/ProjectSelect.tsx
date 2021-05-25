import React, {useEffect, useState} from "react";
import { useProjectsQuery } from "@/generated/graphql/graphql";
import {Authenticated} from "@/store/user/userSlice";
import {Spinner} from "@geist-ui/react";
import { getApp } from "@firebase/app";
import { getFirestore, collection, where, onSnapshot, query } from "@firebase/firestore";
import { useDispatch } from "react-redux";
import  { push } from "connected-react-router";
import {ProjectControllerService} from "@/generated/openapi";

type Props = {
  login: string;
  user: Authenticated;
};

export const ProjectSelect: React.FC<Props> = ({ login, user }) => {
  const [projects, setProjects] = useState<Array<{
    projectNumber: number;
    id: string;
  }>>([]);
  const {loading, data} = useProjectsQuery({
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
  const goToProjectDetail = async (projectNumber: number) => {
    const maybeProject = projects.find(project => project.projectNumber === projectNumber)
    const projectId = maybeProject == null
      ? await ProjectControllerService
        .create({organization: login, projectNumber})
        .then(resp => resp.projectId as string)
      :  maybeProject.id

    dispatch(push(`/projects/${projectId}`));
  }

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

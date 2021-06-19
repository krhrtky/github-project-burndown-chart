import React, {useEffect, useState} from "react";
import {
  Redirect,
  useParams
} from "react-router-dom";
import { useSelector } from "react-redux";
import { selectUser } from "@/store/user/userSlice";
import { Loading } from "@geist-ui/react";
import { ProjectControllerService } from "@/generated/openapi";
import { Bar } from "react-chartjs-2";

type Project = {
  organization: string;
  projectNumber: number;
  tasks: Array<string>;
};

export const Burndown: React.VFC = () => {
  const user = useSelector(selectUser);
  const { projectId } = useParams<{ projectId: string; }>();
  const [charts, setCharts] = useState({
    dateRange: [],
    ideal: [],
    finished: [],
    estimate: [],
    result: [],
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    ProjectControllerService
      .burndown(projectId)
      .then(res => setCharts(res))
      .finally(() => setLoading(false));

  }, []);

  if (!user.authenticated) {
    return <Redirect to="/" />;
  }

  return loading ? (
    <Loading size="large" />
  ) : (
    <Bar type="line" data={{
      labels: charts.dateRange,
      datasets: [
        {
          type: 'line',
          label: 'ideal',
          borderColor: 'rgb(54, 162, 235)',
          borderWidth: 2,
          fill: false,
          data: charts.ideal
        },
        {
          type: 'line',
          label: 'result',
          borderColor: 'rgb(255, 99, 132)',
          data: charts.finished,
          borderWidth: 2,
        },
        {
          type: 'bar',
          label: 'total',
          backgroundColor: 'rgb(75, 192, 192)',
          data: charts.estimate,
        },
        {
          type: 'bar',
          label: 'total result',
          backgroundColor: 'rgb(0, 203, 50)',
          data: charts.result,
        },
      ]
    }} />
  )
}
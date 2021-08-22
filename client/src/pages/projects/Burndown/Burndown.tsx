import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { selectUser } from "@/store/store";
import { Button, Loading, Popover, Grid, Description } from "@geist-ui/react";
import { Configuration, ProjectControllerApi } from "@/generated/openapi";
import { Bar } from "react-chartjs-2";
import { subDays, format } from "date-fns";
import "react-date-range/dist/styles.css";
import "react-date-range/dist/theme/default.css";
import { DateRangePicker } from "react-date-range";
import { Calendar } from "@geist-ui/react-icons";
import { format as formatWithTZ } from "date-fns-tz";

const formatDate = (date: Date) => format(date, "yyyy/MM/dd");
const f = (date: Date) => formatWithTZ(date, "yyyy/MM/dd", { timeZone: "Asia/Tokyo" });

export const Burndown: React.VFC = () => {
  const user = useSelector(selectUser);
  const { projectId } = useParams<{ projectId: string }>();
  const [charts, setCharts] = useState<{
    result: Array<number>;
    ideal: Array<number>;
    dateRange: Array<string>;
    estimate: Array<number>;
    finished: Array<number>;
  }>({
    dateRange: [],
    ideal: [],
    finished: [],
    estimate: [],
    result: [],
  });
  const [loading, setLoading] = useState(false);
  const [dateRange, setDateRange] = useState([
    {
      startDate: subDays(new Date(), 7),
      endDate: new Date(),
      key: "selection",
    },
  ]);

  useEffect(() => {
    setLoading(true);
    const api = new ProjectControllerApi(
      new Configuration({
        headers: {
          Authorization: `Bearer ${user.authenticated ? user.token : ""}`,
        },
      })
    );

    api
      .burndown({ projectId, from: dateRange[0].startDate, to: dateRange[0].endDate })
      .then((res) => {
        setCharts({
          ...res,
          dateRange: res.dateRange.map(f),
        });
      })
      .catch((e: Error) => e.message)
      .finally(() => setLoading(false));
  }, [projectId, user.authenticated, dateRange]);

  if (!user.authenticated) {
    return null;
  }

  return loading ? (
    <Loading size="large" />
  ) : (
    <div>
      <Grid.Container>
        <Grid xs justify="flex-start">
          <Description
            title="Section date range"
            content={`${formatDate(dateRange[0].startDate)} ~ ${formatDate(dateRange[0].endDate)}`}
          />
        </Grid>
        <Grid xs justify="flex-end">
          <Popover
            placement="bottomEnd"
            content={
              <DateRangePicker
                // NOTE: OnChangeProps で type を絞り込めない
                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                // @ts-ignore
                onChange={(range) => setDateRange([range.selection])}
                showSelectionPreview
                moveRangeOnFirstSelection={false}
                months={2}
                ranges={dateRange}
                maxDate={new Date()}
                direction="horizontal"
              />
            }
          >
            <Button iconRight={<Calendar />} auto size="small" />
          </Popover>
        </Grid>
      </Grid.Container>
      <Bar
        type="line"
        data={{
          labels: charts.dateRange,
          datasets: [
            {
              type: "line",
              label: "ideal",
              borderColor: "rgb(54, 162, 235)",
              borderWidth: 2,
              fill: false,
              data: charts.ideal,
            },
            {
              type: "line",
              label: "result",
              borderColor: "rgb(255, 99, 132)",
              data: charts.finished,
              borderWidth: 2,
            },
            {
              type: "bar",
              label: "total",
              backgroundColor: "rgb(75, 192, 192)",
              data: charts.estimate,
            },
            {
              type: "bar",
              label: "total result",
              backgroundColor: "rgb(0, 203, 50)",
              data: charts.result,
            },
          ],
        }}
      />
    </div>
  );
};

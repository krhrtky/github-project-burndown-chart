import React, {useMemo, useState} from "react";
import {Input, Modal, Text, useToasts} from "@geist-ui/react";
import {useFormik} from "formik";
import { Configuration, TaskControllerApi } from "@/generated/openapi";
import {useSelector} from "react-redux";
import {selectUser} from "@/store/user/userSlice";

type Props = {
  open: boolean;
  onClose: () => void;
  taskId: string;
};

export const FinishTaskModal: React.FC<Props> = ({ open, onClose, taskId }) => {
  const user = useSelector(selectUser);
  const close = () => {
    resetForm();
    onClose();
  };
  const [loading, setLoading] = useState(false);
  const [, setToast] = useToasts();
  const toastSuccess = () => setToast({
    text: "Task finish!!",
    type: "success",
  })
  const toastFail = () => setToast({
    text: "Fail update task..",
    type: "error",
  });
  const { values, handleSubmit, handleChange, resetForm, setValues, setFieldValue } = useFormik({
    initialValues: {
      resultStoryPoint: "",
      finishedAt: new Date(),
    },
    onSubmit: async values => {
      const api = new TaskControllerApi(new Configuration({
        headers: {
          Authorization: `Bearer ${user.authenticated ? user.token : ""}`
        }
      }));
      try {
        setLoading(true);
        await api.finish({
          taskId,
          finishTaskRequestBody: {
            resultStoryPoint: Number(values.resultStoryPoint),
            finishedAt: values.finishedAt,
          }
        });
        toastSuccess()
      } catch (e) {
        toastFail();
      } finally {
        setLoading(false);
        close();
      }
    }
  });

  const formattedDate = useMemo(() => formatDate(values.finishedAt), [values.finishedAt]);

  return (
    <Modal open={open} onClose={close} disableBackdropClick={loading}>
      <Modal.Title>Finish task</Modal.Title>
      <Modal.Content>
        <Text span b>result story point</Text>
        <Input
          placeholder="Input fibonacci number"
          name="resultStoryPoint"
          width="100%"
          value={values.resultStoryPoint}
          onChange={handleChange}
        />
        <Text span b>finished at</Text>
        <Input
          placeholder="Input fibonacci number"
          name="finishedAt"
          width="100%"
          type="date"
          value={formattedDate}
          onChange={(e => setFieldValue ("finishedAt", new Date(e.target.value)))}
        />
      </Modal.Content>
      <Modal.Action passive onClick={close} disabled={loading}>Cancel</Modal.Action>
      <Modal.Action onClick={() => handleSubmit()} loading={loading}>Finish</Modal.Action>
    </Modal>
  );
}

const formatDate = (date: Date): string => {
  const toDoubleDigits = (num: number): string => {
    const toString = num.toString();
    return toString.length === 1 ? `0${toString}` : toString;
  };
  const yyyy = date.getFullYear();
  const mm = toDoubleDigits(date.getMonth() + 1);
  const dd = toDoubleDigits(date.getDate());
  return [yyyy, mm, dd].join("-");
}

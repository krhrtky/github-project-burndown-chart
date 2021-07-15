import React, { useState } from "react";
import { Input, Modal, useToasts } from "@geist-ui/react";
import { useParams } from "react-router-dom";
import { useFormik } from "formik";
import { Configuration, TaskControllerApi } from "@/generated/openapi";
import { useSelector } from "react-redux";
import { selectUser } from "@/store/store";

type Props = {
  open: boolean;
  onClose: () => void;
  projectCardId: string;
};

export const CreateTaskModal: React.FC<Props> = ({ open, onClose, projectCardId }) => {
  const user = useSelector(selectUser);
  const { projectId } = useParams<{ projectId: string }>();
  const [loading, setLoading] = useState(false);
  const [, setToast] = useToasts();
  const toastSuccess = () =>
    setToast({
      text: "Task created !",
      type: "success",
    });
  const toastFail = () =>
    setToast({
      text: "Fail create task..",
      type: "error",
    });
  const { values, handleSubmit, handleChange, resetForm } = useFormik<{ estimateStoryPoint?: string }>({
    initialValues: {
      estimateStoryPoint: "",
    },
    onSubmit: async (inputValues) => {
      setLoading(true);
      const api = new TaskControllerApi(
        new Configuration({
          headers: {
            Authorization: `Bearer ${user.authenticated ? user.token : ""}`,
          },
        })
      );
      try {
        await api.create1({
          projectId,
          createTaskRequestBody: {
            projectCardId,
            estimateStoryPoint: Number(inputValues.estimateStoryPoint),
          },
        });
        toastSuccess();
      } catch (_) {
        toastFail();
      } finally {
        setLoading(false);
        resetForm();
        onClose();
      }
    },
  });
  const close = () => {
    resetForm();
    onClose();
  };

  return (
    <Modal open={open} onClose={close} disableBackdropClick={loading}>
      <Modal.Title>Register task</Modal.Title>
      <Modal.Content>
        <Input
          placeholder="Input fibonacci number"
          name="estimateStoryPoint"
          width="100%"
          value={values.estimateStoryPoint}
          onChange={handleChange}
        />
      </Modal.Content>
      <Modal.Action passive onClick={close} disabled={loading}>
        Cancel
      </Modal.Action>
      <Modal.Action onClick={() => handleSubmit()} loading={loading}>
        Register
      </Modal.Action>
    </Modal>
  );
};

import React from "react";
import {Input, Modal} from "@geist-ui/react";
import {useParams} from "react-router-dom";
import {useFormik} from "formik";
import {TaskControllerService} from "@/generated/openapi";

type Props = {
  open: boolean;
  onClose: () => void;
  projectCardId: string;
};

export const CreateTaskModal: React.FC<Props> = ({ open, onClose, projectCardId }) => {
  const { projectId } = useParams<{ projectId: string; }>();
  const { values, handleSubmit, handleChange } = useFormik<{estimateStoryPoint?: string }>({
    initialValues: {
      estimateStoryPoint: undefined,
    },
    onSubmit: async values => {
      await TaskControllerService
        .create1(
          projectId,
          {
            projectCardId: Number(projectCardId),
            estimateStoryPoint: Number(values.estimateStoryPoint)
          });
    }
  });
  return (
    <Modal open={open} onClose={onClose}>
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
      <Modal.Action passive onClick={onClose}>Cancel</Modal.Action>
      <Modal.Action onClick={() => handleSubmit()}>Register</Modal.Action>
    </Modal>
  );
}

import React, {useState} from "react";
import {Input, Modal, useToasts} from "@geist-ui/react";
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
  const [loading, setLoading] = useState(false);
  const [, setToast] = useToasts();
  const toastSuccess = () => setToast({
    text: "Task created !",
    type: "success",
  })
  const { values, handleSubmit, handleChange } = useFormik<{estimateStoryPoint?: string }>({
    initialValues: {
      estimateStoryPoint: undefined,
    },
    onSubmit: async values => {
      setLoading(true);
      try {
        await TaskControllerService
          .create1(
            projectId,
            {
              projectCardId,
              estimateStoryPoint: Number(values.estimateStoryPoint)
            });
        toastSuccess();
      } finally {
        setLoading(false);
        onClose();
      }
    }
  });
  return (
    <Modal open={open} onClose={onClose} disableBackdropClick={loading}>
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
      <Modal.Action passive onClick={onClose} disabled={loading}>Cancel</Modal.Action>
      <Modal.Action onClick={() => handleSubmit()} loading={loading}>Register</Modal.Action>
    </Modal>
  );
}

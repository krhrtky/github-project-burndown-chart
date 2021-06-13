import React from "react";
import { Col, Modal, Row } from "@geist-ui/react";
import {FinishedTask} from "@/pages/projects/Task/type";

type Props = {
  open: boolean;
  onClose: () => void;
  task: FinishedTask
};

export const FinishedTaskModal: React.FC<Props> = ({ open, onClose, task }) => {
  const close = () => {
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Modal.Title>Finish task</Modal.Title>
      <Modal.Content>
        <Row gap={.8}>
          <Col>Estimate</Col>
          <Col>{task.estimateStoryPoint}</Col>
        </Row>
        <Row gap={.8}>
          <Col>Result</Col>
          <Col>{task.resultStoryPoint}</Col>
        </Row>
        <Row gap={.8}>
          <Col>Finished at</Col>
          <Col>{formatDate(task.finishedAt.toDate())}</Col>
        </Row>
      </Modal.Content>
      <Modal.Action passive onClick={close}>Close</Modal.Action>
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

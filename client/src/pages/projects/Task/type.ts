import type { Timestamp } from "@firebase/firestore";

export type UnFinishedTask = {
  id: string;
  estimateStoryPoint: number;
  finishedAt: Timestamp;
  projectCardId: null;
  resultStoryPoint: null;
};

export type FinishedTask = {
  id: string;
  estimateStoryPoint: number;
  finishedAt: Timestamp;
  projectCardId: string;
  resultStoryPoint: number;
};

export type Task = UnFinishedTask | FinishedTask;

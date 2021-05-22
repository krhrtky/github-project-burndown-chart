import { getApps, initializeApp } from "@firebase/app";
import { firebaseConfig } from "@/libs/env";

const initializeFirebase = () => {
  if (getApps().length == 0) {
    initializeApp(firebaseConfig);
  }
}

export {
  initializeFirebase,
};

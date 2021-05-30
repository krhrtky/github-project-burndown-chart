import { getApps, initializeApp } from "@firebase/app";
import { initializePerformance } from "@firebase/performance";
import { firebaseConfig } from "@/libs/env";

const initializeFirebase = () => {
  if (getApps().length == 0) {
    const app = initializeApp(firebaseConfig);
    initializePerformance(app);
  }
}

export {
  initializeFirebase,
};

import { getApps, initializeApp } from "@firebase/app";
import { initializePerformance } from "@firebase/performance";
import { getAnalytics } from "@firebase/analytics";
import { firebaseConfig } from "@/libs/env";

const initializeFirebase = () => {
  if (getApps().length == 0) {
    const app = initializeApp(firebaseConfig);
    initializePerformance(app);
    getAnalytics();
  }
}

export {
  initializeFirebase,
};

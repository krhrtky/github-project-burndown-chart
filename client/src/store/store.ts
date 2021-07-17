import { configureStore, combineReducers } from "@reduxjs/toolkit";
import { connectRouter, routerMiddleware } from "connected-react-router";
import { createBrowserHistory } from "history";
import { persistReducer, persistStore, FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER } from "redux-persist";
import storage from "redux-persist/es/storage";
import { User, userReducer } from "./user/userSlice";

export const history = createBrowserHistory();

export type RootState = ReturnType<typeof store.getState>;

const persistConfig = {
  key: "root",
  whitelist: ["user", "router"],
  storage,
};

const persistedReducer = persistReducer(
  persistConfig,
  combineReducers({
    user: userReducer,
    router: connectRouter(history),
  })
);

export const store = configureStore({
  reducer: persistedReducer,
  devTools: true,
  middleware(getDefaultMiddleware) {
    return getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
      },
    }).concat(routerMiddleware(history));
  },
});

export const persistor = persistStore(store);
export const selectUser = (rootState: RootState): User => rootState.user;

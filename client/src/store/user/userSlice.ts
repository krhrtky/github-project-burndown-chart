import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { Omit } from "react-redux";

export type User = Authenticated | UnAuthenticated;

export type Authenticated = {
  uid: string;
  email: string | null;
  name: string | null;
  photoUrl: string | null;
  token: string;
  accessToken: string;
  expirationTime: string;
  authenticated: true;
};

export type UnAuthenticated = {
  authenticated: false;
};

const initialState = {
  authenticated: false,
} as User;

export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    signIn: (_, action: PayloadAction<Omit<Authenticated, "authenticated">>) => ({
      ...action.payload,
      authenticated: true as const,
    }),
    signOut: () => ({
      authenticated: false as const,
    }),
    refreshToken: (state, action: PayloadAction<Pick<Authenticated, "token" | "expirationTime">>) => ({
      ...state,
      ...action.payload,
    }),
  },
});

export const userReducer = userSlice.reducer;
export const { signIn, signOut, refreshToken } = userSlice.actions;

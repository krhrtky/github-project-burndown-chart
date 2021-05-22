import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '../store';
import {Omit} from "react-redux";

export type User = Authenticated | UnAuthenticated;

type Authenticated = {
  uid: string;
  email: string | null;
  name: string | null;
  photoUrl: string | null;
  token: string;
  accessToken: string;
  expirationTime: string;
  authenticated: true;
};

type UnAuthenticated = {
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
    })
  },
});

export const userReducer = userSlice.reducer;
export const { signIn, signOut } = userSlice.actions;
export const selectUser = (state: RootState) => state.user;

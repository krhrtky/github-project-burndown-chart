import React, { useState, useEffect, useContext, createContext } from 'react';
import { signInWithPopup, GithubAuthProvider, getAuth, User } from "@firebase/auth";
import {
  signIn as signInAction,
  signOut as signOutAction,
  selectUser,
  refreshToken
} from "@/store/user/userSlice";
import {
  useDispatch,
  useSelector
} from "react-redux";
import { push } from "connected-react-router";

// @ts-ignore
const authContext = createContext<ReturnType<typeof useFirebaseAuth>>(null);

export const AuthProvider = ({ children }) => {
  const firebaseAuth = useFirebaseAuth();
  return <authContext.Provider value={firebaseAuth}>{children}</authContext.Provider>;
}

export const useAuth = () => {
  return useContext(authContext);
};

const useFirebaseAuth = () => {
  const auth = getAuth();
  const user = useSelector(selectUser);
  const dispatch = useDispatch();
  const [loading, setLoading] = useState(false);

  const signIn = async () => {
    setLoading(true);
    const provider = new GithubAuthProvider();
    provider.addScope("repo")
    provider.addScope("read:org")
    provider.addScope("user")

    const result = await signInWithPopup(auth, provider);

    if (result) {
      const user = result.user;
      const credential = GithubAuthProvider.credentialFromResult(result);
      dispatch(
        signInAction({
          ...await formatUser(user),
          accessToken: (credential?.accessToken == null) ? "" : credential.accessToken,
        })
      );
    }

    setLoading(false);
  };

  const signOut = () => auth.signOut();

  useEffect(() => {
    const unsubscribe = auth
      .onAuthStateChanged(async maybeUser => {
        if (maybeUser == null) {
          dispatch(signOutAction());
          dispatch(push("/"));
        }
      });
    return () => unsubscribe();
  }, []);

  useEffect(() => {

    window.addEventListener("focus", () => {
      const currentUser = auth.currentUser;
      if (currentUser == null || !user.authenticated) {
        return;
      }
      const current = new Date();
      const expirationTime = new Date(user.expirationTime);
      if (current.getTime() > expirationTime.getTime()) {
        return;
      }
      signOut();
    });

    if (!user.authenticated) {
      dispatch(push("/"));
      return;
    }
    const current = new Date();
    const expirationTime = new Date(user.expirationTime);
    if (current.getTime() > expirationTime.getTime()) {
      signOut();
    }

  }, [user]);

  useEffect(() => {
    const id = setInterval(() => {
      const currentUser = auth.currentUser;
      if (currentUser == null || !user.authenticated) {
        return;
      }
      const current = new Date();
      const expirationTime = new Date(user.expirationTime);
      if (current.getTime() > expirationTime.getTime()) {
        return;
      }

      currentUser
        .getIdTokenResult(true)
        .then(refreshedToken => dispatch(refreshToken(refreshedToken)));
    }, 100000);

    return () => clearInterval(id);

  }, []);

  return {
    loading,
    user,
    signIn,
    signOut,
  };
}

const formatUser = async (user: User) => {
  const { token, expirationTime } = await user.getIdTokenResult(true);

  return {
    uid: user.uid,
    email: user.email,
    name: user.displayName,
    provider: user.providerData[0].providerId,
    photoUrl: user.photoURL,
    token,
    expirationTime,
  };
};

import React, { useState, useEffect, useContext, createContext } from 'react';
import Router from 'next/router';
import { firebase } from './Firebase';

type Await<T> = T extends Promise<infer PT> ? PT : never;

const authContext = createContext<ReturnType<typeof useFirebaseAuth> | null>(null);

export const AuthProvider = ({ children }) => {
    const auth = useFirebaseAuth();
    return <authContext.Provider value={auth}>{children}</authContext.Provider>;
}

export const useAuth = () => {
    return useContext(authContext);
};

const useFirebaseAuth = () => {
    const [user, setUser] = useState<Await<ReturnType<typeof formatUser>>>(null);
    const [loading, setLoading] = useState(true);

    const handleUser = async (rawUser?: firebase.User) => {
        if (rawUser != null) {
            const user = await formatUser(rawUser);
            const { token, ...userWithoutToken } = user;

            setUser(user);

            setLoading(false);
            return user;
        } else {
            setUser(null);
            setLoading(false);
            return null;
        }
    };

    const signInWithGitHub = (redirect) => {
        setLoading(true);
        return firebase
          .auth()
          .signInWithPopup(new firebase.auth.GithubAuthProvider())
          .then(async (response) => {
              await handleUser(response.user);

              if (redirect) {
                  await Router.push(redirect);
              }
          });
    };

    const signOut = () => {
        return firebase
          .auth()
          .signOut()
          .then(() => handleUser(null));
    };

    useEffect(() => {
        const unsubscribe = firebase.auth().onIdTokenChanged(handleUser);
        return () => unsubscribe();
    }, []);

    const getFreshToken = async () => {
        const currentUser = firebase.auth().currentUser;
        if (currentUser) {
            const token = await currentUser.getIdToken(false);
            return `${token}`;
        } else {
            return '';
        }
    };

    return {
        user,
        loading,
        signInWithGitHub,
        signOut: signOut,
        getFreshToken,
    };
}

const formatUser = async (user: firebase.User) => {
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

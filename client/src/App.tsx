import React from "react";
import { render } from "react-dom";
import { initializeFirebase} from "@/libs/firebase/firebase";
import { AuthProvider } from "@/libs/firebase/auth";
import { Provider as ReduxProvider } from "react-redux";
import { store } from "@/store/store";
import { ApolloClient, ApolloProvider, InMemoryCache } from "@apollo/client";

initializeFirebase();
const cache = new InMemoryCache();
const client = new ApolloClient({
  uri: "https://api.github.com/graphql",
  cache,
});

const App = () => {
  return (
    <ReduxProvider store={store}>
      <ApolloProvider client={client}>
        <AuthProvider >
          <div>Hello World!</div>
        </AuthProvider>
      </ApolloProvider>
    </ReduxProvider>
  )
}

render(<App/>, document.getElementById("app"));

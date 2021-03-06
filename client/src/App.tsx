import React from "react";
import { render } from "react-dom";
import { initializeFirebase } from "@/libs/firebase/firebase";
import { AuthProvider } from "@/libs/firebase/auth";
import { Provider as ReduxProvider } from "react-redux";
import { store, history, persistor } from "@/store/store";
import { ApolloClient, ApolloProvider, InMemoryCache } from "@apollo/client";
import { CssBaseline, Divider, GeistProvider, Page } from "@geist-ui/react";
import { Route, Switch } from "react-router-dom";
import { Top } from "@/pages/Top";
import { ConnectedRouter } from "connected-react-router";
import { ProjectRoot } from "@/pages/project/ProjectRoot";
import { PersistGate } from "redux-persist/integration/react";
import { ProjectsRoot } from "@/pages/projects/ProjectsRoute";
import { Header } from "@/components/layout/Header";

initializeFirebase();
const cache = new InMemoryCache();
const client = new ApolloClient({
  uri: "https://api.github.com/graphql",
  cache,
});

const App = () => {
  return (
    <ReduxProvider store={store}>
      <PersistGate persistor={persistor}>
        <ApolloProvider client={client}>
          <AuthProvider>
            <GeistProvider>
              <CssBaseline />
              <Page size="medium">
                <Page.Header>
                  <Header />
                </Page.Header>
                <Divider y={0} />
                <Page.Content style={{ padding: "16pt 0", overflowY: "hidden" }}>
                  <ConnectedRouter history={history}>
                    <Switch>
                      <Route exact path="/">
                        <Top />
                      </Route>
                      <Route path="/project">
                        <ProjectRoot />
                      </Route>
                      <ProjectsRoot />
                    </Switch>
                  </ConnectedRouter>
                </Page.Content>
              </Page>
            </GeistProvider>
          </AuthProvider>
        </ApolloProvider>
      </PersistGate>
    </ReduxProvider>
  );
};

render(<App />, document.getElementById("app"));

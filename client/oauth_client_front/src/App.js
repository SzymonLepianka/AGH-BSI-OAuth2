import "./App.css";
import React, { useEffect, useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { LoginPage } from "./pages/LoginPage";
import { getSessionCookie } from "./middleware/session";

export const SessionContext = React.createContext(getSessionCookie());

function App() {
  const [session, setSession] = useState(getSessionCookie());
  useEffect(() => {
    setSession(getSessionCookie());
  }, [session]);

  return (
    <div className="App">
      <BrowserRouter>
        <SessionContext.Provider value={[session, setSession]}>
          <Routes>
            <Route path="login" element={<LoginPage />} />
          </Routes>
        </SessionContext.Provider>
      </BrowserRouter>
    </div>
  );
}

export default App;

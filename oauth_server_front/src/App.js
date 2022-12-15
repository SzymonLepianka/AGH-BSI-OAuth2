import "./App.css";
import React, { useContext, useEffect, useState } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { OauthLoginPage } from "./pages/OauthLoginPage.jsx";
import { LoginPage } from "./pages/LoginPage.jsx";
import { HomePage } from "./pages/HomePage.jsx";
import { LoginSuccessPage } from "./pages/LoginSuccessPage";
import { AlreadyLoggedPage } from "./pages/AlreadyLoggedPage";
import { getSessionCookie } from "./middleware/session";

export const SessionContext = React.createContext(getSessionCookie());

const ProtectedRoute = ({ element }) => {
  const [session] = useContext(SessionContext);
  return session ? element() : <Navigate to="/login" />;
};

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
            {/* oauth login functionality */}
            <Route path="/login/:clientID" element={<OauthLoginPage />} />
            <Route path="/login-success" element={<LoginSuccessPage />} />
            <Route path="/already-logged" element={<AlreadyLoggedPage />} />

            {/* app functionalities */}
            <Route path="/" element={<ProtectedRoute element={HomePage} />} />
            <Route path="/login" element={<LoginPage />} />
          </Routes>
        </SessionContext.Provider>
      </BrowserRouter>
    </div>
  );
}

export default App;

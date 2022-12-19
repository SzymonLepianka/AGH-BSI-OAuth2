import "./App.css";
import React, { useContext, useEffect, useState } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { LoginPage } from "./pages/LoginPage";
import { getSessionCookie } from "./middleware/session";
import { HomePage } from "./pages/HomePage";

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
            <Route path="/" element={<ProtectedRoute element={HomePage} />} />
            <Route path="login" element={<LoginPage />} />
          </Routes>
        </SessionContext.Provider>
      </BrowserRouter>
    </div>
  );
}

export default App;

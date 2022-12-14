import "./App.css";
import React, { useContext, useState } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { HomePage } from "./pages/HomePage.jsx";
import { LoginSuccessPage } from "./pages/LoginSuccessPage";

export const TokenContext = React.createContext(null);

const ProtectedRoute = ({ element }) => {
  const [token] = useContext(TokenContext);
  return token ? element() : <Navigate to="/login" />;
};

function App() {
  const [token, setToken] = useState(null);

  return (
    <div className="App">
      <BrowserRouter>
        <TokenContext.Provider value={[token, setToken]}>
          <Routes>
            {/* <Route path="/" element={<ProtectedRoute element={HomePage} />} /> */}
            <Route path="/login/:clientID" element={<HomePage />} />
            <Route path="/login-success" element={<LoginSuccessPage />} />
          </Routes>
        </TokenContext.Provider>
      </BrowserRouter>
    </div>
  );
}

export default App;
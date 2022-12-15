import React, { useContext, useEffect, useState } from "react";
import loginRequest from "../api/loginRequest";
import { useNavigate, useParams } from "react-router-dom";
import accessTokenRequest from "../api/accessTokenRequest";
import { SessionContext } from "../App";
import { getSessionCookie } from "../middleware/session";

export const LoginPage = () => {
  const { clientID } = useParams();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [session, setSession] = useContext(SessionContext);
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    loginRequest(username, password, clientID)
      .then(() => {
        accessTokenRequest().then(() => {
          setSession(getSessionCookie());
          navigate("/");
        });
      })
      .catch((err) => {
        setError(err.message);
      });
  };

  useEffect(() => {
    if (session) {
      navigate("/");
    }
  });

  return (
    <div>
      <h1>OAuth-server-front-login-page</h1>
      <div style={{ color: "red" }}>{error}</div>
      <form onSubmit={handleLogin}>
        {`Username: `}
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <br></br>
        {`Password: `}
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <br></br>
        <button>Login</button>
      </form>
    </div>
  );
};

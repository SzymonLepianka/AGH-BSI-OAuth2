import React, { useState } from "react";
import oauthLoginRequest from "../api/oauthLoginRequest";
import { useNavigate, useParams } from "react-router-dom";

export const OauthLoginPage = () => {
  const { clientID } = useParams();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const timeout = (delay) => {
    return new Promise((res) => setTimeout(res, delay));
  };

  const handleOauthLogin = (e) => {
    e.preventDefault();
    oauthLoginRequest(username, password, clientID)
      .then(() => {
        navigate("/login-success");
        timeout(1500).then(() => {
          window.close();
        });
      })
      .catch((err) => {
        setError(err.message);
      });
  };

  return (
    <div>
      <h1>OAuth-server-front-oauth-login-page</h1>
      <div style={{ color: "red" }}>{error}</div>
      <form onSubmit={handleOauthLogin}>
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

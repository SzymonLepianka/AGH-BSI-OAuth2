import React, { useState } from "react";
import loginRequest from "../api/loginRequest";
import { useNavigate, useParams } from "react-router-dom";
import accessTokenRequest from "../api/accessTokenRequest";

export const LoginPage = () => {
  const { clientID } = useParams();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleOauthLogin = (e) => {
    e.preventDefault();
    loginRequest(username, password, clientID)
      .then(() => {
        accessTokenRequest().then(() => {
          navigate("/");
        });
      })
      .catch((err) => {
        setError(err.message);
      });
  };

  return (
    <div>
      <h1>OAuth-server-front-login-page</h1>
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

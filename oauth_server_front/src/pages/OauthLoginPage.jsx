import React, { useContext, useEffect, useState } from "react";
import oauthLoginRequest from "../api/oauthLoginRequest";
import { useNavigate, useParams } from "react-router-dom";
import { SessionContext } from "../App";
import oauthAlreadyLoggedRequest from "../api/oauthAlreadyLoggedRequest";
import Cookies from "js-cookie";

export const OauthLoginPage = () => {
  const { clientID } = useParams();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [session, setSession] = useContext(SessionContext);
  const navigate = useNavigate();

  const timeout = (delay) => {
    return new Promise((res) => setTimeout(res, delay));
  };

  const handleOauthLogin = (e) => {
    e.preventDefault();
    oauthLoginRequest(username, password, clientID)
      .then((res) => {
        if (res.status === 200) {
          navigate("/define-scope", {
            state: {
              username: username,
              password: password,
              clientID: clientID,
            },
          });
        } else {
          setError(res.data);
        }
      })
      .catch((err) => {
        console.log(err);
        setError(err.message);
      });
  };

  useEffect(() => {
    if (session) {
      oauthAlreadyLoggedRequest(clientID).then((res) => {
        if (res === "AlreadyLogged") {
          navigate("/already-logged");
          timeout(1500).then(() => {
            window.close();
          });
        } else if (res === "loginForm") {
          console.log("User should log in!");
          return;
        } else {
          navigate("/define-scope", {
            state: {
              clientID: clientID,
              authCode: res,
            },
          });
        }
      });
    } else {
      console.log("No session! User should log in!");
    }
  });

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

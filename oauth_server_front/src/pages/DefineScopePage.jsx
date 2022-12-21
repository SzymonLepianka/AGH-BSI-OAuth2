import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import oauthLoginForAuthCodeRequest from "../api/oauthLoginForAuthCodeRequest";
import Cookies from "js-cookie";

export const DefineScopePage = () => {
  const [error, setError] = useState("");

  // const [session, setSession] = useContext(SessionContext);

  const navigate = useNavigate();

  const timeout = (delay) => {
    return new Promise((res) => setTimeout(res, delay));
  };

  const { state } = useLocation();

  const clientID = state ? state.clientID : undefined;
  const password = state ? state.password : undefined;
  const username = state ? state.username : undefined;
  const authCode = state ? state.authCode : undefined;

  useEffect(() => {
    if (
      clientID === undefined &&
      password === undefined &&
      username === undefined
    ) {
      navigate("/");
    }
  });

  const handleAuthorizeApp = (e) => {
    e.preventDefault();

    if (authCode !== undefined) {
      Cookies.set("AuthCode", authCode, { path: "/" });
      navigate("/login-success");
      timeout(1500).then(() => {
        window.close();
      });
      return;
    }

    oauthLoginForAuthCodeRequest(username, password, clientID)
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
      <div style={{ color: "red" }}>{error}</div>
      <h1>OAuth-server-front-define-scope-page</h1>
      <h2>Authorize the app to access:</h2>
      <li key={"Email"}>Email</li>
      <li key={"Username"}>Username</li>
      <li key={"FirstName"}>FirstName</li>
      <li key={"Surname"}>Surname</li>
      <li key={"PhoneNumber"}>PhoneNumber</li>
      <li key={"BirthDate"}>BirthDate</li>
      <br />
      <button type="button" onClick={handleAuthorizeApp}>
        Authorize App
      </button>
    </div>
  );
};

import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import accessTokenRequest from "../api/accessTokenRequest";
import { TokenContext } from "../App";

export const LoginPage = () => {
  const clientID = "2";

  const [error, setError] = useState("");
  const [token, setToken] = useContext(TokenContext);
  const navigate = useNavigate();

  const handleOauthLogin = (e) => {
    var myWindow = window.open(
      `http://localhost:3002/login/${clientID}`,
      "_blank",
      "height=500,width=600"
    );

    var timer = setInterval(function () {
      if (myWindow.closed) {
        clearInterval(timer);
        accessTokenRequest(clientID);
        //TODO get user data
      }
    }, 1000);
  };

  return (
    <div>
      <h1>Login with OAuth2 - client1 front</h1>
      <div style={{ color: "red" }}>{error}</div>
      <button type="button" onClick={handleOauthLogin}>
        Login
      </button>
    </div>
  );
};

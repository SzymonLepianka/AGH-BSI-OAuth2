import React, { useContext, useEffect, useState } from "react";
import accessTokenRequest from "../api/accessTokenRequest";
import { gapi } from "gapi-script";
import GoogleLoginComponent from "../components/googleLogin";
import GoogleLogoutComponent from "../components/googleLogout";
import {
  CLIENT_ID,
  GOOGLE_CLIENT_ID,
  OAUTH_SERVER_FRONT_URL,
} from "../api/config";
import { SessionContext } from "../App";
import { checkAuthCodeCookie, getSessionCookie } from "../middleware/session";
import { useNavigate } from "react-router-dom";

export const LoginPage = () => {
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState("");
  const [logged, setLogged] = useState(false);

  const [session, setSession] = useContext(SessionContext);

  const navigate = useNavigate();

  useEffect(() => {
    const initClient = () => {
      gapi.auth2.init({
        clientId: GOOGLE_CLIENT_ID,
        scope: "",
      });
    };
    gapi.load("client:auth2", initClient);
  });

  useEffect(() => {
    if (session) {
      navigate("/");
    }
  });

  const handleOauthLogin = (e) => {
    var myWindow = window.open(
      `${OAUTH_SERVER_FRONT_URL}/login/${CLIENT_ID}`,
      "_blank",
      "height=500,width=600"
    );

    var timer = setInterval(function () {
      if (myWindow.closed) {
        clearInterval(timer);
        if (!checkAuthCodeCookie()) {
          setError("The user did not authorize the app.");
          return;
        }
        accessTokenRequest()
          .then(() => {
            setSession(getSessionCookie());
            navigate("/");
            setLogged(true);
            setError("");
          })
          .catch((error) => {
            setError(error);
          });
      }
    }, 1000);
  };

  return (
    <div>
      <h1>Login with OAuth2 - client1 front</h1>
      {error && <div style={{ color: "red" }}>Error: {error}</div>}
      {!logged && !profile && (
        <div>
          <button type="button" onClick={handleOauthLogin}>
            Login with OAuth
          </button>
          <br />
        </div>
      )}
      <br />
      {!logged && (
        <div>
          {profile ? (
            <div>
              <img src={profile.imageUrl} alt="user" />
              <h3>User Logged in</h3>
              <p>Name: {profile.name}</p>
              <p>Email Address: {profile.email}</p>
              <br />
              <br />
              <GoogleLogoutComponent setProfile={setProfile} />
            </div>
          ) : (
            <GoogleLoginComponent setProfile={setProfile} />
          )}
        </div>
      )}
    </div>
  );
};

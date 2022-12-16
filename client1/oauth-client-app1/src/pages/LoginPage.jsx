import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import accessTokenRequest from "../api/accessTokenRequest";
import userDataRequest from "../api/userDataRequest";
import { gapi } from "gapi-script";
import GoogleLoginComponent from "../components/googleLogin";
import GoogleLogoutComponent from "../components/googleLogout";
import {
  CLIENT_ID,
  GOOGLE_CLIENT_ID,
  OAUTH_SERVER_FRONT_URL,
} from "../api/config";

export const LoginPage = () => {
  const [profile, setProfile] = useState(null);

  const [error, setError] = useState("");
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [surname, setSurname] = useState("");
  const [username, setUsername] = useState("");
  const [birthDate, setBirthDate] = useState("");
  const [logged, setLogged] = useState(false);

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

  const handleOauthLogin = (e) => {
    var myWindow = window.open(
      `${OAUTH_SERVER_FRONT_URL}/login/${CLIENT_ID}`,
      "_blank",
      "height=500,width=600"
    );

    var timer = setInterval(function () {
      if (myWindow.closed) {
        clearInterval(timer);
        accessTokenRequest()
          .then(() => {
            userDataRequest().then((userData) => {
              setLogged(true);
              setError("");
              setEmail(userData.email);
              setFirstName(userData.first_name);
              setSurname(userData.surname);
              setUsername(userData.username);
              setBirthDate(userData.birth_date);
            });
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
      {email && <div style={{ color: "black" }}>Email: {email}</div>}
      {username && <div style={{ color: "black" }}>User Name: {username}</div>}
      {firstName && (
        <div style={{ color: "black" }}>First Name: {firstName}</div>
      )}
      {surname && <div style={{ color: "black" }}>Surname: {surname}</div>}
      {birthDate && (
        <div style={{ color: "black" }}>Birthdate: {birthDate}</div>
      )}
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

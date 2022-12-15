import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import accessTokenRequest from "../api/accessTokenRequest";
import userDataRequest from "../api/userDataRequest";
import { TokenContext } from "../App";

export const LoginPage = () => {
  const clientID = "2";

  const [error, setError] = useState("");
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [surname, setSurname] = useState("");
  const [username, setUsername] = useState("");
  const [birthDate, setBirthDate] = useState("");
  const [logged, setLogged] = useState(false);

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
        accessTokenRequest(clientID)
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
      {!logged && (
        <button type="button" onClick={handleOauthLogin}>
          Login
        </button>
      )}
    </div>
  );
};

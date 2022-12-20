import React, { useContext, useEffect, useState } from "react";
import userDataRequest from "../api/userDataRequest";
import { gapi } from "gapi-script";
import { GOOGLE_CLIENT_ID } from "../api/config";
import { SessionContext } from "../App";
import logoutRequest from "../api/logoutRequest";
import { useNavigate } from "react-router-dom";

export const HomePage = () => {
  const [error, setError] = useState("");
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [surname, setSurname] = useState("");
  const [username, setUsername] = useState("");
  const [birthDate, setBirthDate] = useState("");

  const [session, setSession] = useContext(SessionContext);

  const navigate = useNavigate();

  useEffect(() => {
    if (session) {
      userDataRequest().then((userData) => {
        setEmail(userData.email);
        setFirstName(userData.first_name);
        setSurname(userData.surname);
        setUsername(userData.username);
        setBirthDate(userData.birth_date);
      });
    }
  });

  useEffect(() => {
    const initClient = () => {
      gapi.auth2.init({
        clientId: GOOGLE_CLIENT_ID,
        scope: "",
      });
    };
    gapi.load("client:auth2", initClient);
  });

  const handleLogout = (e) => {
    e.preventDefault();
    logoutRequest(session)
      .then(() => {
        setSession("");
        navigate("/login");
      })
      .catch((err) => {
        setError(err.message);
      });
  };

  return (
    <div>
      <h1>OAuth2 client front - home page</h1>
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
      <br />
      <button type="button" onClick={handleLogout}>
        Logout OAuth2
      </button>
    </div>
  );
};

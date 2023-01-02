import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { SessionContext } from "../App";
import userDataRequest from "../api/userDataRequest";
import logoutRequest from "../api/logoutRequest";
import { removeCookie } from "../middleware/session";

export const HomePage = () => {
  const [error, setError] = useState("");
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [surname, setSurname] = useState("");
  const [username, setUsername] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [birthDate, setBirthDate] = useState("");
  const [session, setSession] = useContext(SessionContext);

  const navigate = useNavigate();

  useEffect(() => {
    if (session) {
      userDataRequest(session)
        .then((res) => {
          setEmail(res.user_email);
          setFirstName(res.user_firstname);
          setSurname(res.user_surname);
          setUsername(res.user_username);
          setBirthDate(res.user_birthdate);
          setPhoneNumber(res.user_phonenumber);
        })
        .catch((err) => {
          if (err.response) {
            setError(err.message + ": " + err.response.data);
          } else {
            setError(err.message);
          }
          removeCookie(session);
          setSession("");
          navigate("/login");
        });
    }
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
      <h1>OAuth-server-front-home-page</h1>
      {error && <div style={{ color: "red" }}>Error: {error}</div>}
      <h2>User data:</h2>
      {email && <div style={{ color: "black" }}>Email: {email}</div>}
      {username && <div style={{ color: "black" }}>User Name: {username}</div>}
      {firstName && (
        <div style={{ color: "black" }}>First Name: {firstName}</div>
      )}
      {surname && <div style={{ color: "black" }}>Surname: {surname}</div>}
      {birthDate && (
        <div style={{ color: "black" }}>Birthdate: {birthDate}</div>
      )}
      {phoneNumber && (
        <div style={{ color: "black" }}>Phone number: {phoneNumber}</div>
      )}
      <h2>Actions:</h2>
      {
        <button type="button" onClick={handleLogout}>
          Logout
        </button>
      }
    </div>
  );
};

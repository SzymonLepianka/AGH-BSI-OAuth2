import "./App.css";

import Login from "./components/login";
import Logout from "./components/logout";
import React, { useState, useEffect } from "react";
import { gapi } from "gapi-script";

const clientId =
  "868591954044-jsaqecvi69jev4u38kus5qj3h0atio3g.apps.googleusercontent.com";

function App() {
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    const initClient = () => {
      gapi.auth2.init({
        clientId: clientId,
        scope: "",
      });
    };
    gapi.load("client:auth2", initClient);
  });

  // console.log(gapi.auth?.getToken().access_token)

  return (
    <div>
      <h2>React Google Login</h2>
      {profile ? (
        <div>
          <img src={profile.imageUrl} alt="user" />
          <h3>User Logged in</h3>
          <p>Name: {profile.name}</p>
          <p>Email Address: {profile.email}</p>
          <br />
          <br />
          <Logout setProfile={setProfile} />
        </div>
      ) : (
        <Login setProfile={setProfile} />
      )}
    </div>
  );
}

export default App;

import { GoogleLogout } from "react-google-login";
import { GOOGLE_CLIENT_ID } from "../api/config";

function GoogleLogoutComponent(props) {
  const onSuccess = (res) => {
    console.log("Log out successfull!");
    props.setProfile(null);
    props.setError("");
  };

  return (
    <div id="signOutButton">
      <GoogleLogout
        clientId={GOOGLE_CLIENT_ID}
        buttonText="Logout"
        onLogoutSuccess={onSuccess}
      />
    </div>
  );
}

export default GoogleLogoutComponent;

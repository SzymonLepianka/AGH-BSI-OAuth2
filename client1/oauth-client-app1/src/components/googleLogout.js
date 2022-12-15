import { GoogleLogout } from "react-google-login";

const clientId =
  "868591954044-jsaqecvi69jev4u38kus5qj3h0atio3g.apps.googleusercontent.com";

function GoogleLogoutComponent(props) {
  const onSuccess = (res) => {
    console.log("Log out successfull!");
    props.setProfile(null);
  };

  return (
    <div id="signOutButton">
      <GoogleLogout
        clientId={clientId}
        buttonText="Logout"
        onLogoutSuccess={onSuccess}
      />
    </div>
  );
}

export default GoogleLogoutComponent;

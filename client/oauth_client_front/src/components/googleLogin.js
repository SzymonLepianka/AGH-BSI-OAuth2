import { GoogleLogin } from "react-google-login";
import { GOOGLE_CLIENT_ID } from "../api/config";

function GoogleLoginComponent(props) {
  const onSuccess = (res) => {
    console.log("LOGIN SUCCESS! Current user: ", res.profileObj);
    props.setProfile(res.profileObj);
    props.setError("");
  };

  const onFailure = (res) => {
    console.log("LOGIN FAILED! res: ", res);
    props.setError("LOGIN FAILED! res: ", res);
  };

  return (
    <div id="googleSignInButton">
      <GoogleLogin
        clientId={GOOGLE_CLIENT_ID}
        buttonText="Sign in with Google"
        onSuccess={onSuccess}
        onFailure={onFailure}
        cookiePolicy={"single_host_origin"}
        isSignedIn={true}
      />
    </div>
  );
}

export default GoogleLoginComponent;

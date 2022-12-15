import { GoogleLogin } from "react-google-login";

const clientId =
  "868591954044-jsaqecvi69jev4u38kus5qj3h0atio3g.apps.googleusercontent.com";

function GoogleLoginComponent(props) {
  const onSuccess = (res) => {
    console.log("LOGIN SSUCCESS! Current user: ", res.profileObj);
    props.setProfile(res.profileObj);
  };

  const onFailure = (res) => {
    console.log("LOGIN FAILED! res: ", res);
  };

  return (
    <div id="googleSignInButton">
      <GoogleLogin
        clientId={clientId}
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

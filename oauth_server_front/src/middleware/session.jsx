import Cookies from "js-cookie";

export const getSessionCookie = () => {
  const sessionCookie = Cookies.get("AccessToken1");

  if (sessionCookie === undefined) {
    return "";
  } else {
    return sessionCookie;
  }
};

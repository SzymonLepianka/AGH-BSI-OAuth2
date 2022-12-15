import Cookies from "js-cookie";

export const getSessionCookie = () => {
  const sessionCookie1 = Cookies.get("AccessToken1");
  const sessionCookie2 = Cookies.get("AccessToken2");

  if (sessionCookie1 === undefined && sessionCookie2 === undefined) {
    return "";
  } else {
    return sessionCookie1 ? sessionCookie1 : sessionCookie2;
  }
};

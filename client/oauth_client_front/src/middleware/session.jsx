import { Cookies } from "react-cookie";
import { CLIENT_ID } from "../api/config";

export const getSessionCookie = () => {
  const cookieManager = new Cookies();
  const cookies = cookieManager.getAll();

  for (const cookie in cookies) {
    if (cookie === "AccessToken" + CLIENT_ID) {
      // TODO call validate AccessToken
      return cookies[cookie];
    }
  }
  return "";
};

export const removeClientIdCookies = () => {
  const cookieManager = new Cookies();
  const cookies = cookieManager.getAll();

  for (const cookie in cookies) {
    if (
      cookie === "AccessToken" + CLIENT_ID ||
      cookie === "RefreshToken" + CLIENT_ID
    ) {
      cookieManager.remove(cookie);
    }
  }
};

import { Cookies } from "react-cookie";

export const getSessionCookie = () => {
  const cookieManager = new Cookies();
  const cookies = cookieManager.getAll();

  for (const cookie in cookies) {
    if (cookie.startsWith("AccessToken")) {
      // TODO call validate AccessToken
      return cookies[cookie];
    }
  }
  return "";
};

export const removeAllCookies = () => {
  const cookieManager = new Cookies();
  const cookies = cookieManager.getAll();

  for (const cookie in cookies) {
    if (cookie.startsWith("AccessToken") || cookie.startsWith("RefreshToken")) {
      cookieManager.remove(cookie);
    }
  }
};

import axios from "axios";
import { Cookies } from "react-cookie";
import { CLIENT_ID, OAUTH_SERVER_URL } from "./config";

export default (sessionCookie) => {
  const cookieManager = new Cookies();
  const cookies = cookieManager.getAll();
  let clientID = "";
  for (const cookie in cookies) {
    if (cookies[cookie] === sessionCookie) {
      clientID = cookie.substring(11, cookie.length);
      break;
    }
  }

  return axios
    .get(
      `${OAUTH_SERVER_URL}/api/getUserData?clientID=${
        clientID ? clientID : CLIENT_ID
      }&accessToken=${sessionCookie}`,
      {
        withCredentials: true,
      }
    )
    .then((response) => {
      if (response.status === 200) {
        console.log(response.data);
        return response.data;
      } else {
        console.log(response);
        throw new Error("Getting user data failed (" + response.status + ")");
      }
    })
    .catch((error) => {
      console.log(error);
    });
};

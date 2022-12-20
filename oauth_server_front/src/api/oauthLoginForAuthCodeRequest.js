import axios from "axios";
import Cookies from "js-cookie";
import { OAUTH_SERVER_URL } from "./config";

export default (username, password, clientID) => {
  return axios
    .post(
      `${OAUTH_SERVER_URL}/web/loginForAuthCode`,
      {
        username,
        password,
        clientID,
      },
      {
        withCredentials: true,
      }
    )
    .then((response) => {
      if (response.status === 200) {
        Cookies.set("AuthCode", response.data, { path: "/" });
      } else {
        throw new Error(response.status + " " + response.data);
      }
    });
};

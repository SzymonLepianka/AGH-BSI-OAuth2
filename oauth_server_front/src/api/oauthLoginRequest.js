import axios from "axios";
import Cookies from "js-cookie";
import { OAUTH_SERVER_URL } from "./config";

export default (username, password, clientID) => {
  return axios
    .post(
      `${OAUTH_SERVER_URL}/web/login`,
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
      Cookies.set("AuthCode", response.data, { path: "/" });
    })
    .catch((error) => {
      console.log(error);
    });
};

import axios from "axios";
import { CLIENT_ID, OAUTH_SERVER_URL } from "./config";

export default (username, password) => {
  return axios
    .post(
      `${OAUTH_SERVER_URL}/web/loginForAuthCode`,
      {
        username,
        password,
        clientID: CLIENT_ID,
      },
      {
        withCredentials: true,
      }
    )
    .then((response) => {
      return response.data;
    })
};

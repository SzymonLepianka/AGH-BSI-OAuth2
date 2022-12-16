import axios from "axios";
import { OAUTH_SERVER_URL } from "./config";

export default (username, password) => {
  return axios
    .post(
      `${OAUTH_SERVER_URL}/web/login`,
      {
        username,
        password,
        clientID: "1",
      },
      {
        withCredentials: true,
      }
    )
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

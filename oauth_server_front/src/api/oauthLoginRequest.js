import axios from "axios";
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
      if (response.status === 200) {
        return response;
      } else {
        throw new Error(response.status + " " + response.data);
      }
    });
};

import axios from "axios";
import { OAUTH_SERVER_URL } from "./config";

export default (clientID) => {
  return axios
    .get(`${OAUTH_SERVER_URL}/web/login?clientID=${clientID}`, {
      withCredentials: true,
    })
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

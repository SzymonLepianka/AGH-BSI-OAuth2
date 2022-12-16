import axios from "axios";
import { CLIENT_ID, OAUTH_SERVER_URL } from "./config";

export default () => {
  return axios
    .get(`${OAUTH_SERVER_URL}/api/createToken?clientID=${CLIENT_ID}`, {
      withCredentials: true,
    })
    .then((response) => {
      if (response.status === 200) {
        console.log(response.data);
        return response.data;
      } else {
        console.log(response);
        throw new Error("Creating token failed (" + response.status + ")");
      }
    })
    .catch((error) => {
      console.log(error);
    });
};

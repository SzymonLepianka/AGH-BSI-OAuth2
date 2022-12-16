import axios from "axios";
import { CLIENT_SERVER_URL } from "./config";

export default () => {
  return axios
    .get(`${CLIENT_SERVER_URL}/users/getUserData`, {
      withCredentials: true,
    })
    .then((response) => {
      console.log(response)
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

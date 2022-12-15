import axios from "axios";
import Cookies from "js-cookie";

export default (clientID) => {
  return axios
    .get(`http://localhost:8080/web/login?clientID=${clientID}`, {
      withCredentials: true,
    })
    .then((response) => {
      console.log("already logged res: ", response);
      return response.data;
      // Cookies.set("AuthCode", response.data, { path: "/" });
    })
    .catch((error) => {
      console.log(error);
    });
};

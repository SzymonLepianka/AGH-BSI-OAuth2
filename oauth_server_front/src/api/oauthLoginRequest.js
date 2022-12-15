import axios from "axios";
import Cookies from "js-cookie";

export default (username, password, clientID) => {
  return axios
    .post(
      "http://localhost:8080/web/login",
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

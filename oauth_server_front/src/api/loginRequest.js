import axios from "axios";

export default (username, password) => {
  return axios
    .post(
      "http://localhost:8080/web/login",
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

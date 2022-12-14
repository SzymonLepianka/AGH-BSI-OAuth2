import axios from "axios";

export default (clientID) => {
  return axios
    .get(`http://localhost:8080/api/createToken?clientID=${clientID}`, {
      withCredentials: true,
    })
    .then((response) => {
      if (response.status === 200) {
        console.log(response.data);
        return response.data;
      } else {
        console.log(response);
        throw new Error("Crating token failed (" + response.status + ")");
      }
    })
    .catch((error) => {
      console.log(error);
    });
};

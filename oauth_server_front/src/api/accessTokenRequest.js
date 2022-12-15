import axios from "axios";

export default () => {
  return axios
    .get(`http://localhost:8080/api/createToken?clientID=1`, {
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

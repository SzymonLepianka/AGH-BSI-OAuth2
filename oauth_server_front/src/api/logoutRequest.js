import axios from "axios";
import { Cookies } from "react-cookie";
import { removeAllCookies } from "../middleware/session";

export default () => {
  const cookieManager = new Cookies();
  const cookies = cookieManager.getAll();
  let promises = [];
  for (const cookie in cookies) {
    if (cookie.startsWith("AccessToken")) {
      const clientID = cookie.substring(11, cookie.length);
      promises.push(
        axios
          .get(
            `http://localhost:8080/api/revokeToken?clientID=${clientID}&accessToken=${cookies[cookie]}`,
            {
              withCredentials: true,
            }
          )
          .then((response) => {
            return response.data;
          })
          .catch((error) => {
            console.log(error);
          })
      );
    }
  }
  return Promise.all(promises).then(() => {
    removeAllCookies();
  });
};

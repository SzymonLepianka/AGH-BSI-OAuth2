import axios from "axios";
import { Cookies } from "react-cookie";
import { removeClientIdCookies } from "../middleware/session";
import { CLIENT_ID, OAUTH_SERVER_URL } from "./config";

const logoutRequest = function () {
  const cookieManager = new Cookies();
  const cookies = cookieManager.getAll();
  let promises = [];
  for (const cookie in cookies) {
    if (cookie === "AccessToken" + CLIENT_ID) {
      promises.push(
        axios
          .get(
            `${OAUTH_SERVER_URL}/api/revokeToken?clientID=${CLIENT_ID}&accessToken=${cookies[cookie]}`,
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
    removeClientIdCookies();
  });
};

export default logoutRequest;

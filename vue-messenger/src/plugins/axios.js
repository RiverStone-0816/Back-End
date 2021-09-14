import axios from "axios"
import sessionUtils from "@/utillities/sessionUtils";

axios.defaults.baseURL = 'http://localhost:8081'
axios.defaults.headers = {
    /**
     * @ses kr.co.eicn.ippbx.front.config.CookieAndXHeaderHttpSessionIdResolver
     */
    'X-Auth-Token': sessionUtils.getSessionId()
}

export default axios

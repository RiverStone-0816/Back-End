import axios from "axios"
import sessionUtils from "@/utillities/sessionUtils";

axios.defaults.baseURL = 'http://localhost:8081'
// axios.defaults.withCredentials = true
axios.defaults.headers = {
//     'Access-Control-Allow-Credentials': true,
//     'Access-Control-Allow-Origin': '*',
//     /**
//      * @ses kr.co.eicn.ippbx.front.config.CookieAndXHeaderHttpSessionIdResolver
//      */
    'X-Auth-Token': sessionUtils.getId()
}

export default axios

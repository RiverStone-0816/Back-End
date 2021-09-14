import cookieUtils from '@/utillities/cookieUtils'
import axios from "@/plugins/axios";
import store from "@/store/index"

export const BROWSER_SESSION_KEY = "JSESSIONID";
export const LOCALSTORAGE_SESSION_KEY = "session";

const uuidv4 = () => 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
})

export default {
    getSessionId() {
        const cookedSessionId = cookieUtils.get(BROWSER_SESSION_KEY)
        if (cookedSessionId)
            return cookedSessionId

        this._generateSessionId()
        return cookieUtils.get(BROWSER_SESSION_KEY)
    },
    _generateSessionId() {
        const sessionId = cookieUtils.get(BROWSER_SESSION_KEY) || uuidv4()
        cookieUtils.set(BROWSER_SESSION_KEY, sessionId)
        this.set(BROWSER_SESSION_KEY, sessionId)
    },
    get(key) {
        const sessionString = localStorage.getItem(LOCALSTORAGE_SESSION_KEY);
        if (!sessionString)
            return null

        const session = JSON.parse(sessionString);
        if (this.getSessionId() !== session[BROWSER_SESSION_KEY])
            return null

        return session[key]
    },
    set(key, value) {
        const sessionId = this.getSessionId()
        let session = JSON.parse(localStorage.getItem(LOCALSTORAGE_SESSION_KEY) || '{}')

        if (!session[BROWSER_SESSION_KEY] || sessionId !== session[BROWSER_SESSION_KEY]) {
            session = {}
            session[BROWSER_SESSION_KEY] = sessionId
        }

        session[key] = value
        localStorage.setItem(LOCALSTORAGE_SESSION_KEY, JSON.stringify(session))
    },
    clear() {
        localStorage.setItem(LOCALSTORAGE_SESSION_KEY, '{}')
    },
    getId() {
        return this.get('currentUserId')
    },
    setId(o) {
        this.set('currentUserId', o)
    },
    getMe() {
        return this.get('me')
    },
    setMe(o) {
        this.set('me', o)
        this.set('currentUserId', o.id)
    },
    async fetchMe() {
        try {
            const currentUserId = this.getId()
            if (!currentUserId)
                return

            const me = this.get('me') || (await axios.get(`/api/user/${currentUserId}`)).data.data
            console.log(me)

            this.set('me', me)
            store.state.user.credential = me
            return me
        } catch (ignored) {
            this.set('me', null)
            store.state.user.credential = null
        }
    },
}

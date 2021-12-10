import cookieUtils from './cookieUtils'

export const BROWSER_SESSION_KEY = "BROWSER_SESSION_ATTRIBUTE";
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
        if (cookieUtils.get(BROWSER_SESSION_KEY) !== sessionId)
            throw 'cannot use cookie'

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
}

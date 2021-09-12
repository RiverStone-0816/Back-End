import cookieUtils from '@/utillities/cookieUtils'

export const BROWSER_SESSION_KEY = "JSESSIONID";
export const LOCALSTORAGE_SESSION_KEY = "session";

const uuidv4 = () => 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
})

export default {
    getId() {
        const sessionId = cookieUtils.get(BROWSER_SESSION_KEY) || uuidv4()
        this.setId(sessionId)
        return sessionId
    },
    setId(id) {
        cookieUtils.set(BROWSER_SESSION_KEY, id)
    },
    get(key) {
        const sessionString = localStorage.getItem(LOCALSTORAGE_SESSION_KEY)
        if (!sessionString)
            return null;

        return JSON.parse(sessionString)[key];
    },
    set(key, value) {
        const session = JSON.parse(localStorage.getItem(LOCALSTORAGE_SESSION_KEY) || '{}')
        session[key] = value
        localStorage.setItem(LOCALSTORAGE_SESSION_KEY, JSON.stringify(session))
    },
    clear() {
        this.setId('')
    },
}

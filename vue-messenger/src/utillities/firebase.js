import {initializeApp} from 'firebase/app'
import {getAuth, onAuthStateChanged, signInWithEmailAndPassword, signOut} from "firebase/auth"
import {addDoc, collection, doc, getDocs, getFirestore, onSnapshot, query, serverTimestamp, setDoc} from "firebase/firestore";

const app = initializeApp({
    apiKey: process.env.VUE_APP_API_KEY,
    authDomain: process.env.VUE_APP_AUTH_DOMAIN,
    projectId: process.env.VUE_APP_PROJECT_ID,
    storageBucket: process.env.VUE_APP_STORAGE_BUCKET,
    messagingSenderId: process.env.VUE_APP_MESSAGING_SENDER_ID,
    appId: process.env.VUE_APP_VUE_APP_ID,
    measurementId: process.env.VUE_APP_MEASUREMENT_ID,
})

export default app

export const getLoginedUser = getAuth

export const login = (email, password) => {
    return signInWithEmailAndPassword(getAuth(), email, password)
}
export const logout = () => signOut(getAuth())

export const registerUserStateChangeEventHandler = fn => onAuthStateChanged(getAuth(), fn)

const db = getFirestore()
const CHATROOM_COLLECTION_NAME = 'rooms'
const DEFAULT_CHATROOM_ID = 'DEFAULT'
const DEFAULT_CHATROOM_NAME = 'default chatroom'

const messagesCollection = () => collection(doc(db, CHATROOM_COLLECTION_NAME, DEFAULT_CHATROOM_ID), "messages")

export const createMessageCollection = async () => {
    try {
        await setDoc(doc(collection(db, CHATROOM_COLLECTION_NAME), DEFAULT_CHATROOM_ID), {name: DEFAULT_CHATROOM_NAME})
    } catch (e) {
        console.error(e)
    }
}

export const registerMessageHandler = (func) => {
    try {
        onSnapshot(query(messagesCollection()), (snapshot) => {
            snapshot.docChanges().forEach((change) => func(change.type, change.doc.data()))
        })
    } catch (e) {
        console.error(e)
    }
}

export const sendMessage = async (message, userId, senderName, avatar) => {
    try {
        await addDoc(messagesCollection(), {
            userId: userId,
            message: message,
            timeStamp: serverTimestamp(),
            sender: senderName,
            avatar: avatar
        })
    } catch (e) {
        console.error("Error adding document: ", e)
    }
}

export const getMessages = async () => await getDocs(messagesCollection())

import {createStore} from 'vuex'
import alert from './alert'
import communicator from './communicator'

export default createStore({
    modules: {alert, communicator}
})

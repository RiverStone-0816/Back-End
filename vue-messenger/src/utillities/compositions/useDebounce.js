import {ref} from "vue";

export default () => {
    const timeout = ref(null)
    return (func, wait = 500) => {
        clearTimeout(timeout.value)
        timeout.value = setTimeout(func, wait)
    }
}

import {onMounted, onUnmounted} from "vue";

export default function (eventType, handler) {
    onMounted(() => window.addEventListener(eventType, handler))
    onUnmounted(() => window.removeEventListener(eventType, handler))
}

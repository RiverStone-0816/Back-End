const MODAL_NAME = 'modal-eicn-chat'

export default {
    methods: {
        openChat(url, senderKey, userKey, ip, mode ) {
            const modal = window.open(location.href, MODAL_NAME, `width=460,height=700,top=0,left=0,scrollbars=yes`)
            modal.form = {url, senderKey, userKey, ip, mode}
            return modal
        },
        isModal() {
            return MODAL_NAME === window.name
        },
    }
}

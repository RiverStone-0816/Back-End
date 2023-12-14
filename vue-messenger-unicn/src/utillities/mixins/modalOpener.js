const MODAL_NAME = 'modal-eicn-chat'

export default {
    methods: {
        openChat(message) {
            const modal = window.open(location.href, MODAL_NAME, `width=460,height=700,top=0,left=0,scrollbars=yes`)
            modal.form = message;
            return modal
        },
        isModal() {
            return MODAL_NAME === window.name
        },
    }
}

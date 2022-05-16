export default {
    get: function (key) {
        const o = document.cookie.split(';').map(function (e) {
            const splits = e.split('=');
            return {key: splits[0] && splits[0].trim(), value: splits[1] && splits[1].trim()};
        }).filter(function (e) {
            return e.key === key;
        })[0];

        return o && o.value;
    },
    set: function (key, value) {
        document.cookie = key + '=' + value +'; SameSite=None; Secure'
    }
}

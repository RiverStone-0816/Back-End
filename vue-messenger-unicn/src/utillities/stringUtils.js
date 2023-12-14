export default {
    addQueryString: function (url, data) {
        function concatDelimiter(url) {
            return url + (url.indexOf('?') >= 0 ? '&' : '?');
        }

        function addQueryString(url, key, value) {
            if (data === null || data === undefined)
                return url;

            if (value instanceof Array) {
                value.map(function (e) {
                    url = concatDelimiter(url) + encodeURIComponent(key) + '=' + encodeURIComponent(e);
                });
            } else {
                url = concatDelimiter(url) + encodeURIComponent(key) + '=' + encodeURIComponent(value);
            }

            return url;
        }

        if (!data || typeof data !== "object")
            return concatDelimiter(url) + encodeURIComponent((data === null || data === undefined) ? '' : data);

        for (let key in data) {
            url = addQueryString(url, key, data[key]);
        }

        return url;
    }
}

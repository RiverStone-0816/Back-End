if (!String.prototype.contains) {
    String.prototype.contains = function (arg) {
        return this.indexOf(arg) >= 0;
    };
}

String.prototype.toObject = function () {
    const options = {};
    if (this == null)
        return options;
    const tokens = this.trim().split(';');
    for (let i = 0; i < tokens.length; i++) {
        const pair = tokens[i].trim().split(':');
        if (pair.length < 2)
            continue;
        const key = pair[0].trim();
        const value = pair[1].trim();
        if (key.length <= 0)
            continue;
        options[key] = value;
    }
    return options;
};

String.prototype.toJquerySelector = function () {
    let result = '';
    const demlist = '.[]/!:';
    for (let i = 0; i < this.length; i++) {
        const c = this.charAt(i);
        if (demlist.indexOf(c) >= 0)
            result += '\\';
        result += c;
    }
    return result;
};

// ref: https://stackoverflow.com/questions/7616461/generate-a-hash-from-string-in-javascript
String.prototype.hashCode = function () {
    var hash = 0, i, chr;
    if (this.length === 0) return hash;
    for (i = 0; i < this.length; i++) {
        chr = this.charCodeAt(i);
        hash = ((hash << 5) - hash) + chr;
        hash |= 0; // Convert to 32bit integer
    }
    return hash;
};

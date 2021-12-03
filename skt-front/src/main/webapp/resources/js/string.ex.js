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

// ref: http://daplus.net/javascript-%EC%A0%95%EA%B7%9C-%ED%91%9C%ED%98%84%EC%8B%9D%EC%9D%84-%ED%97%88%EC%9A%A9%ED%95%98%EB%8A%94-javascript%EC%9D%98-string-indexof-%EB%B2%84%EC%A0%84%EC%9D%B4-%EC%9E%88%EC%8A%B5%EB%8B%88/
String.prototype.regexIndexOf = function(regex, startpos) {
    var indexOf = this.substring(startpos || 0).search(regex);
    return (indexOf >= 0) ? (indexOf + (startpos || 0)) : indexOf;
}

String.prototype.regexLastIndexOf = function(regex, startpos) {
    regex = (regex.global) ? regex : new RegExp(regex.source, "g" + (regex.ignoreCase ? "i" : "") + (regex.multiLine ? "m" : ""));
    if(typeof (startpos) == "undefined") {
        startpos = this.length;
    } else if(startpos < 0) {
        startpos = 0;
    }
    var stringToWorkWith = this.substring(0, startpos + 1);
    var lastIndexOf = -1;
    var nextStop = 0;
    while((result = regex.exec(stringToWorkWith)) != null) {
        lastIndexOf = result.index;
        regex.lastIndex = ++nextStop;
    }
    return lastIndexOf;
}

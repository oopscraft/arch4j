/**
 * alert
 * @param message
 * @param option_
 * @returns {*}
 */
const _alert = function(message, option_) {
    message = '<img class="icon font-size--larger" src="/static/image/icon-dialog-alert.svg" alt="alert"/><br/>' + message + '<br/>';
    return duice.alert(message)
        .onOpening(option_?.onOpening)
        .onOpened(option_?.onOpened)
        .onClosing(option_?.onClosing)
        .onClosed(option_?.onClosed)
        .open();
}

/**
 * confirm
 * @param message
 * @param option_
 * @returns {*}
 */
const _confirm = function(message, option_) {
    message = '<img class="icon font-size--larger" src="/static/image/icon-dialog-confirm.svg" alt="confirm"/><br/>' + message + '<br/>';
    return duice.confirm(message)
        .onOpening(option_?.onOpening)
        .onOpened(option_?.onOpened)
        .onClosing(option_?.onClosing)
        .onClosed(option_?.onClosed)
        .open();
}

/**
 * prompt
 * @param message
 * @param type
 * @param option_
 * @returns {*}
 */
const _prompt = function(message, type, option_) {
    message = '<img class="icon font-size--larger" src="/static/image/icon-dialog-prompt.svg" alt="prompt"/><br/>' + message + '<br/>';
    return duice.prompt(message, type)
        .onOpening(option_?.onOpening)
        .onOpened(option_?.onOpened)
        .onClosing(option_?.onClosing)
        .onClosed(option_?.onClosed)
        .open();
}

/**
 * Gets cookie value
 * @param name
 */
const _getCookie = function(name) {
    let value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

/**
 * Sets cookie value
 * @param name
 * @param value
 * @param day
 */
const _setCookie = function(name, value, day) {
    let date = new Date();
    date.setTime(date.getTime() + day * 60 * 60 * 24 * 1000);
    document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
}

/**
 * Deletes cookie
 * @param name
 */
const _deleteCookie = function(name) {
    _setCookie(name, '', -1);
}

/**
 * start progress
 * @private
 */
const _startProgress = function() {
    if(window['NProgress']) {
        NProgress.start();
    }
}

/**
 * stop progress
 * @private
 */
const _stopProgress = function() {
    if(window['NProgress']) {
        NProgress.done();
    }
}

/**
 * _fetch
 * @param url
 * @param options
 * @param _bypass
 */
const _fetch = function(url, options, _bypass) {
    if(!options){
        options = {};
    }
    if(!options.headers){
        options.headers = {};
    }
    // csrf token
    ['XSRF-TOKEN', 'CSRF-TOKEN'].forEach(tokenName => {
        let tokenValue = _getCookie(tokenName);
        if(tokenValue){
            options.headers[`X-${tokenName}`] = tokenValue;
        }
    });
    options.headers['Cache-Control'] = 'no-cache, no-store, must-revalidate';
    options.headers['Pragma'] = 'no-cache';
    options.headers['Expires'] = '0';
    options.redirect = 'follow';
    _startProgress();
    return globalThis.fetch(url, options)
        .then(async function(response) {
            console.debug(response);

            // bypass
            if (_bypass) {
                return response;
            }

            // checks response
            if (response.ok) {
                return response;
            }else{
                const contentType = response.headers.get('content-type');
                let errorMessage;
                if(contentType === 'application/json'){
                    let responseJson = await response.json();
                    errorMessage = responseJson.message;
                }else{
                    errorMessage = await response.text();
                }
                throw Error(errorMessage);
            }
        })
        .catch((error)=>{
            if(!_bypass) {
                _alert(error).then();
            }
            throw Error(error);
        })
        .finally(() => {
            _stopProgress();
        });
}

/**
 * Parsed total count from Content-Range header
 * @Param {Response} response
 */
const _parseTotalCount = function(response){
    let totalCount = -1;
    let contentRange = response.headers.get("Content-Range");
    try {
        totalCount = contentRange.split(' ')[1].split('/')[1];
        totalCount = parseInt(totalCount);
        if(isNaN(totalCount)){
            return -1;
        }
        return totalCount;
    }catch(e){
        console.error(e);
        return -1;
    }
}

/**
 * _isDarkMode
 * @returns {boolean}
 * @private
 */
const _isDarkMode = function() {

    // checks cookie
    if(_getCookie('dark-mode') === 'true') {
        return true;
    }
    if(_getCookie('dark-mode') === 'false') {
        return false;
    }

    // checks media query
    if (window.matchMedia) {
        if(window.matchMedia('(prefers-color-scheme: dark)')?.matches){
            return true;
        }
    }

    // returns false
    return false;
}

/**
 * setDarkMode
 * @param enable
 */
const _setDarkMode = function(enable) {
    if(enable){
        document.documentElement.classList.add('dark-mode');
        _setCookie('dark-mode', 'true', 356);
    }else{
        document.documentElement.classList.remove('dark-mode');
        _setCookie('dark-mode', 'false', 356);
    }
}

/**
 * toggle dark mode
 */
const _toggleDarkMode = function() {
    if(_isDarkMode()) {
        _setCookie('dark-mode', 'false', 356);
    }else{
        _setCookie('dark-mode', 'true', 356);
    }
    window.location.reload();
}

// set color scheme
_setDarkMode(_isDarkMode());

/**
 * return random color code
 * @private
 */
const _getRandomColor = function() {
    if (_isDarkMode()) {
        return randomColor({
            luminosity: 'dark',
            format: 'rgb'
        });
    } else {
        return randomColor({
            luminosity: 'bright',
            format: 'rgb'
        });
    }
}

/**
 * Opens link
 * @param linkUrl
 * @param linkTarget
 */
const _openLink = function(linkUrl, linkTarget){
    if(linkUrl) {
        if(linkTarget) {
            window.open(linkUrl, linkTarget);
        }else{
            window.location.href = linkUrl;
        }
    }
}

/**
 * checks is empty
 * @param value
 * @returns {boolean}
 * @private
 */
const _isEmpty = function(value) {
    return !(value && value.trim().length > 0);
}

/**
 * Checks generic ID (alphabet + number + -,_)
 * @param value
 */
const _isIdFormat = function(value) {
    if(value){
        let pattern = /^[a-zA-Z0-9\-\_\.]{1,}$/;
        return pattern.test(value);
    }
    return false;
}

/**
 * Checks generic password (At least 1 alphabet, 1 number, 1 special char)
 * @param value
 */
const _isPasswordFormat = function(value) {
    if(value){
        let pattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;
        return pattern.test(value);
    }
    return false;
}

/**
 * Checks valid email address pattern
 * @param value
 */
const _isEmailFormat = function(value) {
    if(value){
        let pattern = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        return pattern.test(value);
    }
    return false;
}

/**
 * Checks if value is URL address format
 * @param value
 */
const _isUrlFormat = function(value) {
    if(value){
        let pattern = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
        return pattern.test(value);
    }
    return false;
}

/**
 * change language
 * @param language
 * @private
 */
const _changeLanguage = function(language) {
    if(language) {
        // reload with language parameter
        let url = new URL(document.location.href);
        url.searchParams.delete('_language');
        url.searchParams.append('_language', language);
        document.location.href = url.href;
    }
}

/**
 * websocket client
 */
const _webSocketClient = {
    stomp: Stomp.over(new SockJS('/ws')),
    subscriptions: [],
    subscribe: function(subscription) {
        this.subscriptions.push(subscription);
        if(this.stomp.connected) {
            subscription.subscribe = this.stomp.subscribe(subscription.destination, subscription.listener);
        }
        return subscription;
    },
    unsubscribe: function(subscription) {
        if(subscription?.subscribe) {
            subscription.subscribe.unsubscribe();
        }
    },
    connect: function() {
        this.stomp.heartbeat.outgoing = 5000;
        this.stomp.heartbeat.incoming = 5000;
        this.stomp.reconnect = true;
        this.stomp.reconnect_delay = 2000;
        this.stomp.reconnect_delay_max = 60000;
        const _this = this;
        this.stomp.connect({}, function(frame) {
            for(let i = 0; i < _this.subscriptions.length; i++) {
                let subscription = _this.subscriptions[i];
                subscription.subscribe = _this.stomp.subscribe(subscription.destination, subscription.listener);
            }
        });
    },
    send: function(message) {
        this.stomp.send(message.destination, message.headers, message.body);
    },
    disconnect: function() {
        this.stomp.disconnect();
    }
};

/**
 * DOMContentLoaded
 */
document.addEventListener('DOMContentLoaded', event => {
    _webSocketClient.connect();
});

/**
 * beforeunload
 */
window.addEventListener('beforeunload', () => {
    _webSocketClient.disconnect();
});

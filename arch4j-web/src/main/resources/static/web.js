/**
 * alert
 * @param message
 * @returns {*}
 */
const _alert = function(message) {
    return duice.alert(message);
}

/**
 * confirm
 * @param message
 * @returns {*}
 */
const _confirm = function(message) {
    return duice.confirm(message);
}

/**
 * prompt
 * @param message
 * @param type
 * @returns {*}
 */
const _prompt = function(message, type) {
    return duice.prompt(message, type);
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
    _startProgress();
    return globalThis.fetch(url, options)
        .then(async function(response){
            console.debug(response);

            // bypass
            if(_bypass){
                return response;
            }

            // checks response
            if(response.ok) {
                return response;
            }else{
                let responseJson = await response.json();
                console.warn(responseJson);
                let message = responseJson.message;
                _alert(message).then();
                throw Error(message);
            }
        })
        .catch((error)=>{
            _alert(error).then();
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
        let totalCount = contentRange.split(' ')[1].split('/')[1];
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
    if(_getCookie('color-scheme') === 'dark') {
        return true;
    }
    if(_getCookie('color-scheme') === 'light') {
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
        document.documentElement.classList.add('dark');
        _setCookie('color-scheme', 'dark', 356);
    }else{
        document.documentElement.classList.remove('dark');
        _setCookie('color-scheme', 'light', 356);
    }
}

// set color scheme
//_setDarkMode(_isDarkMode());

/**
 * Opens link
 */
const _openLink = function(linkUrl, linkTarget){
    if(linkTarget === '_blank'){
        window.open(linkUrl,'_blank');
    }else{
        window.location.href = linkUrl;
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
function _changeLanguage(language) {
    if(language) {
        let url = new URL(document.location.href);
        url.searchParams.delete('_language');
        url.searchParams.append('_language', language);
        document.location.href = url.href;
    }
}


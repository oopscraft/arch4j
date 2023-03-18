/**
 * _isDarkMode
 * @returns {boolean}
 * @private
 */
function _isDarkMode() {

    // checks cookie
    if(duice.getCookie('color-scheme') === 'dark') {
        return true;
    }
    if(duice.getCookie('color-scheme') === 'light') {
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
function _setDarkMode(enable) {
    if(enable){
        document.documentElement.classList.add('dark');
        duice.setCookie('color-scheme', 'dark', 356);
    }else{
        document.documentElement.classList.remove('dark');
        duice.setCookie('color-scheme', 'light', 356);
    }
}

// set color scheme
_setDarkMode(_isDarkMode());
/*!
 * duice - v0.2.42
 * git: https://gitbub.com/chomookun/duice
 * website: https://duice.chomookun.com
 * Released under the LGPL(GNU Lesser General Public License version 3) License
 */
var duice = (function (exports) {
    'use strict';

    class Configuration {
        static setNamespace(value) {
            this.namespace = value;
        }
        static getNamespace() {
            return this.namespace;
        }
    }
    Configuration.namespace = 'duice';

    function getElementQuerySelector() {
        let namespace = Configuration.getNamespace();
        return `*[data-${namespace}-bind]:not([data-${namespace}-id])`;
    }
    function markInitialized(container) {
        container.querySelectorAll(getElementQuerySelector()).forEach(element => {
            setElementAttribute(element, 'id', '_');
        });
    }
    function findVariable(context, name) {
        // find in context
        try {
            let object = new Function(`return this.${name};`).call(context);
            if (object) {
                return object;
            }
        }
        catch (ignore) { }
        // find in global
        try {
            let object = new Function(`return ${name};`).call(null);
            if (object) {
                return object;
            }
        }
        catch (ignore) { }
        // throw error
        console.warn(`Object[${name}] is not found`);
        return undefined;
    }
    function runCode(code, htmlElement, context) {
        try {
            let args = [];
            let values = [];
            for (let property in context) {
                args.push(property);
                values.push(context[property]);
            }
            return Function(...args, code).call(htmlElement, ...values);
        }
        catch (e) {
            console.error(code, e);
            throw e;
        }
    }
    function runIfCode(htmlElement, context) {
        let ifClause = getElementAttribute(htmlElement, 'if');
        if (ifClause) {
            let result = runCode(ifClause, htmlElement, context);
            if (!result) {
                htmlElement.hidden = true;
            }
            else {
                htmlElement.hidden = false;
            }
        }
    }
    function runExecuteCode(htmlElement, context) {
        let script = getElementAttribute(htmlElement, 'execute');
        if (script) {
            runCode(script, htmlElement, context);
        }
    }
    function hasElementAttribute(htmlElement, name) {
        let namespace = Configuration.getNamespace();
        return htmlElement.hasAttribute(`data-${namespace}-${name}`);
    }
    function getElementAttribute(htmlElement, name) {
        let namespace = Configuration.getNamespace();
        return htmlElement.getAttribute(`data-${namespace}-${name}`);
    }
    function setElementAttribute(htmlElement, name, value) {
        let namespace = Configuration.getNamespace();
        htmlElement.setAttribute(`data-${namespace}-${name}`, value);
    }
    function assert(condition, message) {
        console.assert(condition, message);
        if (!condition) {
            throw new Error(message || 'Assertion Failed');
        }
    }

    class DataElementFactory {
    }

    class Observable {
        constructor() {
            this.observers = [];
            this.notifyEnabled = true;
        }
        addObserver(observer) {
            this.observers.push(observer);
        }
        removeObserver(observer) {
            for (let i = 0, size = this.observers.length; i < size; i++) {
                if (this.observers[i] === observer) {
                    this.observers.splice(i, 1);
                    return;
                }
            }
        }
        suspendNotify() {
            this.notifyEnabled = false;
        }
        resumeNotify() {
            this.notifyEnabled = true;
        }
        notifyObservers(dataEvent) {
            if (this.notifyEnabled) {
                this.observers.forEach(observer => {
                    observer.update(this, dataEvent);
                });
            }
        }
    }

    class DataElement extends Observable {
        constructor(htmlElement, bindData, context) {
            var _a;
            super();
            this.htmlElement = htmlElement;
            this.bindData = bindData;
            this.context = context;
            setElementAttribute(this.htmlElement, 'id', this.generateId());
            // bind data
            let dataHandler = (_a = globalThis.Object.getOwnPropertyDescriptor(this.bindData, '_handler_')) === null || _a === void 0 ? void 0 : _a.value;
            assert(dataHandler, 'DataHandler is not found');
            this.addObserver(dataHandler);
            dataHandler.addObserver(this);
        }
        generateId() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        }
        getHtmlElement() {
            return this.htmlElement;
        }
        getContext() {
            return this.context;
        }
        getBindData() {
            return this.bindData;
        }
    }

    class DataEvent {
        constructor(source) {
            this.source = source;
        }
    }

    var __awaiter$5 = (undefined && undefined.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    class DataHandler extends Observable {
        constructor() {
            super();
            this.readonlyAll = false;
            this.readonly = new Set();
            this.disableAll = false;
            this.disable = new Set();
            this.listenerEnabled = true;
        }
        setTarget(target) {
            this.target = target;
        }
        getTarget() {
            return this.target;
        }
        setReadonlyAll(readonly) {
            this.readonlyAll = readonly;
            if (!readonly) {
                this.readonly.clear();
            }
            this.notifyObservers(new DataEvent(this));
        }
        isReadonlyAll() {
            return this.readonlyAll;
        }
        setReadonly(property, readonly) {
            if (readonly) {
                this.readonly.add(property);
            }
            else {
                this.readonly.delete(property);
            }
            this.notifyObservers(new DataEvent(this));
        }
        isReadonly(property) {
            return this.readonlyAll || this.readonly.has(property);
        }
        setDisableAll(disable) {
            this.disableAll = disable;
            if (!disable) {
                this.disable.clear();
            }
            this.notifyObservers(new DataEvent(this));
        }
        isDisableAll() {
            return this.disableAll;
        }
        setDisable(property, disable) {
            if (disable) {
                this.disable.add(property);
            }
            else {
                this.disable.delete(property);
            }
            this.notifyObservers(new DataEvent(this));
        }
        isDisable(property) {
            return this.disableAll || this.disable.has(property);
        }
        suspendListener() {
            this.listenerEnabled = false;
        }
        resumeListener() {
            this.listenerEnabled = true;
        }
        checkListener(listener, event) {
            return __awaiter$5(this, void 0, void 0, function* () {
                if (this.listenerEnabled && listener) {
                    let result = yield listener.call(this.getTarget(), event);
                    if (result == false) {
                        return false;
                    }
                }
                return true;
            });
        }
    }

    class StringFormat {
        constructor(pattern) {
            this.pattern = pattern;
        }
        format(value) {
            if (!value) {
                return value;
            }
            let encodedValue = '';
            let patternChars = this.pattern.split('');
            let valueChars = value.split('');
            let valueCharsPosition = 0;
            for (let i = 0, size = patternChars.length; i < size; i++) {
                let patternChar = patternChars[i];
                if (patternChar === '#') {
                    encodedValue += valueChars[valueCharsPosition++] || '';
                }
                else {
                    encodedValue += patternChar;
                }
                if (valueCharsPosition >= valueChars.length) {
                    break;
                }
            }
            return encodedValue;
        }
        parse(value) {
            if (!value) {
                return value;
            }
            let decodedValue = '';
            let patternChars = this.pattern.split('');
            let valueChars = value.split('');
            let valueCharsPosition = 0;
            for (let i = 0, size = patternChars.length; i < size; i++) {
                let patternChar = patternChars[i];
                if (patternChar === '#') {
                    decodedValue += valueChars[valueCharsPosition++] || '';
                }
                else {
                    valueCharsPosition++;
                }
            }
            return decodedValue;
        }
    }

    class NumberFormat {
        constructor(scale) {
            this.scale = 0;
            this.scale = scale;
        }
        format(number) {
            if (isNaN(Number(number))) {
                return '';
            }
            number = Number(number);
            let string;
            if (this.scale > 0) {
                string = String(number.toFixed(this.scale));
            }
            else {
                string = String(number);
            }
            let reg = /(^[+-]?\d+)(\d{3})/;
            while (reg.test(string)) {
                string = string.replace(reg, '$1' + ',' + '$2');
            }
            return string;
        }
        parse(string) {
            if (!string) {
                return null;
            }
            if (string.length === 1 && /[+-]/.test(string)) {
                string += '0';
            }
            string = string.replace(/,/gi, '');
            if (isNaN(Number(string))) {
                throw 'NaN';
            }
            let number = Number(string);
            if (this.scale > 0) {
                number = Number(number.toFixed(this.scale));
            }
            return number;
        }
    }

    class DateFormat {
        constructor(pattern) {
            this.patternRex = /yyyy|yy|MM|dd|HH|hh|mm|ss/gi;
            this.pattern = pattern;
        }
        format(string) {
            if (!string) {
                return '';
            }
            let date = new Date(string);
            string = this.pattern.replace(this.patternRex, function ($1) {
                switch ($1) {
                    case "yyyy":
                        return date.getFullYear();
                    case "yy":
                        return String(date.getFullYear() % 1000).padStart(2, '0');
                    case "MM":
                        return String(date.getMonth() + 1).padStart(2, '0');
                    case "dd":
                        return String(date.getDate()).padStart(2, '0');
                    case "HH":
                        return String(date.getHours()).padStart(2, '0');
                    case "hh":
                        return String(date.getHours() <= 12 ? date.getHours() : date.getHours() % 12).padStart(2, '0');
                    case "mm":
                        return String(date.getMinutes()).padStart(2, '0');
                    case "ss":
                        return String(date.getSeconds()).padStart(2, '0');
                    default:
                        return $1;
                }
            });
            return string;
        }
        parse(string) {
            if (!string) {
                return null;
            }
            let date = new Date(0, 0, 0, 0, 0, 0);
            let match;
            while ((match = this.patternRex.exec(this.pattern)) != null) {
                let formatString = match[0];
                let formatIndex = match.index;
                let formatLength = formatString.length;
                let matchValue = string.substr(formatIndex, formatLength);
                matchValue = matchValue.padEnd(formatLength, '0');
                switch (formatString) {
                    case 'yyyy': {
                        let fullYear = parseInt(matchValue);
                        date.setFullYear(fullYear);
                        break;
                    }
                    case 'yy': {
                        let yyValue = parseInt(matchValue);
                        let yearPrefix = Math.floor(new Date().getFullYear() / 100);
                        let fullYear = yearPrefix * 100 + yyValue;
                        date.setFullYear(fullYear);
                        break;
                    }
                    case 'MM': {
                        let monthValue = parseInt(matchValue);
                        date.setMonth(monthValue - 1);
                        break;
                    }
                    case 'dd': {
                        let dateValue = parseInt(matchValue);
                        date.setDate(dateValue);
                        break;
                    }
                    case 'HH': {
                        let hoursValue = parseInt(matchValue);
                        date.setHours(hoursValue);
                        break;
                    }
                    case 'hh': {
                        let hoursValue = parseInt(matchValue);
                        date.setHours(hoursValue > 12 ? (hoursValue + 12) : hoursValue);
                        break;
                    }
                    case 'mm': {
                        let minutesValue = parseInt(matchValue);
                        date.setMinutes(minutesValue);
                        break;
                    }
                    case 'ss': {
                        let secondsValue = parseInt(matchValue);
                        date.setSeconds(secondsValue);
                        break;
                    }
                }
            }
            // timezone offset
            let tzo = new Date().getTimezoneOffset() * -1, dif = tzo >= 0 ? '+' : '-', pad = function (num) {
                return (num < 10 ? '0' : '') + num;
            };
            // return iso string
            return date.getFullYear() +
                '-' + pad(date.getMonth() + 1) +
                '-' + pad(date.getDate()) +
                'T' + pad(date.getHours()) +
                ':' + pad(date.getMinutes()) +
                ':' + pad(date.getSeconds()) +
                dif + pad(Math.floor(Math.abs(tzo) / 60)) +
                ':' + pad(Math.abs(tzo) % 60);
        }
    }

    class FormatFactory {
        static getFormat(format) {
            let args;
            const match = format.match(/^([a-zA-Z_][a-zA-Z0-9_]*)\((.*)\)$/);
            if (match) {
                match[1];
                args = match[2].split(',').map(arg => arg.trim().replace(/^['"]|['"]$/g, ''));
            }
            if (format.startsWith('string')) {
                return new StringFormat(args[0]);
            }
            if (format.startsWith('number')) {
                return new NumberFormat(args[0]);
            }
            if (format.startsWith('date')) {
                return new DateFormat(args[0]);
            }
            return null;
        }
    }

    class ObjectElement extends DataElement {
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            // attributes
            this.property = getElementAttribute(htmlElement, 'property');
            let format = getElementAttribute(htmlElement, 'format');
            if (format) {
                this.format = FormatFactory.getFormat(format);
            }
        }
        getProperty() {
            return this.property;
        }
        getFormat() {
            return this.format;
        }
        render() {
            // check if
            this.checkIf();
            if (this.property) {
                let objectHandler = ObjectProxy.getHandler(this.getBindData());
                // set value
                let value = objectHandler.getValue(this.property);
                this.setValue(value);
                // set readonly
                let readonly = objectHandler.isReadonly(this.property);
                this.setReadonly(readonly);
                // set disable
                let disable = objectHandler.isDisable(this.property);
                this.setDisable(disable);
            }
            // executes script
            this.executeScript();
        }
        checkIf() {
            let context = Object.assign({}, this.getContext());
            let bind = getElementAttribute(this.getHtmlElement(), 'bind');
            let bindSplit = bind.split('.');
            if (bindSplit.length > 1) {
                context[bindSplit[0]] = findVariable(context, bindSplit[0]);
            }
            else {
                context[bind] = this.getBindData();
            }
            runIfCode(this.htmlElement, context);
        }
        executeScript() {
            let context = Object.assign({}, this.getContext());
            let bind = getElementAttribute(this.getHtmlElement(), 'bind');
            let bindSplit = bind.split('.');
            if (bindSplit.length > 1) {
                context[bindSplit[0]] = findVariable(context, bindSplit[0]);
            }
            else {
                context[bind] = this.getBindData();
            }
            runExecuteCode(this.htmlElement, context);
        }
        update(observable, event) {
            console.debug('ObjectElement.update', observable, event);
            // ObjectHandler
            if (observable instanceof ObjectHandler) {
                // check if
                this.checkIf();
                if (this.property) {
                    // set value
                    this.setValue(observable.getValue(this.property));
                    // set readonly
                    this.setReadonly(observable.isReadonly(this.property));
                    // set disable
                    this.setDisable(observable.isDisable(this.property));
                }
                // executes script
                this.executeScript();
            }
        }
        setValue(value) {
            if (value !== undefined && value !== null) {
                value = this.getFormat() ? this.getFormat().format(value) : value;
                this.htmlElement.innerText = value;
            }
            else {
                this.htmlElement.innerText = '';
            }
        }
        getValue() {
            let value = this.htmlElement.innerText;
            if (value && value.trim().length > 0) {
                value = this.getFormat() ? this.getFormat().parse(value) : value;
            }
            else {
                value = null;
            }
            return value;
        }
        setReadonly(readonly) {
            // no-op
        }
        setDisable(disable) {
            // no-op
        }
        getIndex() {
            let index = getElementAttribute(this.htmlElement, 'index');
            if (index) {
                return Number(index);
            }
        }
        focus() {
            // no-ops
            return false;
        }
    }

    class PropertyChangeEvent extends DataEvent {
        constructor(source, property, value, index) {
            super(source);
            this.property = property;
            this.value = value;
            this.index = index;
        }
        getProperty() {
            return this.property;
        }
        getValue() {
            return this.value;
        }
        getIndex() {
            return this.index;
        }
    }

    var __awaiter$4 = (undefined && undefined.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    class ObjectHandler extends DataHandler {
        constructor() {
            super();
        }
        get(target, property, receiver) {
            return Reflect.get(target, property, receiver);
        }
        set(target, property, value) {
            // change value
            Reflect.set(target, property, value);
            // notify
            let event = new PropertyChangeEvent(this, property, value);
            this.notifyObservers(event);
            // returns
            return true;
        }
        update(observable, event) {
            return __awaiter$4(this, void 0, void 0, function* () {
                console.debug("ObjectHandler.update", observable, event);
                // Element
                if (observable instanceof ObjectElement) {
                    let property = observable.getProperty();
                    let value = observable.getValue();
                    if (yield this.checkListener(this.propertyChangingListener, event)) {
                        this.setValue(property, value);
                        yield this.checkListener(this.propertyChangedListener, event);
                    }
                }
                // notify
                this.notifyObservers(event);
            });
        }
        getValue(property) {
            property = property.replace('.', '?.');
            return new Function(`return this.${property};`).call(this.getTarget());
        }
        setValue(property, value) {
            new Function('value', `this.${property} = value;`).call(this.getTarget(), value);
        }
        focus(property) {
            this.observers.forEach(observer => {
                if (observer instanceof ObjectElement) {
                    if (observer.getProperty() === property) {
                        if (observer.focus()) {
                            return false;
                        }
                    }
                }
            });
        }
    }

    class ItemInsertEvent extends DataEvent {
        constructor(source, index, items) {
            super(source);
            this.items = [];
            this.index = index;
            this.items = items;
        }
        getIndex() {
            return this.index;
        }
        getItems() {
            return this.items;
        }
    }

    class ItemSelectEvent extends DataEvent {
        constructor(source, index) {
            super(source);
            this.index = index;
        }
        getIndex() {
            return this.index;
        }
    }

    class ItemMoveEvent extends DataEvent {
        constructor(source, fromIndex, toIndex) {
            super(source);
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }
        getFromIndex() {
            return this.fromIndex;
        }
        getToIndex() {
            return this.toIndex;
        }
    }

    class ItemDeleteEvent extends DataEvent {
        constructor(source, index, items) {
            super(source);
            this.items = [];
            this.index = index;
            this.items = items;
        }
        getIndex() {
            return this.index;
        }
        getItems() {
            return this.items;
        }
    }

    var __awaiter$3 = (undefined && undefined.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    class ArrayHandler extends DataHandler {
        constructor() {
            super();
        }
        get(target, property, receiver) {
            let _this = this;
            const value = target[property];
            if (typeof value === 'function') {
                // push, unshift
                if (['push', 'unshift'].includes(property)) {
                    return function () {
                        var arguments_1 = arguments;
                        return __awaiter$3(this, void 0, void 0, function* () {
                            let index;
                            if (property === 'push') {
                                index = receiver['length'];
                            }
                            else if (property === 'unshift') {
                                index = 0;
                            }
                            let rows = [];
                            for (let i in arguments_1) {
                                rows.push(arguments_1[i]);
                            }
                            yield _this.insertItem(target, index, ...rows);
                            return target.length;
                        });
                    };
                }
                // splice
                if (['splice'].includes(property)) {
                    return function () {
                        var arguments_2 = arguments;
                        return __awaiter$3(this, void 0, void 0, function* () {
                            // parse arguments
                            let start = arguments_2[0];
                            let deleteCount = arguments_2[1];
                            let deleteRows = [];
                            for (let i = start; i < (start + deleteCount); i++) {
                                deleteRows.push(target[i]);
                            }
                            let insertRows = [];
                            for (let i = 2; i < arguments_2.length; i++) {
                                insertRows.push(arguments_2[i]);
                            }
                            // delete rows
                            if (deleteCount > 0) {
                                yield _this.deleteItem(target, start, deleteCount);
                            }
                            // insert rows
                            if (insertRows.length > 0) {
                                yield _this.insertItem(target, start, ...insertRows);
                            }
                            // returns deleted rows
                            return deleteRows;
                        });
                    };
                }
                // pop, shift
                if (['pop', 'shift'].includes(property)) {
                    return function () {
                        return __awaiter$3(this, void 0, void 0, function* () {
                            let index;
                            if (property === 'pop') {
                                index = receiver['length'] - 1;
                            }
                            else if (property === 'shift') {
                                index = 0;
                            }
                            let rows = [target[index]];
                            yield _this.deleteItem(target, index);
                            return rows;
                        });
                    };
                }
                // bind
                return value.bind(target);
            }
            // return
            return value;
        }
        set(target, property, value) {
            Reflect.set(target, property, value);
            if (property === 'length') {
                this.notifyObservers(new DataEvent(this));
            }
            return true;
        }
        update(observable, event) {
            return __awaiter$3(this, void 0, void 0, function* () {
                console.debug("ArrayHandler.update", observable, event);
                // instance is array component
                if (observable instanceof ArrayElement) {
                    // row select event
                    if (event instanceof ItemSelectEvent) {
                        this.selectedItemIndex = event.getIndex();
                        return;
                    }
                    // row move event
                    if (event instanceof ItemMoveEvent) {
                        let object = this.getTarget().splice(event.getFromIndex(), 1)[0];
                        this.getTarget().splice(event.getToIndex(), 0, object);
                    }
                }
                // notify observers
                this.notifyObservers(event);
            });
        }
        insertItem(arrayProxy, index, ...rows) {
            return __awaiter$3(this, void 0, void 0, function* () {
                let arrayHandler = ArrayProxy.getHandler(arrayProxy);
                let proxyTarget = ArrayProxy.getTarget(arrayProxy);
                rows.forEach((object, index) => {
                    let objectProxy = new ObjectProxy(object);
                    let objectHandler = ObjectProxy.getHandler(objectProxy);
                    objectHandler.propertyChangingListener = this.propertyChangingListener;
                    objectHandler.propertyChangedListener = this.propertyChangedListener;
                    rows[index] = objectProxy;
                });
                let event = new ItemInsertEvent(this, index, rows);
                if (yield arrayHandler.checkListener(arrayHandler.rowInsertingListener, event)) {
                    proxyTarget.splice(index, 0, ...rows);
                    yield arrayHandler.checkListener(arrayHandler.rowInsertedListener, event);
                    arrayHandler.notifyObservers(event);
                }
            });
        }
        deleteItem(arrayProxy, index, size) {
            return __awaiter$3(this, void 0, void 0, function* () {
                let arrayHandler = ArrayProxy.getHandler(arrayProxy);
                let proxyTarget = ArrayProxy.getTarget(arrayProxy);
                let sliceBegin = index;
                let sliceEnd = (size ? index + size : index + 1);
                let rows = proxyTarget.slice(sliceBegin, sliceEnd);
                let event = new ItemDeleteEvent(this, index, rows);
                if (yield arrayHandler.checkListener(arrayHandler.rowDeletingListener, event)) {
                    let spliceStart = index;
                    let spliceDeleteCount = (size ? size : 1);
                    proxyTarget.splice(spliceStart, spliceDeleteCount);
                    yield arrayHandler.checkListener(arrayHandler.rowDeletedListener, event);
                    arrayHandler.notifyObservers(event);
                }
            });
        }
        appendItem(arrayProxy, ...rows) {
            return __awaiter$3(this, void 0, void 0, function* () {
                let index = arrayProxy.length;
                return this.insertItem(arrayProxy, index, ...rows);
            });
        }
        selectItem(index) {
            this.selectedItemIndex = index;
            // notify row select event
            let rowSelectEvent = new ItemSelectEvent(this, this.selectedItemIndex);
            this.notifyObservers(rowSelectEvent);
        }
        getSelectedItemIndex() {
            return this.selectedItemIndex;
        }
    }

    class ArrayProxy extends globalThis.Array {
        /**
         * constructor
         * @param array
         */
        constructor(array) {
            super();
            // is already proxy
            if (ArrayProxy.isProxy(array)) {
                return array;
            }
            // array handler
            let arrayHandler = new ArrayHandler();
            // copy array elements
            if (globalThis.Array.isArray(array)) {
                for (let i = 0; i < array.length; i++) {
                    array[i] = new ObjectProxy(array[i]);
                }
            }
            // create proxy
            let arrayProxy = new Proxy(array, arrayHandler);
            arrayHandler.setTarget(array);
            // set property
            ArrayProxy.setHandler(arrayProxy, arrayHandler);
            ArrayProxy.setTarget(arrayProxy, array);
            // save
            ArrayProxy.save(arrayProxy);
            // returns
            return arrayProxy;
        }
        static isProxy(array) {
            return array.hasOwnProperty('_target_');
        }
        static setTarget(arrayProxy, target) {
            globalThis.Object.defineProperty(arrayProxy, '_target_', {
                value: target,
                writable: true
            });
        }
        static getTarget(arrayProxy) {
            return globalThis.Object.getOwnPropertyDescriptor(arrayProxy, '_target_').value;
        }
        static setHandler(arrayProxy, arrayHandler) {
            globalThis.Object.defineProperty(arrayProxy, '_handler_', {
                value: arrayHandler,
                writable: true
            });
        }
        static getHandler(arrayProxy) {
            let handler = globalThis.Object.getOwnPropertyDescriptor(arrayProxy, '_handler_').value;
            assert(handler, 'handler is not found');
            return handler;
        }
        /**
         * Assigns array to array proxy
         * @param arrayProxy
         * @param array
         */
        static assign(arrayProxy, array) {
            let arrayHandler = this.getHandler(arrayProxy);
            try {
                // suspend
                arrayHandler.suspendListener();
                arrayHandler.suspendNotify();
                // clears elements
                arrayProxy.length = 0;
                // creates elements
                array.forEach((object, index) => {
                    let objectProxy = new ObjectProxy(object);
                    arrayProxy[index] = objectProxy;
                    // readonly
                    ObjectProxy.setReadonlyAll(objectProxy, arrayHandler.isReadonlyAll());
                    arrayHandler.readonly.forEach(property => {
                        ObjectProxy.setReadonly(objectProxy, property, true);
                    });
                    // disable
                    ObjectProxy.setDisableAll(objectProxy, arrayHandler.isDisableAll());
                    arrayHandler.disable.forEach(property => {
                        ObjectProxy.setDisable(objectProxy, property, true);
                    });
                    // add listener
                    ObjectProxy.onPropertyChanging(objectProxy, arrayHandler.propertyChangingListener);
                    ObjectProxy.onPropertyChanged(objectProxy, arrayHandler.propertyChangedListener);
                });
            }
            finally {
                // resume
                arrayHandler.resumeListener();
                arrayHandler.resumeNotify();
            }
            // notify observers
            arrayHandler.notifyObservers(new DataEvent(this));
        }
        /**
         * Clears array elements
         * @param arrayProxy
         */
        static clear(arrayProxy) {
            let arrayHandler = this.getHandler(arrayProxy);
            try {
                // suspend
                arrayHandler.suspendListener();
                arrayHandler.suspendNotify();
                // clear element
                arrayProxy.length = 0;
            }
            finally {
                // resume
                arrayHandler.resumeListener();
                arrayHandler.resumeNotify();
            }
            // notify observers
            arrayHandler.notifyObservers(new DataEvent(this));
        }
        /**
         * Save array proxy
         * @param arrayProxy
         */
        static save(arrayProxy) {
            let origin = JSON.stringify(arrayProxy);
            globalThis.Object.defineProperty(arrayProxy, '_origin_', {
                value: origin,
                writable: true
            });
        }
        /**
         * Reset array proxy
         * @param arrayProxy
         */
        static reset(arrayProxy) {
            let origin = JSON.parse(globalThis.Object.getOwnPropertyDescriptor(arrayProxy, '_origin_').value);
            this.assign(arrayProxy, origin);
        }
        static setReadonly(arrayProxy, property, readonly) {
            this.getHandler(arrayProxy).setReadonly(property, readonly);
            arrayProxy.forEach(objectProxy => {
                ObjectProxy.setReadonly(objectProxy, property, readonly);
            });
        }
        static isReadonly(arrayProxy, property) {
            return this.getHandler(arrayProxy).isReadonly(property);
        }
        static setReadonlyAll(arrayProxy, readonly) {
            this.getHandler(arrayProxy).setReadonlyAll(readonly);
            arrayProxy.forEach(objectProxy => {
                ObjectProxy.setReadonlyAll(objectProxy, readonly);
            });
        }
        static isReadonlyAll(arrayProxy) {
            return this.getHandler(arrayProxy).isReadonlyAll();
        }
        static setDisable(arrayProxy, property, disable) {
            this.getHandler(arrayProxy).setDisable(property, disable);
            arrayProxy.forEach(objectProxy => {
                ObjectProxy.setDisable(objectProxy, property, disable);
            });
        }
        static isDisable(arrayProxy, property) {
            return this.getHandler(arrayProxy).isDisable(property);
        }
        static setDisableAll(arrayProxy, disable) {
            this.getHandler(arrayProxy).setDisableAll(disable);
            arrayProxy.forEach(objectProxy => {
                ObjectProxy.setDisableAll(objectProxy, disable);
            });
        }
        static isDisableAll(arrayProxy) {
            return this.getHandler(arrayProxy).isDisableAll();
        }
        static selectItem(arrayProxy, index) {
            return this.getHandler(arrayProxy).selectItem(index);
        }
        static getSelectedItemIndex(arrayProxy) {
            return this.getHandler(arrayProxy).getSelectedItemIndex();
        }
        static onPropertyChanging(arrayProxy, listener) {
            this.getHandler(arrayProxy).propertyChangingListener = listener;
            arrayProxy.forEach(objectProxy => {
                ObjectProxy.getHandler(objectProxy).propertyChangingListener = listener;
            });
        }
        static onPropertyChanged(arrayProxy, listener) {
            this.getHandler(arrayProxy).propertyChangedListener = listener;
            arrayProxy.forEach(objectProxy => {
                ObjectProxy.getHandler(objectProxy).propertyChangedListener = listener;
            });
        }
        static onRowInserting(arrayProxy, listener) {
            this.getHandler(arrayProxy).rowInsertingListener = listener;
        }
        static onRowInserted(arrayProxy, listener) {
            this.getHandler(arrayProxy).rowInsertedListener = listener;
        }
        static onRowDeleting(arrayProxy, listener) {
            this.getHandler(arrayProxy).rowDeletingListener = listener;
        }
        static onRowDeleted(arrayProxy, listener) {
            this.getHandler(arrayProxy).rowDeletedListener = listener;
        }
    }

    class ObjectProxy extends globalThis.Object {
        /**
         * constructor
         * @param object
         */
        constructor(object) {
            super();
            // is already proxy
            if (ObjectProxy.isProxy(object)) {
                return object;
            }
            // object handler
            let objectHandler = new ObjectHandler();
            // copy property
            for (let name in object) {
                let value = object[name];
                // value is array
                if (Array.isArray(value)) {
                    let arrayProxy = new ArrayProxy(value);
                    ArrayProxy.getHandler(arrayProxy).addObserver(objectHandler);
                    object[name] = arrayProxy;
                    continue;
                }
                // value is object
                if (value != null && typeof value === 'object') {
                    let objectProxy = new ObjectProxy(value);
                    ObjectProxy.getHandler(objectProxy).addObserver(objectHandler);
                    object[name] = objectProxy;
                    continue;
                }
                // value is primitive
                object[name] = value;
            }
            // delete not exists property
            for (let name in object) {
                if (!Object.keys(object).includes(name)) {
                    delete this[name];
                }
            }
            // creates proxy
            let objectProxy = new Proxy(object, objectHandler);
            objectHandler.setTarget(object);
            // set property
            ObjectProxy.setHandler(objectProxy, objectHandler);
            ObjectProxy.setTarget(objectProxy, object);
            // save
            ObjectProxy.save(objectProxy);
            // returns
            return objectProxy;
        }
        static isProxy(object) {
            return object.hasOwnProperty('_target_');
        }
        static setTarget(objectProxy, target) {
            globalThis.Object.defineProperty(objectProxy, '_target_', {
                value: target,
                writable: true
            });
        }
        static getTarget(objectProxy) {
            return globalThis.Object.getOwnPropertyDescriptor(objectProxy, '_target_').value;
        }
        static setHandler(objectProxy, objectHandler) {
            globalThis.Object.defineProperty(objectProxy, '_handler_', {
                value: objectHandler,
                writable: true
            });
        }
        static getHandler(objectProxy) {
            let handler = globalThis.Object.getOwnPropertyDescriptor(objectProxy, '_handler_').value;
            assert(handler, 'handler is not found');
            return handler;
        }
        /**
         * Assign object to object proxy
         * @param objectProxy
         * @param object
         */
        static assign(objectProxy, object) {
            let objectHandler = this.getHandler(objectProxy);
            try {
                // suspend
                objectHandler.suspendListener();
                objectHandler.suspendNotify();
                // loop object properties
                for (let name in object) {
                    let value = object[name];
                    // source value is array
                    if (Array.isArray(value)) {
                        if (Array.isArray(objectProxy[name])) {
                            ArrayProxy.assign(objectProxy[name], value);
                        }
                        else {
                            objectProxy[name] = new ArrayProxy(value);
                        }
                        continue;
                    }
                    // source value is object
                    if (value != null && typeof value === 'object') {
                        if (objectProxy[name] != null && typeof objectProxy[name] === 'object') {
                            ObjectProxy.assign(objectProxy[name], value);
                        }
                        else {
                            let objectProxy = new ObjectProxy(value);
                            ObjectProxy.getHandler(objectProxy).addObserver(objectHandler);
                            objectProxy[name] = objectProxy;
                        }
                        continue;
                    }
                    // source value is primitive
                    objectProxy[name] = value;
                }
            }
            finally {
                // resume
                objectHandler.resumeListener();
                objectHandler.resumeNotify();
            }
            // notify observers
            objectHandler.notifyObservers(new DataEvent(this));
        }
        /**
         * Clear object properties
         * @param objectProxy
         */
        static clear(objectProxy) {
            let objectHandler = this.getHandler(objectProxy);
            try {
                // suspend
                objectHandler.suspendListener();
                objectHandler.suspendNotify();
                // clear properties
                for (let name in objectProxy) {
                    let value = objectProxy[name];
                    if (Array.isArray(value)) {
                        ArrayProxy.clear(value);
                        continue;
                    }
                    if (value != null && typeof value === 'object') {
                        ObjectProxy.clear(value);
                        continue;
                    }
                    objectProxy[name] = null;
                }
            }
            finally {
                // resume
                objectHandler.resumeListener();
                objectHandler.resumeNotify();
            }
            // notify observers
            objectHandler.notifyObservers(new DataEvent(this));
        }
        /**
         * Save object properties
         * @param objectProxy
         */
        static save(objectProxy) {
            let origin = JSON.stringify(objectProxy);
            globalThis.Object.defineProperty(objectProxy, '_origin_', {
                value: origin,
                writable: true
            });
        }
        /**
         * Reset object properties
         * @param objectProxy
         */
        static reset(objectProxy) {
            let origin = JSON.parse(globalThis.Object.getOwnPropertyDescriptor(objectProxy, '_origin_').value);
            this.assign(objectProxy, origin);
        }
        /**
         * Set property to be readonly
         * @param objectProxy
         * @param property
         * @param readonly
         */
        static setReadonly(objectProxy, property, readonly) {
            this.getHandler(objectProxy).setReadonly(property, readonly);
        }
        /**
         * Get whether property is readonly
         * @param objectProxy
         * @param property
         */
        static isReadonly(objectProxy, property) {
            return this.getHandler(objectProxy).isReadonly(property);
        }
        /**
         * Set all properties to be readonly
         * @param objectProxy
         * @param readonly
         */
        static setReadonlyAll(objectProxy, readonly) {
            this.getHandler(objectProxy).setReadonlyAll(readonly);
        }
        /**
         * Get whether all properties are readonly
         * @param objectProxy
         */
        static isReadonlyAll(objectProxy) {
            return this.getHandler(objectProxy).isReadonlyAll();
        }
        /**
         * Set object to be disabled
         * @param objectProxy
         * @param property
         * @param disable
         */
        static setDisable(objectProxy, property, disable) {
            this.getHandler(objectProxy).setDisable(property, disable);
        }
        /**
         * Get whether property is disabled
         * @param objectProxy
         * @param property
         */
        static isDisable(objectProxy, property) {
            return this.getHandler(objectProxy).isDisable(property);
        }
        /**
         * Set all properties to be disabled
         * @param objectProxy
         * @param disable
         */
        static setDisableAll(objectProxy, disable) {
            this.getHandler(objectProxy).setDisableAll(disable);
        }
        /**
         * Get whether all properties are disabled
         * @param objectProxy
         */
        static isDisableAll(objectProxy) {
            return this.getHandler(objectProxy).isDisableAll();
        }
        /**
         * Set property to be focused
         * @param objectProxy
         * @param property
         */
        static focus(objectProxy, property) {
            this.getHandler(objectProxy).focus(property);
        }
        /**
         * Set readonly before changing event listener
         * @param objectProxy
         * @param listener
         */
        static onPropertyChanging(objectProxy, listener) {
            this.getHandler(objectProxy).propertyChangingListener = listener;
        }
        /**
         * Set property after changed event listener
         * @param objectProxy
         * @param listener
         */
        static onPropertyChanged(objectProxy, listener) {
            this.getHandler(objectProxy).propertyChangedListener = listener;
        }
    }

    var __awaiter$2 = (undefined && undefined.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    class ArrayElement extends DataElement {
        constructor(htmlElement, bindData, context) {
            super(htmlElement.cloneNode(true), bindData, context);
            this.editable = false;
            this.slot = document.createElement('slot');
            this.itemHtmlElements = [];
            // attributes
            this.loop = getElementAttribute(htmlElement, 'loop');
            this.hierarchy = getElementAttribute(htmlElement, 'hierarchy');
            this.editable = (getElementAttribute(htmlElement, 'editable') === 'true');
            this.selectedItemClass = getElementAttribute(htmlElement, 'selected-item-class');
            // replace with slot for position
            htmlElement.replaceWith(this.slot);
            // mark initialized (not using after clone as templates)
            markInitialized(htmlElement);
        }
        render() {
            var _a;
            let arrayProxy = this.getBindData();
            // reset row elements
            this.itemHtmlElements.forEach(rowElement => {
                rowElement.parentNode.removeChild(rowElement);
            });
            this.itemHtmlElements.length = 0;
            // loop
            if (this.loop) {
                let loopArgs = this.loop.split(',');
                let itemName = loopArgs[0].trim();
                let statusName = (_a = loopArgs[1]) === null || _a === void 0 ? void 0 : _a.trim();
                // hierarchy loop
                if (this.hierarchy) {
                    let hierarchyArray = this.hierarchy.split(',');
                    let idName = hierarchyArray[0];
                    let parentIdName = hierarchyArray[1];
                    //let index = -1;
                    const _this = this;
                    // visit function
                    let visit = function (array, parentId, depth) {
                        for (let index = 0; index < array.length; index++) {
                            const object = array[index];
                            if (object[parentIdName] === parentId) {
                                // context
                                let context = Object.assign({}, _this.getContext());
                                context[itemName] = object;
                                context[statusName] = new ObjectProxy({
                                    index: index,
                                    count: index + 1,
                                    size: arrayProxy.length,
                                    first: (index === 0),
                                    last: (arrayProxy.length == index + 1),
                                    depth: depth
                                });
                                // create row element
                                _this.createItemHtmlElement(index, object, context);
                                // visit child elements
                                let id = object[idName];
                                visit(array, id, depth + 1);
                            }
                        }
                    };
                    // start visit
                    visit(arrayProxy, null, 0);
                }
                // default loop
                else {
                    // normal
                    for (let index = 0; index < arrayProxy.length; index++) {
                        // element data
                        let object = arrayProxy[index];
                        // context
                        let context = Object.assign({}, this.getContext());
                        context[itemName] = object;
                        context[statusName] = new ObjectProxy({
                            index: index,
                            count: index + 1,
                            size: arrayProxy.length,
                            first: (index === 0),
                            last: (arrayProxy.length == index + 1)
                        });
                        // create row element
                        this.createItemHtmlElement(index, object, context);
                    }
                }
            }
            // not loop
            else {
                // initialize
                let itemHtmlElement = this.getHtmlElement().cloneNode(true);
                let context = Object.assign({}, this.getContext());
                Initializer.initialize(itemHtmlElement, this.getContext());
                this.itemHtmlElements.push(itemHtmlElement);
                // append to slot
                // this.slot.appendChild(itemHtmlElement);
                this.slot.parentNode.insertBefore(itemHtmlElement, this.slot);
                // check if
                runIfCode(itemHtmlElement, context);
                // execute script
                runExecuteCode(itemHtmlElement, context);
            }
        }
        createItemHtmlElement(index, object, context) {
            // clones row elements
            let itemHtmlElement = this.getHtmlElement().cloneNode(true);
            // adds embedded attribute
            setElementAttribute(itemHtmlElement, 'index', index.toString());
            // editable
            let _this = this;
            if (this.editable) {
                itemHtmlElement.setAttribute('draggable', 'true');
                itemHtmlElement.addEventListener('dragstart', function (e) {
                    let fromIndex = getElementAttribute(this, 'index');
                    e.dataTransfer.setData("text", fromIndex);
                });
                itemHtmlElement.addEventListener('dragover', function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                });
                itemHtmlElement.addEventListener('drop', function (e) {
                    return __awaiter$2(this, void 0, void 0, function* () {
                        e.preventDefault();
                        e.stopPropagation();
                        let fromIndex = parseInt(e.dataTransfer.getData('text'));
                        let toIndex = parseInt(getElementAttribute(this, 'index'));
                        let itemMoveEvent = new ItemMoveEvent(_this, fromIndex, toIndex);
                        _this.notifyObservers(itemMoveEvent);
                    });
                });
            }
            // initializes row element
            Initializer.initialize(itemHtmlElement, context, index);
            this.itemHtmlElements.push(itemHtmlElement);
            // insert into slot
            this.slot.parentNode.insertBefore(itemHtmlElement, this.slot);
            // check if clause
            runIfCode(itemHtmlElement, context);
            // execute script
            runExecuteCode(itemHtmlElement, context);
            // selectable
            itemHtmlElement.addEventListener('click', e => {
                // selected class
                if (this.selectedItemClass) {
                    this.itemHtmlElements.forEach(element => {
                        element.classList.remove(this.selectedItemClass);
                    });
                    e.currentTarget.classList.add(this.selectedItemClass);
                    e.stopPropagation();
                }
                // trigger row select event
                let rowSelectEvent = new ItemSelectEvent(this, index);
                this.notifyObservers(rowSelectEvent);
            });
        }
        update(observable, event) {
            console.debug('ArrayElement.update', observable, event);
            if (observable instanceof ArrayHandler) {
                // row select event
                if (event instanceof ItemSelectEvent) {
                    if (this.selectedItemClass) {
                        this.itemHtmlElements.forEach(el => el.classList.remove(this.selectedItemClass));
                        let index = event.getIndex();
                        if (index >= 0) {
                            this.itemHtmlElements.forEach(itemHtmlElement => {
                                if (itemHtmlElement.dataset.duiceIndex === event.getIndex().toString()) {
                                    itemHtmlElement.classList.add(this.selectedItemClass);
                                }
                            });
                        }
                    }
                    return;
                }
                // render
                this.render();
            }
        }
    }

    class ArrayElementFactory extends DataElementFactory {
        createElement(htmlElement, bindData, context) {
            return new ArrayElement(htmlElement, bindData, context);
        }
    }

    class CustomElementFactory extends DataElementFactory {
        createElement(htmlElement, bindData, context) {
            return this.doCreateElement(htmlElement, bindData, context);
        }
    }

    class ObjectElementFactory extends DataElementFactory {
        createElement(htmlElement, bindData, context) {
            return new ObjectElement(htmlElement, bindData, context);
        }
    }

    class DataElementRegistry {
        static register(tagName, elementFactory) {
            if (elementFactory instanceof ArrayElementFactory) {
                this.arrayElementFactories.set(tagName, elementFactory);
            }
            else if (elementFactory instanceof ObjectElementFactory) {
                this.objectElementFactories.set(tagName, elementFactory);
            }
            else if (elementFactory instanceof CustomElementFactory) {
                this.customElementFactories.set(tagName, elementFactory);
            }
            // register custom html element
            if (tagName.includes('-')) {
                globalThis.customElements.define(tagName, class extends HTMLElement {
                });
            }
        }
        static getFactory(htmlElement, bindData, context) {
            let tagName = htmlElement.tagName.toLowerCase();
            // custom element
            if (this.customElementFactories.has(tagName)) {
                return this.customElementFactories.get(tagName);
            }
            // array element
            if (Array.isArray(bindData)) {
                if (this.arrayElementFactories.has(tagName)) {
                    return this.arrayElementFactories.get(tagName);
                }
                return this.defaultArrayElementFactory;
            }
            // object element
            else {
                if (this.objectElementFactories.has(tagName)) {
                    return this.objectElementFactories.get(tagName);
                }
                return this.defaultObjectElementFactory;
            }
        }
    }
    DataElementRegistry.defaultObjectElementFactory = new ObjectElementFactory();
    DataElementRegistry.defaultArrayElementFactory = new ArrayElementFactory();
    DataElementRegistry.objectElementFactories = new Map();
    DataElementRegistry.arrayElementFactories = new Map();
    DataElementRegistry.customElementFactories = new Map();

    class Initializer {
        static initialize(container, context, index) {
            // scan DOM tree
            container.querySelectorAll(getElementQuerySelector()).forEach(element => {
                var _a, _b;
                // if (element instanceof HTMLElement) {
                const htmlElement = element;
                if (!hasElementAttribute(htmlElement, 'id')) {
                    try {
                        let bindName = getElementAttribute(htmlElement, 'bind');
                        let bindData = findVariable(context, bindName);
                        (_b = (_a = DataElementRegistry.getFactory(htmlElement, bindData, context)) === null || _a === void 0 ? void 0 : _a.createElement(htmlElement, bindData, context)) === null || _b === void 0 ? void 0 : _b.render();
                        // index
                        if (index !== undefined) {
                            setElementAttribute(htmlElement, "index", index.toString());
                        }
                    }
                    catch (e) {
                        console.error(e, htmlElement, container, JSON.stringify(context));
                    }
                }
                // }
            });
        }
    }

    class CustomElement extends DataElement {
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
        }
        render() {
            // do render
            this.doRender(this.getBindData());
            // check if
            runIfCode(this.getHtmlElement(), this.getContext());
            // initialize
            Initializer.initialize(this.getHtmlElement(), this.getContext());
            // execute script
            runExecuteCode(this.getHtmlElement(), this.getContext());
        }
        update(observable, event) {
            if (observable instanceof DataHandler) {
                this.doUpdate(observable.getTarget());
            }
        }
    }

    var __awaiter$1 = (undefined && undefined.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    class ImgElement extends ObjectElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
            this.editable = false;
            this.closeButtonImg = 'data:image/svg+xml;base64,' + window.btoa('<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path opacity="0.4" d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" fill="#292D32"></path> <path d="M13.0594 12.0001L15.3594 9.70011C15.6494 9.41011 15.6494 8.93011 15.3594 8.64011C15.0694 8.35011 14.5894 8.35011 14.2994 8.64011L11.9994 10.9401L9.69937 8.64011C9.40937 8.35011 8.92937 8.35011 8.63938 8.64011C8.34938 8.93011 8.34938 9.41011 8.63938 9.70011L10.9394 12.0001L8.63938 14.3001C8.34938 14.5901 8.34938 15.0701 8.63938 15.3601C8.78938 15.5101 8.97937 15.5801 9.16937 15.5801C9.35937 15.5801 9.54937 15.5101 9.69937 15.3601L11.9994 13.0601L14.2994 15.3601C14.4494 15.5101 14.6394 15.5801 14.8294 15.5801C15.0194 15.5801 15.2094 15.5101 15.3594 15.3601C15.6494 15.0701 15.6494 14.5901 15.3594 14.3001L13.0594 12.0001Z" fill="#292D32"></path> </g></svg>');
            this.originSrc = String(this.getHtmlElement().src);
            // editable
            this.editable = (getElementAttribute(this.getHtmlElement(), 'editable') === 'true');
            if (this.editable) {
                // add click event listener
                this.getHtmlElement().style.cursor = 'pointer';
                this.getHtmlElement().addEventListener('click', event => {
                    this.changeImage();
                });
                // create clear button
                this.clearButton = document.createElement('img');
                this.clearButton.src = this.closeButtonImg;
                this.clearButton.style.cursor = 'pointer';
                this.clearButton.style.width = '16px';
                this.clearButton.style.height = '16px';
                this.clearButton.addEventListener('mouseout', event => {
                    this.hideClearImageButton();
                }, true);
                this.clearButton.addEventListener('click', event => {
                    this.clearImage();
                });
                // mouse over
                this.getHtmlElement().addEventListener('mouseover', event => {
                    this.showClearImageButton();
                }, true);
                // mouse over
                this.getHtmlElement().addEventListener('mouseout', event => {
                    // related target is overlay button
                    if (event.relatedTarget === this.clearButton) {
                        return;
                    }
                    this.hideClearImageButton();
                }, true);
            }
            // size
            let size = getElementAttribute(this.getHtmlElement(), 'size');
            if (size) {
                let sizeArgs = size.split(',');
                this.width = parseInt(sizeArgs[0].trim());
                this.height = parseInt(sizeArgs[1].trim());
            }
        }
        showClearImageButton() {
            this.getHtmlElement().parentNode.insertBefore(this.clearButton, this.getHtmlElement().nextSibling);
            this.clearButton.style.position = 'absolute';
            this.clearButton.style.zIndex = '100';
        }
        hideClearImageButton() {
            this.getHtmlElement().parentNode.removeChild(this.clearButton);
        }
        clearImage() {
            if (this.originSrc) {
                this.getHtmlElement().src = this.originSrc;
            }
            else {
                this.getHtmlElement().src = null;
            }
            // notify observers
            let event = new PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
            this.notifyObservers(event);
        }
        changeImage() {
            let input = document.createElement('input');
            input.setAttribute("type", "file");
            input.setAttribute("accept", "image/gif, image/jpeg, image/png");
            let _this = this;
            input.addEventListener('change', function (e) {
                let fileReader = new FileReader();
                if (this.files && this.files[0]) {
                    fileReader.addEventListener("load", (e) => __awaiter$1(this, void 0, void 0, function* () {
                        let image = e.target.result;
                        let value;
                        if (_this.width && _this.height) {
                            value = yield _this.convertImage(image, _this.width, _this.height);
                        }
                        else {
                            value = yield _this.convertImage(image);
                        }
                        _this.setValue(value);
                        // notify observers
                        let event = new PropertyChangeEvent(_this, _this.getProperty(), _this.getValue(), _this.getIndex());
                        _this.notifyObservers(event);
                    }));
                    fileReader.readAsDataURL(this.files[0]);
                }
                e.preventDefault();
                e.stopPropagation();
            });
            input.click();
        }
        convertImage(dataUrl, width, height) {
            return new Promise(function (resolve, reject) {
                try {
                    let canvas = document.createElement("canvas");
                    let ctx = canvas.getContext("2d");
                    let image = new Image();
                    image.onload = function () {
                        if (width && height) {
                            canvas.width = width;
                            canvas.height = height;
                            ctx.drawImage(image, 0, 0, width, height);
                        }
                        else {
                            canvas.width = image.naturalWidth;
                            canvas.height = image.naturalHeight;
                            ctx.drawImage(image, 0, 0);
                        }
                        let dataUrl = canvas.toDataURL("image/png");
                        resolve(dataUrl);
                    };
                    image.src = dataUrl;
                }
                catch (e) {
                    reject(e);
                }
            });
        }
        setValue(value) {
            if (value) {
                this.getHtmlElement().src = value;
            }
            else {
                this.getHtmlElement().src = this.originSrc;
            }
        }
        getValue() {
            let value = this.getHtmlElement().src;
            if (value === this.originSrc) {
                return null;
            }
            else {
                return value;
            }
        }
        setReadonly(readonly) {
            this.getHtmlElement().style.pointerEvents = (readonly ? 'none' : 'unset');
        }
        setDisable(disable) {
            this.getHtmlElement().style.pointerEvents = (disable ? 'none' : 'unset');
        }
    }

    class ImgElementFactory extends ObjectElementFactory {
        createElement(element, bindData, context) {
            return new ImgElement(element, bindData, context);
        }
    }
    (() => {
        // register factory instance
        DataElementRegistry.register('img', new ImgElementFactory());
    })();

    class InputElement extends ObjectElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
            // adds change listener
            this.getHtmlElement().addEventListener('change', e => {
                let event = new PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                this.notifyObservers(event);
            }, true);
            // turn off autocomplete
            this.getHtmlElement().setAttribute('autocomplete', 'off');
        }
        setValue(value) {
            if (value) {
                value = this.getFormat() ? this.getFormat().format(value) : value;
            }
            else {
                value = '';
            }
            this.getHtmlElement().value = value;
        }
        getValue() {
            let value = this.getHtmlElement().value;
            if (value) {
                value = this.getFormat() ? this.getFormat().parse(value) : value;
            }
            else {
                value = null;
            }
            return value;
        }
        setReadonly(readonly) {
            this.getHtmlElement().readOnly = readonly;
        }
        setDisable(disable) {
            if (disable) {
                this.getHtmlElement().setAttribute('disabled', 'disabled');
            }
            else {
                this.getHtmlElement().removeAttribute('disabled');
            }
        }
        focus() {
            this.getHtmlElement().focus();
            return true;
        }
    }

    class InputNumberElement extends InputElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
            // changes type and style
            this.getHtmlElement().setAttribute('type', 'text');
            this.getHtmlElement().style.textAlign = 'right';
            // prevents invalid key press
            this.getHtmlElement().addEventListener('keypress', event => {
                if (/[\d|\.|,]/.test(event.key) === false) {
                    event.preventDefault();
                }
            });
        }
        getValue() {
            let value = super.getValue();
            return Number(value);
        }
    }

    class InputCheckboxElement extends InputElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
            this.trueValue = true;
            this.falseValue = false;
            // true false value
            let trueValue = getElementAttribute(this.getHtmlElement(), 'true-value');
            this.trueValue = trueValue ? trueValue : this.trueValue;
            let falseValue = getElementAttribute(this.getHtmlElement(), 'false-value');
            this.falseValue = falseValue ? falseValue : this.falseValue;
        }
        setValue(value) {
            if (value === this.trueValue) {
                this.getHtmlElement().checked = true;
            }
            else {
                this.htmlElement.checked = false;
            }
        }
        getValue() {
            if (this.htmlElement.checked) {
                return this.trueValue;
            }
            else {
                return this.falseValue;
            }
        }
        disableClick(event) {
            event.preventDefault();
        }
        setReadonly(readonly) {
            if (readonly) {
                this.getHtmlElement().addEventListener('click', this.disableClick);
            }
            else {
                this.getHtmlElement().removeEventListener('click', this.disableClick);
            }
        }
    }

    class InputDatetimeLocalElement extends InputElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
            this.dateFormat = new DateFormat('yyyy-MM-ddTHH:mm');
        }
        setValue(value) {
            if (value) {
                this.getHtmlElement().value = this.dateFormat.format(value);
            }
            else {
                this.getHtmlElement().value = '';
            }
        }
        getValue() {
            let value = this.getHtmlElement().value;
            if (value) {
                return new Date(value).toISOString();
            }
            else {
                return null;
            }
        }
    }

    class InputRadioElement extends InputElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
        }
        setValue(value) {
            this.getHtmlElement().checked = (this.getHtmlElement().value === value);
        }
        getValue() {
            return this.getHtmlElement().value;
        }
        setReadonly(readonly) {
            if (readonly) {
                this.getHtmlElement().style.pointerEvents = 'none';
            }
            else {
                this.getHtmlElement().style.pointerEvents = '';
            }
        }
    }

    class InputElementFactory extends ObjectElementFactory {
        createElement(element, bindData, context) {
            let type = element.getAttribute('type');
            switch (type) {
                case 'number':
                    return new InputNumberElement(element, bindData, context);
                case 'checkbox':
                    return new InputCheckboxElement(element, bindData, context);
                case 'radio':
                    return new InputRadioElement(element, bindData, context);
                case 'datetime-local':
                    return new InputDatetimeLocalElement(element, bindData, context);
                default:
                    return new InputElement(element, bindData, context);
            }
        }
    }
    (() => {
        // register factory instance
        DataElementRegistry.register('input', new InputElementFactory());
    })();

    class SelectElement extends ObjectElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
            this.defaultOptions = [];
            // adds event listener
            this.getHtmlElement().addEventListener('change', () => {
                let event = new PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                this.notifyObservers(event);
            }, true);
            // stores default option
            for (let i = 0; i < this.getHtmlElement().options.length; i++) {
                this.defaultOptions.push(this.getHtmlElement().options[i]);
            }
            // option property
            let optionName = getElementAttribute(this.getHtmlElement(), 'option');
            if (optionName) {
                this.option = findVariable(this.getContext(), optionName);
                this.optionValueProperty = getElementAttribute(this.getHtmlElement(), 'option-value-property');
                this.optionTextProperty = getElementAttribute(this.getHtmlElement(), 'option-text-property');
                ArrayProxy.getHandler(this.option).addObserver(this);
                this.updateOptions();
            }
        }
        updateOptions() {
            let value = this.getHtmlElement().value;
            this.getHtmlElement().innerHTML = '';
            this.defaultOptions.forEach(defaultOption => {
                this.getHtmlElement().appendChild(defaultOption);
            });
            this.option.forEach(data => {
                let option = document.createElement('option');
                option.value = data[this.optionValueProperty];
                option.appendChild(document.createTextNode(data[this.optionTextProperty]));
                this.getHtmlElement().appendChild(option);
            });
            this.getHtmlElement().value = value;
        }
        update(observable, dataEvent) {
            super.update(observable, dataEvent);
            if (this.option) {
                this.updateOptions();
            }
        }
        setValue(value) {
            this.getHtmlElement().value = value;
            // force select option
            if (!value) {
                for (let i = 0; i < this.getHtmlElement().options.length; i++) {
                    let option = this.getHtmlElement().options[i];
                    if (!option.nodeValue) {
                        option.selected = true;
                        break;
                    }
                }
            }
        }
        getValue() {
            let value = this.getHtmlElement().value;
            if (!value || value.trim().length < 1) {
                value = null;
            }
            return value;
        }
        setReadonly(readonly) {
            if (readonly) {
                console.warn("==ok");
                this.getHtmlElement().style.pointerEvents = 'none';
            }
            else {
                this.getHtmlElement().style.pointerEvents = '';
            }
        }
        setDisable(disable) {
            if (disable) {
                this.getHtmlElement().setAttribute('disabled', 'disabled');
            }
            else {
                this.getHtmlElement().removeAttribute('disabled');
            }
        }
    }

    class SelectElementFactory extends ObjectElementFactory {
        createElement(element, bindData, context) {
            return new SelectElement(element, bindData, context);
        }
    }
    (() => {
        // register factory instance
        DataElementRegistry.register('select', new SelectElementFactory());
    })();

    class TextareaElement extends ObjectElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
            // adds change event listener
            this.getHtmlElement().addEventListener('change', e => {
                let event = new PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                this.notifyObservers(event);
            }, true);
        }
        setValue(value) {
            if (value) {
                this.getHtmlElement().value = value;
            }
            else {
                this.getHtmlElement().value = '';
            }
        }
        getValue() {
            let value = this.getHtmlElement().value;
            if (value != null && value.length > 0) {
                return value;
            }
            else {
                return null;
            }
        }
        setReadonly(readonly) {
            if (readonly) {
                this.getHtmlElement().setAttribute('readonly', 'readonly');
            }
            else {
                this.getHtmlElement().removeAttribute('readonly');
            }
        }
        setDisable(disable) {
            if (disable) {
                this.getHtmlElement().setAttribute('disabled', 'disabled');
            }
            else {
                this.getHtmlElement().removeAttribute('disabled');
            }
        }
    }

    class TextareaElementFactory extends ObjectElementFactory {
        createElement(element, bindData, context) {
            return new TextareaElement(element, bindData, context);
        }
    }
    (() => {
        // register
        DataElementRegistry.register('textarea', new TextareaElementFactory());
    })();

    var __awaiter = (undefined && undefined.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    class Dialog {
        constructor(dialogElement) {
            this.dialogElement = dialogElement;
            let _this = this;
            // dialog fixed style
            this.dialogElement.style.position = 'absolute';
            this.dialogElement.style.left = '0';
            this.dialogElement.style.right = '0';
            // header
            this.header = document.createElement('div');
            this.dialogElement.appendChild(this.header);
            this.header.style.display = 'flex';
            this.header.style.justifyContent = 'end';
            this.header.style.lineHeight = '2rem';
            this.header.style.position = 'absolute';
            this.header.style.left = '0';
            this.header.style.top = '0';
            this.header.style.width = '100%';
            this.header.style.cursor = 'pointer';
            // drag
            this.header.onmousedown = function (event) {
                let pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
                pos3 = event.clientX;
                pos4 = event.clientY;
                window.document.onmouseup = function (event) {
                    window.document.onmousemove = null;
                    window.document.onmouseup = null;
                };
                window.document.onmousemove = function (event) {
                    pos1 = pos3 - event.clientX;
                    pos2 = pos4 - event.clientY;
                    pos3 = event.clientX;
                    pos4 = event.clientY;
                    _this.dialogElement.style.left = (_this.dialogElement.offsetLeft - pos1) + 'px';
                    _this.dialogElement.style.top = (_this.dialogElement.offsetTop - pos2) + 'px';
                };
            };
            // creates close button
            this.closeButton = document.createElement('span');
            this.closeButton.appendChild(document.createTextNode('X'));
            this.closeButton.style.marginRight = '0.5rem';
            this.closeButton.style.fontFamily = 'monospace';
            this.closeButton.addEventListener('click', event => {
                _this.hide();
                _this.close();
            });
            this.dialogElement.addEventListener('scroll', () => {
                const scrollTop = this.dialogElement.scrollTop;
                this.header.style.top = `${scrollTop}px`;
            });
            this.header.appendChild(this.closeButton);
            // on resize event
            window.addEventListener('resize', function (event) {
                _this.moveToCenterPosition();
            });
        }
        onOpening(listener) {
            this.openingListener = listener;
            return this;
        }
        onOpened(listener) {
            this.openedListener = listener;
            return this;
        }
        onClosing(listener) {
            this.closingListener = listener;
            return this;
        }
        onClosed(listener) {
            this.closedListener = listener;
            return this;
        }
        moveToCenterPosition() {
            let computedStyle = window.getComputedStyle(this.dialogElement);
            let computedWidth = parseInt(computedStyle.getPropertyValue('width').replace(/px/gi, ''));
            let computedHeight = parseInt(computedStyle.getPropertyValue('height').replace(/px/gi, ''));
            let scrollX = window.scrollX;
            let scrollY = window.scrollY;
            this.dialogElement.style.left = Math.max(0, window.innerWidth / 2 - computedWidth / 2) + scrollX + 'px';
            this.dialogElement.style.top = Math.max(0, window.innerHeight / 3 - computedHeight / 3) + scrollY + 'px';
        }
        getDialogElement() {
            return this.dialogElement;
        }
        show() {
            // saves current scroll position
            let scrollX = window.scrollX;
            let scrollY = window.scrollY;
            // show dialog modal
            this.dialogElement.style.opacity = '0';
            window.document.body.appendChild(this.dialogElement);
            this.dialogElement.showModal();
            // restore previous scroll position
            window.scrollTo(scrollX, scrollY);
            // adjusting position
            this.moveToCenterPosition();
            // fade in
            let _this = this;
            (function fade() {
                let val = parseFloat(_this.dialogElement.style.opacity);
                if (!((val += .1) > 1)) {
                    _this.dialogElement.style.opacity = String(val);
                    requestAnimationFrame(fade);
                }
            })();
        }
        hide() {
            // closes modal
            this.dialogElement.close();
        }
        open() {
            return __awaiter(this, void 0, void 0, function* () {
                // opening listener
                if (this.openingListener) {
                    if (this.openingListener.call(this) == false) {
                        return;
                    }
                }
                // show modal
                this.show();
                // opened listener
                if (this.openedListener) {
                    this.openedListener.call(this);
                }
                // creates promise
                let _this = this;
                this.promise = new Promise(function (resolve, reject) {
                    _this.promiseResolve = resolve;
                    _this.promiseReject = reject;
                });
                return this.promise;
            });
        }
        close(...args) {
            // closing listener
            if (this.closingListener) {
                if (this.closingListener.call(this) == false) {
                    return;
                }
            }
            // closed listener
            if (this.closedListener) {
                this.closedListener.call(this);
            }
            // resolve
            this.hide();
            this.promiseResolve(...args);
        }
    }

    class AlertDialog extends Dialog {
        constructor(message) {
            super(document.createElement('dialog'));
            this.getDialogElement().style.padding = '1rem';
            this.getDialogElement().style.minWidth = '20rem';
            this.getDialogElement().style.textAlign = 'center';
            // message pre
            this.messagePre = document.createElement('pre');
            this.messagePre.style.marginTop = '1rem';
            this.messagePre.style.marginBottom = '1rem';
            this.messagePre.innerHTML = message;
            this.getDialogElement().appendChild(this.messagePre);
            // confirm button
            this.confirmButton = document.createElement('button');
            this.confirmButton.appendChild(document.createTextNode('Yes'));
            this.confirmButton.style.width = '4rem';
            this.confirmButton.style.cursor = 'pointer';
            this.confirmButton.addEventListener('click', event => {
                this.confirm();
            });
            this.getDialogElement().appendChild(this.confirmButton);
        }
        open() {
            let promise = super.open();
            this.confirmButton.focus();
            return promise;
        }
        confirm() {
            super.close();
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        close() {
            super.close();
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
    }

    class ConfirmDialog extends Dialog {
        constructor(message) {
            super(document.createElement('dialog'));
            this.getDialogElement().style.padding = '1rem';
            this.getDialogElement().style.minWidth = '20rem';
            this.getDialogElement().style.textAlign = 'center';
            // message pre
            this.messagePre = document.createElement('pre');
            this.messagePre.style.marginTop = '1rem';
            this.messagePre.style.marginBottom = '1rem';
            this.messagePre.innerHTML = message;
            this.getDialogElement().appendChild(this.messagePre);
            // cancel button
            this.cancelButton = document.createElement('button');
            this.cancelButton.appendChild(document.createTextNode('No'));
            this.cancelButton.style.width = '4rem';
            this.cancelButton.style.cursor = 'pointer';
            this.cancelButton.addEventListener('click', event => {
                this.cancel();
            });
            this.getDialogElement().appendChild(this.cancelButton);
            // divider
            this.getDialogElement().appendChild(document.createTextNode(' '));
            // confirm button
            this.confirmButton = document.createElement('button');
            this.confirmButton.appendChild(document.createTextNode('Yes'));
            this.confirmButton.style.width = '4rem';
            this.confirmButton.style.cursor = 'pointer';
            this.confirmButton.addEventListener('click', event => {
                this.confirm();
            });
            this.getDialogElement().appendChild(this.confirmButton);
        }
        open() {
            let promise = super.open();
            this.confirmButton.focus();
            return promise;
        }
        close(...args) {
            super.close(false);
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        confirm() {
            super.close(true);
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        cancel() {
            super.close(false);
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
    }

    class PromptDialog extends Dialog {
        constructor(message, type) {
            super(document.createElement('dialog'));
            this.getDialogElement().style.padding = '1rem';
            this.getDialogElement().style.minWidth = '20rem';
            this.getDialogElement().style.textAlign = 'center';
            // message pre
            this.messagePre = document.createElement('pre');
            this.messagePre.style.marginTop = '1rem';
            this.messagePre.style.marginBottom = '1rem';
            this.messagePre.innerHTML = message;
            this.getDialogElement().appendChild(this.messagePre);
            // prompt input
            this.promptInput = document.createElement('input');
            this.promptInput.style.textAlign = 'center';
            this.promptInput.style.marginBottom = '1rem';
            this.promptInput.style.width = '100%';
            if (type) {
                this.promptInput.type = type;
            }
            this.getDialogElement().appendChild(this.promptInput);
            // cancel button
            this.cancelButton = document.createElement('button');
            this.cancelButton.appendChild(document.createTextNode('No'));
            this.cancelButton.style.width = '4rem';
            this.cancelButton.style.cursor = 'pointer';
            this.cancelButton.addEventListener('click', event => {
                this.cancel();
            });
            this.getDialogElement().appendChild(this.cancelButton);
            // divider
            this.getDialogElement().appendChild(document.createTextNode(' '));
            // confirm button
            this.confirmButton = document.createElement('button');
            this.confirmButton.appendChild(document.createTextNode('Yes'));
            this.confirmButton.style.width = '4rem';
            this.confirmButton.style.cursor = 'pointer';
            this.confirmButton.addEventListener('click', event => {
                this.confirm(this.promptInput.value);
            });
            this.getDialogElement().appendChild(this.confirmButton);
        }
        open() {
            let promise = super.open();
            this.promptInput.focus();
            return promise;
        }
        close(...args) {
            super.close();
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        confirm(value) {
            super.close(value);
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
        cancel() {
            super.close();
            this.getDialogElement().parentNode.removeChild(this.getDialogElement());
        }
    }

    class TabFolder {
        constructor() {
            this.items = [];
        }
        addItem(item) {
            item.setTabFolder(this);
            item.setTabIndex(this.items.length);
            this.items.push(item);
        }
        setActive(index) {
            for (let i = 0; i < this.items.length; i++) {
                this.items[i].setActive(i === index);
            }
        }
    }

    class TabItem {
        constructor(button, content, listener) {
            this.button = button;
            this.content = content;
            this.listener = listener;
            // default style
            button.style.cursor = 'pointer';
            // add listener
            let _this = this;
            button.addEventListener('click', () => {
                _this.tabFolder.setActive(_this.tabIndex);
            });
            // set de-active
            this.setActive(false);
        }
        setTabFolder(tabFolder) {
            this.tabFolder = tabFolder;
        }
        setTabIndex(tabIndex) {
            this.tabIndex = tabIndex;
        }
        setActive(active) {
            if (active) {
                this.button.style.opacity = 'unset';
                this.content.removeAttribute('hidden');
                this.listener.call(this);
            }
            else {
                this.button.style.opacity = '0.5';
                this.content.setAttribute('hidden', String(true));
            }
        }
    }

    //  listens DOMContentLoaded event
    if (globalThis.document) {
        // initialize elements
        document.addEventListener("DOMContentLoaded", event => {
            Initializer.initialize(document.documentElement, {});
        });
    }

    exports.AlertDialog = AlertDialog;
    exports.ArrayElement = ArrayElement;
    exports.ArrayElementFactory = ArrayElementFactory;
    exports.ArrayProxy = ArrayProxy;
    exports.Configuration = Configuration;
    exports.ConfirmDialog = ConfirmDialog;
    exports.CustomElement = CustomElement;
    exports.CustomElementFactory = CustomElementFactory;
    exports.DataElementRegistry = DataElementRegistry;
    exports.Dialog = Dialog;
    exports.ImgElementFactory = ImgElementFactory;
    exports.Initializer = Initializer;
    exports.InputElementFactory = InputElementFactory;
    exports.ObjectElement = ObjectElement;
    exports.ObjectElementFactory = ObjectElementFactory;
    exports.ObjectProxy = ObjectProxy;
    exports.PromptDialog = PromptDialog;
    exports.SelectElementFactory = SelectElementFactory;
    exports.TabFolder = TabFolder;
    exports.TabItem = TabItem;
    exports.TextareaElementFactory = TextareaElementFactory;
    exports.assert = assert;
    exports.findVariable = findVariable;
    exports.getElementAttribute = getElementAttribute;
    exports.getElementQuerySelector = getElementQuerySelector;
    exports.hasElementAttribute = hasElementAttribute;
    exports.markInitialized = markInitialized;
    exports.runCode = runCode;
    exports.runExecuteCode = runExecuteCode;
    exports.runIfCode = runIfCode;
    exports.setElementAttribute = setElementAttribute;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({});
//# sourceMappingURL=duice.js.map

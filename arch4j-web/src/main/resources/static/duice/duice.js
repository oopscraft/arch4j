var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var duice;
(function (duice) {
    /**
     * Observable
     */
    class Observable {
        constructor() {
            this.observers = [];
            this.notifyEnabled = true;
        }
        /**
         * addObserver
         * @param observer
         */
        addObserver(observer) {
            this.observers.push(observer);
        }
        /**
         * removeObserver
         * @param observer
         */
        removeObserver(observer) {
            for (let i = 0, size = this.observers.length; i < size; i++) {
                if (this.observers[i] === observer) {
                    this.observers.splice(i, 1);
                    return;
                }
            }
        }
        /**
         * suspend notify
         */
        suspendNotify() {
            this.notifyEnabled = false;
        }
        /**
         * resume notify
         */
        resumeNotify() {
            this.notifyEnabled = true;
        }
        /**
         * notifyObservers
         * @param event
         */
        notifyObservers(event) {
            if (this.notifyEnabled) {
                this.observers.forEach(observer => {
                    observer.update(this, event);
                });
            }
        }
    }
    duice.Observable = Observable;
})(duice || (duice = {}));
///<reference path="Observable.ts"/>
var duice;
(function (duice) {
    /**
     * element abstract class
     */
    class DataElement extends duice.Observable {
        /**
         * constructor
         * @param htmlElement
         * @param context
         * @protected
         */
        constructor(htmlElement, context) {
            super();
            this.htmlElement = htmlElement;
            this.context = context;
            duice.setElementAttribute(this.htmlElement, 'id', duice.generateId());
        }
        /**
         * return HTML element
         */
        getHtmlElement() {
            return this.htmlElement;
        }
        /**
         * return context
         */
        getContext() {
            return this.context;
        }
        /**
         * set data
         * @param dataName
         */
        setData(dataName) {
            var _a;
            // finds proxy data
            let data = duice.findVariable(this.context, dataName);
            // bind with data handler
            let dataHandler = (_a = globalThis.Object.getOwnPropertyDescriptor(data, '_handler_')) === null || _a === void 0 ? void 0 : _a.value;
            duice.assert(dataHandler, 'DataHandler is not found');
            this.addObserver(dataHandler);
            dataHandler.addObserver(this);
            // set data
            this.data = dataHandler.getTarget();
        }
        /**
         * return data
         */
        getData() {
            return this.data;
        }
        /**
         * execute script if exists
         * @param htmlElement
         * @param context
         */
        executeScript(htmlElement, context) {
            let script = duice.getElementAttribute(htmlElement, 'script');
            if (script) {
                duice.executeScript(script, htmlElement, context);
            }
        }
    }
    duice.DataElement = DataElement;
})(duice || (duice = {}));
///<reference path="DataElement.ts"/>
var duice;
(function (duice) {
    /**
     * array element class
     */
    class ArrayElement extends duice.DataElement {
        /**
         * constructor
         * @param htmlElement
         * @param context
         */
        constructor(htmlElement, context) {
            super(htmlElement.cloneNode(true), context);
            this.slot = document.createElement('slot');
            this.editable = false;
            this.rowHtmlElements = [];
            // replace with slot for position
            htmlElement.replaceWith(this.slot);
            // mark initialized (not using after clone as templates)
            duice.markInitialized(htmlElement);
        }
        /**
         * set array
         * @param arrayName
         */
        setArray(arrayName) {
            this.setData(arrayName);
        }
        /**
         * set loop
         * @param loop
         */
        setLoop(loop) {
            this.loop = loop;
        }
        /**
         * set editable
         * @param editable
         */
        setEditable(editable) {
            this.editable = editable;
        }
        /**
         * set hierarchy
         * @param hierarchy
         */
        setHierarchy(hierarchy) {
            this.hierarchy = hierarchy;
        }
        /**
         * render
         */
        render() {
            var _a;
            let _this = this;
            let arrayProxy = this.getData();
            // reset row elements
            this.rowHtmlElements.forEach(rowElement => {
                rowElement.parentNode.removeChild(rowElement);
            });
            this.rowHtmlElements.length = 0;
            // loop
            if (this.loop) {
                let loopArgs = this.loop.split(',');
                let itemName = loopArgs[0].trim();
                let statusName = (_a = loopArgs[1]) === null || _a === void 0 ? void 0 : _a.trim();
                for (let index = 0; index < arrayProxy.length; index++) {
                    // context
                    let context = globalThis.Object.assign({}, this.context);
                    context[itemName] = arrayProxy[index];
                    context[statusName] = new duice.ObjectProxy({
                        index: index,
                        count: index + 1,
                        size: arrayProxy.length,
                        first: (index === 0),
                        last: (arrayProxy.length == index + 1)
                    });
                    // clones row elements
                    let rowHtmlElement = this.getHtmlElement().cloneNode(true);
                    // adds embedded attribute
                    duice.setElementAttribute(rowHtmlElement, 'index', index.toString());
                    if (this.hierarchy) {
                        let hierarchyArray = this.hierarchy.split(',');
                        duice.setElementAttribute(rowHtmlElement, 'hierarchy-id', arrayProxy[index][hierarchyArray[0]]);
                        duice.setElementAttribute(rowHtmlElement, 'hierarchy-pid', arrayProxy[index][hierarchyArray[1]]);
                    }
                    // editable
                    if (this.editable) {
                        rowHtmlElement.setAttribute('draggable', 'true');
                        rowHtmlElement.addEventListener('dragstart', function (e) {
                            let fromIndex = duice.getElementAttribute(this, 'index');
                            e.dataTransfer.setData("text", fromIndex);
                        });
                        rowHtmlElement.addEventListener('dragover', function (e) {
                            e.preventDefault();
                            e.stopPropagation();
                        });
                        rowHtmlElement.addEventListener('drop', function (e) {
                            return __awaiter(this, void 0, void 0, function* () {
                                e.preventDefault();
                                e.stopPropagation();
                                let fromIndex = parseInt(e.dataTransfer.getData('text'));
                                let toIndex = parseInt(duice.getElementAttribute(this, 'index'));
                                let rowIndexChangeEvent = new duice.event.RowMoveEvent(_this, fromIndex, toIndex);
                                _this.notifyObservers(rowIndexChangeEvent);
                            });
                        });
                    }
                    // initializes row element
                    duice.initialize(rowHtmlElement, context);
                    this.rowHtmlElements.push(rowHtmlElement);
                    // insert into slot
                    this.slot.appendChild(rowHtmlElement);
                    // execute script
                    this.executeScript(rowHtmlElement, context);
                }
                // hierarchy
                if (this.hierarchy) {
                    let _this = this;
                    let visit = function (currentElement) {
                        let currentPid = duice.getElementAttribute(currentElement, 'hierarchy-pid');
                        _this.slot.querySelectorAll(`*[data-${duice.getNamespace()}-index]`).forEach(element => {
                            let id = duice.getElementAttribute(element, 'hierarchy-id');
                            let pid = duice.getElementAttribute(element, 'hierarchy-pid');
                            if (currentPid === id) {
                                element.appendChild(currentElement.parentNode.removeChild(currentElement));
                                return false;
                            }
                        });
                    };
                    this.rowHtmlElements.forEach(element => {
                        visit(element);
                    });
                }
            }
            // not loop
            else {
                // initialize
                let rowHtmlElement = this.getHtmlElement().cloneNode(true);
                let context = this.getContext();
                duice.initialize(rowHtmlElement, context);
                this.rowHtmlElements.push(rowHtmlElement);
                // append to slot
                this.slot.appendChild(rowHtmlElement);
                // execute script
                this.executeScript(rowHtmlElement, context);
            }
        }
        /**
         * update
         * @param observable
         * @param event
         */
        update(observable, event) {
            console.debug('ArrayElement.update', observable, event);
            if (observable instanceof duice.ArrayHandler) {
                this.render();
            }
        }
    }
    duice.ArrayElement = ArrayElement;
})(duice || (duice = {}));
var duice;
(function (duice) {
    /**
     * element factory abstract class
     */
    class DataElementFactory {
    }
    duice.DataElementFactory = DataElementFactory;
})(duice || (duice = {}));
///<reference path="DataElementFactory.ts"/>
var duice;
(function (duice) {
    /**
     * array element factory class
     */
    class ArrayElementFactory extends duice.DataElementFactory {
        /**
         * adds factory instance
         * @param elementFactory
         */
        static addInstance(elementFactory) {
            this.instances.push(elementFactory);
        }
        /**
         * return factory instance
         * @param htmlElement
         */
        static getInstance(htmlElement) {
            for (let componentFactory of this.instances) {
                if (componentFactory.support(htmlElement)) {
                    return componentFactory;
                }
            }
            if (this.defaultInstance.support(htmlElement)) {
                return this.defaultInstance;
            }
            return null;
        }
        /**
         * check support
         * @param htmlElement
         */
        support(htmlElement) {
            if (duice.hasElementAttribute(htmlElement, 'array')) {
                if (this.doSupport(htmlElement)) {
                    return true;
                }
            }
            return false;
        }
        /**
         * support template method
         * @param htmlElement
         */
        doSupport(htmlElement) {
            return true;
        }
        /**
         * creates array component
         * @param htmlElement
         * @param context
         */
        createElement(htmlElement, context) {
            let component = new duice.ArrayElement(htmlElement, context);
            // array
            let array = duice.getElementAttribute(htmlElement, 'array');
            component.setArray(array);
            // loop
            let loop = duice.getElementAttribute(htmlElement, 'loop');
            if (loop) {
                component.setLoop(loop);
            }
            // hierarchy
            let hierarchy = duice.getElementAttribute(htmlElement, 'hierarchy');
            if (hierarchy) {
                component.setHierarchy(hierarchy);
            }
            // editable
            let editable = duice.getElementAttribute(htmlElement, 'editable');
            if (editable) {
                component.setEditable(editable.toLowerCase() === 'true');
            }
            // returns
            return component;
        }
    }
    ArrayElementFactory.defaultInstance = new ArrayElementFactory();
    ArrayElementFactory.instances = [];
    duice.ArrayElementFactory = ArrayElementFactory;
})(duice || (duice = {}));
///<Reference path="Observable.ts"/>
///<Reference path="Observer.ts"/>
var duice;
(function (duice) {
    /**
     * data handler class
     */
    class DataHandler extends duice.Observable {
        /**
         * constructor
         * @protected
         */
        constructor() {
            super();
            this.readonlyAll = false;
            this.readonly = new Set();
            this.disableAll = false;
            this.disable = new Set();
            this.listenerEnabled = true;
        }
        /**
         * set target
         * @param target
         */
        setTarget(target) {
            this.target = target;
        }
        /**
         * return target
         */
        getTarget() {
            return this.target;
        }
        /**
         * set readonly all
         * @param readonly
         */
        setReadonlyAll(readonly) {
            this.readonlyAll = readonly;
            if (readonly === false) {
                this.readonly.clear();
            }
            this.notifyObservers(new duice.event.Event(this));
        }
        /**
         * set readonly
         * @param property
         * @param readonly
         */
        setReadonly(property, readonly) {
            if (readonly) {
                this.readonly.add(property);
            }
            else {
                this.readonly.delete(property);
            }
            this.notifyObservers(new duice.event.Event(this));
        }
        /**
         * return whether readonly is
         * @param property
         */
        isReadonly(property) {
            return this.readonlyAll || this.readonly.has(property);
        }
        /**
         * set disable all
         * @param disable
         */
        setDisableAll(disable) {
            this.disableAll = disable;
            if (disable === false) {
                this.disable.clear();
            }
            this.notifyObservers(new duice.event.Event(this));
        }
        /**
         * set disable
         * @param property
         * @param disable
         */
        setDisable(property, disable) {
            if (disable) {
                this.disable.add(property);
            }
            else {
                this.disable.delete(property);
            }
            this.notifyObservers(new duice.event.Event(this));
        }
        /**
         * returns property is disabled
         * @param property
         */
        isDisable(property) {
            return this.disableAll || this.disable.has(property);
        }
        /**
         * suspends listener
         */
        suspendListener() {
            this.listenerEnabled = false;
        }
        /**
         * resumes listener
         */
        resumeListener() {
            this.listenerEnabled = true;
        }
        /**
         * executes listener
         * @param listener
         * @param event
         */
        checkListener(listener, event) {
            return __awaiter(this, void 0, void 0, function* () {
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
    duice.DataHandler = DataHandler;
})(duice || (duice = {}));
var duice;
(function (duice) {
    var event;
    (function (event) {
        /**
         * Event
         */
        class Event {
            /**
             * constructor
             * @param source
             */
            constructor(source) {
                this.source = source;
            }
        }
        event.Event = Event;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
///<reference path="Event.ts"/>
var duice;
(function (duice) {
    var event;
    (function (event) {
        /**
         * RowMoveEvent
         */
        class RowMoveEvent extends event.Event {
            /**
             * constructor
             * @param source
             * @param fromIndex
             * @param toIndex
             */
            constructor(source, fromIndex, toIndex) {
                super(source);
                this.fromIndex = fromIndex;
                this.toIndex = toIndex;
            }
            /**
             * getFromIndex
             */
            getFromIndex() {
                return this.fromIndex;
            }
            /**
             * getToIndex
             */
            getToIndex() {
                return this.toIndex;
            }
        }
        event.RowMoveEvent = RowMoveEvent;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
///<reference path="DataHandler.ts"/>
///<reference path="event/RowMoveEvent.ts"/>
var duice;
(function (duice) {
    /**
     * array handler class
     */
    class ArrayHandler extends duice.DataHandler {
        /**
         * constructor
         */
        constructor() {
            super();
        }
        /**
         * get
         * @param target
         * @param property
         * @param receiver
         */
        get(target, property, receiver) {
            let _this = this;
            const value = target[property];
            if (typeof value === 'function') {
                // push, unshift
                if (['push', 'unshift'].includes(property)) {
                    return function () {
                        return __awaiter(this, arguments, void 0, function* () {
                            let index;
                            if (property === 'push') {
                                index = receiver['length'];
                            }
                            else if (property === 'unshift') {
                                index = 0;
                            }
                            let rows = [];
                            for (let i in arguments) {
                                rows.push(arguments[i]);
                            }
                            yield _this.insertRow(target, index, ...rows);
                            return target.length;
                        });
                    };
                }
                // splice
                if (['splice'].includes(property)) {
                    return function () {
                        return __awaiter(this, arguments, void 0, function* () {
                            // parse arguments
                            let start = arguments[0];
                            let deleteCount = arguments[1];
                            let deleteRows = [];
                            for (let i = start; i < (start + deleteCount); i++) {
                                deleteRows.push(target[i]);
                            }
                            let insertRows = [];
                            for (let i = 2; i < arguments.length; i++) {
                                insertRows.push(arguments[i]);
                            }
                            // delete rows
                            if (deleteCount > 0) {
                                yield _this.deleteRow(target, start, deleteCount);
                            }
                            // insert rows
                            if (insertRows.length > 0) {
                                yield _this.insertRow(target, start, ...insertRows);
                            }
                            // returns deleted rows
                            return deleteRows;
                        });
                    };
                }
                // pop, shift
                if (['pop', 'shift'].includes(property)) {
                    return function () {
                        return __awaiter(this, void 0, void 0, function* () {
                            let index;
                            if (property === 'pop') {
                                index = receiver['length'] - 1;
                            }
                            else if (property === 'shift') {
                                index = 0;
                            }
                            let rows = [target[index]];
                            yield _this.deleteRow(target, index);
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
        /**
         * set
         * @param target
         * @param property
         * @param value
         */
        set(target, property, value) {
            Reflect.set(target, property, value);
            if (property === 'length') {
                this.notifyObservers(new duice.event.Event(this));
            }
            return true;
        }
        /**
         * update
         * @param observable
         * @param event
         */
        update(observable, event) {
            return __awaiter(this, void 0, void 0, function* () {
                console.debug("ArrayHandler.update", observable, event);
                // instance is array component
                if (observable instanceof duice.ArrayElement) {
                    if (event instanceof duice.event.RowMoveEvent) {
                        let object = this.getTarget().splice(event.getFromIndex(), 1)[0];
                        this.getTarget().splice(event.getToIndex(), 0, object);
                    }
                }
                // notify observers
                this.notifyObservers(event);
            });
        }
        /**
         * insertRow
         * @param arrayProxy
         * @param index
         * @param rows
         */
        insertRow(arrayProxy, index, ...rows) {
            return __awaiter(this, void 0, void 0, function* () {
                let arrayHandler = duice.ArrayProxy.getHandler(arrayProxy);
                let proxyTarget = duice.ArrayProxy.getTarget(arrayProxy);
                rows.forEach((object, index) => {
                    rows[index] = new duice.ObjectProxy(object);
                });
                let event = new duice.event.RowInsertEvent(this, index, rows);
                if (yield arrayHandler.checkListener(arrayHandler.rowInsertingListener, event)) {
                    proxyTarget.splice(index, 0, ...rows);
                    yield arrayHandler.checkListener(arrayHandler.rowInsertedListener, event);
                    arrayHandler.notifyObservers(event);
                }
            });
        }
        /**
         * deleteRow
         * @param arrayProxy
         * @param index
         * @param size
         */
        deleteRow(arrayProxy, index, size) {
            return __awaiter(this, void 0, void 0, function* () {
                let arrayHandler = duice.ArrayProxy.getHandler(arrayProxy);
                let proxyTarget = duice.ArrayProxy.getTarget(arrayProxy);
                let sliceBegin = index;
                let sliceEnd = (size ? index + size : index + 1);
                let rows = proxyTarget.slice(sliceBegin, sliceEnd);
                let event = new duice.event.RowDeleteEvent(this, index, rows);
                if (yield arrayHandler.checkListener(arrayHandler.rowDeletingListener, event)) {
                    let spliceStart = index;
                    let spliceDeleteCount = (size ? size : 1);
                    proxyTarget.splice(spliceStart, spliceDeleteCount);
                    yield arrayHandler.checkListener(arrayHandler.rowDeletedListener, event);
                    arrayHandler.notifyObservers(event);
                }
            });
        }
        /**
         * appendRow
         * @param arrayProxy
         * @param rows
         */
        appendRow(arrayProxy, ...rows) {
            return __awaiter(this, void 0, void 0, function* () {
                let index = arrayProxy.length;
                return this.insertRow(arrayProxy, index, ...rows);
            });
        }
    }
    duice.ArrayHandler = ArrayHandler;
})(duice || (duice = {}));
///<reference path="Observable.ts"/>
var duice;
(function (duice) {
    /**
     * custom element
     */
    class CustomElement extends duice.DataElement {
        /**
         * constructor
         * @param htmlElement
         * @param context
         */
        constructor(htmlElement, context) {
            super(htmlElement, context);
        }
        /**
         * set object data
         * @param objectName
         */
        setObject(objectName) {
            this.setData(objectName);
        }
        /**
         * set array data
         * @param arrayName
         */
        setArray(arrayName) {
            this.setData(arrayName);
        }
        /**
         * render
         */
        render() {
            // removes child
            this.htmlElement.innerHTML = '';
            // create template element
            let templateElement = this.doRender(this.getData());
            if (this.htmlElement.shadowRoot) {
                this.htmlElement.shadowRoot.appendChild(templateElement);
            }
            else {
                this.htmlElement.appendChild(templateElement);
            }
            // add style if exists
            let styleLiteral = this.doStyle(this.getData());
            if (styleLiteral) {
                let style = document.createElement('style');
                style.textContent = styleLiteral.trim();
                this.htmlElement.appendChild(style);
            }
            // initializes
            let context = {};
            globalThis.Object.assign(context, this.context);
            context['object'] = this.data;
            context['array'] = this.data;
            duice.initialize(this.htmlElement, context);
            // execute script
            this.executeScript(this.htmlElement, context);
        }
        /**
         * setting style
         * @param data
         */
        doStyle(data) {
            return null;
        }
        /**
         * create element
         * @param templateLiteral
         */
        createElement(templateLiteral) {
            let templateElement = document.createElement('template');
            templateElement.innerHTML = templateLiteral;
            return templateElement.content.firstElementChild.cloneNode(true);
        }
        /**
         * update
         * @param observable
         * @param event
         */
        update(observable, event) {
            if (observable instanceof duice.DataHandler) {
                this.render();
            }
        }
    }
    duice.CustomElement = CustomElement;
})(duice || (duice = {}));
///<reference path="DataElementFactory.ts"/>
var duice;
(function (duice) {
    /**
     * custom component factory
     */
    class CustomElementFactory extends duice.DataElementFactory {
        /**
         * constructor
         * @param tagName
         * @param elementType
         */
        constructor(tagName, elementType) {
            super();
            this.tagName = tagName;
            this.elementType = elementType;
        }
        /**
         * adds factory instance
         * @param elementFactory
         */
        static addInstance(elementFactory) {
            // register custom html element
            customElements.define(elementFactory.tagName, class extends HTMLElement {
            });
            // register instance
            this.instances.push(elementFactory);
        }
        /**
         * returns factory instance to be supported
         * @param htmlElement
         */
        static getInstance(htmlElement) {
            for (let componentFactory of this.instances) {
                if (componentFactory.support(htmlElement)) {
                    return componentFactory;
                }
            }
            return null;
        }
        /**
         * creates component
         * @param htmlElement
         * @param context
         */
        createElement(htmlElement, context) {
            // creates instance
            let element = Reflect.construct(this.elementType, [htmlElement, context]);
            // set object
            let objectName = duice.getElementAttribute(htmlElement, 'object');
            if (objectName) {
                element.setObject(objectName);
            }
            // set array
            let arrayName = duice.getElementAttribute(htmlElement, 'array');
            if (arrayName) {
                element.setArray(arrayName);
            }
            // returns
            return element;
        }
        /**
         * checks supported elements
         * @param htmlElement
         */
        support(htmlElement) {
            return (htmlElement.tagName.toLowerCase() === this.tagName);
        }
    }
    CustomElementFactory.instances = [];
    duice.CustomElementFactory = CustomElementFactory;
})(duice || (duice = {}));
var duice;
(function (duice) {
    var format;
    (function (format_1) {
        class FormatFactory {
            /**
             * return format instance
             * @param format
             */
            static getFormat(format) {
                if (format.startsWith('string')) {
                    format = format.replace('string', 'StringFormat');
                }
                if (format.startsWith('number')) {
                    format = format.replace('number', 'NumberFormat');
                }
                if (format.startsWith('date')) {
                    format = format.replace('date', 'DateFormat');
                }
                return Function(`return new duice.format.${format};`).call(null);
            }
        }
        format_1.FormatFactory = FormatFactory;
    })(format = duice.format || (duice.format = {}));
})(duice || (duice = {}));
///<reference path="Observable.ts"/>
///<reference path="./format/FormatFactory.ts"/>
///<reference path="DataElement.ts"/>
var duice;
(function (duice) {
    /**
     * object element class
     */
    class ObjectElement extends duice.DataElement {
        /**
         * constructor
         * @param htmlElement
         * @param context
         */
        constructor(htmlElement, context) {
            super(htmlElement, context);
        }
        /**
         * set object
         * @param objectName
         */
        setObject(objectName) {
            this.setData(objectName);
        }
        /**
         * set property
         * @param property
         */
        setProperty(property) {
            this.property = property;
        }
        /**
         * return property
         */
        getProperty() {
            return this.property;
        }
        /**
         * set format
         * @param format
         */
        setFormat(format) {
            this.format = duice.format.FormatFactory.getFormat(format);
        }
        /**
         * return format
         */
        getFormat() {
            return this.format;
        }
        /**
         * render
         */
        render() {
            if (this.property) {
                let objectHandler = duice.ObjectProxy.getHandler(this.getData());
                // set value
                let value = objectHandler.getValue(this.property);
                this.setValue(value);
                // set readonly
                let readonly = objectHandler.isReadonly(this.property);
                this.setReadonly(readonly);
            }
            // executes script
            this.executeScript(this.htmlElement, this.context);
        }
        /**
         * update event received
         * @param observable
         * @param event
         */
        update(observable, event) {
            console.debug('ObjectElement.update', observable, event);
            // ObjectHandler
            if (observable instanceof duice.ObjectHandler) {
                if (this.property) {
                    // set value
                    this.setValue(observable.getValue(this.property));
                    // set readonly
                    this.setReadonly(observable.isReadonly(this.property));
                    // set disable
                    this.setDisable(observable.isDisable(this.property));
                }
                // executes script
                this.executeScript(this.htmlElement, this.context);
            }
        }
        /**
         * set value
         * @param value
         */
        setValue(value) {
            value = this.getFormat() ? this.getFormat().format(value) : value;
            this.htmlElement.innerText = value;
        }
        /**
         * return value
         */
        getValue() {
            let value = this.htmlElement.innerText;
            value = this.getFormat() ? this.getFormat().parse(value) : value;
            return value;
        }
        /**
         * set readonly
         * @param readonly
         */
        setReadonly(readonly) {
            // no-op
        }
        /**
         * set disable
         * @param disable
         */
        setDisable(disable) {
            // no-op
        }
        /**
         * return index
         */
        getIndex() {
            let index = duice.getElementAttribute(this.htmlElement, 'index');
            if (index) {
                return Number(index);
            }
        }
        /**
         * focus
         */
        focus() {
            // no-ops
            return false;
        }
    }
    duice.ObjectElement = ObjectElement;
})(duice || (duice = {}));
///<reference path="DataElementFactory.ts"/>
var duice;
(function (duice) {
    /**
     * object element factory class
     */
    class ObjectElementFactory extends duice.DataElementFactory {
        /**
         * adds factory instance to registry
         * @param elementFactory
         */
        static addInstance(elementFactory) {
            this.instances.push(elementFactory);
        }
        /**
         * returns supported instance
         * @param htmlElement
         */
        static getInstance(htmlElement) {
            for (let componentFactory of this.instances) {
                if (componentFactory.support(htmlElement)) {
                    return componentFactory;
                }
            }
            if (this.defaultInstance.support(htmlElement)) {
                return this.defaultInstance;
            }
            return null;
        }
        /**
         * check support
         * @param htmlElement
         */
        support(htmlElement) {
            if (duice.hasElementAttribute(htmlElement, 'object')) {
                if (this.doSupport(htmlElement)) {
                    return true;
                }
            }
            return false;
        }
        /**
         * support template method
         * @param htmlElement
         */
        doSupport(htmlElement) {
            return true;
        }
        /**
         * create component
         * @param element
         * @param context
         */
        createElement(element, context) {
            // creates element
            let component = this.doCreateElement(element, context);
            // object
            let object = duice.getElementAttribute(element, 'object');
            component.setObject(object);
            // property
            let property = duice.getElementAttribute(element, 'property');
            if (property) {
                component.setProperty(property);
            }
            // format
            let format = duice.getElementAttribute(element, 'format');
            if (format) {
                component.setFormat(format);
            }
            // returns
            return component;
        }
        /**
         * template method to create component
         * @param htmlElement
         * @param context
         */
        doCreateElement(htmlElement, context) {
            return new duice.ObjectElement(htmlElement, context);
        }
    }
    ObjectElementFactory.defaultInstance = new ObjectElementFactory();
    ObjectElementFactory.instances = [];
    duice.ObjectElementFactory = ObjectElementFactory;
})(duice || (duice = {}));
var duice;
(function (duice) {
    var event;
    (function (event) {
        /**
         * PropertyChangeEvent
         */
        class PropertyChangeEvent extends event.Event {
            /**
             * constructor
             * @param source
             * @param property
             * @param value
             * @param index
             */
            constructor(source, property, value, index) {
                super(source);
                this.property = property;
                this.value = value;
                this.index = index;
            }
            /**
             * getProperty
             */
            getProperty() {
                return this.property;
            }
            /**
             * getValue
             */
            getValue() {
                return this.value;
            }
            /**
             * getIndex
             */
            getIndex() {
                return this.index;
            }
        }
        event.PropertyChangeEvent = PropertyChangeEvent;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
///<reference path="Observable.ts"/>
///<reference path="Observer.ts"/>
///<reference path="DataHandler.ts"/>
///<reference path="event/PropertyChangeEvent.ts"/>
var duice;
(function (duice) {
    /**
     * object handler class
     */
    class ObjectHandler extends duice.DataHandler {
        /**
         * constructor
         */
        constructor() {
            super();
        }
        /**
         * get
         * @param target
         * @param property
         * @param receiver
         */
        get(target, property, receiver) {
            return Reflect.get(target, property, receiver);
        }
        /**
         * set
         * @param target
         * @param property
         * @param value
         */
        set(target, property, value) {
            // change value
            Reflect.set(target, property, value);
            // notify
            let event = new duice.event.PropertyChangeEvent(this, property, value);
            this.notifyObservers(event);
            // returns
            return true;
        }
        /**
         * update
         * @param observable
         * @param event
         */
        update(observable, event) {
            return __awaiter(this, void 0, void 0, function* () {
                console.debug("ObjectHandler.update", observable, event);
                // Element
                if (observable instanceof duice.ObjectElement) {
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
        /**
         * getValue
         * @param property
         */
        getValue(property) {
            property = property.replace('.', '?.');
            return new Function(`return this.${property};`).call(this.getTarget());
        }
        /**
         * setValue
         * @param property
         * @param value
         */
        setValue(property, value) {
            new Function('value', `this.${property} = value;`).call(this.getTarget(), value);
        }
        /**
         * focus
         * @param property
         */
        focus(property) {
            this.observers.forEach(observer => {
                if (observer instanceof duice.ObjectElement) {
                    if (observer.getProperty() === property) {
                        if (observer.focus()) {
                            return false;
                        }
                    }
                }
            });
        }
    }
    duice.ObjectHandler = ObjectHandler;
})(duice || (duice = {}));
///<reference path="CustomElementFactory.ts"/>
var duice;
(function (duice) {
    let namespace = 'duice';
    /**
     * sets namespace
     * @param value
     */
    function setNamespace(value) {
        globalThis[value] = value;
        namespace = value;
    }
    duice.setNamespace = setNamespace;
    /**
     * returns alias of namespace
     */
    function getNamespace() {
        return namespace;
    }
    duice.getNamespace = getNamespace;
    /**
     * returns query selector for element scan
     */
    function getElementQuerySelector() {
        return [
            `*[data-${getNamespace()}-object]:not([data-${getNamespace()}-id])`,
            `*[data-${getNamespace()}-array]:not([data-${getNamespace()}-id])`,
        ].join(',');
    }
    duice.getElementQuerySelector = getElementQuerySelector;
    /**
     * initializes
     * @param container
     * @param context
     */
    function initialize(container, context) {
        // scan DOM tree
        container.querySelectorAll(getElementQuerySelector()).forEach(htmlElement => {
            var _a, _b, _c, _d, _e;
            if (!hasElementAttribute(htmlElement, 'id')) {
                try {
                    // custom element
                    let customElementFactory = duice.CustomElementFactory.getInstance(htmlElement);
                    if (customElementFactory) {
                        (_a = customElementFactory.createElement(htmlElement, context)) === null || _a === void 0 ? void 0 : _a.render();
                        return;
                    }
                    // array element
                    if (hasElementAttribute(htmlElement, 'array')) {
                        (_c = (_b = duice.ArrayElementFactory.getInstance(htmlElement)) === null || _b === void 0 ? void 0 : _b.createElement(htmlElement, context)) === null || _c === void 0 ? void 0 : _c.render();
                        return;
                    }
                    // object element
                    if (hasElementAttribute(htmlElement, 'object')) {
                        (_e = (_d = duice.ObjectElementFactory.getInstance(htmlElement)) === null || _d === void 0 ? void 0 : _d.createElement(htmlElement, context)) === null || _e === void 0 ? void 0 : _e.render();
                        return;
                    }
                }
                catch (e) {
                    console.error(e, htmlElement, container, JSON.stringify(context));
                }
            }
        });
    }
    duice.initialize = initialize;
    /**
     * markInitialized
     * @param container
     */
    function markInitialized(container) {
        container.querySelectorAll(getElementQuerySelector()).forEach(element => {
            setElementAttribute(element, 'id', generateId());
        });
    }
    duice.markInitialized = markInitialized;
    /**
     * finds variable by name
     * @param context
     * @param name
     */
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
    duice.findVariable = findVariable;
    /**
     * generates component ID
     */
    function generateId() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
    duice.generateId = generateId;
    /**
     * checks has component attribute
     * @param htmlElement
     * @param name
     */
    function hasElementAttribute(htmlElement, name) {
        return htmlElement.hasAttribute(`data-${getNamespace()}-${name}`);
    }
    duice.hasElementAttribute = hasElementAttribute;
    /**
     * returns element attribute
     * @param htmlElement
     * @param name
     */
    function getElementAttribute(htmlElement, name) {
        return htmlElement.getAttribute(`data-${getNamespace()}-${name}`);
    }
    duice.getElementAttribute = getElementAttribute;
    /**
     * set component attribute
     * @param htmlElement
     * @param name
     * @param value
     */
    function setElementAttribute(htmlElement, name, value) {
        htmlElement.setAttribute(`data-${getNamespace()}-${name}`, value);
    }
    duice.setElementAttribute = setElementAttribute;
    /**
     * execute script
     * @param script
     * @param thisArg
     * @param context
     */
    function executeScript(script, thisArg, context) {
        try {
            let args = [];
            let values = [];
            for (let property in context) {
                args.push(property);
                values.push(context[property]);
            }
            return Function(...args, script).call(thisArg, ...values);
        }
        catch (e) {
            console.error(script, e);
            throw e;
        }
    }
    duice.executeScript = executeScript;
    /**
     * assert
     * @param condition
     * @param message
     */
    function assert(condition, message) {
        console.assert(condition, message);
        if (!condition) {
            throw new Error(message || 'Assertion Failed');
        }
    }
    duice.assert = assert;
    /**
     * alert
     * @param message
     */
    function alert(message) {
        return __awaiter(this, void 0, void 0, function* () {
            yield new duice.dialog.AlertDialog(message).open();
        });
    }
    duice.alert = alert;
    /**
     * confirm
     * @param message
     */
    function confirm(message) {
        return __awaiter(this, void 0, void 0, function* () {
            return yield new duice.dialog.ConfirmDialog(message).open();
        });
    }
    duice.confirm = confirm;
    /**
     * prompt
     * @param message
     */
    function prompt(message) {
        return __awaiter(this, void 0, void 0, function* () {
            return yield new duice.dialog.PromptDialog(message).open();
        });
    }
    duice.prompt = prompt;
    /**
     * open dialog
     * @param dialogElement
     */
    function openDialog(dialogElement) {
        return __awaiter(this, void 0, void 0, function* () {
            return yield new duice.dialog.Dialog(dialogElement).open();
        });
    }
    duice.openDialog = openDialog;
    /**
     * tab folder
     * @param tabItems
     */
    function tabFolder(...tabItems) {
        let tab = new duice.tab.TabFolder();
        if (tabItems) {
            tabItems.forEach(tabItem => {
                tab.addItem(tabItem);
            });
        }
        return tab;
    }
    duice.tabFolder = tabFolder;
    /**
     * tab item
     * @param button
     * @param content
     * @param listener
     */
    function tabItem(button, content, listener) {
        return new duice.tab.TabItem(button, content, listener);
    }
    duice.tabItem = tabItem;
    /**
     * defines custom element
     * @param tagName
     * @param elementType
     */
    function defineElement(tagName, elementType) {
        let customElementFactory = new duice.CustomElementFactory(tagName, elementType);
        duice.CustomElementFactory.addInstance(customElementFactory);
    }
    duice.defineElement = defineElement;
    /**
     * listens DOMContentLoaded event
     */
    if (globalThis.document) {
        // initialize elements
        document.addEventListener("DOMContentLoaded", event => {
            initialize(document.documentElement, {});
        });
    }
})(duice || (duice = {}));
var duice;
(function (duice) {
    var dialog;
    (function (dialog) {
        /**
         * Dialog
         */
        class Dialog {
            /**
             * constructor
             * @param dialogElement
             */
            constructor(dialogElement) {
                this.closeButtonImg = 'data:image/svg+xml;base64,' + window.btoa('<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M6 6L18 18M18 6L6 18" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> </g></svg>');
                this.dialogElement = dialogElement;
                let _this = this;
                // dialog fixed style
                this.dialogElement.style.position = 'absolute';
                this.dialogElement.style.left = '0';
                this.dialogElement.style.right = '0';
                // header
                this.header = document.createElement('span');
                this.dialogElement.appendChild(this.header);
                this.header.style.display = 'block';
                this.header.style.position = 'absolute';
                this.header.style.left = '0';
                this.header.style.top = '0';
                this.header.style.width = '100%';
                this.header.style.height = '1rem';
                this.header.style.cursor = 'pointer';
                // drag
                this.dialogElement.style.margin = '0px';
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
                this.closeButton = document.createElement('img');
                this.closeButton.src = this.closeButtonImg;
                this.closeButton.style.position = 'absolute';
                this.closeButton.style.top = '5px';
                this.closeButton.style.right = '5px';
                this.closeButton.style.cursor = 'pointer';
                this.closeButton.style.width = '16px';
                this.closeButton.style.height = '16px';
                this.closeButton.addEventListener('click', event => {
                    _this.close();
                });
                this.dialogElement.appendChild(this.closeButton);
                // on resize event
                window.addEventListener('resize', function (event) {
                    _this.moveToCenterPosition();
                });
            }
            /**
             * moveToCenterPosition
             */
            moveToCenterPosition() {
                let computedStyle = window.getComputedStyle(this.dialogElement);
                let computedWidth = parseInt(computedStyle.getPropertyValue('width').replace(/px/gi, ''));
                let computedHeight = parseInt(computedStyle.getPropertyValue('height').replace(/px/gi, ''));
                let scrollX = window.scrollX;
                let scrollY = window.scrollY;
                this.dialogElement.style.left = Math.max(0, window.innerWidth / 2 - computedWidth / 2) + scrollX + 'px';
                this.dialogElement.style.top = Math.max(0, window.innerHeight / 3 - computedHeight / 3) + scrollY + 'px';
            }
            /**
             * getDialogElement
             */
            getDialogElement() {
                return this.dialogElement;
            }
            /**
             * Shows modal
             */
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
            /**
             * Hides modal
             */
            hide() {
                // closes modal
                this.dialogElement.close();
            }
            /**
             * open
             */
            open() {
                return __awaiter(this, void 0, void 0, function* () {
                    // show modal
                    this.show();
                    // creates promise
                    let _this = this;
                    this.promise = new Promise(function (resolve, reject) {
                        _this.promiseResolve = resolve;
                        _this.promiseReject = reject;
                    });
                    return this.promise;
                });
            }
            /**
             * close
             */
            close(...args) {
                this.reject(...args);
            }
            /**
             * resolve
             * @param args
             */
            resolve(...args) {
                this.hide();
                this.promiseResolve(...args);
            }
            /**
             * reject
             * @param args
             */
            reject(...args) {
                this.hide();
                this.promiseReject(...args);
            }
        }
        dialog.Dialog = Dialog;
    })(dialog = duice.dialog || (duice.dialog = {}));
})(duice || (duice = {}));
///<reference path="Dialog.ts"/>
var duice;
(function (duice) {
    var dialog;
    (function (dialog) {
        /**
         * AlertDialog
         */
        class AlertDialog extends dialog.Dialog {
            /**
             * constructor
             * @param message
             */
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
            /**
             * open
             */
            open() {
                let promise = super.open();
                this.confirmButton.focus();
                return promise;
            }
            /**
             * confirm
             */
            confirm() {
                this.resolve();
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
            /**
             * close
             */
            close() {
                this.resolve();
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
        }
        dialog.AlertDialog = AlertDialog;
    })(dialog = duice.dialog || (duice.dialog = {}));
})(duice || (duice = {}));
///<reference path="Dialog.ts"/>
var duice;
(function (duice) {
    var dialog;
    (function (dialog) {
        /**
         * Confirm
         */
        class ConfirmDialog extends dialog.Dialog {
            /**
             * constructor
             * @param message
             */
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
                // divider
                this.getDialogElement().appendChild(document.createTextNode(' '));
                // cancel button
                this.cancelButton = document.createElement('button');
                this.cancelButton.appendChild(document.createTextNode('No'));
                this.cancelButton.style.width = '4rem';
                this.cancelButton.style.cursor = 'pointer';
                this.cancelButton.addEventListener('click', event => {
                    this.cancel();
                });
                this.getDialogElement().appendChild(this.cancelButton);
            }
            /**
             * open
             */
            open() {
                let promise = super.open();
                this.confirmButton.focus();
                return promise;
            }
            /**
             * close
             */
            close(...args) {
                this.resolve(false);
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
            /**
             * confirm
             */
            confirm() {
                this.resolve(true);
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
            /**
             * cancel
             */
            cancel() {
                this.resolve(false);
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
        }
        dialog.ConfirmDialog = ConfirmDialog;
    })(dialog = duice.dialog || (duice.dialog = {}));
})(duice || (duice = {}));
///<reference path="Dialog.ts"/>
var duice;
(function (duice) {
    var dialog;
    (function (dialog) {
        /**
         * PromptDialog
         */
        class PromptDialog extends dialog.Dialog {
            /**
             * constructor
             * @param message
             */
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
                // prompt input
                this.promptInput = document.createElement('input');
                this.promptInput.style.textAlign = 'center';
                this.promptInput.style.marginBottom = '1rem';
                this.promptInput.style.width = '100%';
                this.getDialogElement().appendChild(this.promptInput);
                // confirm button
                this.confirmButton = document.createElement('button');
                this.confirmButton.appendChild(document.createTextNode('Yes'));
                this.confirmButton.style.width = '4rem';
                this.confirmButton.style.cursor = 'pointer';
                this.confirmButton.addEventListener('click', event => {
                    this.confirm(this.promptInput.value);
                });
                this.getDialogElement().appendChild(this.confirmButton);
                // divider
                this.getDialogElement().appendChild(document.createTextNode(' '));
                // cancel button
                this.cancelButton = document.createElement('button');
                this.cancelButton.appendChild(document.createTextNode('No'));
                this.cancelButton.style.width = '4rem';
                this.cancelButton.style.cursor = 'pointer';
                this.cancelButton.addEventListener('click', event => {
                    this.cancel();
                });
                this.getDialogElement().appendChild(this.cancelButton);
            }
            /**
             * open
             */
            open() {
                let promise = super.open();
                this.promptInput.focus();
                return promise;
            }
            /**
             * close
             */
            close(...args) {
                this.resolve();
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
            /**
             * confirm
             */
            confirm(value) {
                this.resolve(value);
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
            /**
             * cancel
             */
            cancel() {
                this.resolve();
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
        }
        dialog.PromptDialog = PromptDialog;
    })(dialog = duice.dialog || (duice.dialog = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * image element factory class
         */
        class ImgElementFactory extends duice.ObjectElementFactory {
            /**
             * creates component
             * @param element
             * @param context
             */
            doCreateElement(element, context) {
                return new component.ImgElement(element, context);
            }
            /**
             * returns supported
             * @param element
             */
            doSupport(element) {
                return (element.tagName.toLowerCase() === 'img');
            }
        }
        component.ImgElementFactory = ImgElementFactory;
        // register factory instance
        duice.ObjectElementFactory.addInstance(new ImgElementFactory());
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * input element component
         */
        class InputElement extends duice.ObjectElement {
            /**
             * constructor
             * @param element
             * @param context
             */
            constructor(element, context) {
                super(element, context);
                // adds change listener
                this.getHtmlElement().addEventListener('change', e => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                    this.notifyObservers(event);
                }, true);
                // turn off autocomplete
                this.getHtmlElement().setAttribute('autocomplete', 'off');
            }
            /**
             * set value
             * @param value
             */
            setValue(value) {
                if (value) {
                    value = this.getFormat() ? this.getFormat().format(value) : value;
                }
                else {
                    value = '';
                }
                this.getHtmlElement().value = value;
            }
            /**
             * return value
             */
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
            /**
             * set readonly
             * @param readonly
             */
            setReadonly(readonly) {
                this.getHtmlElement().readOnly = readonly;
            }
            /**
             * set disable
             * @param disable
             */
            setDisable(disable) {
                if (disable) {
                    this.getHtmlElement().setAttribute('disabled', 'disabled');
                }
                else {
                    this.getHtmlElement().removeAttribute('disabled');
                }
            }
            /**
             * focus
             */
            focus() {
                this.getHtmlElement().focus();
                return true;
            }
        }
        component.InputElement = InputElement;
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
///<reference path="InputElement.ts"/>
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * InputCheckboxElement
         */
        class InputCheckboxElement extends component.InputElement {
            /**
             * constructor
             * @param element
             * @param context
             */
            constructor(element, context) {
                super(element, context);
                this.trueValue = true;
                this.falseValue = false;
                // true false value
                let trueValue = duice.getElementAttribute(this.getHtmlElement(), 'true-value');
                this.trueValue = trueValue ? trueValue : this.trueValue;
                let falseValue = duice.getElementAttribute(this.getHtmlElement(), 'false-value');
                this.falseValue = falseValue ? falseValue : this.falseValue;
            }
            /**
             * set value
             * @param value
             */
            setValue(value) {
                if (value === this.trueValue) {
                    this.getHtmlElement().checked = true;
                }
                else {
                    this.htmlElement.checked = false;
                }
            }
            /**
             * get value
             */
            getValue() {
                if (this.htmlElement.checked) {
                    return this.trueValue;
                }
                else {
                    return this.falseValue;
                }
            }
            /**
             * set readonly
             * @param readonly
             */
            setReadonly(readonly) {
                if (readonly) {
                    this.getHtmlElement().style.pointerEvents = 'none';
                }
                else {
                    this.getHtmlElement().style.pointerEvents = '';
                }
            }
        }
        component.InputCheckboxElement = InputCheckboxElement;
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * input element factory class
         */
        class InputElementFactory extends duice.ObjectElementFactory {
            /**
             * creates component
             * @param element
             * @param context
             */
            doCreateElement(element, context) {
                let type = element.getAttribute('type');
                switch (type) {
                    case 'number':
                        return new component.InputNumberElement(element, context);
                    case 'checkbox':
                        return new component.InputCheckboxElement(element, context);
                    case 'radio':
                        return new component.InputRadioElement(element, context);
                    case 'datetime-local':
                        return new component.InputDatetimeLocalElement(element, context);
                    default:
                        return new component.InputElement(element, context);
                }
            }
            /**
             * check supported
             * @param element
             */
            doSupport(element) {
                return (element.tagName.toLowerCase() === 'input');
            }
        }
        component.InputElementFactory = InputElementFactory;
        // register factory instance
        duice.ObjectElementFactory.addInstance(new InputElementFactory());
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var format;
    (function (format) {
        /**
         * NumberFormat
         * @param scale number
         */
        class NumberFormat {
            /**
             * Constructor
             * @param scale
             */
            constructor(scale) {
                this.scale = 0;
                this.scale = scale;
            }
            /**
             * Encodes number as format
             * @param number
             */
            format(number) {
                if (!number || isNaN(Number(number))) {
                    return '';
                }
                number = Number(number);
                let string = String(number.toFixed(this.scale));
                let reg = /(^[+-]?\d+)(\d{3})/;
                while (reg.test(string)) {
                    string = string.replace(reg, '$1' + ',' + '$2');
                }
                return string;
            }
            /**
             * Decodes formatted value as original value
             * @param string
             */
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
                number = Number(number.toFixed(this.scale));
                return number;
            }
        }
        format.NumberFormat = NumberFormat;
    })(format = duice.format || (duice.format = {}));
})(duice || (duice = {}));
///<reference path="../format/NumberFormat.ts"/>
///<reference path="InputElement.ts"/>
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * input number element component
         */
        class InputNumberElement extends component.InputElement {
            /**
             * constructor
             * @param element
             * @param context
             */
            constructor(element, context) {
                super(element, context);
                // changes type and style
                this.getHtmlElement().removeAttribute('type');
                this.getHtmlElement().style.textAlign = 'right';
                // prevents invalid key press
                this.getHtmlElement().addEventListener('keypress', event => {
                    if (/[\d|\.|,]/.test(event.key) === false) {
                        event.preventDefault();
                    }
                });
            }
            /**
             * return value
             */
            getValue() {
                let value = super.getValue();
                return Number(value);
            }
        }
        component.InputNumberElement = InputNumberElement;
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
///<reference path="InputElement.ts"/>
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * input radio element component
         */
        class InputRadioElement extends component.InputElement {
            /**
             * constructor
             * @param element
             * @param context
             */
            constructor(element, context) {
                super(element, context);
            }
            /**
             * set value
             * @param value
             */
            setValue(value) {
                this.getHtmlElement().checked = (this.getHtmlElement().value === value);
            }
            /**
             * return value
             */
            getValue() {
                return this.getHtmlElement().value;
            }
            /**
             * set readonly
             * @param readonly
             */
            setReadonly(readonly) {
                if (readonly) {
                    this.getHtmlElement().style.pointerEvents = 'none';
                }
                else {
                    this.getHtmlElement().style.pointerEvents = '';
                }
            }
        }
        component.InputRadioElement = InputRadioElement;
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var component;
    (function (component) {
        class Pagination extends duice.CustomElement {
            doRender(object) {
                // attribute
                let pageProperty = duice.getElementAttribute(this.getHtmlElement(), 'page-property');
                let sizeProperty = duice.getElementAttribute(this.getHtmlElement(), 'size-property');
                let countProperty = duice.getElementAttribute(this.getHtmlElement(), 'count-property');
                let onclick = duice.getElementAttribute(this.getHtmlElement(), 'onclick');
                // optional
                let prevText = duice.getElementAttribute(this.getHtmlElement(), 'prev-text') || '<︎';
                let nextText = duice.getElementAttribute(this.getHtmlElement(), 'next-text') || '>︎';
                // page,size,count
                let page = Number(object[pageProperty]);
                let size = Number(object[sizeProperty]);
                let count = Number(object[countProperty]);
                // calculate page
                let totalPage = Math.ceil(count / size);
                let startPageIndex = Math.floor(page / 10) * 10;
                let endPageIndex = Math.min(startPageIndex + 9, totalPage - 1);
                console.debug('page', page);
                console.debug('totalPage', totalPage);
                console.debug('startPage', startPageIndex);
                console.debug('endPage', endPageIndex);
                // template
                let pagination = document.createElement('ul');
                pagination.classList.add(`${duice.getNamespace()}-pagination`);
                // prev
                let prev = document.createElement('li');
                prev.appendChild(document.createTextNode(prevText));
                prev.classList.add(`${duice.getNamespace()}-pagination__item-prev`);
                prev.dataset.page = String(Math.max(startPageIndex - 10, 0));
                prev.addEventListener('click', function () {
                    Function(onclick).call(prev);
                });
                if (page < 10) {
                    prev.classList.add(`${duice.getNamespace()}-pagination__item--disable`);
                }
                pagination.appendChild(prev);
                // pages
                for (let index = startPageIndex; index <= endPageIndex; index++) {
                    let item = document.createElement('li');
                    item.appendChild(document.createTextNode(String(index + 1)));
                    item.dataset.page = String(index);
                    item.classList.add(`${duice.getNamespace()}-pagination__item-page`);
                    if (index === page) {
                        item.classList.add(`${duice.getNamespace()}-pagination__item--active`);
                    }
                    item.addEventListener('click', function () {
                        Function(onclick).call(item);
                    });
                    pagination.appendChild(item);
                }
                // next
                let next = document.createElement('li');
                next.appendChild(document.createTextNode(nextText));
                next.classList.add(`${duice.getNamespace()}-pagination__item-next`);
                next.dataset.page = String(Math.min(endPageIndex + 1, totalPage));
                next.addEventListener('click', function () {
                    Function(onclick).call(next);
                });
                if (endPageIndex >= (totalPage - 1)) {
                    next.classList.add(`${duice.getNamespace()}-pagination__item--disable`);
                }
                pagination.appendChild(next);
                // returns
                return pagination;
            }
            doStyle(object) {
                return `
                .${duice.getNamespace()}-pagination {
                    list-style: none;
                    display: flex;
                    padding-left: 0;
                    margin: 0;
                }
                .${duice.getNamespace()}-pagination__item-page {
                    cursor: pointer;
                    padding: 0 0.5rem;
                }
                .${duice.getNamespace()}-pagination__item-prev {
                    cursor: pointer;
                    padding: 0 0.5rem;
                    font-size: smaller;    
                }
                .${duice.getNamespace()}-pagination__item-next {
                    cursor: pointer;
                    padding: 0 0.5rem;
                    font-size: smaller;
                }
                .${duice.getNamespace()}-pagination__item--active {
                    font-weight: bold;
                    text-decoration: underline;
                    pointer-events: none;
                }
                .${duice.getNamespace()}-pagination__item--disable {
                    pointer-events: none;
                }
           `;
            }
        }
        component.Pagination = Pagination;
        // register
        let customElementFactory = new duice.CustomElementFactory(`${duice.getNamespace()}-pagination`, Pagination);
        duice.CustomElementFactory.addInstance(customElementFactory);
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * select element component
         */
        class SelectElement extends duice.ObjectElement {
            /**
             * constructor
             * @param element
             * @param context
             */
            constructor(element, context) {
                super(element, context);
                // adds event listener
                this.getHtmlElement().addEventListener('change', (e) => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                    this.notifyObservers(event);
                }, true);
            }
            /**
             * set value
             * @param value
             */
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
            /**
             * return value
             */
            getValue() {
                return this.getHtmlElement().value;
            }
            /**
             * set readonly
             * @param readonly
             */
            setReadonly(readonly) {
                if (readonly) {
                    console.warn("==ok");
                    this.getHtmlElement().style.pointerEvents = 'none';
                }
                else {
                    this.getHtmlElement().style.pointerEvents = '';
                }
            }
            /**
             * set disable
             * @param disable
             */
            setDisable(disable) {
                if (disable) {
                    this.getHtmlElement().setAttribute('disabled', 'disabled');
                }
                else {
                    this.getHtmlElement().removeAttribute('disabled');
                }
            }
        }
        component.SelectElement = SelectElement;
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * select element factory class
         */
        class SelectElementFactory extends duice.ObjectElementFactory {
            /**
             * create component
             * @param element
             * @param context
             */
            doCreateElement(element, context) {
                return new component.SelectElement(element, context);
            }
            /**
             * return supported
             * @param element
             */
            doSupport(element) {
                return (element.tagName.toLowerCase() === 'select');
            }
        }
        component.SelectElementFactory = SelectElementFactory;
        // register factory instance
        duice.ObjectElementFactory.addInstance(new SelectElementFactory());
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * textarea element component
         */
        class TextareaElement extends duice.ObjectElement {
            /**
             * constructor
             * @param element
             * @param context
             */
            constructor(element, context) {
                super(element, context);
                // adds change event listener
                this.getHtmlElement().addEventListener('change', e => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                    this.notifyObservers(event);
                }, true);
            }
            /**
             * set value
             * @param value
             */
            setValue(value) {
                if (value) {
                    this.getHtmlElement().value = value;
                }
                else {
                    this.getHtmlElement().value = '';
                }
            }
            /**
             * return value
             */
            getValue() {
                let value = this.getHtmlElement().value;
                if (value != null && value.length > 0) {
                    return value;
                }
                else {
                    return null;
                }
            }
            /**
             * set readonly
             * @param readonly
             */
            setReadonly(readonly) {
                if (readonly) {
                    this.getHtmlElement().setAttribute('readonly', 'readonly');
                }
                else {
                    this.getHtmlElement().removeAttribute('readonly');
                }
            }
            /**
             * set disable
             * @param disable
             */
            setDisable(disable) {
                if (disable) {
                    this.getHtmlElement().setAttribute('disabled', 'disabled');
                }
                else {
                    this.getHtmlElement().removeAttribute('disabled');
                }
            }
        }
        component.TextareaElement = TextareaElement;
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * textarea element factory class
         */
        class TextareaElementFactory extends duice.ObjectElementFactory {
            /**
             * creates component
             * @param element
             * @param context
             */
            doCreateElement(element, context) {
                return new component.TextareaElement(element, context);
            }
            /**
             * returns supported
             * @param element
             */
            doSupport(element) {
                return (element.tagName.toLowerCase() === 'textarea');
            }
        }
        component.TextareaElementFactory = TextareaElementFactory;
        // register factory instance
        duice.ObjectElementFactory.addInstance(new TextareaElementFactory());
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var event;
    (function (event) {
        /**
         * RowInsertEvent
         */
        class RowInsertEvent extends event.Event {
            /**
             * constructor
             * @param source
             * @param index
             */
            constructor(source, index, rows) {
                super(source);
                this.rows = [];
                this.index = index;
                this.rows = rows;
            }
            /**
             * return index
             */
            getIndex() {
                return this.index;
            }
            /**
             * getRows
             */
            getRows() {
                return this.rows;
            }
        }
        event.RowInsertEvent = RowInsertEvent;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var event;
    (function (event) {
        /**
         * RowDeleteEvent
         */
        class RowDeleteEvent extends event.Event {
            /**
             * constructor
             * @param source
             * @param index
             */
            constructor(source, index, rows) {
                super(source);
                this.rows = [];
                this.index = index;
                this.rows = rows;
            }
            /**
             * return index
             */
            getIndex() {
                return this.index;
            }
            /**
             * getRows
             */
            getRows() {
                return this.rows;
            }
        }
        event.RowDeleteEvent = RowDeleteEvent;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var format;
    (function (format) {
        /**
         * date format
         */
        class DateFormat {
            /**
             * Constructor
             * @param pattern
             */
            constructor(pattern) {
                this.patternRex = /yyyy|yy|MM|dd|HH|hh|mm|ss/gi;
                this.pattern = pattern;
            }
            /**
             * Encodes date string
             * @param string
             */
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
            /**
             * Decodes formatted date string to ISO date string.
             * @param string
             */
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
        format.DateFormat = DateFormat;
    })(format = duice.format || (duice.format = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var format;
    (function (format) {
        /**
         * StringFormat
         * @param string format
         */
        class StringFormat {
            /**
             * Constructor
             * @param pattern
             */
            constructor(pattern) {
                this.pattern = pattern;
            }
            /**
             * encode string as format
             * @param value
             */
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
            /**
             * decodes string as format
             * @param value
             */
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
        format.StringFormat = StringFormat;
    })(format = duice.format || (duice.format = {}));
})(duice || (duice = {}));
///<reference path="TabFolder.ts"/>
var duice;
(function (duice) {
    var tab;
    (function (tab) {
        class TabItem {
            /**
             * constructor
             * @param button
             * @param content
             * @param listener
             */
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
            /**
             * set tab folder
             * @param tabFolder
             */
            setTabFolder(tabFolder) {
                this.tabFolder = tabFolder;
            }
            /**
             * set tab index
             * @param tabIndex
             */
            setTabIndex(tabIndex) {
                this.tabIndex = tabIndex;
            }
            /**
             * set active
             * @param active
             */
            setActive(active) {
                if (active === true) {
                    this.button.style.opacity = 'unset';
                    this.content.style.display = null;
                    this.listener.call(this);
                }
                else {
                    this.button.style.opacity = '0.5';
                    this.content.style.display = 'none';
                }
            }
        }
        tab.TabItem = TabItem;
    })(tab = duice.tab || (duice.tab = {}));
})(duice || (duice = {}));
///<reference path="TabItem.ts"/>
var duice;
(function (duice) {
    var tab;
    (function (tab) {
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
        tab.TabFolder = TabFolder;
    })(tab = duice.tab || (duice.tab = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    /**
     * object proxy class
     */
    class ObjectProxy /*implements DataProxy*/ {
        /**
         * constructor
         */
        constructor(object) {
            // object handler
            let objectHandler = new duice.ObjectHandler();
            // copy property
            for (let name in object) {
                let value = object[name];
                // value is array
                if (Array.isArray(value)) {
                    let arrayProxy = new duice.ArrayProxy(value);
                    duice.ArrayProxy.getHandler(arrayProxy).addObserver(objectHandler);
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
        /**
         * clear
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
                        duice.ArrayProxy.clear(value);
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
            objectHandler.notifyObservers(new duice.event.Event(this));
        }
        /**
         * assign
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
                            duice.ArrayProxy.assign(objectProxy[name], value);
                        }
                        else {
                            objectProxy[name] = new duice.ArrayProxy(value);
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
                // save
                this.save(objectProxy);
            }
            finally {
                // resume
                objectHandler.resumeListener();
                objectHandler.resumeNotify();
            }
            // notify observers
            objectHandler.notifyObservers(new duice.event.Event(this));
        }
        /**
         * setTarget
         * @param objectProxy
         * @param target
         */
        static setTarget(objectProxy, target) {
            globalThis.Object.defineProperty(objectProxy, '_target_', {
                value: target,
                writable: true
            });
        }
        /**
         * getTarget
         * @param objectProxy
         */
        static getTarget(objectProxy) {
            return globalThis.Object.getOwnPropertyDescriptor(objectProxy, '_target_').value;
        }
        /**
         * setHandler
         * @param objectProxy
         * @param objectHandler
         */
        static setHandler(objectProxy, objectHandler) {
            globalThis.Object.defineProperty(objectProxy, '_handler_', {
                value: objectHandler,
                writable: true
            });
        }
        /**
         * getHandler
         * @param objectProxy
         */
        static getHandler(objectProxy) {
            let handler = globalThis.Object.getOwnPropertyDescriptor(objectProxy, '_handler_').value;
            duice.assert(handler, 'handler is not found');
            return handler;
        }
        /**
         * save
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
         * reset
         * @param objectProxy
         */
        static reset(objectProxy) {
            let origin = JSON.parse(globalThis.Object.getOwnPropertyDescriptor(objectProxy, '_origin_').value);
            this.assign(objectProxy, origin);
        }
        /**
         * onPropertyChanging
         * @param objectProxy
         * @param listener
         */
        static onPropertyChanging(objectProxy, listener) {
            this.getHandler(objectProxy).propertyChangingListener = listener;
        }
        /**
         * onPropertyChanged
         * @param objectProxy
         * @param listener
         */
        static onPropertyChanged(objectProxy, listener) {
            this.getHandler(objectProxy).propertyChangedListener = listener;
        }
        /**
         * setReadonly
         * @param objectProxy
         * @param property
         * @param readonly
         */
        static setReadonly(objectProxy, property, readonly) {
            this.getHandler(objectProxy).setReadonly(property, readonly);
        }
        /**
         * isReadonly
         * @param objectProxy
         * @param property
         */
        static isReadonly(objectProxy, property) {
            return this.getHandler(objectProxy).isReadonly(property);
        }
        /**
         * setReadonlyAll
         * @param objectProxy
         * @param readonly
         */
        static setReadonlyAll(objectProxy, readonly) {
            this.getHandler(objectProxy).setReadonlyAll(readonly);
        }
        /**
         * setDisable
         * @param objectProxy
         * @param property
         * @param disable
         */
        static setDisable(objectProxy, property, disable) {
            this.getHandler(objectProxy).setDisable(property, disable);
        }
        /**
         * isDisable
         * @param objectProxy
         * @param property
         */
        static isDisable(objectProxy, property) {
            return this.getHandler(objectProxy).isDisable(property);
        }
        /**
         * setDisableAll
         * @param objectProxy
         * @param disable
         */
        static setDisableAll(objectProxy, disable) {
            this.getHandler(objectProxy).setDisableAll(disable);
        }
        /**
         * focus
         * @param objectProxy
         * @param property
         */
        static focus(objectProxy, property) {
            this.getHandler(objectProxy).focus(property);
        }
    }
    duice.ObjectProxy = ObjectProxy;
})(duice || (duice = {}));
var duice;
(function (duice) {
    /**
     * array proxy class
     */
    class ArrayProxy /*implements DataProxy*/ {
        /**
         * constructor
         */
        constructor(array) {
            // array handler
            let arrayHandler = new duice.ArrayHandler();
            // copy array elements
            if (globalThis.Array.isArray(array)) {
                array.forEach((object, index) => {
                    let objectProxy = new duice.ObjectProxy(object);
                    duice.ObjectProxy.getHandler(objectProxy).addObserver(arrayHandler);
                    object = objectProxy;
                });
            }
            // create proxy
            let arrayProxy = new Proxy(array, arrayHandler);
            arrayHandler.setTarget(array);
            // set property
            ArrayProxy.setHandler(arrayProxy, arrayHandler);
            ArrayProxy.setTarget(arrayProxy, array);
            // returns
            return arrayProxy;
        }
        /**
         * clear
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
            arrayHandler.notifyObservers(new duice.event.Event(this));
        }
        /**
         * assign
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
                    let objectProxy = new duice.ObjectProxy(object);
                    duice.ObjectProxy.getHandler(objectProxy).addObserver(arrayHandler);
                    arrayProxy[index] = objectProxy;
                    // add listener
                    duice.ObjectProxy.onPropertyChanging(objectProxy, arrayHandler.propertyChangingListener);
                    duice.ObjectProxy.onPropertyChanged(objectProxy, arrayHandler.propertyChangedListener);
                });
            }
            finally {
                // resume
                arrayHandler.resumeListener();
                arrayHandler.resumeNotify();
            }
            // notify observers
            arrayHandler.notifyObservers(new duice.event.Event(this));
        }
        /**
         * setTarget
         * @param arrayProxy
         * @param target
         */
        static setTarget(arrayProxy, target) {
            globalThis.Object.defineProperty(arrayProxy, '_target_', {
                value: target,
                writable: true
            });
        }
        /**
         * getTarget
         * @param arrayProxy
         */
        static getTarget(arrayProxy) {
            return globalThis.Object.getOwnPropertyDescriptor(arrayProxy, '_target_').value;
        }
        /**
         * setHandler
         * @param arrayProxy
         * @param arrayHandler
         */
        static setHandler(arrayProxy, arrayHandler) {
            globalThis.Object.defineProperty(arrayProxy, '_handler_', {
                value: arrayHandler,
                writable: true
            });
        }
        /**
         * getHandler
         * @param arrayProxy
         */
        static getHandler(arrayProxy) {
            let handler = globalThis.Object.getOwnPropertyDescriptor(arrayProxy, '_handler_').value;
            duice.assert(handler, 'handler is not found');
            return handler;
        }
        /**
         * save
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
         * reset
         * @param arrayProxy
         */
        static reset(arrayProxy) {
            let origin = JSON.parse(globalThis.Object.getOwnPropertyDescriptor(arrayProxy, '_origin_').value);
            this.assign(arrayProxy, origin);
        }
        /**
         * onPropertyChanging
         * @param arrayProxy
         * @param listener
         */
        static onPropertyChanging(arrayProxy, listener) {
            this.getHandler(arrayProxy).propertyChangingListener = listener;
            arrayProxy.forEach(objectProxy => {
                duice.ObjectProxy.getHandler(objectProxy).propertyChangingListener = listener;
            });
        }
        /**
         * onPropertyChanged
         * @param arrayProxy
         * @param listener
         */
        static onPropertyChanged(arrayProxy, listener) {
            this.getHandler(arrayProxy).propertyChangedListener = listener;
            arrayProxy.forEach(objectProxy => {
                duice.ObjectProxy.getHandler(objectProxy).propertyChangedListener = listener;
            });
        }
        /**
         * onRowInserting
         * @param arrayProxy
         * @param listener
         */
        static onRowInserting(arrayProxy, listener) {
            this.getHandler(arrayProxy).rowInsertingListener = listener;
        }
        /**
         * onRowInserted
         * @param arrayProxy
         * @param listener
         */
        static onRowInserted(arrayProxy, listener) {
            this.getHandler(arrayProxy).rowInsertedListener = listener;
        }
        /**
         * onRowDeleting
         * @param arrayProxy
         * @param listener
         */
        static onRowDeleting(arrayProxy, listener) {
            this.getHandler(arrayProxy).rowDeletingListener = listener;
        }
        /**
         * onRowDeleted
         * @param arrayProxy
         * @param listener
         */
        static onRowDeleted(arrayProxy, listener) {
            this.getHandler(arrayProxy).rowDeletedListener = listener;
        }
        /**
         * setReadonly
         * @param arrayProxy
         * @param property
         * @param readonly
         */
        static setReadonly(arrayProxy, property, readonly) {
            this.getHandler(arrayProxy).setReadonly(property, readonly);
        }
        /**
         * isReadonly
         * @param arrayProxy
         * @param property
         */
        static isReadonly(arrayProxy, property) {
            return this.getHandler(arrayProxy).isReadonly(property);
        }
        /**
         * setReadonlyAll
         * @param arrayProxy
         * @param readonly
         */
        static setReadonlyAll(arrayProxy, readonly) {
            this.getHandler(arrayProxy).setReadonlyAll(readonly);
            for (let index = 0; index >= this.length; index++) {
                duice.ObjectProxy.setReadonlyAll(this[index], readonly);
            }
        }
    }
    duice.ArrayProxy = ArrayProxy;
})(duice || (duice = {}));
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * image element component
         */
        class ImgElement extends duice.ObjectElement {
            /**
             * constructor
             * @param element
             * @param context
             */
            constructor(element, context) {
                super(element, context);
                this.editable = false;
                this.closeButtonImg = 'data:image/svg+xml;base64,' + window.btoa('<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path opacity="0.4" d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" fill="#292D32"></path> <path d="M13.0594 12.0001L15.3594 9.70011C15.6494 9.41011 15.6494 8.93011 15.3594 8.64011C15.0694 8.35011 14.5894 8.35011 14.2994 8.64011L11.9994 10.9401L9.69937 8.64011C9.40937 8.35011 8.92937 8.35011 8.63938 8.64011C8.34938 8.93011 8.34938 9.41011 8.63938 9.70011L10.9394 12.0001L8.63938 14.3001C8.34938 14.5901 8.34938 15.0701 8.63938 15.3601C8.78938 15.5101 8.97937 15.5801 9.16937 15.5801C9.35937 15.5801 9.54937 15.5101 9.69937 15.3601L11.9994 13.0601L14.2994 15.3601C14.4494 15.5101 14.6394 15.5801 14.8294 15.5801C15.0194 15.5801 15.2094 15.5101 15.3594 15.3601C15.6494 15.0701 15.6494 14.5901 15.3594 14.3001L13.0594 12.0001Z" fill="#292D32"></path> </g></svg>');
                this.originSrc = String(this.getHtmlElement().src);
                // editable
                let editable = duice.getElementAttribute(this.getHtmlElement(), 'editable');
                if (editable) {
                    this.editable = (editable === 'true');
                }
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
                let size = duice.getElementAttribute(this.getHtmlElement(), 'size');
                if (size) {
                    let sizeArgs = size.split(',');
                    this.width = parseInt(sizeArgs[0].trim());
                    this.height = parseInt(sizeArgs[1].trim());
                }
            }
            /**
             * show clear image button
             */
            showClearImageButton() {
                this.getHtmlElement().parentNode.insertBefore(this.clearButton, this.getHtmlElement().nextSibling);
                this.clearButton.style.position = 'absolute';
                this.clearButton.style.zIndex = '100';
            }
            /**
             * hide clear image button
             */
            hideClearImageButton() {
                this.getHtmlElement().parentNode.removeChild(this.clearButton);
            }
            /**
             * clear image
             */
            clearImage() {
                if (this.originSrc) {
                    this.getHtmlElement().src = this.originSrc;
                }
                else {
                    this.getHtmlElement().src = null;
                }
                // notify observers
                let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                this.notifyObservers(event);
            }
            /**
             * open image
             */
            changeImage() {
                let input = document.createElement('input');
                input.setAttribute("type", "file");
                input.setAttribute("accept", "image/gif, image/jpeg, image/png");
                let _this = this;
                input.addEventListener('change', function (e) {
                    let fileReader = new FileReader();
                    if (this.files && this.files[0]) {
                        fileReader.addEventListener("load", (e) => __awaiter(this, void 0, void 0, function* () {
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
                            let event = new duice.event.PropertyChangeEvent(_this, _this.getProperty(), _this.getValue(), _this.getIndex());
                            _this.notifyObservers(event);
                        }));
                        fileReader.readAsDataURL(this.files[0]);
                    }
                    e.preventDefault();
                    e.stopPropagation();
                });
                input.click();
            }
            /**
             * convert image
             * @param dataUrl
             * @param width
             * @param height
             */
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
            /**
             * set value
             * @param value
             */
            setValue(value) {
                if (value) {
                    this.getHtmlElement().src = value;
                }
                else {
                    this.getHtmlElement().src = this.originSrc;
                }
            }
            /**
             * return value
             */
            getValue() {
                let value = this.getHtmlElement().src;
                if (value === this.originSrc) {
                    return null;
                }
                else {
                    return value;
                }
            }
            /**
             * set readonly
             * @param readonly
             */
            setReadonly(readonly) {
                this.getHtmlElement().style.pointerEvents = (readonly ? 'none' : 'unset');
            }
            /**
             * set disable
             * @param disable
             */
            setDisable(disable) {
                this.getHtmlElement().style.pointerEvents = (disable ? 'none' : 'unset');
            }
        }
        component.ImgElement = ImgElement;
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
///<reference path="../format/NumberFormat.ts"/>
///<reference path="InputElement.ts"/>
var duice;
(function (duice) {
    var component;
    (function (component) {
        /**
         * input datetime-local element component
         */
        class InputDatetimeLocalElement extends component.InputElement {
            /**
             * constructor
             * @param element
             * @param context
             */
            constructor(element, context) {
                super(element, context);
                this.dateFormat = new duice.format.DateFormat('yyyy-MM-ddTHH:mm:ss');
            }
            /**
             * set value
             * @param value
             */
            setValue(value) {
                this.getHtmlElement().value = this.dateFormat.format(value);
            }
            /**
             * return value
             */
            getValue() {
                return this.dateFormat.parse(this.getHtmlElement().value);
            }
        }
        component.InputDatetimeLocalElement = InputDatetimeLocalElement;
    })(component = duice.component || (duice.component = {}));
})(duice || (duice = {}));
//# sourceMappingURL=duice.js.map
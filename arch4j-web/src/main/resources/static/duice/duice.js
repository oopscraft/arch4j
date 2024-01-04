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
    class DataElement extends duice.Observable {
        constructor(htmlElement, bindData, context) {
            var _a;
            super();
            this.htmlElement = htmlElement;
            this.bindData = bindData;
            this.context = context;
            duice.setElementAttribute(this.htmlElement, 'id', this.generateId());
            // bind data
            let dataHandler = (_a = globalThis.Object.getOwnPropertyDescriptor(this.bindData, '_handler_')) === null || _a === void 0 ? void 0 : _a.value;
            duice.assert(dataHandler, 'DataHandler is not found');
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
    duice.DataElement = DataElement;
})(duice || (duice = {}));
///<reference path="DataElement.ts"/>
var duice;
(function (duice) {
    class ArrayElement extends duice.DataElement {
        constructor(htmlElement, bindData, context) {
            super(htmlElement.cloneNode(true), bindData, context);
            this.editable = false;
            this.slot = document.createElement('slot');
            this.itemHtmlElements = [];
            // attributes
            this.loop = duice.getElementAttribute(htmlElement, 'loop');
            this.hierarchy = duice.getElementAttribute(htmlElement, 'hierarchy');
            this.editable = (duice.getElementAttribute(htmlElement, 'editable') === 'true');
            this.selectedItemClass = duice.getElementAttribute(htmlElement, 'selected-item-class');
            // replace with slot for position
            htmlElement.replaceWith(this.slot);
            // mark initialized (not using after clone as templates)
            duice.markInitialized(htmlElement);
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
                                context[statusName] = new duice.ObjectProxy({
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
                        context[statusName] = new duice.ObjectProxy({
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
                duice.initialize(itemHtmlElement, this.getContext());
                this.itemHtmlElements.push(itemHtmlElement);
                // append to slot
                this.slot.appendChild(itemHtmlElement);
                // check if
                duice.runIfCode(itemHtmlElement, context);
                // execute script
                duice.runExecuteCode(itemHtmlElement, context);
            }
        }
        createItemHtmlElement(index, object, context) {
            // clones row elements
            let itemHtmlElement = this.getHtmlElement().cloneNode(true);
            // adds embedded attribute
            duice.setElementAttribute(itemHtmlElement, 'index', index.toString());
            // editable
            let _this = this;
            if (this.editable) {
                itemHtmlElement.setAttribute('draggable', 'true');
                itemHtmlElement.addEventListener('dragstart', function (e) {
                    let fromIndex = duice.getElementAttribute(this, 'index');
                    e.dataTransfer.setData("text", fromIndex);
                });
                itemHtmlElement.addEventListener('dragover', function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                });
                itemHtmlElement.addEventListener('drop', function (e) {
                    return __awaiter(this, void 0, void 0, function* () {
                        e.preventDefault();
                        e.stopPropagation();
                        let fromIndex = parseInt(e.dataTransfer.getData('text'));
                        let toIndex = parseInt(duice.getElementAttribute(this, 'index'));
                        let itemMoveEvent = new duice.event.ItemMoveEvent(_this, fromIndex, toIndex);
                        _this.notifyObservers(itemMoveEvent);
                    });
                });
            }
            // initializes row element
            duice.initialize(itemHtmlElement, context, index);
            this.itemHtmlElements.push(itemHtmlElement);
            // insert into slot
            this.slot.appendChild(itemHtmlElement);
            // check if clause
            duice.runIfCode(itemHtmlElement, context);
            // execute script
            duice.runExecuteCode(itemHtmlElement, context);
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
                let rowSelectEvent = new duice.event.ItemSelectEvent(this, index);
                this.notifyObservers(rowSelectEvent);
            });
        }
        update(observable, event) {
            console.debug('ArrayElement.update', observable, event);
            if (observable instanceof duice.ArrayHandler) {
                // row select event
                if (event instanceof duice.event.ItemSelectEvent) {
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
    duice.ArrayElement = ArrayElement;
})(duice || (duice = {}));
var duice;
(function (duice) {
    class DataElementFactory {
    }
    duice.DataElementFactory = DataElementFactory;
})(duice || (duice = {}));
///<reference path="DataElementFactory.ts"/>
var duice;
(function (duice) {
    class ArrayElementFactory extends duice.DataElementFactory {
        createElement(htmlElement, bindData, context) {
            return new duice.ArrayElement(htmlElement, bindData, context);
        }
    }
    duice.ArrayElementFactory = ArrayElementFactory;
})(duice || (duice = {}));
///<Reference path="Observable.ts"/>
///<Reference path="Observer.ts"/>
var duice;
(function (duice) {
    class DataHandler extends duice.Observable {
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
            if (readonly === false) {
                this.readonly.clear();
            }
            this.notifyObservers(new duice.event.Event(this));
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
            this.notifyObservers(new duice.event.Event(this));
        }
        isReadonly(property) {
            return this.readonlyAll || this.readonly.has(property);
        }
        setDisableAll(disable) {
            this.disableAll = disable;
            if (disable === false) {
                this.disable.clear();
            }
            this.notifyObservers(new duice.event.Event(this));
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
            this.notifyObservers(new duice.event.Event(this));
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
        class Event {
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
        class ItemMoveEvent extends event.Event {
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
        event.ItemMoveEvent = ItemMoveEvent;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
///<reference path="DataHandler.ts"/>
///<reference path="event/ItemMoveEvent.ts"/>
var duice;
(function (duice) {
    class ArrayHandler extends duice.DataHandler {
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
                            yield _this.insertItem(target, index, ...rows);
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
                        return __awaiter(this, void 0, void 0, function* () {
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
                this.notifyObservers(new duice.event.Event(this));
            }
            return true;
        }
        update(observable, event) {
            return __awaiter(this, void 0, void 0, function* () {
                console.debug("ArrayHandler.update", observable, event);
                // instance is array component
                if (observable instanceof duice.ArrayElement) {
                    // row select event
                    if (event instanceof duice.event.ItemSelectEvent) {
                        this.selectedItemIndex = event.getIndex();
                        return;
                    }
                    // row move event
                    if (event instanceof duice.event.ItemMoveEvent) {
                        let object = this.getTarget().splice(event.getFromIndex(), 1)[0];
                        this.getTarget().splice(event.getToIndex(), 0, object);
                    }
                }
                // notify observers
                this.notifyObservers(event);
            });
        }
        insertItem(arrayProxy, index, ...rows) {
            return __awaiter(this, void 0, void 0, function* () {
                let arrayHandler = duice.ArrayProxy.getHandler(arrayProxy);
                let proxyTarget = duice.ArrayProxy.getTarget(arrayProxy);
                rows.forEach((object, index) => {
                    let objectProxy = new duice.ObjectProxy(object);
                    let objectHandler = duice.ObjectProxy.getHandler(objectProxy);
                    objectHandler.propertyChangingListener = this.propertyChangingListener;
                    objectHandler.propertyChangedListener = this.propertyChangedListener;
                    rows[index] = objectProxy;
                });
                let event = new duice.event.ItemInsertEvent(this, index, rows);
                if (yield arrayHandler.checkListener(arrayHandler.rowInsertingListener, event)) {
                    proxyTarget.splice(index, 0, ...rows);
                    yield arrayHandler.checkListener(arrayHandler.rowInsertedListener, event);
                    arrayHandler.notifyObservers(event);
                }
            });
        }
        deleteItem(arrayProxy, index, size) {
            return __awaiter(this, void 0, void 0, function* () {
                let arrayHandler = duice.ArrayProxy.getHandler(arrayProxy);
                let proxyTarget = duice.ArrayProxy.getTarget(arrayProxy);
                let sliceBegin = index;
                let sliceEnd = (size ? index + size : index + 1);
                let rows = proxyTarget.slice(sliceBegin, sliceEnd);
                let event = new duice.event.ItemDeleteEvent(this, index, rows);
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
            return __awaiter(this, void 0, void 0, function* () {
                let index = arrayProxy.length;
                return this.insertItem(arrayProxy, index, ...rows);
            });
        }
        selectItem(index) {
            this.selectedItemIndex = index;
            // notify row select event
            let rowSelectEvent = new duice.event.ItemSelectEvent(this, this.selectedItemIndex);
            this.notifyObservers(rowSelectEvent);
        }
        getSelectedItemIndex() {
            return this.selectedItemIndex;
        }
    }
    duice.ArrayHandler = ArrayHandler;
})(duice || (duice = {}));
var duice;
(function (duice) {
    class ArrayProxy extends globalThis.Array {
        constructor(array) {
            super();
            // array handler
            let arrayHandler = new duice.ArrayHandler();
            // copy array elements
            if (globalThis.Array.isArray(array)) {
                for (let i = 0; i < array.length; i++) {
                    array[i] = new duice.ObjectProxy(array[i]);
                }
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
            duice.assert(handler, 'handler is not found');
            return handler;
        }
        static save(arrayProxy) {
            let origin = JSON.stringify(arrayProxy);
            globalThis.Object.defineProperty(arrayProxy, '_origin_', {
                value: origin,
                writable: true
            });
        }
        static reset(arrayProxy) {
            let origin = JSON.parse(globalThis.Object.getOwnPropertyDescriptor(arrayProxy, '_origin_').value);
            this.assign(arrayProxy, origin);
        }
        static onPropertyChanging(arrayProxy, listener) {
            this.getHandler(arrayProxy).propertyChangingListener = listener;
            arrayProxy.forEach(objectProxy => {
                duice.ObjectProxy.getHandler(objectProxy).propertyChangingListener = listener;
            });
        }
        static onPropertyChanged(arrayProxy, listener) {
            this.getHandler(arrayProxy).propertyChangedListener = listener;
            arrayProxy.forEach(objectProxy => {
                duice.ObjectProxy.getHandler(objectProxy).propertyChangedListener = listener;
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
        static setReadonly(arrayProxy, property, readonly) {
            this.getHandler(arrayProxy).setReadonly(property, readonly);
        }
        static isReadonly(arrayProxy, property) {
            return this.getHandler(arrayProxy).isReadonly(property);
        }
        static setReadonlyAll(arrayProxy, readonly) {
            this.getHandler(arrayProxy).setReadonlyAll(readonly);
            for (let index = 0; index >= this.length; index++) {
                duice.ObjectProxy.setReadonlyAll(this[index], readonly);
            }
        }
        static isReadonlyAll(arrayProxy) {
            return this.getHandler(arrayProxy).isReadonlyAll();
        }
        static setDisable(arrayProxy, property, disable) {
            this.getHandler(arrayProxy).setDisable(property, disable);
        }
        static isDisable(arrayProxy, property) {
            return this.getHandler(arrayProxy).isDisable(property);
        }
        static setDisableAll(arrayProxy, disable) {
            this.getHandler(arrayProxy).setDisableAll(disable);
            for (let index = 0; index >= this.length; index++) {
                duice.ObjectProxy.setDisableAll(this[index], disable);
            }
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
    }
    duice.ArrayProxy = ArrayProxy;
})(duice || (duice = {}));
///<reference path="Observable.ts"/>
var duice;
(function (duice) {
    class CustomElement extends duice.DataElement {
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
        }
        render() {
            // do render
            this.doRender(this.getBindData());
            // check if
            duice.runIfCode(this.getHtmlElement(), this.getContext());
            // initialize
            duice.initialize(this.getHtmlElement(), this.getContext());
            // execute script
            duice.runExecuteCode(this.getHtmlElement(), this.getContext());
        }
        update(observable, event) {
            if (observable instanceof duice.DataHandler) {
                this.doUpdate(observable.getTarget());
            }
        }
    }
    duice.CustomElement = CustomElement;
})(duice || (duice = {}));
///<reference path="DataElementFactory.ts"/>
var duice;
(function (duice) {
    class CustomElementFactory extends duice.DataElementFactory {
        createElement(htmlElement, bindData, context) {
            return this.doCreateElement(htmlElement, bindData, context);
        }
    }
    duice.CustomElementFactory = CustomElementFactory;
})(duice || (duice = {}));
///<reference path="DataElementFactory.ts"/>
var duice;
(function (duice) {
    class ObjectElementFactory extends duice.DataElementFactory {
        createElement(htmlElement, bindData, context) {
            return new duice.ObjectElement(htmlElement, bindData, context);
        }
    }
    duice.ObjectElementFactory = ObjectElementFactory;
})(duice || (duice = {}));
///<reference path="ObjectElementFactory.ts"/>
var duice;
(function (duice) {
    class DataElementRegistry {
        static register(tagName, dataElementFactory) {
            if (dataElementFactory instanceof duice.ArrayElementFactory) {
                this.arrayElementFactories.set(tagName, dataElementFactory);
            }
            else if (dataElementFactory instanceof duice.ObjectElementFactory) {
                this.objectElementFactories.set(tagName, dataElementFactory);
            }
            else if (dataElementFactory instanceof duice.CustomElementFactory) {
                this.customElementFactories.set(tagName, dataElementFactory);
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
    DataElementRegistry.defaultObjectElementFactory = new duice.ObjectElementFactory();
    DataElementRegistry.defaultArrayElementFactory = new duice.ArrayElementFactory();
    DataElementRegistry.objectElementFactories = new Map();
    DataElementRegistry.arrayElementFactories = new Map();
    DataElementRegistry.customElementFactories = new Map();
    duice.DataElementRegistry = DataElementRegistry;
})(duice || (duice = {}));
var duice;
(function (duice) {
    var format;
    (function (format_1) {
        class FormatFactory {
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
    class ObjectElement extends duice.DataElement {
        constructor(htmlElement, bindData, context) {
            super(htmlElement, bindData, context);
            // attributes
            this.property = duice.getElementAttribute(htmlElement, 'property');
            let format = duice.getElementAttribute(htmlElement, 'format');
            if (format) {
                this.format = duice.format.FormatFactory.getFormat(format);
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
                let objectHandler = duice.ObjectProxy.getHandler(this.getBindData());
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
            let bind = duice.getElementAttribute(this.getHtmlElement(), 'bind');
            let bindSplit = bind.split('.');
            if (bindSplit.length > 1) {
                context[bindSplit[0]] = duice.findVariable(context, bindSplit[0]);
            }
            else {
                context[bind] = this.getBindData();
            }
            duice.runIfCode(this.htmlElement, context);
        }
        executeScript() {
            let context = Object.assign({}, this.getContext());
            let bind = duice.getElementAttribute(this.getHtmlElement(), 'bind');
            let bindSplit = bind.split('.');
            if (bindSplit.length > 1) {
                context[bindSplit[0]] = duice.findVariable(context, bindSplit[0]);
            }
            else {
                context[bind] = this.getBindData();
            }
            duice.runExecuteCode(this.htmlElement, context);
        }
        update(observable, event) {
            console.debug('ObjectElement.update', observable, event);
            // ObjectHandler
            if (observable instanceof duice.ObjectHandler) {
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
            let index = duice.getElementAttribute(this.htmlElement, 'index');
            if (index) {
                return Number(index);
            }
        }
        focus() {
            // no-ops
            return false;
        }
    }
    duice.ObjectElement = ObjectElement;
})(duice || (duice = {}));
var duice;
(function (duice) {
    var event;
    (function (event) {
        class PropertyChangeEvent extends event.Event {
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
        event.PropertyChangeEvent = PropertyChangeEvent;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
///<reference path="Observable.ts"/>
///<reference path="Observer.ts"/>
///<reference path="DataHandler.ts"/>
///<reference path="event/PropertyChangeEvent.ts"/>
var duice;
(function (duice) {
    class ObjectHandler extends duice.DataHandler {
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
            let event = new duice.event.PropertyChangeEvent(this, property, value);
            this.notifyObservers(event);
            // returns
            return true;
        }
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
        getValue(property) {
            property = property.replace('.', '?.');
            return new Function(`return this.${property};`).call(this.getTarget());
        }
        setValue(property, value) {
            new Function('value', `this.${property} = value;`).call(this.getTarget(), value);
        }
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
var duice;
(function (duice) {
    class ObjectProxy extends globalThis.Object {
        constructor(object) {
            super();
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
            duice.assert(handler, 'handler is not found');
            return handler;
        }
        static save(objectProxy) {
            let origin = JSON.stringify(objectProxy);
            globalThis.Object.defineProperty(objectProxy, '_origin_', {
                value: origin,
                writable: true
            });
        }
        static reset(objectProxy) {
            let origin = JSON.parse(globalThis.Object.getOwnPropertyDescriptor(objectProxy, '_origin_').value);
            this.assign(objectProxy, origin);
        }
        static onPropertyChanging(objectProxy, listener) {
            this.getHandler(objectProxy).propertyChangingListener = listener;
        }
        static onPropertyChanged(objectProxy, listener) {
            this.getHandler(objectProxy).propertyChangedListener = listener;
        }
        static setReadonly(objectProxy, property, readonly) {
            this.getHandler(objectProxy).setReadonly(property, readonly);
        }
        static isReadonly(objectProxy, property) {
            return this.getHandler(objectProxy).isReadonly(property);
        }
        static setReadonlyAll(objectProxy, readonly) {
            this.getHandler(objectProxy).setReadonlyAll(readonly);
        }
        static isReadonlyAll(objectProxy) {
            return this.getHandler(objectProxy).isReadonlyAll();
        }
        static setDisable(objectProxy, property, disable) {
            this.getHandler(objectProxy).setDisable(property, disable);
        }
        static isDisable(objectProxy, property) {
            return this.getHandler(objectProxy).isDisable(property);
        }
        static setDisableAll(objectProxy, disable) {
            this.getHandler(objectProxy).setDisableAll(disable);
        }
        static isDisableAll(objectProxy) {
            return this.getHandler(objectProxy).isDisableAll();
        }
        static focus(objectProxy, property) {
            this.getHandler(objectProxy).focus(property);
        }
    }
    duice.ObjectProxy = ObjectProxy;
})(duice || (duice = {}));
///<reference path="CustomElementFactory.ts"/>
var duice;
(function (duice) {
    let namespace = 'duice';
    function setNamespace(value) {
        globalThis[value] = value;
        namespace = value;
    }
    duice.setNamespace = setNamespace;
    function getNamespace() {
        return namespace;
    }
    duice.getNamespace = getNamespace;
    function getElementQuerySelector() {
        return `*[data-${getNamespace()}-bind]:not([data-${getNamespace()}-id])`;
    }
    duice.getElementQuerySelector = getElementQuerySelector;
    function initialize(container, context, index) {
        // scan DOM tree
        container.querySelectorAll(getElementQuerySelector()).forEach(htmlElement => {
            var _a, _b;
            if (!hasElementAttribute(htmlElement, 'id')) {
                try {
                    let bindName = getElementAttribute(htmlElement, 'bind');
                    let bindData = findVariable(context, bindName);
                    (_b = (_a = duice.DataElementRegistry.getFactory(htmlElement, bindData, context)) === null || _a === void 0 ? void 0 : _a.createElement(htmlElement, bindData, context)) === null || _b === void 0 ? void 0 : _b.render();
                    // index
                    if (index !== undefined) {
                        setElementAttribute(htmlElement, "index", index.toString());
                    }
                }
                catch (e) {
                    console.error(e, htmlElement, container, JSON.stringify(context));
                }
            }
        });
    }
    duice.initialize = initialize;
    function markInitialized(container) {
        container.querySelectorAll(getElementQuerySelector()).forEach(element => {
            setElementAttribute(element, 'id', '_');
        });
    }
    duice.markInitialized = markInitialized;
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
    duice.runCode = runCode;
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
    duice.runIfCode = runIfCode;
    function runExecuteCode(htmlElement, context) {
        let script = getElementAttribute(htmlElement, 'execute');
        if (script) {
            runCode(script, htmlElement, context);
        }
    }
    duice.runExecuteCode = runExecuteCode;
    function hasElementAttribute(htmlElement, name) {
        return htmlElement.hasAttribute(`data-${getNamespace()}-${name}`);
    }
    duice.hasElementAttribute = hasElementAttribute;
    function getElementAttribute(htmlElement, name) {
        return htmlElement.getAttribute(`data-${getNamespace()}-${name}`);
    }
    duice.getElementAttribute = getElementAttribute;
    function setElementAttribute(htmlElement, name, value) {
        htmlElement.setAttribute(`data-${getNamespace()}-${name}`, value);
    }
    duice.setElementAttribute = setElementAttribute;
    function assert(condition, message) {
        console.assert(condition, message);
        if (!condition) {
            throw new Error(message || 'Assertion Failed');
        }
    }
    duice.assert = assert;
    function alert(message) {
        return __awaiter(this, void 0, void 0, function* () {
            yield new duice.dialog.AlertDialog(message).open();
        });
    }
    duice.alert = alert;
    function confirm(message) {
        return __awaiter(this, void 0, void 0, function* () {
            return yield new duice.dialog.ConfirmDialog(message).open();
        });
    }
    duice.confirm = confirm;
    function prompt(message, type) {
        return __awaiter(this, void 0, void 0, function* () {
            return yield new duice.dialog.PromptDialog(message, type).open();
        });
    }
    duice.prompt = prompt;
    function openDialog(dialogElement) {
        return __awaiter(this, void 0, void 0, function* () {
            return yield new duice.dialog.Dialog(dialogElement).open();
        });
    }
    duice.openDialog = openDialog;
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
    function tabItem(button, content, listener) {
        return new duice.tab.TabItem(button, content, listener);
    }
    duice.tabItem = tabItem;
    //  listens DOMContentLoaded event
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
        class Dialog {
            constructor(dialogElement) {
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
                this.closeButton = document.createElement('span');
                this.closeButton.appendChild(document.createTextNode('X'));
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
            close(...args) {
                this.reject(...args);
            }
            resolve(...args) {
                this.hide();
                this.promiseResolve(...args);
            }
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
        class AlertDialog extends dialog.Dialog {
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
                this.resolve();
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
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
        class ConfirmDialog extends dialog.Dialog {
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
                this.resolve(false);
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
            confirm() {
                this.resolve(true);
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
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
        class PromptDialog extends dialog.Dialog {
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
                this.resolve();
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
            confirm(value) {
                this.resolve(value);
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
            cancel() {
                this.resolve();
                this.getDialogElement().parentNode.removeChild(this.getDialogElement());
            }
        }
        dialog.PromptDialog = PromptDialog;
    })(dialog = duice.dialog || (duice.dialog = {}));
})(duice || (duice = {}));
///<reference path="../ObjectElement.ts"/>
var duice;
(function (duice) {
    var element;
    (function (element_1) {
        class ImgElement extends duice.ObjectElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                this.editable = false;
                this.closeButtonImg = 'data:image/svg+xml;base64,' + window.btoa('<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path opacity="0.4" d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" fill="#292D32"></path> <path d="M13.0594 12.0001L15.3594 9.70011C15.6494 9.41011 15.6494 8.93011 15.3594 8.64011C15.0694 8.35011 14.5894 8.35011 14.2994 8.64011L11.9994 10.9401L9.69937 8.64011C9.40937 8.35011 8.92937 8.35011 8.63938 8.64011C8.34938 8.93011 8.34938 9.41011 8.63938 9.70011L10.9394 12.0001L8.63938 14.3001C8.34938 14.5901 8.34938 15.0701 8.63938 15.3601C8.78938 15.5101 8.97937 15.5801 9.16937 15.5801C9.35937 15.5801 9.54937 15.5101 9.69937 15.3601L11.9994 13.0601L14.2994 15.3601C14.4494 15.5101 14.6394 15.5801 14.8294 15.5801C15.0194 15.5801 15.2094 15.5101 15.3594 15.3601C15.6494 15.0701 15.6494 14.5901 15.3594 14.3001L13.0594 12.0001Z" fill="#292D32"></path> </g></svg>');
                this.originSrc = String(this.getHtmlElement().src);
                // editable
                this.editable = (duice.getElementAttribute(this.getHtmlElement(), 'editable') === 'true');
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
                let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
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
        element_1.ImgElement = ImgElement;
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
///<reference path="../DataElementRegistry.ts"/>
var duice;
(function (duice) {
    var element;
    (function (element_2) {
        class ImgElementFactory extends duice.ObjectElementFactory {
            createElement(element, bindData, context) {
                return new element_2.ImgElement(element, bindData, context);
            }
        }
        element_2.ImgElementFactory = ImgElementFactory;
        // register factory instance
        duice.DataElementRegistry.register('img', new ImgElementFactory());
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var element;
    (function (element_3) {
        class InputElement extends duice.ObjectElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                // adds change listener
                this.getHtmlElement().addEventListener('change', e => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
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
        element_3.InputElement = InputElement;
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
///<reference path="InputElement.ts"/>
var duice;
(function (duice) {
    var element;
    (function (element_4) {
        class InputCheckboxElement extends element_4.InputElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                this.trueValue = true;
                this.falseValue = false;
                // true false value
                let trueValue = duice.getElementAttribute(this.getHtmlElement(), 'true-value');
                this.trueValue = trueValue ? trueValue : this.trueValue;
                let falseValue = duice.getElementAttribute(this.getHtmlElement(), 'false-value');
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
            setReadonly(readonly) {
                if (readonly) {
                    this.getHtmlElement().style.pointerEvents = 'none';
                }
                else {
                    this.getHtmlElement().style.pointerEvents = '';
                }
            }
        }
        element_4.InputCheckboxElement = InputCheckboxElement;
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var format;
    (function (format) {
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
        format.NumberFormat = NumberFormat;
    })(format = duice.format || (duice.format = {}));
})(duice || (duice = {}));
///<reference path="../format/NumberFormat.ts"/>
///<reference path="InputElement.ts"/>
var duice;
(function (duice) {
    var element;
    (function (element_5) {
        class InputDatetimeLocalElement extends element_5.InputElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                this.dateFormat = new duice.format.DateFormat('yyyy-MM-ddTHH:mm:ss');
            }
            setValue(value) {
                this.getHtmlElement().value = this.dateFormat.format(value);
            }
            getValue() {
                return this.dateFormat.parse(this.getHtmlElement().value);
            }
        }
        element_5.InputDatetimeLocalElement = InputDatetimeLocalElement;
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
///<reference path="../DataElementRegistry.ts"/>
var duice;
(function (duice) {
    var element;
    (function (element_6) {
        class InputElementFactory extends duice.ObjectElementFactory {
            createElement(element, bindData, context) {
                let type = element.getAttribute('type');
                switch (type) {
                    case 'number':
                        return new element_6.InputNumberElement(element, bindData, context);
                    case 'checkbox':
                        return new element_6.InputCheckboxElement(element, bindData, context);
                    case 'radio':
                        return new element_6.InputRadioElement(element, bindData, context);
                    case 'datetime-local':
                        return new element_6.InputDatetimeLocalElement(element, bindData, context);
                    default:
                        return new element_6.InputElement(element, bindData, context);
                }
            }
        }
        element_6.InputElementFactory = InputElementFactory;
        // register factory instance
        duice.DataElementRegistry.register('input', new InputElementFactory());
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
///<reference path="../format/NumberFormat.ts"/>
///<reference path="InputElement.ts"/>
var duice;
(function (duice) {
    var element;
    (function (element_7) {
        class InputNumberElement extends element_7.InputElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
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
            getValue() {
                let value = super.getValue();
                return Number(value);
            }
        }
        element_7.InputNumberElement = InputNumberElement;
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
///<reference path="InputElement.ts"/>
var duice;
(function (duice) {
    var element;
    (function (element_8) {
        class InputRadioElement extends element_8.InputElement {
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
        element_8.InputRadioElement = InputRadioElement;
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var element;
    (function (element_9) {
        class SelectElement extends duice.ObjectElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                this.defaultOptions = [];
                // adds event listener
                this.getHtmlElement().addEventListener('change', () => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                    this.notifyObservers(event);
                }, true);
                // stores default option
                for (let i = 0; i < this.getHtmlElement().options.length; i++) {
                    this.defaultOptions.push(this.getHtmlElement().options[i]);
                }
                // option property
                let optionName = duice.getElementAttribute(this.getHtmlElement(), 'option');
                if (optionName) {
                    this.option = duice.findVariable(this.getContext(), optionName);
                    this.optionValueProperty = duice.getElementAttribute(this.getHtmlElement(), 'option-value-property');
                    this.optionTextProperty = duice.getElementAttribute(this.getHtmlElement(), 'option-text-property');
                    duice.ArrayProxy.getHandler(this.option).addObserver(this);
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
            update(observable, event) {
                super.update(observable, event);
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
        element_9.SelectElement = SelectElement;
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
///<reference path="../DataElementRegistry.ts"/>
var duice;
(function (duice) {
    var element;
    (function (element_10) {
        class SelectElementFactory extends duice.ObjectElementFactory {
            createElement(element, bindData, context) {
                return new element_10.SelectElement(element, bindData, context);
            }
        }
        element_10.SelectElementFactory = SelectElementFactory;
        // register factory instance
        duice.DataElementRegistry.register('select', new SelectElementFactory());
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var element;
    (function (element_11) {
        class TextareaElement extends duice.ObjectElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                // adds change event listener
                this.getHtmlElement().addEventListener('change', e => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
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
        element_11.TextareaElement = TextareaElement;
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
///<reference path="../DataElementRegistry.ts"/>
var duice;
(function (duice) {
    var element;
    (function (element_12) {
        class TextareaElementFactory extends duice.ObjectElementFactory {
            createElement(element, bindData, context) {
                return new element_12.TextareaElement(element, bindData, context);
            }
        }
        element_12.TextareaElementFactory = TextareaElementFactory;
        // register
        duice.DataElementRegistry.register('textarea', new TextareaElementFactory());
    })(element = duice.element || (duice.element = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var event;
    (function (event) {
        class ItemInsertEvent extends event.Event {
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
        event.ItemInsertEvent = ItemInsertEvent;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var event;
    (function (event) {
        class ItemDeleteEvent extends event.Event {
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
        event.ItemDeleteEvent = ItemDeleteEvent;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
///<reference path="Event.ts"/>
var duice;
(function (duice) {
    var event;
    (function (event) {
        class ItemSelectEvent extends event.Event {
            constructor(source, index) {
                super(source);
                this.index = index;
            }
            getIndex() {
                return this.index;
            }
        }
        event.ItemSelectEvent = ItemSelectEvent;
    })(event = duice.event || (duice.event = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var format;
    (function (format) {
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
        format.DateFormat = DateFormat;
    })(format = duice.format || (duice.format = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var format;
    (function (format) {
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
        format.StringFormat = StringFormat;
    })(format = duice.format || (duice.format = {}));
})(duice || (duice = {}));
///<reference path="TabFolder.ts"/>
var duice;
(function (duice) {
    var tab;
    (function (tab) {
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
//# sourceMappingURL=duice.js.map
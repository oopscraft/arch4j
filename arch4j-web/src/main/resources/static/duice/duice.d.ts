declare namespace duice {
    /**
     * Observable
     */
    class Observable {
        observers: Observer[];
        notifyEnabled: boolean;
        /**
         * addObserver
         * @param observer
         */
        addObserver(observer: Observer): void;
        /**
         * removeObserver
         * @param observer
         */
        removeObserver(observer: Observer): void;
        /**
         * suspend notify
         */
        suspendNotify(): void;
        /**
         * resume notify
         */
        resumeNotify(): void;
        /**
         * notifyObservers
         * @param event
         */
        notifyObservers(event: Event): void;
    }
}
declare namespace duice {
    /**
     * Observer
     */
    interface Observer {
        /**
         * update
         * @param observable
         * @param event
         */
        update(observable: object, event: Event): void;
    }
}
declare namespace duice {
    /**
     * component factory abstract class
     */
    abstract class ComponentFactory<T extends HTMLElement> {
        /**
         * check support
         * @param element
         */
        abstract support(element: T): boolean;
        /**
         * creates component
         * @param element
         * @param context
         */
        abstract createComponent(element: T, context: object): Component<HTMLElement>;
    }
}
declare namespace duice {
    /**
     * custom component factory
     */
    class CustomComponentFactory<T extends CustomElement> extends ComponentFactory<CustomElement> {
        static instances: CustomComponentFactory<CustomElement>[];
        tagName: string;
        /**
         * adds factory instance
         * @param componentFactory
         */
        static addInstance(componentFactory: CustomComponentFactory<CustomElement>): void;
        /**
         * returns factory instance to be supported
         * @param element
         */
        static getInstance(element: CustomElement): CustomComponentFactory<CustomElement>;
        /**
         * constructor
         * @param tagName
         */
        constructor(tagName: string);
        /**
         * creates component
         * @param element
         * @param context
         */
        createComponent(element: T, context: object): CustomComponent<T>;
        /**
         * checks supported elements
         * @param element
         */
        support(element: T): boolean;
    }
}
declare namespace duice {
    /**
     * sets namespace
     * @param value
     */
    function setNamespace(value: string): void;
    /**
     * returns alias of namespace
     */
    function getNamespace(): string;
    /**
     * returns query selector for component scan
     */
    function getComponentQuerySelector(): string;
    /**
     * initializes
     * @param container
     * @param context
     */
    function initialize(container: any, context: object): void;
    /**
     * markInitialized
     * @param container
     */
    function markInitialized(container: any): void;
    /**
     * finds variable by name
     * @param context
     * @param name
     */
    function findVariable(context: object, name: string): any;
    /**
     * generates component ID
     */
    function generateId(): string;
    /**
     * checks has component attribute
     * @param element
     * @param name
     */
    function hasComponentAttribute(element: HTMLElement, name: string): boolean;
    /**
     * returns component attribute
     * @param element
     * @param name
     */
    function getComponentAttribute(element: HTMLElement, name: string): string;
    /**
     * set component attribute
     * @param element
     * @param name
     * @param value
     */
    function setComponentAttribute(element: HTMLElement, name: string, value: string): void;
    /**
     * execute script
     * @param script
     * @param thisArg
     * @param context
     */
    function executeScript(script: string, thisArg: any, context: object): any;
    /**
     * assert
     * @param condition
     * @param message
     */
    function assert(condition: any, message?: string): void;
    /**
     * alert
     * @param message
     */
    function alert(message: string): Promise<void>;
    /**
     * confirm
     * @param message
     */
    function confirm(message: string): Promise<boolean>;
    /**
     * prompt
     * @param message
     */
    function prompt(message: string): Promise<string>;
    /**
     * dialog
     * @param dialogElement
     */
    function dialog(dialogElement: HTMLDialogElement): Promise<void>;
    /**
     * Gets cookie value
     * @param name
     */
    function getCookie(name: string): string;
    /**
     * Sets cookie value
     * @param name
     * @param value
     * @param day
     */
    function setCookie(name: string, value: string, day: number): void;
    /**
     * Deletes cookie
     * @param name
     */
    function deleteCookie(name: string): void;
    /**
     * fetch
     * @param url
     * @param options
     * @param _bypass
     */
    function fetch(url: URL, options: any, _bypass: boolean): Promise<Response>;
    /**
     * defines custom component
     * @param tagName
     * @param constructor
     */
    function defineComponent(tagName: string, constructor: CustomElementConstructor): void;
}
declare namespace duice {
    /**
     * Dialog
     */
    class Dialog {
        protected dialogElement: HTMLDialogElement;
        protected header: HTMLSpanElement;
        protected closeButton: HTMLSpanElement;
        protected promise: Promise<any>;
        protected promiseResolve: Function;
        protected promiseReject: Function;
        /**
         * constructor
         * @param dialogElement
         */
        constructor(dialogElement: HTMLDialogElement);
        /**
         * moveToCenterPosition
         */
        moveToCenterPosition(): void;
        /**
         * getDialogElement
         */
        protected getDialogElement(): HTMLDialogElement;
        /**
         * Shows modal
         */
        protected show(): void;
        /**
         * Hides modal
         */
        protected hide(): void;
        /**
         * open
         */
        open(): Promise<any>;
        /**
         * close
         */
        close(): void;
        /**
         * confirm
         * @param args
         */
        resolve(...args: any[]): void;
        /**
         * close
         * @param args
         */
        reject(...args: any[]): void;
    }
}
declare namespace duice {
    /**
     * AlertDialog
     */
    class AlertDialog extends Dialog {
        messagePre: HTMLPreElement;
        confirmButton: HTMLButtonElement;
        /**
         * constructor
         * @param message
         */
        constructor(message: string);
        /**
         * open
         */
        open(): Promise<any>;
        /**
         * confirm
         */
        confirm(): void;
        /**
         * close
         */
        close(): void;
    }
}
declare namespace duice {
    /**
     * Confirm
     */
    class ConfirmDialog extends Dialog {
        messagePre: HTMLPreElement;
        confirmButton: HTMLButtonElement;
        cancelButton: HTMLButtonElement;
        /**
         * constructor
         * @param message
         */
        constructor(message: string);
        /**
         * open
         */
        open(): Promise<any>;
        /**
         * confirm
         */
        confirm(): void;
        /**
         * cancel
         */
        cancel(): void;
        /**
         * close
         */
        close(): void;
    }
}
declare namespace duice {
    /**
     * PromptDialog
     */
    class PromptDialog extends Dialog {
        messagePre: HTMLPreElement;
        promptInput: HTMLInputElement;
        confirmButton: HTMLButtonElement;
        cancelButton: HTMLButtonElement;
        /**
         * constructor
         * @param message
         */
        constructor(message: string);
        /**
         * open
         */
        open(): Promise<any>;
    }
}
declare namespace duice {
    /**
     * Event
     */
    class Event {
        source: any;
        /**
         * constructor
         * @param source
         */
        constructor(source: any);
    }
}
declare namespace duice {
    /**
     * PropertyChangeEvent
     */
    class PropertyChangeEvent extends Event {
        property: string;
        value: any;
        index: number;
        /**
         * constructor
         * @param source
         * @param property
         * @param value
         */
        constructor(source: any, property: string, value: any, index?: number);
        /**
         * getProperty
         */
        getProperty(): string;
        /**
         * getValue
         */
        getValue(): any;
        /**
         * getIndex
         */
        getIndex(): number;
    }
}
declare namespace duice {
    /**
     * RowInsertEvent
     */
    class RowInsertEvent extends Event {
        index: number;
        rows: object[];
        /**
         * constructor
         * @param source
         * @param index
         */
        constructor(source: any, index: number, rows: object[]);
        /**
         * return index
         */
        getIndex(): number;
        /**
         * getRows
         */
        getRows(): object[];
    }
}
declare namespace duice {
    /**
     * RowDeleteEvent
     */
    class RowDeleteEvent extends Event {
        index: number;
        rows: object[];
        /**
         * constructor
         * @param source
         * @param index
         */
        constructor(source: any, index: number, rows: object[]);
        /**
         * return index
         */
        getIndex(): number;
        /**
         * getRows
         */
        getRows(): object[];
    }
}
declare namespace duice {
    /**
     * RowMoveEvent
     */
    class RowMoveEvent extends Event {
        fromIndex: number;
        toIndex: number;
        /**
         * constructor
         * @param source
         * @param fromIndex
         * @param toIndex
         */
        constructor(source: any, fromIndex: number, toIndex: number);
        /**
         * getFromIndex
         */
        getFromIndex(): number;
        /**
         * getToIndex
         */
        getToIndex(): number;
    }
}
declare namespace duice {
    /**
     * DateFormat
     */
    class DateMask implements Mask {
        pattern: string;
        patternRex: RegExp;
        /**
         * Constructor
         * @param pattern
         */
        constructor(pattern?: string);
        /**
         * Encodes date string
         * @param string
         */
        encode(string: string): string;
        /**
         * Decodes formatted date string to ISO date string.
         * @param string
         */
        decode(string: string): string;
    }
}
declare namespace duice {
    /**
     * Mask interface
     */
    interface Mask {
        /**
         * Encodes original value as formatted value
         * @param value value
         * @return formatted value
         */
        encode(value: any): any;
        /**
         * Decodes formatted value to original value
         * @param value value
         * @return original value
         */
        decode(value: any): any;
    }
}
declare namespace duice {
    class MaskFactory {
        /**
         * getMask
         * @param mask
         */
        static getMask(mask: string): Mask;
    }
}
declare namespace duice {
    /**
     * NumberFormat
     * @param scale number
     */
    class NumberMask implements Mask {
        scale: number;
        /**
         * Constructor
         * @param scale
         */
        constructor(scale?: number);
        /**
         * Encodes number as format
         * @param number
         */
        encode(number: number): string;
        /**
         * Decodes formatted value as original value
         * @param string
         */
        decode(string: string): number;
    }
}
declare namespace duice {
    /**
     * StringFormat
     * @param string format
     */
    class StringMask implements Mask {
        pattern: string;
        /**
         * Constructor
         * @param pattern
         */
        constructor(pattern: string);
        /**
         * encode string as format
         * @param value
         */
        encode(value: string): string;
        /**
         * decodes string as format
         * @param value
         */
        decode(value: string): string;
    }
}
declare namespace duice {
    /**
     * component abstract class
     */
    abstract class Component<T extends HTMLElement> extends Observable implements Observer {
        element: T;
        context: object;
        data: Data;
        /**
         * constructor
         * @param element
         * @param context
         * @protected
         */
        protected constructor(element: T, context: object);
        /**
         * return element
         */
        getElement(): T;
        /**
         * return context
         */
        getContext(): object;
        /**
         * set data
         * @param dataName
         */
        setData(dataName: string): void;
        /**
         * return data
         */
        getData(): Data;
        /**
         * executes script if exists
         */
        executeScript(): void;
        /**
         * render abstract method
         */
        abstract render(): void;
        /**
         * update abstract method
         * @param observable
         * @param event
         */
        abstract update(observable: object, event: duice.Event): void;
    }
}
declare namespace duice {
    /**
     * array component class
     */
    class ArrayComponent<T extends HTMLElement> extends Component<T> {
        slot: HTMLSlotElement;
        loop: string;
        editable: boolean;
        rowElements: HTMLElement[];
        /**
         * constructor
         * @param element
         * @param context
         */
        constructor(element: T, context: object);
        /**
         * set array
         * @param arrayName
         */
        setArray(arrayName: string): void;
        /**
         * set loop
         * @param loop
         */
        setLoop(loop: string): void;
        /**
         * set editable
         * @param editable
         */
        setEditable(editable: boolean): void;
        /**
         * render
         */
        render(): void;
        /**
         * update
         * @param observable
         * @param event
         */
        update(observable: Observable, event: Event): void;
    }
}
declare namespace duice {
    /**
     * array component factory class
     */
    class ArrayComponentFactory<T extends HTMLElement> extends ComponentFactory<HTMLElement> {
        static defaultInstance: ArrayComponentFactory<HTMLElement>;
        static instances: ArrayComponentFactory<HTMLElement>[];
        /**
         * adds factory instance
         * @param componentFactory
         */
        static addInstance(componentFactory: ArrayComponentFactory<HTMLElement>): void;
        /**
         * return factory instance
         * @param element
         */
        static getInstance(element: HTMLElement): ArrayComponentFactory<HTMLElement>;
        /**
         * check support
         * @param element
         */
        support(element: T): boolean;
        /**
         * support template method
         * @param element
         */
        doSupport(element: T): boolean;
        /**
         * creates array component
         * @param element
         * @param context
         */
        createComponent(element: T, context: object): ArrayComponent<any>;
    }
}
declare namespace duice {
    /**
     * object component class
     */
    class ObjectComponent<T extends HTMLElement> extends Component<T> {
        property: string;
        mask: Mask;
        /**
         * constructor
         * @param element
         * @param context
         */
        constructor(element: T, context: object);
        /**
         * set object
         * @param objectName
         */
        setObject(objectName: string): void;
        /**
         * set property
         * @param property
         */
        setProperty(property: string): void;
        /**
         * return property
         */
        getProperty(): string;
        /**
         * set mask
         * @param mask
         */
        setMask(mask: string): void;
        /**
         * return mask
         */
        getMask(): Mask;
        /**
         * render
         */
        render(): void;
        /**
         * update event received
         * @param observable
         * @param event
         */
        update(observable: Observable, event: Event): void;
        /**
         * set value
         * @param value
         */
        setValue(value: any): void;
        /**
         * return value
         */
        getValue(): any;
        /**
         * set readonly
         * @param readonly
         */
        setReadonly(readonly: boolean): void;
        /**
         * return index
         */
        getIndex(): number;
    }
}
declare namespace duice {
    /**
     * object component factory class
     */
    class ObjectComponentFactory<T extends HTMLElement> extends ComponentFactory<T> {
        static defaultInstance: ObjectComponentFactory<HTMLElement>;
        static instances: ObjectComponentFactory<HTMLElement>[];
        /**
         * adds factory instance to registry
         * @param componentFactory
         */
        static addInstance(componentFactory: ObjectComponentFactory<HTMLElement>): void;
        /**
         * returns supported instance
         * @param element
         */
        static getInstance(element: HTMLElement): ObjectComponentFactory<HTMLElement>;
        /**
         * check support
         * @param element
         */
        support(element: T): boolean;
        /**
         * support template method
         * @param element
         */
        doSupport(element: T): boolean;
        /**
         * create component
         * @param element
         * @param context
         */
        createComponent(element: T, context: object): ObjectComponent<T>;
        /**
         * template method to create component
         * @param htmlElement
         * @param context
         */
        doCreateComponent(htmlElement: T, context: object): ObjectComponent<T>;
    }
}
declare namespace duice {
    /**
     * data handler class
     */
    abstract class DataHandler<T> extends Observable implements Observer {
        target: T;
        readonlyAll: boolean;
        readonly: Set<string>;
        listenerEnabled: boolean;
        /**
         * constructor
         * @protected
         */
        protected constructor();
        /**
         * set target
         * @param target
         */
        setTarget(target: T): void;
        /**
         * return target
         */
        getTarget(): T;
        /**
         * update
         * @param observable
         * @param event
         */
        abstract update(observable: object, event: Event): void;
        /**
         * set readonly all
         * @param readonly
         */
        setReadonlyAll(readonly: boolean): void;
        /**
         * set readonly
         * @param property
         * @param readonly
         */
        setReadonly(property: string, readonly: boolean): void;
        /**
         * return whether readonly is
         * @param property
         */
        isReadonly(property: string): boolean;
        /**
         * suspends listener
         */
        suspendListener(): void;
        /**
         * resumes listener
         */
        resumeListener(): void;
        /**
         * executes listener
         * @param listener
         * @param event
         */
        checkListener(listener: Function, event: Event): Promise<boolean>;
    }
}
declare namespace duice {
    /**
     * array handler class
     */
    class ArrayHandler extends DataHandler<ArrayProxy> {
        propertyChangingListener: Function;
        propertyChangedListener: Function;
        rowInsertingListener: Function;
        rowInsertedListener: Function;
        rowDeletingListener: Function;
        rowDeletedListener: Function;
        /**
         * constructor
         */
        constructor();
        /**
         * get
         * @param target
         * @param property
         * @param receiver
         */
        get(target: ArrayProxy, property: string, receiver: object): any;
        /**
         * set
         * @param target
         * @param property
         * @param value
         */
        set(target: ArrayProxy, property: string, value: any): boolean;
        /**
         * update
         * @param observable
         * @param event
         */
        update(observable: Observable, event: Event): Promise<void>;
    }
}
declare namespace duice {
    /**
     * object handler class
     */
    class ObjectHandler extends DataHandler<ObjectProxy> {
        propertyChangingListener: Function;
        propertyChangedListener: Function;
        /**
         * constructor
         */
        constructor();
        /**
         * get
         * @param target
         * @param property
         * @param receiver
         */
        get(target: object, property: string, receiver: object): any;
        /**
         * set
         * @param target
         * @param property
         * @param value
         */
        set(target: object, property: string, value: any): boolean;
        /**
         * update
         * @param observable
         * @param event
         */
        update(observable: Observable, event: Event): Promise<void>;
        /**
         * getValue
         * @param property
         */
        getValue(property: string): any;
        /**
         * setValue
         * @param property
         * @param value
         */
        setValue(property: string, value: any): void;
    }
}
declare namespace duice {
    /**
     * custom component
     */
    class CustomComponent<T> extends Component<CustomElement> {
        /**
         * constructor
         * @param element
         * @param context
         */
        constructor(element: CustomElement, context: object);
        /**
         * set object data
         * @param objectName
         */
        setObject(objectName: string): void;
        /**
         * set array data
         * @param arrayName
         */
        setArray(arrayName: string): void;
        /**
         * check element is shadow DOM
         */
        isShadowDom(): boolean;
        /**
         * render
         */
        render(): void;
        /**
         * update
         * @param observable
         * @param event
         */
        update(observable: Observable, event: duice.Event): void;
    }
}
declare namespace duice {
    /**
     * custom element
     */
    abstract class CustomElement extends HTMLElement {
        /**
         * constructor
         * @protected
         */
        protected constructor();
        /**
         * returns html template literal
         * @param data
         */
        abstract doRender(data: Data): string;
        /**
         * return style literal
         * @param data
         */
        doStyle(data: any): string;
    }
}
declare namespace duice {
    /**
     * object proxy class
     */
    class ObjectProxy extends Object {
        /**
         * constructor
         */
        constructor(object: object);
        /**
         * assign
         * @param objectProxy
         * @param object
         */
        static assign(objectProxy: ObjectProxy, object: object): void;
        /**
         * setTarget
         * @param objectProxy
         * @param target
         */
        static setTarget(objectProxy: ObjectProxy, target: object): void;
        /**
         * getTarget
         * @param objectProxy
         */
        static getTarget(objectProxy: ObjectProxy): any;
        /**
         * setHandler
         * @param objectProxy
         * @param objectHandler
         */
        static setHandler(objectProxy: ObjectProxy, objectHandler: ObjectHandler): void;
        /**
         * getHandler
         * @param objectProxy
         */
        static getHandler(objectProxy: ObjectProxy): ObjectHandler;
        /**
         * onPropertyChanging
         * @param objectProxy
         * @param listener
         */
        static onPropertyChanging(objectProxy: ObjectProxy, listener: Function): void;
        /**
         * onPropertyChanged
         * @param objectProxy
         * @param listener
         */
        static onPropertyChanged(objectProxy: ObjectProxy, listener: Function): void;
        /**
         * setReadonly
         * @param objectProxy
         * @param property
         * @param readonly
         */
        static setReadonly(objectProxy: ObjectProxy, property: string, readonly: boolean): void;
        /**
         * isReadonly
         * @param objectProxy
         * @param property
         */
        static isReadonly(objectProxy: ObjectProxy, property: string): boolean;
        /**
         * setReadonlyAll
         * @param objectProxy
         * @param readonly
         */
        static setReadonlyAll(objectProxy: ObjectProxy, readonly: boolean): void;
    }
}
declare namespace duice {
    /**
     * array proxy class
     */
    class ArrayProxy extends Array {
        /**
         * constructor
         */
        constructor(array?: object[]);
        /**
         * assign
         * @param arrayProxy
         * @param array
         */
        static assign(arrayProxy: ArrayProxy, array: object[]): void;
        /**
         * setTarget
         * @param arrayProxy
         * @param target
         */
        static setTarget(arrayProxy: ArrayProxy, target: object): void;
        /**
         * getTarget
         * @param arrayProxy
         */
        static getTarget(arrayProxy: ArrayProxy): any;
        /**
         * setHandler
         * @param arrayProxy
         * @param arrayHandler
         */
        static setHandler(arrayProxy: ArrayProxy, arrayHandler: ArrayHandler): void;
        /**
         * getHandler
         * @param arrayProxy
         */
        static getHandler(arrayProxy: ArrayProxy): ArrayHandler;
        /**
         * onPropertyChanging
         * @param arrayProxy
         * @param listener
         */
        static onPropertyChanging(arrayProxy: ArrayProxy, listener: Function): void;
        /**
         * onPropertyChanged
         * @param arrayProxy
         * @param listener
         */
        static onPropertyChanged(arrayProxy: ArrayProxy, listener: Function): void;
        /**
         * onRowInserting
         * @param arrayProxy
         * @param listener
         */
        static onRowInserting(arrayProxy: ArrayProxy, listener: Function): void;
        /**
         * onRowInserted
         * @param arrayProxy
         * @param listener
         */
        static onRowInserted(arrayProxy: ArrayProxy, listener: Function): void;
        /**
         * onRowDeleting
         * @param arrayProxy
         * @param listener
         */
        static onRowDeleting(arrayProxy: ArrayProxy, listener: Function): void;
        /**
         * onRowDeleted
         * @param arrayProxy
         * @param listener
         */
        static onRowDeleted(arrayProxy: ArrayProxy, listener: Function): void;
        /**
         * setReadonly
         * @param arrayProxy
         * @param property
         * @param readonly
         */
        static setReadonly(arrayProxy: ArrayProxy, property: string, readonly: boolean): void;
        /**
         * isReadonly
         * @param arrayProxy
         * @param property
         */
        static isReadonly(arrayProxy: ArrayProxy, property: string): boolean;
        /**
         * setReadonlyAll
         * @param arrayProxy
         * @param readonly
         */
        static setReadonlyAll(arrayProxy: ArrayProxy, readonly: boolean): void;
        /**
         * insertRow
         * @param index
         * @param rows
         */
        insertRow(index: number, ...rows: object[]): Promise<void>;
        /**
         * deleteRow
         * @param index
         * @param size
         */
        deleteRow(index: number, size?: number): Promise<void>;
        /**
         * appendRow
         * @param rows
         */
        appendRow(...rows: object[]): Promise<void>;
    }
}
declare namespace duice {
    /**
     * data interface (object,array)
     */
    interface Data {
    }
}
declare namespace duice {
    /**
     * input element component
     */
    class InputElement extends ObjectComponent<HTMLInputElement> {
        /**
         * constructor
         * @param element
         * @param context
         */
        constructor(element: HTMLInputElement, context: object);
        /**
         * set value
         * @param value
         */
        setValue(value: any): void;
        /**
         * return value
         */
        getValue(): any;
        /**
         * set readonly
         * @param readonly
         */
        setReadonly(readonly: boolean): void;
    }
}
declare namespace duice {
    /**
     * input element factory class
     */
    class InputElementFactory extends ObjectComponentFactory<HTMLInputElement> {
        /**
         * creates component
         * @param element
         * @param context
         */
        doCreateComponent(element: HTMLInputElement, context: object): InputElement;
        /**
         * check supported
         * @param element
         */
        doSupport(element: HTMLElement): boolean;
    }
}
declare namespace duice {
    /**
     * InputCheckboxElement
     */
    class InputCheckboxElement extends InputElement {
        trueValue: any;
        falseValue: any;
        /**
         * constructor
         * @param element
         * @param context
         */
        constructor(element: HTMLInputElement, context: object);
        /**
         * set value
         * @param value
         */
        setValue(value: any): void;
        /**
         * get value
         */
        getValue(): any;
        /**
         * set readonly
         * @param readonly
         */
        setReadonly(readonly: boolean): void;
    }
}
declare namespace duice {
    /**
     * input number element component
     */
    class InputNumberElement extends InputElement {
        /**
         * constructor
         * @param element
         * @param context
         */
        constructor(element: HTMLInputElement, context: object);
        /**
         * return value
         */
        getValue(): any;
    }
}
declare namespace duice {
    /**
     * input radio element component
     */
    class InputRadioElement extends InputElement {
        /**
         * constructor
         * @param element
         * @param context
         */
        constructor(element: HTMLInputElement, context: object);
        /**
         * set value
         * @param value
         */
        setValue(value: any): void;
        /**
         * return value
         */
        getValue(): any;
        /**
         * set readonly
         * @param readonly
         */
        setReadonly(readonly: boolean): void;
    }
}
declare namespace duice {
    /**
     * select element component
     */
    class SelectElement extends ObjectComponent<HTMLSelectElement> {
        /**
         * constructor
         * @param element
         * @param context
         */
        constructor(element: HTMLSelectElement, context: object);
        /**
         * set value
         * @param value
         */
        setValue(value: any): void;
        /**
         * return value
         */
        getValue(): any;
        /**
         * set readonly
         * @param readonly
         */
        setReadonly(readonly: boolean): void;
    }
}
declare namespace duice {
    /**
     * select element factory class
     */
    class SelectElementFactory extends ObjectComponentFactory<HTMLSelectElement> {
        /**
         * create component
         * @param element
         * @param context
         */
        doCreateComponent(element: HTMLSelectElement, context: object): SelectElement;
        /**
         * return supported
         * @param element
         */
        doSupport(element: HTMLElement): boolean;
    }
}
declare namespace duice {
    /**
     * textarea element component
     */
    class TextareaElement extends ObjectComponent<HTMLTextAreaElement> {
        /**
         * constructor
         * @param element
         * @param context
         */
        constructor(element: HTMLTextAreaElement, context: object);
        /**
         * set value
         * @param value
         */
        setValue(value: any): void;
        /**
         * return value
         */
        getValue(): any;
        /**
         * set readonly
         * @param readonly
         */
        setReadonly(readonly: boolean): void;
    }
}
declare namespace duice {
    /**
     * textarea element factory class
      */
    class TextareaElementFactory extends ObjectComponentFactory<HTMLTextAreaElement> {
        /**
         * creates component
         * @param element
         * @param context
         */
        doCreateComponent(element: HTMLTextAreaElement, context: object): TextareaElement;
        /**
         * returns supported
         * @param element
         */
        doSupport(element: HTMLElement): boolean;
    }
}

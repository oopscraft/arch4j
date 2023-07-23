var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class MarkdownEditor extends duice.ObjectElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                this.getHtmlElement().style.display = 'block';
                // config
                let config = {
                    mode: 'markdown',
                    inputStyle: 'textarea',
                    lineNumbers: true,
                    theme: "default",
                    extraKeys: { "Enter": "newlineAndIndentContinueMarkdownList" }
                };
                // textarea
                let textarea = document.createElement('textarea');
                this.getHtmlElement().appendChild(textarea);
                // create code mirror
                this.codeMirror = CodeMirror.fromTextArea(textarea, config);
                this.codeMirror.setSize('100%', '100%');
                // add change event listener
                this.codeMirror.on("blur", () => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                    this.notifyObservers(event);
                });
            }
            createToolbar() {
            }
            setValue(value) {
                if (!value) {
                    value = '';
                }
                this.codeMirror.doc.setValue(value);
            }
            getValue() {
                let value = this.codeMirror.doc.getValue();
                if (!value) {
                    return null;
                }
                return value;
            }
        }
        extension.MarkdownEditor = MarkdownEditor;
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class MarkdownViewer extends duice.ObjectElement {
            /**
             * constructor
             * @param element
             * @param bindData
             * @param context
             */
            constructor(element, bindData, context) {
                super(element, bindData, context);
                // creates child div
                this.div = document.createElement('div');
                this.getHtmlElement().appendChild(this.div);
                // config
                this.config = {
                    headerIds: false,
                    mangle: false,
                    breaks: true,
                    gfm: true
                };
            }
            /**
             * set value
             * @param value
             */
            setValue(value) {
                value = value ? value : '';
                value = marked.parse(value, this.config);
                this.div.innerHTML = value;
                this.div.querySelectorAll('[class^=language-]').forEach(function (pre) {
                    pre.classList.add('line-numbers');
                });
                // highlight
                Prism.highlightAll();
            }
        }
        extension.MarkdownViewer = MarkdownViewer;
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class Pagination extends duice.CustomElement {
            constructor(htmlElement, bindData, context) {
                super(htmlElement, bindData, context);
            }
            setPageProperty(value) {
                this.pageProperty = value;
            }
            setSizeProperty(value) {
                this.sizeProperty = value;
            }
            setCountProperty(value) {
                this.countProperty = value;
            }
            onclick(listener) {
                this.onclickListener = listener;
            }
            doRender(object) {
                // optional
                let prevText = duice.getElementAttribute(this.getHtmlElement(), 'prev-text') || '<︎';
                let nextText = duice.getElementAttribute(this.getHtmlElement(), 'next-text') || '>︎';
                // page,size,count
                let page = Number(object[this.pageProperty]);
                let size = Number(object[this.sizeProperty]);
                let count = Number(object[this.countProperty]);
                // calculate page
                let totalPage = Math.ceil(count / size);
                let startPageIndex = Math.floor(page / 10) * 10;
                let endPageIndex = Math.min(startPageIndex + 9, totalPage - 1);
                endPageIndex = Math.max(endPageIndex, 0);
                // template
                let pagination = document.createElement('ul');
                pagination.classList.add(`${duice.getNamespace()}-pagination`);
                // prev
                let prev = document.createElement('li');
                prev.appendChild(document.createTextNode(prevText));
                prev.classList.add(`${duice.getNamespace()}-pagination__item-prev`);
                prev.dataset.page = String(Math.max(startPageIndex - 10, 0));
                prev.addEventListener('click', () => {
                    this.onclickListener.call(prev);
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
                    item.addEventListener('click', () => {
                        this.onclickListener.call(item);
                    });
                    pagination.appendChild(item);
                }
                // next
                let next = document.createElement('li');
                next.appendChild(document.createTextNode(nextText));
                next.classList.add(`${duice.getNamespace()}-pagination__item-next`);
                next.dataset.page = String(Math.min(endPageIndex + 1, totalPage));
                next.addEventListener('click', () => {
                    this.onclickListener.call(next);
                });
                if (endPageIndex >= (totalPage - 1)) {
                    next.classList.add(`${duice.getNamespace()}-pagination__item--disable`);
                }
                pagination.appendChild(next);
                // returns
                return pagination;
            }
            doUpdate(object) {
                this.render();
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
        extension.Pagination = Pagination;
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
/// <reference path="../node_modules/duice/dist/duice.d.ts" />
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class Workflow extends duice.CustomElement {
            constructor(htmlElement, bindData, context) {
                super(htmlElement, bindData, context);
                this.paperElement = document.createElement('div');
                this.htmlElementTemplate = this.getHtmlElement().innerHTML;
                console.log(this.htmlElementTemplate);
                // mark initialized (not using after clone as templates)
                duice.markInitialized(htmlElement);
                // parse attribute
                let positionProperty = duice.getElementAttribute(this.getHtmlElement(), 'position-property');
                let positionPropertyParts = positionProperty.split(',');
                this.positionXProperty = positionPropertyParts[0];
                this.positionYProperty = positionPropertyParts[1];
                // creates paper
                this.namespace = joint.shapes;
                this.graph = new joint.dia.Graph();
                this.paper = new joint.dia.Paper({
                    el: this.paperElement,
                    model: this.graph,
                    width: '100%',
                    height: 2048,
                    gridSize: 10,
                    drawGrid: true,
                    background: {
                        color: 'rbga(0,255,0,0.3)'
                    },
                    cellViewNamespace: this.namespace,
                    linkPinning: false,
                    defaultLink: () => new joint.shapes.standard.Link(),
                    defaultConnectionPoint: { name: 'boundary' },
                    validateConnection: function (cellViewS, magnetS, cellViewT, magnetT, end, linkView) {
                        // Prevent linking from input ports
                        if (magnetS && magnetS.getAttribute('port-group') === 'in')
                            return false;
                        // Prevent linking from output ports to input ports within one element
                        if (cellViewS === cellViewT)
                            return false;
                        // Prevent linking to output ports
                        return magnetT && magnetT.getAttribute('port-group') === 'in';
                    },
                    snapLinks: { radius: 20 },
                    validateMagnet: function (cellView, magnet) {
                        // Note that this is the default behaviour. It is shown for reference purposes.
                        // Disable linking interaction for magnets marked as passive
                        return magnet.getAttribute('magnet') !== 'passive';
                    },
                    guard: function (evt) {
                        let inputs = ['INPUT', 'SELECT', 'TEXTAREA'];
                        return inputs.indexOf(evt.target.tagName.toUpperCase()) > -1;
                    }
                });
                let portsIn = {
                    position: {
                        name: 'top'
                    },
                    attrs: {
                        portBody: {
                            magnet: true,
                            r: 10,
                            fill: '#023047',
                            stroke: '#023047'
                        }
                    },
                    label: {
                        position: {
                            name: 'top',
                            args: { y: -15 }
                        },
                        markup: [{
                                tagName: 'text',
                                selector: 'label',
                                className: 'label-text'
                            }]
                    },
                    markup: [{
                            tagName: 'circle',
                            selector: 'portBody'
                        }]
                };
                let portsOut = {
                    position: {
                        name: 'bottom'
                    },
                    attrs: {
                        portBody: {
                            magnet: true,
                            r: 10,
                            fill: '#E6A502',
                            stroke: '#023047'
                        }
                    },
                    label: {
                        position: {
                            name: 'bottom',
                            args: { y: 15 }
                        },
                        markup: [{
                                tagName: 'text',
                                selector: 'label',
                                className: 'label-text'
                            }]
                    },
                    markup: [{
                            tagName: 'circle',
                            selector: 'portBody'
                        }]
                };
                console.log(this.htmlElement);
                // define item shape
                this.itemShape = joint.dia.Element.define('example.Form', {
                    attrs: {
                        foreignObject: {
                            width: 'calc(w)',
                            height: 'calc(h)'
                        }
                    },
                    ports: {
                        groups: {
                            'in': portsIn,
                            'out': portsOut
                        }
                    }
                }, {
                    markup: joint.util.svg /* xml */ `
            <foreignObject style="border:solid 1px #aaaaaa; padding:1em; background-color:#fefefe;">
            </foreignObject>
        `
                });
                // Register events
                this.paper.on('link:mouseenter', (linkView) => {
                    this.showLinkTools(linkView);
                });
                this.paper.on('link:mouseleave', (linkView) => {
                    linkView.removeTools();
                });
            }
            doRender(array) {
                for (let i = 0; i < array.length; i++) {
                    let object = array[i];
                    this.createItem(object);
                }
                return this.paperElement;
            }
            doUpdate(array) {
                console.log("== array", array);
                this.graph.clear();
                this.doRender(array);
            }
            doStyle(object) {
                return `
                .${duice.getNamespace()}-pagination {
                    list-style: none;
                    display: flex;
                    padding-left: 0;
                    margin: 0;
                }
           `;
            }
            createItem(object) {
                let model = new this.itemShape();
                model.addPorts([
                    {
                        group: 'in',
                        id: 'in',
                        attrs: { label: { text: 'in' } }
                    },
                    {
                        group: 'out',
                        id: 'out',
                        attrs: { label: { text: 'out' } }
                    }
                ]);
                try {
                    model.position(object[this.positionXProperty], object[this.positionYProperty]);
                }
                catch (e) {
                    model.position(20, 20);
                    console.log(e);
                }
                model.resize(200, 100);
                model.addTo(this.graph);
                let foreignObject = this.paper.findViewByModel(model).$el.get(0).querySelector('foreignObject');
                let div = document.createElement('div');
                div.innerHTML = this.htmlElementTemplate;
                let context = Object.assign({}, this.getContext());
                context['task'] = object;
                console.log("========= context", context);
                duice.initialize(div, context, 0);
                foreignObject.appendChild(div);
                model.on('change:position', (element, position) => {
                    console.log("=== change:position", element, position);
                    console.log("== object", object);
                    object[this.positionXProperty] = position.x;
                    object[this.positionYProperty] = position.y;
                });
                // unfreeze
                this.paper.unfreeze();
            }
            showLinkTools(linkView) {
                let tools = new joint.dia.ToolsView({
                    tools: [
                        new joint.linkTools.Remove({
                            distance: '50%',
                            markup: [{
                                    tagName: 'circle',
                                    selector: 'button',
                                    attributes: {
                                        'r': 7,
                                        'fill': '#f6f6f6',
                                        'stroke': '#ff5148',
                                        'stroke-width': 2,
                                        'cursor': 'pointer'
                                    }
                                }, {
                                    tagName: 'path',
                                    selector: 'icon',
                                    attributes: {
                                        'd': 'M -3 -3 3 3 M -3 3 3 -3',
                                        'fill': 'none',
                                        'stroke': '#ff5148',
                                        'stroke-width': 2,
                                        'pointer-events': 'none'
                                    }
                                }]
                        })
                    ]
                });
                linkView.addTools(tools);
            }
        }
        extension.Workflow = Workflow;
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class MarkdownEditorFactory extends duice.ObjectElementFactory {
            doCreateElement(htmlElement, bindData, context) {
                return new extension.MarkdownEditor(htmlElement, bindData, context);
            }
        }
        extension.MarkdownEditorFactory = MarkdownEditorFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-markdown-editor`, new MarkdownEditorFactory());
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class MarkdownViewerFactory extends duice.ObjectElementFactory {
            doCreateElement(htmlElement, bindData, context) {
                return new extension.MarkdownViewer(htmlElement, bindData, context);
            }
        }
        extension.MarkdownViewerFactory = MarkdownViewerFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-markdown-viewer`, new MarkdownViewerFactory());
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class PaginationFactory extends duice.CustomElementFactory {
            doCreateElement(htmlElement, bindData, context) {
                let pagination = new extension.Pagination(htmlElement, bindData, context);
                pagination.setPageProperty(duice.getElementAttribute(htmlElement, 'page-property'));
                pagination.setSizeProperty(duice.getElementAttribute(htmlElement, 'size-property'));
                pagination.setCountProperty(duice.getElementAttribute(htmlElement, 'count-property'));
                pagination.onclick(new Function(duice.getElementAttribute(htmlElement, 'onclick')));
                return pagination;
            }
        }
        extension.PaginationFactory = PaginationFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-pagination`, new PaginationFactory());
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class WorkflowFactory extends duice.CustomElementFactory {
            doCreateElement(htmlElement, bindData, context) {
                return new extension.Workflow(htmlElement, bindData, context);
            }
        }
        extension.WorkflowFactory = WorkflowFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-workflow`, new WorkflowFactory());
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
//# sourceMappingURL=duice-extension.js.map
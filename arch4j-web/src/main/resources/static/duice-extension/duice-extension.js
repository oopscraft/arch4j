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
                // attributes
                this.pageProperty = duice.getElementAttribute(htmlElement, 'page-property');
                this.sizeProperty = duice.getElementAttribute(htmlElement, 'size-property');
                this.countProperty = duice.getElementAttribute(htmlElement, 'count-property');
                this.onclick = new Function(duice.getElementAttribute(htmlElement, 'onclick'));
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
                    this.onclick.call(prev);
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
                        this.onclick.call(item);
                    });
                    pagination.appendChild(item);
                }
                // next
                let next = document.createElement('li');
                next.appendChild(document.createTextNode(nextText));
                next.classList.add(`${duice.getNamespace()}-pagination__item-next`);
                next.dataset.page = String(Math.min(endPageIndex + 1, totalPage));
                next.addEventListener('click', () => {
                    this.onclick.call(next);
                });
                if (endPageIndex >= (totalPage - 1)) {
                    next.classList.add(`${duice.getNamespace()}-pagination__item--disable`);
                }
                pagination.appendChild(next);
                // returns
                this.getHtmlElement().innerHTML = '';
                this.getHtmlElement().appendChild(this.createStyle());
                this.getHtmlElement().appendChild(pagination);
            }
            doUpdate(object) {
                this.render();
            }
            createStyle() {
                let style = document.createElement('style');
                style.innerHTML = `
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
                return style;
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
                this.elementItems = [];
                this.linkItems = [];
                this.isDragging = false;
                // parse attribute
                this.idProperty = duice.getElementAttribute(this.getHtmlElement(), 'id-property');
                let positionProperty = duice.getElementAttribute(this.getHtmlElement(), 'position-property');
                let positionPropertyParts = positionProperty.split(',');
                this.positionXProperty = positionPropertyParts[0];
                this.positionYProperty = positionPropertyParts[1];
                this.link = duice.findVariable(this.getContext(), duice.getElementAttribute(this.getHtmlElement(), 'link'));
                this.linkSourceProperty = duice.getElementAttribute(this.getHtmlElement(), 'link-source-property');
                this.linkTargetProperty = duice.getElementAttribute(this.getHtmlElement(), 'link-target-property');
                // mark initialized (not using after clone as templates)
                this.htmlElementTemplate = this.getHtmlElement().innerHTML;
                duice.markInitialized(htmlElement);
                this.getHtmlElement().innerHTML = '';
                // create paper
                this.createPaper();
                // define element shape
                this.defineElementShape();
            }
            createPaper() {
                let paperContainer = document.createElement('div');
                this.getHtmlElement().appendChild(paperContainer); // for fix link position
                this.namespace = joint.shapes;
                this.graph = new joint.dia.Graph();
                this.paper = new joint.dia.Paper({
                    el: paperContainer,
                    model: this.graph,
                    width: '100%',
                    height: '100%',
                    gridSize: 10,
                    background: {
                        color: 'rgba(0, 255, 0, 0.0)'
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
                        let inputs = ['INPUT', 'SELECT', 'TEXTAREA', 'BUTTON'];
                        return inputs.indexOf(evt.target.tagName.toUpperCase()) > -1;
                    },
                });
                // Register events
                this.paper.on('link:mouseenter', (linkView) => {
                    this.showLinkTools(linkView);
                });
                this.paper.on('link:mouseleave', (linkView) => {
                    linkView.removeTools();
                });
                this.paper.on('element:pointerdown', () => {
                    this.isDragging = true;
                });
                this.paper.on('element:pointerup', (elementView, event) => {
                    this.isDragging = false;
                    if (elementView && elementView.model) {
                        elementView.model.trigger('change:position', elementView.model, elementView.model.get('position'), {});
                        this.paper.fitToContent();
                    }
                });
                this.paper.on('link:connect', (linkView, evt, elementView) => {
                    const linkItem = linkView.model;
                    const sourceElement = linkItem.getSourceElement();
                    const targetElement = linkItem.getTargetElement();
                    let linkData = {};
                    linkData[this.linkSourceProperty] = sourceElement.attributes.data[this.idProperty];
                    linkData[this.linkTargetProperty] = targetElement.attributes.data[this.idProperty];
                    linkItem.prop('data', linkData);
                    this.link.push(linkData);
                    console.debug('linked', 'sourceElement:', sourceElement, 'targetElement:', targetElement);
                });
            }
            defineElementShape() {
                let portsIn = {
                    position: {
                        name: 'top'
                    },
                    attrs: {
                        portBody: {
                            magnet: true,
                            r: 10,
                            fill: 'gray',
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
                            fill: 'lightgray',
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
                // define item shape
                this.elementShape = joint.dia.Element.define('org.oopscraft.duice.workflow', {
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
                    markup: joint.util.svg /* xml */ `<foreignObject></foreignObject>`
                });
            }
            doRender(array) {
                console.debug("doRender:", array);
                this.graph.clear();
                this.elementItems.length = 0;
                this.linkItems.length = 0;
                for (let i = 0; i < array.length; i++) {
                    let object = array[i];
                    this.createElementItem(object);
                }
                // creates link
                for (let i = 0; i < this.link.length; i++) {
                    this.createLinkItem(this.link[i]);
                }
                // fit to content
                this.paper.fitToContent();
            }
            doUpdate(array) {
                console.debug("doUpdate:", array);
                this.doRender(array);
            }
            createElementItem(object) {
                console.debug("createElementItem:", object);
                let elementItem = new this.elementShape();
                elementItem.prop("data", object);
                elementItem.addPorts([{
                        group: 'in',
                        id: 'in',
                        attrs: { label: { text: 'in' } }
                    }, {
                        group: 'out',
                        id: 'out',
                        attrs: { label: { text: 'out' } }
                    }]);
                // position
                let x = object[this.positionXProperty];
                let y = object[this.positionYProperty];
                if (!x || !y) {
                    let lastElementItem = this.elementItems.length == 0 ? null : this.elementItems[this.elementItems.length - 1];
                    if (lastElementItem) {
                        let position = lastElementItem.position();
                        let size = lastElementItem.size();
                        x = position.x;
                        y = position.y + size.height + 20;
                    }
                }
                elementItem.position(x, y);
                elementItem.addTo(this.graph);
                this.elementItems.push(elementItem);
                let foreignObject = this.paper.findViewByModel(elementItem).$el.get(0).querySelector('foreignObject');
                let div = document.createElement('div');
                div.innerHTML = this.htmlElementTemplate;
                let context = Object.assign({}, this.getContext());
                context['task'] = object;
                duice.initialize(div, context, 0);
                foreignObject.appendChild(div);
                // resize for fit to content
                let width = foreignObject.scrollWidth;
                let height = foreignObject.scrollHeight;
                elementItem.resize(width, height);
                elementItem.on('change:position', (element, position) => {
                    if (!this.isDragging) {
                        console.debug("change:position", element, position, object);
                        object[this.positionXProperty] = position.x;
                        object[this.positionYProperty] = position.y;
                        return;
                    }
                });
            }
            showLinkTools(linkView) {
                let tools = new joint.dia.ToolsView({
                    tools: [
                        new joint.linkTools.Button({
                            distance: '50%',
                            markup: [{
                                    tagName: 'circle',
                                    selector: 'button',
                                    attributes: {
                                        'r': 12,
                                        'fill': '#f6f6f6',
                                        'stroke': 'darkgray',
                                        'stroke-width': 1,
                                        'cursor': 'pointer'
                                    }
                                }, {
                                    tagName: 'path',
                                    selector: 'icon',
                                    attributes: {
                                        'd': 'M -3 -3 3 3 M -3 3 3 -3',
                                        'fill': 'none',
                                        'stroke': 'darkgray',
                                        'stroke-width': 2,
                                        'pointer-events': 'none'
                                    },
                                }],
                            action: (evt) => {
                                console.debug(linkView.model);
                                let linkItem = linkView.model;
                                this.removeLinkItem(linkItem);
                                linkItem.remove();
                                evt.stopPropagation();
                            }
                        })
                    ]
                });
                linkView.addTools(tools);
            }
            createLinkItem(object) {
                console.debug('createLinkItem', object);
                if (object == null) {
                    object = {};
                    object[this.linkSourceProperty] = '';
                    object[this.linkTargetProperty] = '';
                }
                let sourceId = object[this.linkSourceProperty];
                let targetId = object[this.linkTargetProperty];
                let sourceElementItem = this.findElementItemById(sourceId);
                let targetElementItem = this.findElementItemById(targetId);
                let linkItem = new joint.shapes.standard.Link();
                linkItem.prop("data", object);
                linkItem.source(sourceElementItem, { port: 'out' });
                linkItem.target(targetElementItem, { port: 'in' });
                linkItem.addTo(this.graph);
                // Register an event listener when a link's source node changes
                linkItem.on('change:source', (link, source, opt) => {
                    console.debug('link source is changed.');
                    object[this.linkSourceProperty] = source.attributes[this.idProperty];
                });
                // Register an event listener when the link's destination node changes
                linkItem.on('change:target', (link, target, opt) => {
                    console.log('link target is changed.');
                    object[this.linkTargetProperty] = target.attributes[this.idProperty];
                });
                this.linkItems.push(linkItem);
            }
            findElementItemById(id) {
                console.debug("== findElementItemById", id);
                for (let item of this.elementItems) {
                    if (item.attributes.data[this.idProperty] === id) {
                        return item;
                    }
                }
                console.error(`id[${id} not found`);
                return;
            }
            removeLinkItem(linkItem) {
                console.debug("removeLinkItem", linkItem);
                let data = linkItem.attributes.data;
                let sourceId = data[this.linkSourceProperty];
                let targetId = data[this.linkTargetProperty];
                let itemToRemove = this.link.filter(item => {
                    return item[this.linkSourceProperty] === sourceId
                        && item[this.linkTargetProperty] === targetId;
                })[0];
                let indexToRemove = this.link.indexOf(itemToRemove);
                if (indexToRemove > -1) {
                    this.link.splice(indexToRemove, 1);
                }
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
            createElement(htmlElement, bindData, context) {
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
            createElement(htmlElement, bindData, context) {
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
                return new extension.Pagination(htmlElement, bindData, context);
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
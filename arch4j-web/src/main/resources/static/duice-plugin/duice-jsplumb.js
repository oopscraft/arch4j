/// <reference path="../node_modules/duice/dist/duice.d.ts" />
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class Jsplumb extends duice.CustomElement {
            constructor(htmlElement, bindData, context) {
                super(htmlElement, bindData, context);
                this.elementItems = new Map();
                this.sourceEndpoints = new Map();
                this.targetEndpoints = new Map();
                this.connectorItems = [];
                // parse attribute
                this.elementProperty = duice.getElementAttribute(this.getHtmlElement(), 'element-property');
                this.elementLoop = duice.getElementAttribute(this.getHtmlElement(), 'element-loop');
                this.elementIdProperty = duice.getElementAttribute(this.getHtmlElement(), 'element-id-property');
                let positionProperty = duice.getElementAttribute(this.getHtmlElement(), 'element-position-property');
                let positionPropertyParts = positionProperty.split(',');
                this.elementPositionXProperty = positionPropertyParts[0];
                this.elementPositionYProperty = positionPropertyParts[1];
                this.connectorProperty = duice.getElementAttribute(this.getHtmlElement(), 'connector-property');
                this.connectorSourceProperty = duice.getElementAttribute(this.getHtmlElement(), 'connector-source-property');
                this.connectorTargetProperty = duice.getElementAttribute(this.getHtmlElement(), 'connector-target-property');
                // mark initialized (not using after clone as templates)
                this.htmlElementTemplate = this.getHtmlElement().innerHTML;
                duice.markInitialized(htmlElement);
                this.getHtmlElement().innerHTML = '';
                this.container = document.createElement('div');
                this.container.classList.add(duice.getNamespace() + '-diagram-container');
                this.container.style.position = 'relative';
                this.container.style.width = '100%';
                this.container.style.height = '100%';
                this.container.style.overflow = 'hidden';
                this.container.classList.add(duice.getNamespace() + '-diagram-container');
                this.container.addEventListener('click', event => {
                    event.preventDefault();
                });
                this.getHtmlElement().appendChild(this.createStyle());
                this.jsPlumbInstance = jsPlumb.getInstance({
                    container: this.container,
                });
                this.getHtmlElement().appendChild(this.container);
                this.jsPlumbInstance.bind('connection', (event, mouseEvent) => {
                    console.debug('== connection:', event, mouseEvent);
                    if (!mouseEvent) {
                        return;
                    }
                    event.connection.addOverlay(["PlainArrow", {
                            location: 1,
                            width: 10,
                            length: 10,
                            id: "arrow",
                        }]);
                    event.connection.addOverlay(['Label', {
                            label: '<span style="cursor:pointer; font-weight:bold;">[X]</span>',
                            location: 0.5,
                            events: {
                                click: (labelOverlay, originalEvent) => {
                                    this.jsPlumbInstance.deleteConnection(event.connection);
                                }
                            }
                        }]);
                    let connectorSourceId = duice.getElementAttribute(event.connection.source, 'element-id');
                    let connectorTargetId = duice.getElementAttribute(event.connection.target, 'element-id');
                    this.addConnectorData(connectorSourceId, connectorTargetId);
                });
                this.jsPlumbInstance.bind('connectionMoved', event => {
                    console.debug('== connectionMoved:', event);
                    let connectorSourceId = duice.getElementAttribute(event.originalSourceEndpoint.element, 'element-id');
                    let connectorTargetId = duice.getElementAttribute(event.originalTargetEndpoint.element, 'element-id');
                    this.removeConnectorData(connectorSourceId, connectorTargetId);
                });
                this.jsPlumbInstance.bind('internal.connectionDetached', event => {
                    console.debug('== internal.connectionDetached:', event);
                    let connectorSourceId = duice.getElementAttribute(event.connection.source, 'element-id');
                    let connectorTargetId = duice.getElementAttribute(event.connection.target, 'element-id');
                    this.removeConnectorData(connectorSourceId, connectorTargetId);
                });
            }
            createStyle() {
                let style = document.createElement('style');
                style.innerHTML = `
                .${duice.getNamespace()}-diagram-container {
                    background-size: 1rem 1rem;
                    background-image: radial-gradient(circle, rgba(128, 128, 128, 0.25) 1px, rgba(0, 0, 0, 0) 1px);
                }
                .${duice.getNamespace()}-diagram-element {
                }
                .${duice.getNamespace()}-diagram-endpoint-source {
                    cursor: pointer;
                    opacity: 0.5;
                }
                .${duice.getNamespace()}-diagram-endpoint-source-hover {
                    opacity: 1.0;
                }
                .${duice.getNamespace()}-diagram-endpoint-target {
                    opacity: 0.5;
                }
                .${duice.getNamespace()}-diagram-endpoint-target-hover {
                    opacity: 1.0;
                }
                .${duice.getNamespace()}-diagram-connector-disconnect {
                    display: block;
                    cursor: pointer;
                    background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAACXBIWXMAAAsTAAALEwEAmpwYAAAApElEQVR4nGNgoBHgZ2Bg0GJgYDCHYhCbjxiNrAwMDFYMDAyROLAVVA1OzV54NMOwJy5DrIjQDMOW2PwciYQfMjAwLETiL4SKIatBCRNtNEmQhn9QGpmNrAYUsHBgjsWZc6Aa/2PRHAnVg9eARVDNIEOWYZE3o6oX+EgMxAhsCcuKkmhkgCYOUCIhOyHBDAGZjk0jyNkgOZya0cMEOTNpEpuZSAYAwus8FYS1LyQAAAAASUVORK5CYII=');
                    background-size: 15px 15px;
                    width: 15px;
                    height: 15px;
                    opacity: 0.5;
                }
                .${duice.getNamespace()}-diagram-connector-disconnect:hover {
                    opacity: 1;
                }
            `;
                return style;
            }
            doRender(object) {
                var _a;
                console.debug("doRender:", object);
                this.clearContainer();
                this.jsPlumbInstance.setSuspendDrawing(true);
                let elementArray = this.bindData[this.elementProperty];
                let elementLoopArgs = this.elementLoop.split(',');
                let elementItemName = elementLoopArgs[0].trim();
                let elementStatusName = (_a = elementLoopArgs[1]) === null || _a === void 0 ? void 0 : _a.trim();
                for (let index = 0; index < elementArray.length; index++) {
                    let elementObject = elementArray[index];
                    let context = Object.assign({}, this.getContext());
                    context[elementItemName] = elementObject;
                    context[elementStatusName] = new duice.ObjectProxy({
                        index: index,
                        count: index + 1,
                        size: elementArray.length,
                        first: (index === 0),
                        last: (elementArray.length == index + 1)
                    });
                    this.createElementItem(elementObject, context, index);
                }
                let connectorArray = this.bindData[this.connectorProperty];
                for (let index = 0; index < connectorArray.length; index++) {
                    this.createConnectorItem(connectorArray[index]);
                }
                this.jsPlumbInstance.setSuspendDrawing(false, true);
                this.fitContainerToContent();
            }
            doUpdate(object) {
                console.debug("doUpdate:", object);
                this.doRender(object);
            }
            clearContainer() {
                this.container.innerHTML = '';
                this.elementItems.clear();
                this.sourceEndpoints.clear();
                this.targetEndpoints.clear();
                this.connectorItems.length = 0;
            }
            fitContainerToContent() {
                let maxWidth = 0;
                let maxHeight = 0;
                this.container.querySelectorAll(`.${duice.getNamespace()}-diagram-element`)
                    .forEach(element => {
                    let rect = element.getBoundingClientRect();
                    let width = parseInt(element.style.left) + rect.width;
                    if (width > maxWidth) {
                        maxWidth = width;
                    }
                    let height = parseInt(element.style.top) + rect.height;
                    if (height > maxHeight) {
                        maxHeight = height;
                    }
                });
                maxWidth += 10;
                maxHeight += 10;
                this.container.style.width = maxWidth + 'px';
                this.container.style.height = maxHeight + 'px';
                let rect = this.getHtmlElement().parentElement.getBoundingClientRect();
                if (maxWidth < rect.width) {
                    this.container.style.width = rect.width + 'px';
                }
                if (maxHeight < rect.height) {
                    this.container.style.height = rect.height + 'px';
                }
            }
            createElementItem(elementObject, context, index) {
                console.debug("createElementItem", elementObject, context, index);
                let elementId = elementObject[this.elementIdProperty];
                let elementItem = document.createElement('div');
                elementItem.innerHTML = this.htmlElementTemplate;
                duice.initialize(elementItem, context, index);
                duice.setElementAttribute(elementItem, 'element-id', elementId);
                elementItem.classList.add(duice.getNamespace() + '-diagram-element');
                elementItem.style.position = 'absolute';
                elementItem.style.left = (elementObject[this.elementPositionXProperty] || 10) + 'px';
                elementItem.style.top = (elementObject[this.elementPositionYProperty] || 10) + 'px';
                this.elementItems.set(elementId, elementItem);
                this.container.appendChild(elementItem);
                if (this.isEditable()) {
                    elementItem.style.cursor = 'move';
                    this.jsPlumbInstance.draggable(elementItem, {
                        stop: event => {
                            console.debug(event.finalPos);
                            elementObject[this.elementPositionXProperty] = event.finalPos[0];
                            elementObject[this.elementPositionYProperty] = event.finalPos[1];
                            this.fitContainerToContent();
                        }
                    });
                }
                let sourceEndpoint = this.jsPlumbInstance.addEndpoint(elementItem, {
                    isSource: this.isEditable(),
                    isTarget: false,
                    maxConnections: -1,
                    anchor: ['Continuous', { faces: ['bottom', 'right', 'top', 'left'] }],
                    endpoint: ['Rectangle', { width: 10, height: 10, cssClass: `${duice.getNamespace()}-diagram-endpoint-source`, hoverClass: `${duice.getNamespace()}-diagram-endpoint-source-hover` }],
                    endpointStyle: { fill: "darkgray", outlineStroke: 'black' },
                    connector: 'Straight',
                });
                this.sourceEndpoints.set(elementId, sourceEndpoint);
                let targetEndpoint = this.jsPlumbInstance.addEndpoint(elementItem, {
                    isTarget: this.isEditable(),
                    isSource: false,
                    maxConnections: -1,
                    anchor: ['Continuous', { faces: ['top', 'left', 'bottom', 'right'] }],
                    endpoint: ['Dot', { radius: 6, cssClass: `${duice.getNamespace()}-diagram-endpoint-target`, hoverClass: `${duice.getNamespace()}-diagram-endpoint-target-hover` }],
                    endpointStyle: { fill: "gray" },
                });
                this.targetEndpoints.set(elementId, targetEndpoint);
            }
            createConnectorItem(connectorObject) {
                let connectorSourceId = connectorObject[this.connectorSourceProperty];
                let connectorTargetId = connectorObject[this.connectorTargetProperty];
                let connectorItem = this.jsPlumbInstance.connect({
                    source: this.sourceEndpoints.get(connectorSourceId),
                    target: this.targetEndpoints.get(connectorTargetId),
                    connector: 'Straight',
                    detachable: this.isEditable(),
                    overlays: [
                        ["PlainArrow", {
                                location: 1,
                                width: 10,
                                length: 10,
                                id: "arrow"
                            }],
                        ['Label', {
                                label: `<span class="${duice.getNamespace() + '-diagram-connector-disconnect'}"></span>`,
                                location: 0.5,
                                events: {
                                    click: (labelOverlay, originalEvent) => {
                                        if (this.isEditable()) {
                                            this.jsPlumbInstance.deleteConnection(connectorItem);
                                        }
                                    }
                                }
                            }]
                    ]
                });
                this.connectorItems.push(connectorItem);
            }
            addConnectorData(connectorSourceId, connectorTargetId) {
                console.debug("addConnectorData", connectorSourceId, connectorTargetId);
                let connectorArray = this.bindData[this.connectorProperty];
                let index = connectorArray.findIndex(connectorObject => {
                    return connectorObject[this.connectorSourceProperty] === connectorSourceId
                        && connectorObject[this.connectorTargetProperty] === connectorTargetId;
                });
                if (index < 0) {
                    let connectorData = {};
                    connectorData[this.connectorSourceProperty] = connectorSourceId;
                    connectorData[this.connectorTargetProperty] = connectorTargetId;
                    connectorArray.push(connectorData);
                }
            }
            removeConnectorData(connectorSourceId, connectorTargetId) {
                console.debug("removeConnectorData", connectorSourceId, connectorTargetId);
                let linkArray = this.bindData[this.connectorProperty];
                let indexToRemove = linkArray.findIndex(linkObject => {
                    return linkObject[this.connectorSourceProperty] === connectorSourceId
                        && linkObject[this.connectorTargetProperty] === connectorTargetId;
                });
                console.debug("== indexToRemove:", indexToRemove);
                if (indexToRemove > -1) {
                    linkArray.splice(indexToRemove, 1);
                }
            }
            isEditable() {
                return !duice.ObjectProxy.isReadonlyAll(this.bindData)
                    && !duice.ObjectProxy.isDisableAll(this.bindData);
            }
        }
        plugin.Jsplumb = Jsplumb;
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class DiagramFactory extends duice.CustomElementFactory {
            doCreateElement(htmlElement, bindData, context) {
                return new plugin.Jsplumb(htmlElement, bindData, context);
            }
        }
        plugin.DiagramFactory = DiagramFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-jsplumb`, new DiagramFactory());
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
//# sourceMappingURL=duice-jsplumb.js.map
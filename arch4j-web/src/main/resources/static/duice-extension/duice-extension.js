/// <reference path="../node_modules/duice/dist/duice.d.ts" />
var duice;
(function (duice) {
    var extension;
    (function (extension) {
        class MarkdownEditor extends duice.ObjectElement {
            /**
             * constructor
             * @param element
             * @param bindData
             * @param context
             */
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
            /**
             * create toolbar
             */
            createToolbar() {
            }
            /**
             * set value
             * @param value
             */
            setValue(value) {
                if (!value) {
                    value = '';
                }
                this.codeMirror.doc.setValue(value);
            }
            /**
             * return value
             */
            getValue() {
                let value = this.codeMirror.doc.getValue();
                if (!value) {
                    return null;
                }
                return value;
            }
        }
        extension.MarkdownEditor = MarkdownEditor;
        /**
         * MarkdownEditorFactory
         */
        class MarkdownEditorFactory extends duice.ObjectElementFactory {
            /**
             * create element
             * @param htmlElement
             * @param bindData
             * @param context
             */
            doCreateElement(htmlElement, bindData, context) {
                return new MarkdownEditor(htmlElement, bindData, context);
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
        class MarkdownViewerFactory extends duice.ObjectElementFactory {
            /**
             * create element
             * @param htmlElement
             * @param bindData
             * @param context
             */
            doCreateElement(htmlElement, bindData, context) {
                return new MarkdownViewer(htmlElement, bindData, context);
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
                endPageIndex = Math.max(endPageIndex, 0);
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
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-pagination`, new duice.CustomElementFactory(Pagination));
    })(extension = duice.extension || (duice.extension = {}));
})(duice || (duice = {}));
//# sourceMappingURL=duice-extension.js.map
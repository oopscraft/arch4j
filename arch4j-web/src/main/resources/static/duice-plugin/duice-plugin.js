var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class Ckeditor extends duice.ObjectElement {
            constructor(htmlElement, bindData, context) {
                super(htmlElement, bindData, context);
                // config
                let config = {};
                if (duice.hasElementAttribute(htmlElement, 'config')) {
                    config = JSON.parse(duice.getElementAttribute(htmlElement, 'config').replace(/'/g, '"'));
                }
                // create ckeditor
                let textarea = document.createElement('textarea');
                this.getHtmlElement().appendChild(textarea);
                this.ckeditor = CKEDITOR.replace(textarea, config);
                // adds change event listener
                this.ckeditor.on('blur', () => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                    this.notifyObservers(event);
                }, true);
            }
            /**
             * set value
             * @param value
             */
            setValue(value) {
                value = value ? value : '';
                this.ckeditor.setData(value);
            }
            /**
             * return value
             */
            getValue() {
                return this.ckeditor.getData();
            }
        }
        plugin.Ckeditor = Ckeditor;
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class CkeditorFactory extends duice.ObjectElementFactory {
            /**
             * create element
             * @param htmlElement
             * @param bindData
             * @param context
             */
            doCreateElement(htmlElement, bindData, context) {
                return new plugin.Ckeditor(htmlElement, bindData, context);
            }
        }
        plugin.CkeditorFactory = CkeditorFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-ckeditor`, new CkeditorFactory());
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class Marked extends duice.ObjectElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                // creates child div
                this.div = document.createElement('div');
                this.getHtmlElement().appendChild(this.div);
                // config
                this.config = {
                    headerIds: false,
                    mangle: false
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
        plugin.Marked = Marked;
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class MarkedFactory extends duice.ObjectElementFactory {
            /**
             * create element
             * @param htmlElement
             * @param bindData
             * @param context
             */
            doCreateElement(htmlElement, bindData, context) {
                return new plugin.Marked(htmlElement, bindData, context);
            }
        }
        plugin.MarkedFactory = MarkedFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-marked`, new MarkedFactory());
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class Simplemde extends duice.ObjectElement {
            constructor(element, bindData, context) {
                super(element, bindData, context);
                // textarea
                let textarea = document.createElement('textarea');
                this.getHtmlElement().appendChild(textarea);
                // setting default config
                let config = {
                    element: textarea,
                    // autoDownloadFontAwesome: false,
                    // previewRender: function(plainText:string):string {
                    // 	return marked(plainText); // Returns HTML from a custom parser
                    // },
                    previewRender: function (plainText, preview) {
                        preview.innerHTML = marked.parse(plainText);
                        preview.querySelectorAll('[class^=language-]').forEach(function (pre) {
                            console.debug(pre);
                            pre.classList.add('line-numbers');
                        });
                        // highlight
                        Prism.highlightAll();
                        return preview.innerHTML;
                    },
                    tabSize: 4,
                    status: false,
                    hideIcons: ['fullscreen', 'side-by-side'],
                    renderingConfig: {
                        insertTexts: {
                            horizontalRule: ["", "\n\n-----\n\n"],
                            image: ["![](http://", ")"],
                            link: ["[", "](http://)"],
                            table: ["", "\n\n| Column 1 | Column 2 | Column 3 |\n| -------- | -------- | -------- |\n| Text     | Text      | Text     |\n\n"],
                        },
                    }
                };
                // creates simpleMDE
                this.simpleMde = new SimpleMDE(config);
                // add change listener
                this.simpleMde.codemirror.on('blur', () => {
                    let event = new duice.event.PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                    this.notifyObservers(event);
                }, true);
            }
            /**
             * set value
             * @param value
             */
            setValue(value) {
                if (!value) {
                    value = '';
                }
                // checks value is changed
                if (value !== this.simpleMde.value()) {
                    // sets value
                    this.simpleMde.value(value);
                    // Fixes CodeMirror bug (#344) - refresh not working after value changed.
                    // let codemirror = this.simpleMde.codemirror;
                    // setTimeout(function() {
                    //     codemirror.refresh();
                    // }.bind(codemirror), 0);
                }
            }
            /**
             * return value
             */
            getValue() {
                return this.simpleMde.value();
            }
        }
        plugin.Simplemde = Simplemde;
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class SimplemdeFactory extends duice.ObjectElementFactory {
            /**
             * create element
             * @param htmlElement
             * @param bindData
             * @param context
             */
            doCreateElement(htmlElement, bindData, context) {
                return new plugin.Simplemde(htmlElement, bindData, context);
            }
        }
        plugin.SimplemdeFactory = SimplemdeFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-simplemde`, new SimplemdeFactory());
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
/// <reference path="../node_modules/duice/dist/duice.d.ts" />
//# sourceMappingURL=duice-plugin.js.map
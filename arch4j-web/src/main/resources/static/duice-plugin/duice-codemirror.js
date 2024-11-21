var duiceCodemirror = (function (exports, duice) {
    'use strict';

    class DataEvent {
        constructor(source) {
            this.source = source;
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

    class Codemirror extends duice.ObjectElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
            this.mode = 'text/x-markdown';
            this.theme = 'default';
            this.getHtmlElement().style.display = 'block';
            // option
            this.mode = duice.getElementAttribute(element, 'mode') || this.mode;
            this.theme = duice.getElementAttribute(element, 'theme') || this.theme;
            // config
            let config = {
                mode: this.mode,
                inputStyle: 'textarea',
                lineNumbers: true,
                theme: this.theme,
                extraKeys: { "Enter": "newlineAndIndentContinueMarkdownList" }
            };
            // textarea
            let textarea = document.createElement('textarea');
            this.getHtmlElement().appendChild(textarea);
            // create code mirror
            this.codeMirror = CodeMirror.fromTextArea(textarea, config);
            this.codeMirror.setSize('100%', '100%');
            // refresh (not working without setTimeout)
            setTimeout(() => {
                this.codeMirror.refresh();
            }, 1);
            // add change event listener
            this.codeMirror.on("blur", () => {
                let event = new PropertyChangeEvent(this, this.getProperty(), this.getValue(), this.getIndex());
                this.notifyObservers(event);
            });
        }
        setValue(value) {
            if (!value) {
                value = '';
            }
            let scrollInfo = this.codeMirror.getScrollInfo();
            this.codeMirror.doc.setValue(value);
            this.codeMirror.scrollTo(scrollInfo.left, scrollInfo.top);
        }
        getValue() {
            let value = this.codeMirror.doc.getValue();
            if (!value) {
                return null;
            }
            return value;
        }
        setReadonly(readonly) {
            this.codeMirror.setOption('readOnly', readonly);
        }
    }

    class CodemirrorFactory extends duice.ObjectElementFactory {
        createElement(htmlElement, bindData, context) {
            return new Codemirror(htmlElement, bindData, context);
        }
    }
    (() => {
        // register
        duice.DataElementRegistry.register(`${duice.Configuration.getNamespace()}-codemirror`, new CodemirrorFactory());
    })();

    exports.CodemirrorFactory = CodemirrorFactory;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, duice);
//# sourceMappingURL=duice-codemirror.js.map

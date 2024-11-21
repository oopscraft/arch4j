var duiceMarked = (function (exports, duice) {
    'use strict';

    class Marked extends duice.ObjectElement {
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

    class MarkedFactory extends duice.ObjectElementFactory {
        createElement(htmlElement, bindData, context) {
            return new Marked(htmlElement, bindData, context);
        }
    }
    (() => {
        // register
        duice.DataElementRegistry.register(`${duice.Configuration.getNamespace()}-marked`, new MarkedFactory());
    })();

    exports.Marked = Marked;
    exports.MarkedFactory = MarkedFactory;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, duice);
//# sourceMappingURL=duice-marked.js.map

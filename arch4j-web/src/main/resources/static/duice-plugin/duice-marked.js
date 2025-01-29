/*!
 * duice-marked - v0.2.2
 * git: https://gitbub.com/chomookun/duice-plugin
 * website: https://duice-plugin.chomookun.org
 * Released under the LGPL(GNU Lesser General Public License version 3) License
 */
var duiceMarked = (function (exports, duice) {
    'use strict';

    class Marked extends duice.ObjectElement {
        constructor(element, bindData, context) {
            super(element, bindData, context);
            // creates child div
            this.div = document.createElement('div');
            this.getHtmlElement().appendChild(this.div);
            // customizes responsive table
            const renderer = new marked.Renderer();
            renderer.table = (header, body) => {
                return `<table style="display:inline-block;overflow-x:scroll;max-width:100%;">\n<thead>${header}</thead>\n<tbody>${body}</tbody>\n</table>`;
            };
            // config
            this.config = {
                headerIds: false,
                mangle: false,
                breaks: true,
                gfm: true,
                renderer: renderer
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

    exports.MarkedFactory = MarkedFactory;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, duice);
//# sourceMappingURL=duice-marked.js.map

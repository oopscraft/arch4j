/// <reference path="../node_modules/duice/dist/duice.d.ts" />
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
        plugin.Marked = Marked;
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class MarkedFactory extends duice.ObjectElementFactory {
            createElement(htmlElement, bindData, context) {
                return new plugin.Marked(htmlElement, bindData, context);
            }
        }
        plugin.MarkedFactory = MarkedFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-marked`, new MarkedFactory());
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
//# sourceMappingURL=duice-marked.js.map
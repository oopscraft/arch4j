/// <reference path="../node_modules/duice/dist/duice.d.ts" />
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class Pagination extends duice.CustomElement {
            constructor(htmlElement, bindData, context) {
                super(htmlElement, bindData, context);
                this.prevContent = '<';
                this.nextContent = '>';
                this.pageNumberSize = 10;
                // attributes
                this.pageProperty = duice.getElementAttribute(htmlElement, 'page-property');
                this.sizeProperty = duice.getElementAttribute(htmlElement, 'size-property');
                this.countProperty = duice.getElementAttribute(htmlElement, 'count-property');
                this.onclick = new Function(duice.getElementAttribute(htmlElement, 'onclick'));
                // optional
                this.pageNumberSize = Number(duice.getElementAttribute(htmlElement, 'page-number-size') || this.pageNumberSize);
                this.prevContent = duice.getElementAttribute(htmlElement, 'prev-content') || this.prevContent;
                this.nextContent = duice.getElementAttribute(htmlElement, 'next-content') || this.nextContent;
            }
            doRender(object) {
                // page,size,count
                let page = Number(object[this.pageProperty]);
                let size = Number(object[this.sizeProperty]);
                let count = Number(object[this.countProperty]);
                // calculate page
                let totalPage = Math.ceil(count / size);
                let startPageIndex = Math.floor(page / this.pageNumberSize) * this.pageNumberSize;
                let endPageIndex = Math.min(startPageIndex + (this.pageNumberSize - 1), totalPage - 1);
                endPageIndex = Math.max(endPageIndex, 0);
                // template
                let pagination = document.createElement('ul');
                pagination.classList.add(`${duice.getNamespace()}-pagination`);
                // prev
                let prev = document.createElement('li');
                prev.innerHTML = this.prevContent;
                prev.classList.add(`${duice.getNamespace()}-pagination__item-prev`);
                prev.dataset.page = String(Math.max(startPageIndex - this.pageNumberSize, 0));
                prev.addEventListener('click', () => {
                    this.onclick.call(prev);
                });
                if (page < this.pageNumberSize) {
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
                next.innerHTML = this.nextContent;
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
        plugin.Pagination = Pagination;
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
var duice;
(function (duice) {
    var plugin;
    (function (plugin) {
        class PaginationFactory extends duice.CustomElementFactory {
            doCreateElement(htmlElement, bindData, context) {
                return new plugin.Pagination(htmlElement, bindData, context);
            }
        }
        plugin.PaginationFactory = PaginationFactory;
        // register
        duice.DataElementRegistry.register(`${duice.getNamespace()}-pagination`, new PaginationFactory());
    })(plugin = duice.plugin || (duice.plugin = {}));
})(duice || (duice = {}));
//# sourceMappingURL=duice-pagination.js.map
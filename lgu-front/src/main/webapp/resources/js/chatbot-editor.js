const editor = Vue.createApp({
    setup: function () {
        return {
            DEFAULT_CANVAS_SIZE: 2000,
            PADDING_CANVAS: 150,
            DISPLAY_TYPE: {TEXT: 'TEXT', IMAGE: 'IMAGE', CARD: 'CARD', LIST: 'LIST'},
            BUTTON_TYPE: {
                TO_NEXT_BLOCK: 'TO_NEXT_BLOCK',
                TO_OTHER_BLOCK: 'TO_OTHER_BLOCK',
                TO_URL: 'TO_URL',
                CALL_CONSULTANT: 'CALL_CONSULTANT',
                MAKE_TEL_CALL: 'MAKE_TEL_CALL',
                CALL_API: 'CALL_API'
            },
        }
    },
    data: function () {
        return {
            canvasWidth: 0,
            canvasHeight: 0,
            /**
             * Block: {
             *     id: String // generate on construct and change when submit to given by server
             *     title: [ null | String ]
             *     submitted: Boolean // is saved to server
             *     x: Number
             *     y: Number
             *     keywords: String array
             *     displayElements: [
             *         {
             *             type: [ TEXT | IMAGE | CARD | LIST ]
             *             content: {
             *                 TEXT: String
             *                 IMAGE: String
             *                 CARD: {
             *                     head: String
             *                     body: String
             *                     image: String
             *                 }
             *                 LIST: [{
             *                     head: String
             *                     body: String
             *                     image: String
             *                     url: String
             *                 }, ... ]
             *             }
             *         }
             *     ]
             *     parent: [ null | `Button.id` ]
             *     buttons: Button array: {
             *         id: String
             *         title: String
             *         type: [ TO_NEXT_BLOCK | TO_OTHER_BLOCK | TO_URL | CALL_CONSULTANT | MAKE_TEL_CALL | CALL_API ]
             *         parameter: {
             *             TO_NEXT_BLOCK: null
             *             TO_OTHER_BLOCK: {block: `Block.id`}
             *             TO_URL: {url: String}
             *             MAKE_TEL_CALL: {tel: String}
             *             CALL_CONSULTANT: {queueId: String}
             *             CALL_API: {
             *                 url: String
             *                 parameters: [ {title,name,type}, ... ]
             *                 useResponse: Boolean
             *                 responseFormat: String
             *             }
             *         }
             *     }
             * }
             */
            blocks: [],
            renderingLineTimers: [],
        }
    },
    methods: {
        init: function () {
            const canvasRect = this.$refs.canvas.getBoundingClientRect()
            this.canvasWidth = canvasRect.width || this.DEFAULT_CANVAS_SIZE
            this.canvasHeight = canvasRect.height || this.DEFAULT_CANVAS_SIZE
        },
        generateId: function () {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                const r = Math.random() * 16 | 0
                const v = c === 'x' ? r : r & 0x3 | 0x8
                return v.toString(16)
            })
        },
        addBlock: function (o) {
            this.blocks.push(Object.assign({
                id: this.generateId(),
                submitted: false,
                keywords: [],
                displayElements: [],
                parent: null,
                buttons: [],
            }, o))
        },
        getBlock: function (option) {
            const blocks = this.blocks
            if (typeof option === 'function') {
                const block = blocks.filter(option)[0]
                if (!block) throw 'cannot find block'
                return block
            }

            const blockId = option
            const block = this.blocks.filter(function (e) {
                return blockId === e.id
            })[0]
            if (!block) throw 'invalid blockId: ' + blockId
            return block
        },
        setMaxSizeIfOverflowByBlock: function (block) {
            const blockRight = block.x + block.width + this.PADDING_CANVAS
            const blockBottom = block.y + block.height + this.PADDING_CANVAS

            this.canvasWidth = Math.max(this.canvasWidth > blockRight ? this.canvasWidth : blockRight, $(this.container).innerWidth())
            this.canvasHeight = Math.max(this.canvasHeight > blockBottom ? this.canvasHeight : blockBottom, $(this.container).innerHeight())
        },
        createLineSvg: function (lineElement) {

            for (let i = 0; i < this.renderingLineTimers.length; i++) {
                const timer = this.renderingLineTimers[i]
                if (timer.button === lineElement.button && timer.block === lineElement.block) {
                    clearTimeout(timer.timerId)
                    this.renderingLineTimers.splice(i--, 1)
                }
            }

            if (!lineElement.button || !lineElement.block)
                return

            const buttonRect = lineElement.button.getBoundingClientRect()
            const blockRect = lineElement.block.getBoundingClientRect()

            const _this = this
            if ((!buttonRect.top && !buttonRect.left) || (!blockRect.top && !blockRect.left))
                this.renderingLineTimers.push({
                    timerId: setTimeout(function () {
                        _this.createLineSvg(lineElement)
                    }, 50),
                    button: lineElement.button,
                    block: lineElement.block,
                })

            function getD() {
                const MAX_CURVE_RADIUS = 50
                const M = 'M', L = 'L', Q = 'Q'
                const matrices = []

                for (let i = 0; i < arguments.length; i++) {
                    const p1 = arguments[i], p2 = arguments[i + 1], p3 = arguments[i + 2]

                    if (i === 0)
                        matrices.push([M, p1[0], p1[1]])

                    if (!p3) {
                        matrices.push([L, p2[0], p2[1]])
                        break
                    }

                    const xSide1 = p2[0] - p1[0], ySide1 = p2[1] - p1[1]
                    const xSide2 = p3[0] - p2[0], ySide2 = p3[1] - p2[1]
                    const radius1 = Math.min(MAX_CURVE_RADIUS, Math.abs((xSide1 || ySide1) / 2))
                    const radius2 = Math.min(MAX_CURVE_RADIUS, Math.abs((xSide2 || ySide2) / 2))
                    const radius = Math.min(radius1, radius2)

                    matrices.push([L, p2[0] - (xSide1 ? radius * (xSide1 > 0 ? 1 : -1) : 0), p2[1] - (ySide1 ? radius * (ySide1 > 0 ? 1 : -1) : 0)])
                    matrices.push([Q, p2[0], p2[1], p2[0] + (xSide2 ? xSide2 > 0 ? radius : -radius : 0), p2[1] + (ySide2 ? ySide2 > 0 ? radius : -radius : 0)])
                }

                return matrices.map(function (e) {
                    return e.join(' ')
                }).join(', ')
            }

            const width = blockRect.left - buttonRect.left
            const height = blockRect.top - buttonRect.top
            const PAD = 50

            const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg')
            svg.style.left = (width >= 0 ? buttonRect.left : blockRect.left - PAD) + 'px' // 만약 block이 button보다 왼쪽에 있으면, 돌아가는 선을 표현하기 위해 pad를 두었다.
            svg.style.top = (height >= 0 ? buttonRect.top : blockRect.top) + 'px'
            svg.setAttribute('class', 'ui-chatbot-line')
            svg.setAttribute('width', '' + (width >= 0 ? width : -width + 2 * PAD))
            svg.setAttribute('height', '' + Math.abs(height))

            //       (x,y)
            // case1 ( width > 0, height > 0 ): 0,0 -> 100,100: 0 0, 50 0, 50 100, 100 100
            // case2 ( width > 0, height < 0 ): 0,100 -> 100,0: 0 100, 50 100, 50 0, 100 0
            // case3 ( width < 0, height > 0 ): 100,0 -> 0,100: 100 0, 100+pad 0, 100+pad 50, 0-pad 50, 0-pad 100, 0 100
            // case4 ( width < 0, height < 0 ): 100,100 -> 0,0: 100 100, 100+pad 100, 100+pad 50, 0-pad 50, 0-pad 0, 0 0
            const path = document.createElementNS('http://www.w3.org/2000/svg', 'path')
            path.setAttribute('d',
                (width >= 0 && height >= 0) ? getD([0, 0], [width / 2, 0], [width / 2, height], [width, height])
                    : (width >= 0 && height < 0) ? getD([0, -height], [width / 2, -height], [width / 2, 0], [width, 0])
                        : (width < 0 && height >= 0) ? getD([-width + PAD, 0], [-width + 2 * PAD, 0], [-width + 2 * PAD, height / 2], [0, height / 2], [0, height], [PAD, height])
                            : getD([-width + PAD, -height], [-width + 2 * PAD, -height], [-width + 2 * PAD, -height / 2], [0, -height / 2], [0, 0], [PAD, 0])
            )
            svg.appendChild(path)

            return svg
        },
    },
    updated: function () {
        const _this = this
        _this.$refs.lines.innerHTML = ''

        const lineElements = (function () {
            const result = []
            _this.blocks.forEach(function (block) {
                if (block.parent)
                    result.push({block: _this.$refs['blockPoint.' + block.id], button: _this.$refs['buttonPoint.' + block.parent]})

                block.buttons.filter(function (button) {
                    return button.type === _this.BUTTON_TYPE.TO_OTHER_BLOCK
                }).forEach(function (button) {
                    result.push({block: _this.$refs['blockPoint.' + button.parameter.TO_OTHER_BLOCK.block], button: _this.$refs['buttonPoint.' + button.id]})
                })
            })
            return result
        })()

        lineElements.forEach(function (e) {
            const svg = _this.createLineSvg(e)
            if (svg)
                _this.$refs.lines.appendChild(svg)
        })
    },
    mounted: function () {
        this.init()
    },
}).mount('#app')

editor.addBlock({
    id: 'block-1',
    title: '1번째 block',
    x: 100,
    y: 10,
    keywords: ['키워드1', '키워드2', '키워드3', '키워드4',],
    displayElements: [{
        type: 'TEXT',
        content: {
            TEXT: '디스플레이 텍스트형'
        }
    }, {
        type: 'IMAGE',
        content: {
            IMAGE: 'https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAORFtK.img'
        }
    }, {
        type: 'CARD',
        content: {
            CARD: {
                head: '디스플레이 카드형',
                body: '디스플레이 카드형 내용 주저리 주저리',
                image: 'https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAORFtK.img',
            }
        }
    }, {
        type: 'LIST',
        content: {
            LIST: [{
                head: '디스플레이 리스트형 아이템1. url 포함',
                body: '디스플레이 리스트형 내용 주저리 주저리',
                image: 'https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAORFtK.img',
                url: 'https://www.msn.com/ko-kr/news/politics/%ED%99%A9%EA%B5%90%EC%9D%B5-%ED%99%94%EC%B2%9C%EB%8C%80%EC%9C%A0-%EB%88%84%EA%B5%AC%EA%BB%8D%EB%8B%88%EA%B9%8C-%EC%9E%90%EA%B8%B0%EB%93%A4-%EA%B2%83%EC%9D%B4%EB%9D%BC-%EC%9E%90%EB%9E%91%ED%95%98%EB%82%98/ar-AAORCag?ocid=msedgntp'
            }, {
                head: '디스플레이 리스트형 아이템2',
                body: '디스플레이 리스트형 내용 주저리 주저리',
                image: 'https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAORFtK.img',
            }, {
                head: '디스플레이 리스트형 아이템3',
                body: '디스플레이 리스트형 내용 주저리 주저리',
                image: 'https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAORFtK.img',
            },]
        }
    },],
    buttons: [{
        id: 'button-1-1',
        title: 'button-1-1',
        type: 'TO_NEXT_BLOCK',
    }, {
        id: 'button-1-2',
        title: 'button-1-2',
        type: 'TO_NEXT_BLOCK',
    }, {
        id: 'button-1-3',
        title: 'button-1-3',
        type: 'TO_OTHER_BLOCK',
        parameter: {
            TO_OTHER_BLOCK: {block: 'block-5'}
        }
    },],
})

editor.addBlock({
    id: 'block-2',
    title: '2번째 block',
    x: 400,
    y: 20,
    parent: 'button-1-1',
    buttons: [{
        id: 'button-2-1',
        title: 'button-2-1',
        type: 'TO_NEXT_BLOCK',
    },],
})

editor.addBlock({
    id: 'block-3',
    title: '3번째 block',
    x: 400,
    y: 210,
    parent: 'button-1-2',
    buttons: [{
        id: 'button-3-1',
        title: 'button-3-1',
        type: 'TO_NEXT_BLOCK',
    }, {
        id: 'button-3-2',
        title: 'button-3-2',
        type: 'TO_NEXT_BLOCK',
    },],
})

editor.addBlock({
    id: 'block-4',
    title: '4번째 block',
    x: 800,
    y: 210,
    parent: 'button-3-1',
    buttons: [{
        id: 'button-4-1',
        title: 'button-4-1',
        type: 'TO_OTHER_BLOCK',
        parameter: {
            TO_OTHER_BLOCK: {block: 'block-1'}
        }
    },],
})

editor.addBlock({
    id: 'block-5',
    title: '5번째 block',
    x: 800,
    y: 510,
    parent: 'button-3-2',
    buttons: [{
        id: 'button-5-1',
        title: 'button-5-1',
        type: 'TO_NEXT_BLOCK',
    },],
})

setInterval(function () {
    editor.$data.blocks[0].y += 3
    editor.$data.blocks[0].x += 1
}, 100)

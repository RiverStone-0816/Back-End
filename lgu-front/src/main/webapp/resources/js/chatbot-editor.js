const editor = Vue.createApp({
    setup: function () {
        return {
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
             *     parent: [ null | Button ]
             *     buttons: Button array: {
             *         id: String
             *         title: String
             *         type: [ TO_NEXT_BLOCK | TO_OTHER_BLOCK | TO_URL | CALL_CONSULTANT | MAKE_TEL_CALL | CALL_API ]
             *         parameter: {
             *             TO_NEXT_BLOCK: null
             *             TO_OTHER_BLOCK: {block: Block}
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
            this.canvasWidth = canvasRect.width || 100
            this.canvasHeight = canvasRect.height || 100
        },
        addBlock: function (o) {
            this.blocks.push(o)
        },
        getBlock: function (option) {
            const blocks = this.blocks;
            if (typeof option === 'function') {
                const block = blocks.filter(option)[0];
                if (!block) throw 'cannot find block';
                return block;
            }

            const blockId = option;
            const block = this.blocks.filter(function (e) {
                return blockId === e.id;
            })[0];
            if (!block) throw 'invalid blockId: ' + blockId;
            return block;
        },
        setMaxSizeIfOverflowByBlock: function (block) {
            const blockRight = block.x + block.width + this.PADDING_CANVAS
            const blockBottom = block.y + block.height + this.PADDING_CANVAS

            this.canvasWidth = Math.max(this.canvasWidth > blockRight ? this.canvasWidth : blockRight, $(this.container).innerWidth())
            this.canvasHeight = Math.max(this.canvasHeight > blockBottom ? this.canvasHeight : blockBottom, $(this.container).innerHeight())
        },
        renderLine: function (lineElement) {
            for (let i = 0; i < this.renderingLineTimers.length; i++) {
                const timer = this.renderingLineTimers[i]
                if (timer.button === lineElement.button && timer.block === lineElement.block) {
                    clearTimeout(timer.timerId)
                    this.renderingLineTimers.splice(i--, 1)
                }
            }

            const buttonRect = lineElement.button.getBoundingClientRect()
            const blockRect = lineElement.block.getBoundingClientRect()

            const _this = this
            if ((!buttonRect.top && !buttonRect.left) || (!blockRect.top && !blockRect.left))
                this.renderingLineTimers.push({
                    timerId: setTimeout(function () {
                        _this.renderLine(lineElement)
                    }, 50),
                    button: lineElement.button,
                    block: lineElement.block,
                })

            //       (x,y)
            // case1 ( width > 0, height > 0 ): 0,0 -> 100,100: 0 0, 50 0, 50 100, 100 100
            // case2 ( width > 0, height < 0 ): 0,100 -> 100,0: 0 100, 50 100, 50 0, 100 0
            // case3 ( width < 0, height > 0 ): 100,0 -> 0,100: 100 0, 100+pad 0, 100+pad 50, 0-pad 50, 0-pad 100, 0 100
            // case4 ( width < 0, height < 0 ): 100,100 -> 0,0: 100 100, 100+pad 100, 100+pad 50, 0-pad 50, 0-pad 0, 0 0

            const width = buttonRect.left - blockRect.left
            const height = buttonRect.top - blockRect.top
            const PAD = 20
            return (width && height) ? `M 0 0, L ${width / 2} 0, L ${width / 2} ${height}, L ${width} ${height}`
                : (width && !height) ? `M 0 ${height}, L ${width / 2} ${height}, L ${width / 2} 0, L ${width} 0`
                    : (!width && height) ? `M ${width} 0, L ${width + PAD} 0, L ${width + PAD} ${height / 2}, L ${0 - PAD} ${height / 2}, L ${0 - PAD} ${height}, L 0 ${height}`
                        : `M ${width} ${height}, L ${width + PAD}, ${height}, L ${width + PAD}, ${height / 2}, L ${0 - PAD}, ${height / 2}, L ${0 - PAD}, 0, L 0 0`
        },
    },
    computed: {
        getLineElements: function () {
            const result = []

            const _this = this
            this.blocks.forEach(function (block) {
                if (block.parent)
                    result.push({block: _this.$refs['blockPoint.' + block.id], button: _this.$refs['buttonPoint.' + block.parent.id]})

                block.buttons.filter(function (button) {
                    return button.type === _this.BUTTON_TYPE.TO_OTHER_BLOCK
                }).forEach(function (button) {
                    result.push({block: _this.$refs['blockPoint.' + button.parameter.TO_OTHER_BLOCK.id], button: _this.$refs['buttonPoint.' + button.id]})
                })
            })

            return result
        },
    },
    mounted: function () {
        this.init()
    },
}).mount('#app')


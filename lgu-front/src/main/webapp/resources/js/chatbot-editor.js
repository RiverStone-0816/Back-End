const editor = Vue.createApp({
    setup: function () {
        return {
            PADDING_CANVAS: 150
        }
    },
    data: function () {
        return {
            canvasWidth: 0,
            canvasHeight: 0,

            /**
             * class Node: {
             *     id: String,
             *     title: String,
             *     x: Number,
             *     y: Number,
             *     empty: Boolean,
             *     parentNode: Node,
             *     points: Point
             * }
             * class Point: {
             *     id: String,
             *     title: String
             * }
             */
            nodes: []
        }
    },
    methods: {
        init: function () {
            const canvasRect = this.$refs.canvas.getBoundingClientRect()
            this.canvasWidth = canvasRect.width || 100
            this.canvasHeight = canvasRect.height || 100
        },
        addNode: function (o) {
            this.nodes.push(o)
        },
        getNode: function (option) {
            const nodes = this.nodes;
            if (typeof option === 'function') {
                const node = nodes.filter(option)[0];
                if (!node) throw 'cannot find node';
                return node;
            }

            const nodeId = option;
            const node = this.nodes.filter(function (e) {
                return nodeId === e.option.id;
            })[0];
            if (!node) throw 'invalid nodeId: ' + nodeId;
            return node;
        },
        updateNode: function (nodeId, optionParam) {
            this.getNode(nodeId).setContent(optionParam)
        },
        setMaxSizeIfOverflowByNode: function (node) {
            const nodeRight = node.x + node.width + this.PADDING_CANVAS
            const nodeBottom = node.y + node.height + this.PADDING_CANVAS

            this.canvasWidth = this.canvasWidth > nodeRight ? this.canvasWidth : nodeRight
            this.canvasHeight = this.canvasHeight > nodeBottom ? this.canvasHeight : nodeBottom

            this.canvasWidth = Math.max(this.canvasWidth, $(this.container).innerWidth())
            this.canvasHeight = Math.max(this.canvasHeight, $(this.container).innerHeight())
        }
    },
    mounted: function () {
        this.init()
    },
}).mount('#app')

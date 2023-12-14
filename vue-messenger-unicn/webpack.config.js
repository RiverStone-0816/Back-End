const webpack = require('webpack')

module.exports = {
    configureWebpack: {
        plugins: [
            new webpack.ProvidePlugin({adapter: 'webrtc-adapter'})
        ],
        module: {
            rules: [
                {
                    test: require.resolve('janus-gateway'),
                    loader: 'exports-loader',
                    options: {
                        exports: 'Janus',
                    },
                }
            ]
        }
    }
}
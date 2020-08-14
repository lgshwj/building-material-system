module.exports = {
    baseUrl: './',
    assetsDir: 'static',
    productionSourceMap: false,
    devServer: {
        port: 80
    }
    /*devServer: {
         proxy: {
             '/api':{
                 target: 'http://localhost:8080',
                 changeOrigin: true,
                 pathRewrite: {
                    '^/api':''
                 }
             }
         }
    }*/
}
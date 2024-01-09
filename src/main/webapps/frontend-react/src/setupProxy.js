/* eslint-disable @typescript-eslint/no-var-requires */
const { createProxyMiddleware } = require('http-proxy-middleware')

module.exports = function (app) {
  app.use(
    '/pcms/service',
    createProxyMiddleware({
      target: 'http://localhost:7207',
      changeOrigin: true
    })
  )
}

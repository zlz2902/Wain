/*
 * @Author: daidai
 * @Date: 2021-12-06 10:58:24
 * @LastEditTime: 2024-08-29 17:46:49
 */
let UtilVar = {
    ENC: false, //返回结果是否加密
    // dev 模式下 .env.dev 可留空，配合 vue.config.js 代理 /bigscreen；非空则直连该地址
    baseUrl: process.env.VUE_APP_BASE_API || '',
    code: 401,
}

console.log( process.env);

export default UtilVar


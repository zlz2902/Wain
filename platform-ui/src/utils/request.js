import axios from 'axios'
import { Notification, MessageBox, Message, Loading } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from "@/utils/platform";
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'
// 导入防重复提交所需的 Token 处理函数
import { getCurrentToken, generateSubmitToken, setCurrentToken, clearCurrentToken } from '@/utils/submitToken'

let downloadLoadingInstance;
// 是否显示重新登录
export let isRelogin = { show: false };

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // 改用环境变量配置基础地址（更灵活，也可改回你原有配置）
  baseURL: process.env.VUE_APP_BASE_API || configSrc?.src,
  // 超时
  timeout: 60000
})

// request拦截器（合并原有逻辑 + 防重复提交Token逻辑）
service.interceptors.request.use(config => {
  // 1. 原有 Token 处理逻辑
  const isToken = (config.headers || {}).isToken === false
  // 是否需要防止数据重复提交（原有缓存方式）
  const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken()
  }

  // 2. 原有 GET 参数映射逻辑
  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params);
    url = url.slice(0, -1);
    config.params = {};
    config.url = url;
  }

  // 3. 原有基于缓存的防重复提交逻辑（保留，可与Token方式并行）
  if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
    const requestObj = {
      url: config.url,
      data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
      time: new Date().getTime()
    }
    const requestSize = Object.keys(JSON.stringify(requestObj)).length;
    const limitSize = 5 * 1024 * 1024;
    if (requestSize >= limitSize) {
      console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
      return config;
    }
    const sessionObj = cache.session.getJSON('sessionObj')
    if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
      cache.session.setJSON('sessionObj', requestObj)
    } else {
      const s_url = sessionObj.url;
      const s_data = sessionObj.data;
      const s_time = sessionObj.time;
      const interval = 1000;
      if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
        const message = '数据正在处理，请勿重复提交';
        console.warn(`[${s_url}]: ` + message)
        return Promise.reject(new Error(message))
      } else {
        cache.session.setJSON('sessionObj', requestObj)
      }
    }
  }

  // 4. 新增：防重复提交 Token 逻辑（POST/PUT/DELETE 请求添加）
  if (['post', 'put', 'delete'].includes(config.method)) {
    let token = getCurrentToken();
    if (!token) {
      token = generateSubmitToken();
      setCurrentToken(token);
    }
    config.headers['X-Submit-Token'] = token;
  }

  return config
}, error => {
  console.log(error)
  return Promise.reject(error) // 补充缺失的 return，避免 Promise 状态异常
})

// 响应拦截器（合并原有逻辑 + Token 清除逻辑）
service.interceptors.response.use(res => {
  // 1. 新增：请求成功后清除防重复提交 Token
  if (['post', 'put', 'delete'].includes(res.config.method)) {
    clearCurrentToken();
  }

  // 2. 新增：特殊处理重复提交错误（后端返回重复提交提示时）
  const resData = res.data;
  if (resData.code === 500 && resData.msg?.includes('重复提交')) {
    clearCurrentToken(); // 清除无效 Token，允许重新提交
    Message({
      message: resData.msg || '重复提交请求，请稍后再试',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(new Error(resData.msg || '重复提交请求'))
  }

  // 3. 原有响应处理逻辑
  // 未设置状态码则默认成功状态
  const code = resData.code || 200;
  // 获取错误信息
  const msg = errorCode[code] || resData.msg || errorCode['default']
  // 二进制数据则直接返回
  if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
    return resData
  }
  if (code === 401) {
    if (!isRelogin.show) {
      isRelogin.show = true;
      MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' }).then(() => {
        isRelogin.show = false;
        store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => {
        isRelogin.show = false;
      });
    }
    return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
  } else if (code === 500) {
    Message({ message: msg, type: 'error' })
    return Promise.reject(new Error(msg))
  } else if (code === 601) {
    Message({ message: msg, type: 'warning' })
    return Promise.reject('error')
  } else if (code !== 200) {
    Notification.error({ title: msg })
    return Promise.reject('error')
  } else {
    return resData
  }
}, error => {
  console.log('err' + error)
  let { message } = error;
  if (message === "Network Error") {
    message = "后端接口连接异常";
  } else if (message.includes("timeout")) {
    message = "系统接口请求超时";
  } else if (message.includes("Request failed with status code")) {
    message = "系统接口" + message.substr(message.length - 3) + "异常";
  }
  Message({ message: message, type: 'error', duration: 5 * 1000 })
  return Promise.reject(error)
})

// 通用下载方法（保持原有逻辑不变）
export function download(url, params, filename, config) {
  downloadLoadingInstance = Loading.service({ text: "正在下载数据，请稍候", spinner: "el-icon-loading", background: "rgba(0, 0, 0, 0.7)", })
  return service.post(url, params, {
    transformRequest: [(params) => { return tansParams(params) }],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config
  }).then(async (data) => {
    const isBlob = blobValidate(data);
    if (isBlob) {
      const blob = new Blob([data])
      saveAs(blob, filename)
    } else {
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      Message.error(errMsg);
    }
    downloadLoadingInstance.close();
  }).catch((r) => {
    console.error(r)
    Message.error('下载文件出现错误，请联系管理员！')
    downloadLoadingInstance.close();
  })
}

export default service

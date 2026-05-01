// src/utils/submitToken.js

/**
 * 生成提交Token
 * @returns {string} 唯一Token
 */
export function generateSubmitToken() {
  const timestamp = Date.now().toString(36);
  const randomStr = Math.random().toString(36).substr(2, 9);
  return `submit_${timestamp}_${randomStr}`;
}

/**
 * 获取当前Token（从sessionStorage）
 * @returns {string} Token值
 */
export function getCurrentToken() {
  return sessionStorage.getItem('currentSubmitToken');
}

/**
 * 设置当前Token（到sessionStorage）
 * @param {string} token Token值
 */
export function setCurrentToken(token) {
  sessionStorage.setItem('currentSubmitToken', token);
}

/**
 * 清除当前Token
 */
export function clearCurrentToken() {
  sessionStorage.removeItem('currentSubmitToken');
}

export function getChartPerms() {
  try {
    const raw = window.location.search.match(/[?&]chartPerms=([^&]+)/)
    const decoded = raw && raw[1] ? decodeURIComponent(raw[1]) : '[]'
    const data = JSON.parse(decoded)
    return Promise.resolve({ success: true, data })
  } catch (e) {
    return Promise.resolve({ success: true, data: [] })
  }
}

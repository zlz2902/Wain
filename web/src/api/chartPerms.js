import { GET } from './api'

export function getChartPerms() {
  return GET('/getChartPerms')
}

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css'
import request from "./utils/request";
import axios from 'axios';

// 配置 Axios 基础 URL 和全局设置
axios.defaults.baseURL = 'http://localhost:9090'; // 后端接口地址
axios.defaults.timeout = 5000; // 请求超时（毫秒）

Vue.config.productionTip = false
Vue.use(ElementUI, { size: "mini" });

// 将 Axios 挂载到 Vue 原型上
Vue.prototype.$axios = axios;
export default axios;

Vue.prototype.request = request

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
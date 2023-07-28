import './assets/main.css';
import { createApp } from 'vue';
import axios from 'axios';
import VueAxios from 'vue-axios';
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
// import './index.css'

import App from './App.vue';

const app = createApp(App);
app.use(VueAxios, axios);
app.use(Antd).mount("#app");
app.provide('axios', app.config.globalProperties.axios);
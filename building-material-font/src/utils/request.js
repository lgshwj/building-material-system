import axios from 'axios';
import qs from 'qs'

import { Message } from 'element-ui';
import router from '../router';
import Vue from 'vue';
Vue.prototype.$axios = axios;
const service = axios.create({
    // process.env.NODE_ENV === 'development' 来判断是否开发环境
    baseURL: 'http://localhost:8080',
    //baseURL: 'http://47.116.14.78:8080',
    timeout: 5000,

});

// 请求拦截器 将token至于header中
service.interceptors.request.use(
    config => {
        const token = localStorage.getItem("token");

        if (token) {
            config.headers.token = token // 请求头部添加token
        }
        return config
    },
    error => {
        /*Message.error('登录已失效，请重新登录');
        localStorage.removeItem('userName');
        localStorage.removeItem('token');
        router.push('/login');*/
        return Promise.reject(error)
    }
);

/*service.interceptors.request.use(
    config => {
        return config;
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
);*/

service.interceptors.response.use(
    response => {
        if (response.status === 200) {
            return response.data;
        } else {
            Message.error('登录已失效，请重新登录');
            localStorage.removeItem('userName');
            localStorage.removeItem('token');
            router.push('/login');
            Promise.reject();
        }
    },
    error => {
        Message.error('登录已失效，请重新登录');
        localStorage.removeItem('userName');
        localStorage.removeItem('token');
        router.push('/login');
        return Promise.reject();
    }
);

export default service;

import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI, { Message } from 'element-ui';
import VueI18n from 'vue-i18n';
import { messages } from './components/common/i18n';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import './assets/css/theme-green/index.css'; // 浅绿色主题
import './assets/css/icon.css';
import './components/common/directives';
import 'babel-polyfill';
import { checkPermission } from './api/permission';

import './assets/icon/iconfont.css'
import infiniteScroll from 'vue-infinite-scroll';
Vue.use(infiniteScroll);
Vue.config.productionTip = false;
Vue.use(VueI18n);
Vue.use(ElementUI, {
    size: 'small'
});
const i18n = new VueI18n({
    locale: 'zh',
    messages
});

//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | 建筑材料管理系统`;
    const token = localStorage.getItem('token');
    // 进行权限验证
    // 没有token而且不是去登录页
    if (!token && to.path !== '/login') {
        next('/login');
    } else {
        // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
        if (navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor') {
            Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
                confirmButtonText: '确定'
            });
        } else {
            let data = {"token": token, "navigationRoute": to.path};

            if (to.path === '/login') {
                next();
            } else { // 鉴别此登录账户有没有权限
                checkPermission(data).then(res => {
                    if (res.code === '1001') {
                        next();
                    } else if (res.code === '2001') {
                        next('/403');
                    } else {
                        Message.error('登录已失效，请重新登录');
                        localStorage.removeItem('userName');
                        localStorage.removeItem('token');
                        next('/login');
                    }
                });
            }
        }
    }
});

new Vue({
    router,
    i18n,
    render: h => h(App)
}).$mount('#app');

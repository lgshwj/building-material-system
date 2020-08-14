import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/dashboard'
        },
        {
            path: '/',
            component: () => import('../components/common/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/dashboard',
                    component: () => import('../components/page/Dashboard.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/404',
                    component: () => import('../components/page/404.vue'),
                    meta: { title: '404' }
                },
                {
                    path: '/403',
                    component: () => import('../components/page/403.vue'),
                    meta: { title: '403' }
                },
                {
                    path: '/navigation',
                    component: () => import('../components/page/Navigation.vue'),
                    meta: { title: '系统导航' }
                },
                {
                    path: '/account',
                    component: () => import('../components/page/Account.vue'),
                    meta: { title: '账户管理' }
                },
                {
                    path: '/materialCategory',
                    component: () => import('../components/page/MaterialCategory.vue'),
                    meta: { title: '材料类别' }
                },
                {
                    path: '/supplier',
                    component: () => import('../components/page/Supplier.vue'),
                    meta: { title: '供应商管理' }
                },
                {
                    path: '/factory',
                    component: () => import('../components/page/Factory.vue'),
                    meta: { title: '生产商管理' }
                },
                {
                    path: '/purchasePlan',
                    component: () => import('../components/page/PurchasePlan.vue'),
                    meta: { title: '采购审核' }
                },
                {
                    path: '/myPurchase',
                    component: () => import('../components/page/MyPurchase.vue'),
                    meta: { title: '我的采购' }
                },
                {
                    path: '/materialStock',
                    component: () => import('../components/page/MaterialStock.vue'),
                    meta: { title: '库存管理' }
                },
                {
                    path: '/quotePrice',
                    component: () => import('../components/page/QuotePrice.vue'),
                    meta: { title: '报价信息' }
                },
                {
                    path: '/siteInformation',
                    component: () => import('../components/page/SiteInformation.vue'),
                    meta: { title: '工地信息' }
                },
                {
                    path: '/siteUsage',
                    component: () => import('../components/page/SiteUsage.vue'),
                    meta: { title: '用料详情' }
                }
                ,
                {
                    path: '/statisticPurchase',
                    component: () => import('../components/page/StatisticPurchase.vue'),
                    meta: { title: '采购统计' }
                }
            ]
        },
        {
            path: '/login',
            component: () => import('../components/page/Login.vue'),
            meta: { title: '登录' }
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
});

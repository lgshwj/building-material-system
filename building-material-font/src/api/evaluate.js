import request from '../utils/request';
import qs from 'qs'

export const addEvaluate = (data) => {
    return request({
        url: '/evaluate/addEvaluate',
        method: 'post',
        data: qs.stringify(data)
    });
};

export const queryEvaluateByPurchasePlanId = (query) => {
    return request({
        url: '/evaluate/queryEvaluateByPurchasePlanId',
        method: 'get',
        params: query
    });
};


export const getFacotryEvaluate = (query) => {
    return request({
        url: '/evaluate/getFactoryEvaluate',
        method: 'get',
        params: query
    });
};

export const getSupplierEvaluate = (query) => {
    return request({
        url: '/evaluate/getSupplierEvaluate',
        method: 'get',
        params: query
    });
};
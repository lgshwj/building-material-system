import request from '../utils/request';
import qs from 'qs'

export const querySupplierWithLimit = (query) => {
    return request({
        url: '/supplier/querySupplier',
        method: 'get',
        params: query
    });
};

export const updateSupplier = (data) => {
    return request({
        url: '/supplier/editSupplier',
        method: 'post',
        data: qs.stringify(data)
    });
};

export const addSupplier = (data) => {
    return request({
        url: '/supplier/addSupplier',
        method: 'post',
        data: qs.stringify(data)
    });
};

export const delSupplier = (data) => {
    return request({
        url: '/supplier/deleteSupplier',
        method: 'get',
        params: {
            ids: data
        },
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    })
};

export const querySupplierAll = () => {
    return request({
        url: '/supplier/querySupplierAll',
        method: 'get'
    });
};
import request from '../utils/request';
import qs from 'qs'



export const queryMaterialStock = (query) => {
    return request({
        url: '/materialStock/queryMaterial',
        method: 'get',
        params: query
    });
};

export const updateMaterial = (data) => {
    return request({
        url: '/materialStock/editMaterial',
        method: 'post',
        data: qs.stringify(data)
    });
};


export const addMaterial = (data) => {
    return request({
        url: '/materialStock/addMaterial',
        method: 'post',
        data: qs.stringify(data)
    });
};

export const delMaterial = (data) => {
    return request({
        url: '/materialStock/delMaterial',
        method: 'get',
        params: {
            ids: data
        },
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    })
};

export const queryMaterialInCategory = (data) => {
    return request({
        url: '/materialStock/queryMaterialInCategory',
        method: 'get',
        params: data
    });
};

export const searchMaterialAndCategory = () => {
    return request({
        url: '/materialStock/searchMaterialAndCategory',
        method: 'get',
    });
};

import request from '../utils/request';
import qs from 'qs'

export const queryByLimitCategory = (query) => {
    return request({
        url: '/materialCategory/queryMaterialCategoryWithLimit',
        method: 'get',
        params: query
    });
};

export const updateCategory = (data) => {
    return request({
        url: '/materialCategory/updateMaterialCategory',
        method: 'post',
        data: qs.stringify(data)
    });
};

export const addCategory = (data) => {
    return request({
        url: '/materialCategory/addMaterialCategory',
        method: 'post',
        data: qs.stringify(data)
    });
};

export const delCategory = (data) => {
    return request({
        url: '/materialCategory/deleteMaterialCategory',
        method: 'get',
        params: {
            ids: data
        },
        paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
        }
    })
};

export const queryCategroyAll = () => {
    return request({
        url: '/materialCategory/queryCategroyAll',
        method: 'get'
    });
};
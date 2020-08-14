import request from '../utils/request';
import qs from 'qs'

export const checkPermission = (query) => {
    return request({
        url: '/auth/checkPermission',
        method: 'get',
        params: query
    });
};


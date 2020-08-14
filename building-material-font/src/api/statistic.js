import request from '../utils/request';
import qs from 'qs'

export const statisticPurchase = () => {
    return request({
        url: '/statistic/statisticTotalPurchase',
        method: 'get',
    });
};

export const statisticBySelect = (data) => {
    return request({
        url: '/statistic/statisticBySelect',
        method: 'get',
        params: data
    });
};
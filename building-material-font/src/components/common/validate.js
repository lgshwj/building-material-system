
// 手机号验证
export function isvalidPhone(str) {
    const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
    return reg.test(str)
};

export function isvalidPurchaseQuantity(str) {
    return str > 0 ;
};
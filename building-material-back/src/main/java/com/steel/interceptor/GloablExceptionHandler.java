package com.steel.interceptor;

import com.steel.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ProjectName: building-material
 * @Package: com.steel.interceptor
 * @ClassName: GloablExceptionHandler
 * @Author: Hero
 * @Description: 全局异常类
 * @Date: 2020/2/21 22:11
 * @Version: 1.0
 */
@ControllerAdvice
public class GloablExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        e.printStackTrace();
        return new Result("3001",e.getMessage()!=null?e.getMessage():"服务器发生错误！");
    }
}

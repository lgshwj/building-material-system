package com.steel.interceptor;

import com.steel.common.Result;
import com.steel.common.TokenUtil;
import com.steel.common.VerifyToken;
import com.steel.entity.User;
import com.steel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @ProjectName: building-material
 * @Package: com.steel.interceptor
 * @ClassName: AuthInterceptor
 * @Author: Hero
 * @Description: 拦截器
 * @Date: 2020/3/13 13:02
 * @Version: 1.0
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 方法不是映射到controller上直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        // controller方法上有VerifyToken注解
        if (method.isAnnotationPresent(VerifyToken.class)) {
            //获取请求头里的token值
            String token = request.getHeader("token");

            if (token == null) {
                PrintWriter printWriter1 = response.getWriter();
                response.reset();
                printWriter1.print(new Result("3001", "登录已失效，请重新登录"));
                printWriter1.flush();
                printWriter1.close();
                return false;
            }
            if (!TokenUtil.verify(token)) {
                PrintWriter printWriter2 = response.getWriter();
                response.reset();
                printWriter2.print(new Result("3001", "登录已失效，请重新登录"));
                printWriter2.flush();
                printWriter2.close();
                return false;
            }
            String userId = TokenUtil.getInfoFromToken(token, "id");
            String password = TokenUtil.getInfoFromToken(token, "password");
            if (userId == null || password == null) {
                PrintWriter printWriter3 = response.getWriter();
                response.reset();
                printWriter3.print(new Result("3001", "登录已失效，请重新登录"));
                printWriter3.flush();
                printWriter3.close();
                return false;
            }
            User user = userService.queryById(Integer.parseInt(userId));
            if (user == null) {
                PrintWriter printWriter4 = response.getWriter();
                response.reset();
                printWriter4.print(new Result("3001", "登录已失效，请重新登录"));
                printWriter4.flush();
                printWriter4.close();
                return false;
            }
            if (!user.getPassword().equals(password)) {
                PrintWriter printWriter5 = response.getWriter();
                response.reset();
                printWriter5.print(new Result("3001", "登录已失效，请重新登录"));
                printWriter5.flush();
                printWriter5.close();
                return false;
            }
            return true;
        }
        return true;
    }
}

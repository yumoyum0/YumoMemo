package top.yumoyumo.service;

import top.yumoyumo.entity.Result;
import top.yumoyumo.entity.User;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 20:38
 **/
public interface UserService {
    /**
     * 用户登录接口
     * @param user 用户信息
     * @return 返回登录结果
     */
    Result login(User user, HttpServletResponse response);

    /**
     * 用户注册接口
     * @param user 用户信息
     * @return 返回注册结果
     */
    Result regist(User user);
}

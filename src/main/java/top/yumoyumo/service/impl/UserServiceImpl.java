package top.yumoyumo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.yumoyumo.entity.Result;
import top.yumoyumo.entity.User;
import top.yumoyumo.mapper.UserMapper;
import top.yumoyumo.service.UserService;
import top.yumoyumo.utils.AesEncryptUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 20:39
 **/
@Service
public class UserServiceImpl implements UserService {
    private final String SECRETKEY="xr9A2r2+KTsU3VLP";
    @Resource
    private UserMapper userMapper;

    @Override
    public Result login(User user, HttpServletResponse response) {
        Result result = new Result();
        try {
            user.setPassword(AesEncryptUtils.encrypt(user.getPassword(),SECRETKEY));
            final User userDB = userMapper.selectOne(new QueryWrapper<User>()
                    .eq("username",user.getUsername()).eq("password",user.getPassword()));
            if(userDB==null){
                result=Result.failure("用户名或密码错误");
            }else {
                Cookie cookie1 = new Cookie("username",user.getUsername());
                cookie1.setPath("/");
                cookie1.setMaxAge(30 * 24 * 60 * 60);
                response.addCookie(cookie1);

                Cookie cookie2 = new Cookie("userId",userDB.getUserId().toString());
                cookie2.setPath("/");
                cookie2.setMaxAge(30 * 24 * 60 * 60);
                response.addCookie(cookie2);
                result= Result.success(user.getPassword());
            }
        }catch (Exception e){
            result=Result.failure(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Result regist(User user) {
        Result result = new Result();
        //限制密码
        if (!user.getPassword().matches("^[a-zA-Z0-9!@#$%^&*()_=+]{6,32}")) {
            return Result.failure("输入密码规则错误");
        }
        System.out.println(user.getUsername() + "-" + user.getPassword());
        try {
            //判断数据库中是否存在用户
            boolean exists = userMapper.exists(new QueryWrapper<User>().eq("username", user.getUsername()));
            if (exists){
                result=Result.failure("用户已存在");
            }else {
                //注册用户
                user.setPassword(AesEncryptUtils.encrypt(user.getPassword(),SECRETKEY));
                user.setMethod("SSP-MMC");
                userMapper.insert(user);
                result=Result.success(null);
            }
        }catch (Exception e){
            result=Result.failure(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}

package top.yumoyumo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.yumoyumo.entity.Result;
import top.yumoyumo.entity.User;
import top.yumoyumo.mapper.UserMapper;
import top.yumoyumo.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 20:37
 **/
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @GetMapping("/getLogin")
    public String getLogin() {
        return "login";
    }



    /**
     * 登录
     * @return Result
     */
    @PostMapping(value = "/login")
    public String login(@RequestParam("name") String name
            , @RequestParam("password") String password
            , Model model, HttpServletResponse response) {

        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        Result result = userService.login(user, response);
        if (result.getSuccess()) {
            model.addAttribute("code", "200");

            return "home";
        } else {
            model.addAttribute("code", "500");
            model.addAttribute("errMsg", "登陆失败！"+result.getErrMsg());
            return "login";
        }
    }


    @GetMapping("/getRegister")
    public String getRegister() {
        return "register";
    }

    /**
     * 注册
     * @return Result
     */
    @PostMapping(value = "/register")
    public String register(
            String name
            ,String password
            , Model model) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        Result result = userService.regist(user);
        if (result.getSuccess()) {
            model.addAttribute("code", "200");
            model.addAttribute("errMsg", "注册成功！");
            return "login";
        } else {
            model.addAttribute("code", "500");
            model.addAttribute("errMsg", "注册失败！"+result.getErrMsg());
            return "register";
        }
    }

    @GetMapping("/getMethod")
    @ResponseBody
    public Result getMethod(@RequestParam Long userId) {
        String method = userMapper.selectById(userId).getMethod();
        return Result.success(method);
    }

    @GetMapping("/update")
    @ResponseBody
    public Result update(@RequestParam Long userId, @RequestParam String method) {
        User user = userMapper.selectById(userId);
        user.setMethod(method);
        int i = userMapper.updateById(user);
        return Result.success(i);
    }
}

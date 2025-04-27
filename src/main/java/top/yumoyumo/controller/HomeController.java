package top.yumoyumo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 2025/4/10 16:47
 **/
@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/cards")
    public String showCards() {
        return "cards";
    }

    @GetMapping("/create-card")
    public String createCards() {
        return "create-card";
    }
    @GetMapping("/card-detail")
    public String cardDetail() {
        return "card-detail";
    }
    @GetMapping("/edit-card")
    public String editCard() {
        return "edit-card";
    }
    @GetMapping("/review")
    public String review() {
        return "review";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }
}

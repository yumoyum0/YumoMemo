package top.yumoyumo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.yumoyumo.entity.Card;
import top.yumoyumo.entity.Result;
import top.yumoyumo.service.CardService;
import top.yumoyumo.vo.CardVO;
import top.yumoyumo.vo.ReviewFeedbackVO;
import top.yumoyumo.vo.ReviewScheduleVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 14:24
 **/
@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Resource
    private CardService cardService;

    @GetMapping("/ping")
    public String ping() {
        System.out.println("pong!");
        return "pong";
    }


    /**
     * 创建卡片
     * @param cardVO 卡片信息（前端传入的对象）
     * @return 创建成功的卡片对象
     */
    @PostMapping("/create")
    public Result createCard(@RequestBody CardVO cardVO) {
        Card createdCard = cardService.createCard(cardVO);
        return Result.success(createdCard);
    }

    /**
     * 获取用户的所有卡片
     * @param userId 用户ID
     * @return 卡片列表
     */
    @GetMapping("getAllCards")
    public Result getAllCards(@RequestParam Long userId) {
        List<Card> cards = cardService.getAllCards(userId);
        return Result.success(cards);
    }

    /**
     * 获取某张卡片详情
     * @param cardId 卡片ID
     * @return 卡片详情
     */
    @GetMapping("/detail")
    public Result getCardDetails(@RequestParam Long cardId) {
        Card card = cardService.getCardDetails(cardId);
        return Result.success(card);
    }

    /**
     * 更新卡片信息
     * @param cardId 卡片ID
     * @param cardVO 更新的卡片信息
     * @return 更新后的卡片对象
     */
    @PostMapping("/update")
    public Result updateCard(@RequestParam Long cardId, @RequestBody CardVO cardVO) {
        Card updatedCard = cardService.updateCard(cardId, cardVO);
        return Result.success(updatedCard);
    }

    /**
     * 删除卡片
     * @param cardId 卡片ID
     * @return 删除成功消息
     */
    @GetMapping("/delete")
    public Result deleteCard(@RequestParam Long cardId) {
        cardService.deleteCard(cardId);
        return Result.success("Card deleted successfully.");
    }

    /**
     * 生成复习计划
     * @param userId 用户ID
     * @return 复习计划列表
     */
    @GetMapping("/review/schedule")
    public Result generateReviewSchedule(@RequestParam Long userId) {
        List<Card> schedule = cardService.generateReviewSchedule(userId);
        return Result.success(schedule);
    }

    /**
     * 提交复习反馈
     * @param cardId 卡片ID
     * @param feedbackVO 复习反馈信息
     * @return 反馈提交成功消息
     */
    @PostMapping("/review/feedback")
    public Result submitReviewFeedback(@RequestBody ReviewFeedbackVO feedbackVO) {
        cardService.submitReviewFeedback(feedbackVO);
        return Result.success("Review feedback submitted successfully.");
    }
}

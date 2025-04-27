package top.yumoyumo.service;

import top.yumoyumo.entity.Card;
import top.yumoyumo.vo.CardVO;
import top.yumoyumo.vo.ReviewFeedbackVO;
import top.yumoyumo.vo.ReviewScheduleVO;

import java.util.List;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 14:25
 **/
public interface CardService {

    Card createCard(CardVO cardVO);

    List<Card> getAllCards(Long userId);

    Card getCardDetails(Long cardId);

    Card updateCard(Long cardId, CardVO cardVO);

    void deleteCard(Long cardId);

    List<Card> generateReviewSchedule(Long userId);

    void submitReviewFeedback(ReviewFeedbackVO feedbackVO);
}
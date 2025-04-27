package top.yumoyumo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yumoyumo.entity.Card;
import top.yumoyumo.mapper.CardMapper;
import top.yumoyumo.scheduler.SSPMMCScheduler;
import top.yumoyumo.scheduler.SchedulerContext;
import top.yumoyumo.scheduler.SchedulerStrategy;
import top.yumoyumo.service.CardService;
import top.yumoyumo.utils.JsonUtil;
import top.yumoyumo.vo.CardVO;
import top.yumoyumo.vo.ReviewFeedbackVO;
import top.yumoyumo.vo.ReviewScheduleVO;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 14:26
 **/
@Service
@Slf4j
public class CardServiceImpl implements CardService {
    @Autowired
    private SchedulerContext schedulerContext;
    private SchedulerStrategy schedulerStrategy;

    @Autowired
    private CardMapper cardMapper;

    private static final int DIFFICULTY_LIMIT = 18; // 最大难度
    private static final int TARGET_HALFLIFE = 360; // 目标半衰期
    private static final int DIFFICULTY_OFFSET = 2; // 回忆失败后难度增加偏移量
    /**
     * 创建卡片
     */
    @Override
    public Card createCard(final CardVO cardVO) {
        Card card = new Card();
        card.setUserId(cardVO.getUserId());
        card.setFront(cardVO.getFront());
        card.setBack(cardVO.getBack());
        card.setDifficulty(1);
        card.setHalflife(-1 / (Math.log(Math.max(0.925f - 0.05f * 1, 0.025f)) / Math.log(2))); // 初始半衰期
        card.setPRecall(0.0); // 初始回忆概率
        card.setDeltaT(1); // 初始时间间隔
        card.setReps(0); // 初始复习次数
        card.setLapses(0); // 初始遗忘次数
        card.setLastDate(LocalDateTime.now().minusDays(1)); // 创建时设置最后复习时间
        card.setDueDate(LocalDateTime.now().minusDays(1)); // 初始复习时间为前一天，新卡片立即学习
        card.setRHistory("0"); // 初始回忆历史为空
        card.setTHistory("0"); // 初始时间间隔历史为空
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());

        cardMapper.insert(card); // 使用 MyBatis-Plus 插入数据
        log.info("Created card: {}", card);
        return card;
    }

    /**
     * 获取用户的所有卡片
     */
    @Override
    public List<Card> getAllCards(final Long userId) {
        return cardMapper.selectByUserId(userId);
    }

    /**
     * 获取某张卡片详情
     */
    @Override
    public Card getCardDetails(final Long cardId) {
        return cardMapper.selectById(cardId);
    }

    /**
     * 更新卡片信息
     */
    @Override
    public Card updateCard(final Long cardId, final CardVO cardVO) {
        Card card = cardMapper.selectById(cardId);
        if (card == null) {
            log.error("Card not found for ID: {}", cardId);
            throw new IllegalArgumentException("Card not found.");
        }
        card.setFront(cardVO.getFront());
        card.setBack(cardVO.getBack());
        card.setDifficulty(1);
        card.setUpdatedAt(LocalDateTime.now());

        cardMapper.updateById(card);
        log.info("Updated card: {}", card);
        return card;
    }

    /**
     * 删除卡片
     */
    @Override
    public void deleteCard(final Long cardId) {
        int rows = cardMapper.deleteById(cardId);
        if (rows == 0) {
            log.error("Failed to delete card with ID: {}", cardId);
            throw new IllegalArgumentException("Card not found.");
        }
        log.info("Deleted card with ID: {}", cardId);
    }

    /**
     * 生成复习计划
     */
    @Override
    public List<Card> generateReviewSchedule(final Long userId) {
        List<Card> dueCards = cardMapper.selectDueCards(userId, LocalDateTime.now());
        return dueCards;
    }

    @Override
    public void submitReviewFeedback(ReviewFeedbackVO feedbackVO) {
        schedulerStrategy = schedulerContext.getStrategy(feedbackVO.getMethod());

        long cardId = feedbackVO.getCardId();
        Card card = cardMapper.selectById(cardId);

        if (card == null) {
            throw new IllegalArgumentException("Card not found for ID: " + cardId);
        }
        int recallResult = feedbackVO.getRecallResult(); // 用户的回忆结果（成功/失败）
        int reps = card.getReps(); // 当前复习次数
        int lapses = card.getLapses(); // 当前遗忘次数
        double halflife = card.getHalflife(); // 当前半衰期
        int difficulty = card.getDifficulty(); // 当前难度
        int delta_t = (int) Duration.between(card.getLastDate(),LocalDateTime.now()).toDays(); // 距上次复习的间隔天数

        double pRecall = Math.pow(2, (-delta_t / halflife));
        // 更新时间间隔、回忆记录和时间间隔记录
        card.setDeltaT(delta_t);
        card.setRHistory(card.getRHistory() + "," + recallResult);
        card.setTHistory(card.getTHistory() + "," + delta_t);
        card.setPRecall(pRecall);

        long nextInterval = 0;
        if (recallResult == 1) {
            // 回忆成功
            log.info("Recall successful for card ID: {}", cardId);
            reps++;
            // 使用策略计算回忆成功后的半衰期
            double nextHalflife = calculateRecallHalflife(difficulty, halflife, pRecall);
            card.setHalflife(nextHalflife);

            if (nextHalflife >= TARGET_HALFLIFE) {
                // 如果达到目标半衰期，标记为已掌握
                card.setDueDate(LocalDateTime.now().plusYears(100)); // 不再需要复习
            } else {
                // 根据策略计算下次复习的时间间隔
                nextInterval = Math.round(schedulerStrategy.getNextInterval(difficulty, nextHalflife, reps, lapses));
                card.setDueDate(LocalDateTime.now().plusDays(nextInterval));
                // 减小卡片的难度，但不小于最小难度
                difficulty = Math.max(difficulty - DIFFICULTY_OFFSET, 1);
                card.setDifficulty(difficulty);
            }
        } else {
            // 回忆失败
            log.info("Recall failed for card ID: {}", cardId);
            lapses++;
            reps = 0; // 复习次数重置为 0
            // 使用策略计算回忆失败后的半衰期
            double nextHalflife = calculateForgetHalflife(difficulty, halflife, card.getPRecall());
            card.setHalflife(nextHalflife);

            // 增加卡片的难度，但不超过最大难度
            difficulty = Math.min(difficulty + DIFFICULTY_OFFSET, DIFFICULTY_LIMIT);
            card.setDifficulty(difficulty);

            // 根据策略计算下次复习的时间间隔
            nextInterval = Math.round(schedulerStrategy.getNextInterval(difficulty, nextHalflife, reps, lapses));
            card.setDueDate(LocalDateTime.now().plusDays(nextInterval));
        }

        // 更新复习次数和遗忘次数
        card.setReps(reps);
        card.setLapses(lapses);

        card.setPRecall(pRecall);
        // 更新最后复习时间
        card.setLastDate(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        // 保存更新后的卡片
        cardMapper.updateById(card);

        log.info("Updated card after review feedback: {}", card);
    }

    /**
     * 计算回忆成功后的半衰期
     *
     * @param difficulty 卡片的难度
     * @param halflife 当前的半衰期
     * @param pRecall 当前的回忆概率
     * @return 新的半衰期
     */
    private double calculateRecallHalflife(int difficulty, double halflife, double pRecall) {
        // 根据策略计算回忆成功后的半衰期
        return halflife * (1 + Math.exp(3.81) * Math.pow(difficulty, -0.534) * Math.pow(halflife, -0.127) * Math.pow(1 - pRecall, 0.97));
    }

    /**
     * 计算回忆失败后的半衰期
     *
     * @param difficulty 卡片的难度
     * @param halflife 当前的半衰期
     * @param pRecall 当前的回忆概率
     * @return 新的半衰期
     */
    private double calculateForgetHalflife(int difficulty, double halflife, double pRecall) {
        // 根据策略计算回忆失败后的半衰期
        return (double) (Math.exp(-0.041) * Math.pow(difficulty, -0.041) * Math.pow(halflife, 0.377) * Math.pow(1 - pRecall, -0.227));
    }
}

package top.yumoyumo.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 15:08
 **/
@Component("SSP-MMC")
@Slf4j
public class SSPMMCScheduler implements SchedulerStrategy {

    private final List<double[][]> policy = new ArrayList<>();
    private static final double BASE = 1.05;
    private static final int MIN_INDEX = -30;
    private static final int TARGET_HALFLIFE = 360;

    /**
     * 获取下次复习的时间间隔（SSP-MMC 策略）
     *
     * @param difficulty 卡片难度
     * @param halflife 当前半衰期
     * @param reps 复习次数
     * @param lapses 遗忘次数
     * @return 下次复习的时间间隔（天数）
     */
    @Override
    public double getNextInterval(int difficulty, double halflife, int reps, int lapses) {
        halflife = Math.min(halflife, TARGET_HALFLIFE);
        int index = (int) (Math.log(halflife) / Math.log(BASE) - MIN_INDEX);

        // 检查 index 是否越界
        if (difficulty < 1 || difficulty > policy.size() || index < 1 || index > policy.get(difficulty - 1).length) {
            log.error("Invalid parameters for SSP-MMC strategy: difficulty={}, halflife={}, index={}", difficulty, halflife, index);
            return 1; // 返回默认间隔
        }

        // 获取策略中的间隔值
        return policy.get(difficulty - 1)[index - 1][1];
    }

    /**
     * 初始化：加载策略文件
     */
    @PostConstruct
    public void loadPolicy() {
        for (int d = 1; d <= 18; d++) {
            String fileName = String.format("algo/ivl-%d.csv", d);
            try {
                // 使用 ClassPathResource 加载文件
                ClassPathResource resource = new ClassPathResource(fileName);

                // 使用 BufferedReader 读取文件内容
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    List<double[]> values = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // 解析 CSV 行，将其转换为 double 数组
                        double[] row = Arrays.stream(line.split(","))
                                .mapToDouble(Double::parseDouble)
                                .toArray();
                        values.add(row);
                    }
                    policy.add(values.toArray(new double[0][]));
                    log.info("Successfully loaded policy file: {}", fileName);
                }
            } catch (Exception e) {
                log.error("Error loading policy file: {}", fileName, e);
            }
        }
    }
}
package top.yumoyumo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan("top.yumoyumo.mapper")
public class MybatisPlusConfig {
}

package net.andresbustamante.yafoot.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "net.andresbustamante.yafoot.core.dao")
public class MyBatisConfig {
}

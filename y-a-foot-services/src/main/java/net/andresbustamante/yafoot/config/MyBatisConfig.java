package net.andresbustamante.yafoot.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "net.andresbustamante.yafoot.dao")
public class MyBatisConfig {
}

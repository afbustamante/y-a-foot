package net.andresbustamante.yafoot.core.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import jakarta.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "net.andresbustamante.yafoot.core.dao", sqlSessionFactoryRef = "sqlSessionFactory")
public class MyBatisTestConfig {

    @Resource
    private DataSource dataSource;

    /**
     * Test transaction manager.
     *
     * @return Transaction manager bean
     */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setRollbackOnCommitFailure(true);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    /**
     * Test SQL session factory.
     *
     * @return Factory bean
     */
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        return sqlSessionFactoryBean;
    }
}

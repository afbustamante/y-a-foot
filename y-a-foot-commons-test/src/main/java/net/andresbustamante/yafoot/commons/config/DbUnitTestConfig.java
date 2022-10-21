package net.andresbustamante.yafoot.commons.config;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DbUnitTestConfig {

    @Bean
    public IDataTypeFactory dbUnitDataTypeFactory() {
        return new PostgresqlDataTypeFactory();
    }

    @Bean
    public DatabaseConfigBean dbUnitDatabaseConfig() {
        DatabaseConfigBean databaseConfigBean = new DatabaseConfigBean();
        databaseConfigBean.setDatatypeFactory(dbUnitDataTypeFactory());
        return databaseConfigBean;
    }

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(DataSource dataSource) {
        DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnectionFactory =
                new DatabaseDataSourceConnectionFactoryBean();
        dbUnitDatabaseConnectionFactory.setDatabaseConfig(dbUnitDatabaseConfig());
        dbUnitDatabaseConnectionFactory.setDataSource(dataSource);
        dbUnitDatabaseConnectionFactory.setTransactionAware(true);
        return dbUnitDatabaseConnectionFactory;
    }
}

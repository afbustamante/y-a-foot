package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import net.andresbustamante.yafoot.config.JdbcTestConfig;
import net.andresbustamante.yafoot.config.MyBatisTestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcTestConfig.class, MyBatisTestConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
abstract class AbstractDAOTest {
}

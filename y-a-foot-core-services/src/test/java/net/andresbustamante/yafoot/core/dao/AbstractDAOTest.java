package net.andresbustamante.yafoot.core.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import net.andresbustamante.yafoot.commons.config.DbUnitTestConfig;
import net.andresbustamante.yafoot.commons.config.JdbcTestConfig;
import net.andresbustamante.yafoot.core.config.MyBatisTestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcTestConfig.class, MyBatisTestConfig.class, DbUnitTestConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
abstract class AbstractDAOTest {
}

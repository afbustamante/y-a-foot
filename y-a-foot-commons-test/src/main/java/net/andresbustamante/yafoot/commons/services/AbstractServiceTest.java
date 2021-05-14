package net.andresbustamante.yafoot.commons.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.PropertySource;

@ExtendWith(MockitoExtension.class)
@PropertySource("classpath:application.properties")
public abstract class AbstractServiceTest {
}

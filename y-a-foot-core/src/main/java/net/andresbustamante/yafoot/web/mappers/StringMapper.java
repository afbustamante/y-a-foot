package net.andresbustamante.yafoot.web.mappers;

import org.mapstruct.Mapper;

import java.nio.charset.StandardCharsets;

/**
 * Mapper utile pour le mapping des éléments encodés en base64
 */
@Mapper(componentModel = "spring")
public interface StringMapper {

    default String map(byte[] value) {
        return (value != null) ? new String(value, StandardCharsets.UTF_8) : null;
    }

    default byte[] map(String value) {
        return (value != null) ? value.getBytes(StandardCharsets.UTF_8) : null;
    }
}

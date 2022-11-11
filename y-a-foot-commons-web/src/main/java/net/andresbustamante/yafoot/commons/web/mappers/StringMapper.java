package net.andresbustamante.yafoot.commons.web.mappers;

import org.mapstruct.Mapper;

import java.nio.charset.StandardCharsets;

/**
 * Mapper used to transform values between plain strings and base64 encoded strings.
 */
@Mapper(componentModel = "spring")
public interface StringMapper {

    /**
     * Maps a byte array to a string using UTF-8 encoding.
     *
     * @param value Byte array to map
     * @return Resulting string from byte array
     */
    default String map(byte[] value) {
        return (value != null) ? new String(value, StandardCharsets.UTF_8) : null;
    }

    /**
     * Maps a string to a byte array using UTF-8 encoding.
     *
     * @param value String to map
     * @return Resulting byte array
     */
    default byte[] map(String value) {
        return (value != null) ? value.getBytes(StandardCharsets.UTF_8) : null;
    }
}

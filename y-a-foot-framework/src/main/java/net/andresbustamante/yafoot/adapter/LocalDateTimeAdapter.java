package net.andresbustamante.yafoot.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

/**
 * Adapter XML pour LocalDateTime
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String str) throws Exception {
        return LocalDateTime.parse(str);
    }

    @Override
    public String marshal(LocalDateTime localDateTime) throws Exception {
        if (localDateTime != null) {
            return localDateTime.toString();
        } else {
            return null;
        }
    }
}

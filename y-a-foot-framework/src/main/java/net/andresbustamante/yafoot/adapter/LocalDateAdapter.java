package net.andresbustamante.yafoot.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * Adapter XML pour LocalDate
 */
public class LocalDateAdapter extends XmlAdapter<String,LocalDate> {

    @Override
    public LocalDate unmarshal(String str) throws Exception {
        return LocalDate.parse(str);
    }

    @Override
    public String marshal(LocalDate localDate) throws Exception {
        if (localDate != null) {
            return localDate.toString();
        } else {
            return null;
        }
    }
}
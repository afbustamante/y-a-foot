package net.andresbustamante.yafoot.commons.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;

@Component
public class TemplateUtils {

    private final Configuration freemarkerConfiguration;

    public TemplateUtils(Configuration freemarkerConfiguration) {
        this.freemarkerConfiguration = freemarkerConfiguration;
    }

    /**
     * Gets some content text from a Freemarker template using a content model.
     *
     * @param contentTemplate Template to use for processing text
     * @param contentModel Model containing the variables to replace in text
     * @return Resulting content
     * @throws IOException When the template cannot be loaded from the FS
     * @throws TemplateException When the template has errors
     */
    public String getContent(String contentTemplate, Object contentModel) throws IOException, TemplateException {
        Template t = freemarkerConfiguration.getTemplate(contentTemplate);
        return FreeMarkerTemplateUtils.processTemplateIntoString(t, contentModel);
    }
}

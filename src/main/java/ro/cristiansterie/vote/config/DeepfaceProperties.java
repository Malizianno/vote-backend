package ro.cristiansterie.vote.config;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "deepface")
public class DeepfaceProperties implements InitializingBean {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private String url;

    @Override
    public void afterPropertiesSet() {
        if (null == url || url.isEmpty()) {
            throw new IllegalArgumentException("Deepface URL must be provided in application properties.");
        }

        log.info("DeepfaceProperties loaded: url = {}", url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

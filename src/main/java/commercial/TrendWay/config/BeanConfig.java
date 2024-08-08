package commercial.TrendWay.config;

import commercial.TrendWay.logging.Slf4jMDCFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public FilterRegistrationBean<Slf4jMDCFilter> servletRegistrationBean() {
        FilterRegistrationBean<Slf4jMDCFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new Slf4jMDCFilter());
        return filterRegistrationBean;
    }
}

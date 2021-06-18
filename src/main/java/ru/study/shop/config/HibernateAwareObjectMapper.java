package ru.study.shop.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateAwareObjectMapper {
    @Bean
    public Hibernate5Module hibernateAwareModuleRegistration() {
        Hibernate5Module module = new Hibernate5Module();
        module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
        return module;
    }
}

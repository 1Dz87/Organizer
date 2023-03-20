package by.itstep.organizaer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public Session session(Authenticator authenticator, Properties mail) {
        return Session.getInstance(mail);
    }

    @Bean
    public Authenticator authenticator(Properties mail) {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail.getProperty("username"), mail.getProperty("password"));
            }
        };
    }

    @Bean
    @ConfigurationProperties("project.mail")
    public Properties mail() {
        return new Properties();
    }
/*
    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setViewNames(new String[] {".html", ".xhtml"});
        return viewResolver;
    }*/

}

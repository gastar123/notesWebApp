package com.xe72.notesWebApp.config;

import com.xe72.notesWebApp.entities.Tag;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.Printer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addPrinter(new Printer<List<Tag>>() {
            @Override
            public String print(List<Tag> object, Locale locale) {
                return object.stream().map(Object::toString).collect(Collectors.joining(", "));
            }
        });
    }
}

package com.xe72.notesWebApp;

import com.xe72.notesWebApp.entities.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.Printer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@SpringBootApplication
public class NotesWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesWebAppApplication.class, args);
	}

}

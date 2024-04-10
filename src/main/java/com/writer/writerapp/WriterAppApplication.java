package com.writer.writerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
public class WriterAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WriterAppApplication.class, args);
	}

}

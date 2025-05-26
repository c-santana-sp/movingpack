package com.movingpack.movingpack;

import com.movingpack.movingpack.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class TestMovingpackApplication {

	public static void main(String[] args) {
		SpringApplication.from(MovingPackApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

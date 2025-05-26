package com.movingpack.movingpack.config;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

	public static final PostgreSQLContainer<?> postgresContainer =
			new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
					.withDatabaseName("test_db")
					.withUsername("test")
					.withPassword("test");

	public static final WireMockServer wireMockServer =
			new WireMockServer(WireMockConfiguration.options()
					.dynamicPort()
					.usingFilesUnderClasspath("wiremock"));

	static {
		postgresContainer.start();
		wireMockServer.start();
	}

	/**
	 * Allows Spring Boot tests to inherit container properties.
	 */
	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(ConfigurableApplicationContext context) {
			TestPropertyValues.of(
					"spring.datasource.url=" + postgresContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgresContainer.getUsername(),
					"spring.datasource.password=" + postgresContainer.getPassword(),
					"spring.flyway.enabled=true",
					"spring.flyway.locations=classpath:db/migration",

					"external.api.base-url=http://localhost:" + wireMockServer.port()
			).applyTo(context.getEnvironment());
		}
	}

	/**
	 * JUnit 5 extension hook to start containers before tests if needed.
	 */
	public static class TestcontainersExtension implements BeforeAllCallback {
		@Override
		public void beforeAll(ExtensionContext context) {
			// Ensures containers start if not already running
			if (!postgresContainer.isRunning()) postgresContainer.start();
			if (!wireMockServer.isRunning()) wireMockServer.start();
		}
	}
}
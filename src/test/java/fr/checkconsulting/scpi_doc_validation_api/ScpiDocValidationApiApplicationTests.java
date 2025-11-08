package fr.checkconsulting.scpi_doc_validation_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.kafka.bootstrap-servers=dummy:9092")
class ScpiDocValidationApiApplicationTests {

	@Test
	void contextLoads() {
	}

}

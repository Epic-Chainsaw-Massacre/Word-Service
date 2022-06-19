package EpicChainsawMassacre.reversehangmanonlinebackend;

import EpicChainsawMassacre.reversehangmanonlinebackend.resources.WordResource;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class ReverseHangmanOnlineBackendApplicationTests {

	@ParameterizedTest
	@CsvSource({"help,true", "ksgujfudg,false"})
	//@ValueSource(strings = {"help", "asgfkjas"}, booleans = {true, false})
	void checkWordExistance(String word, Boolean exists) {
		WordResource wordResource = new WordResource();
		assertTrue(wordResource.wordExists(word) == exists);
	}

	@Test
	void apiBucket(){
		// so every minute you can use 5 tokens
		Refill refill = Refill.intervally(5, Duration.ofMinutes(1));
		// there are 5 tokens in total?
		Bandwidth limit = Bandwidth.classic(5, refill);
		// Build the bucket
		Bucket bucket = Bucket4j.builder()
				.addLimit(limit)
				.build();

		// consume all 5 tokens
		for (int i = 1; i <= 5; i++) {
			assertTrue(bucket.tryConsume(1));
		}
		// should be no tokens left
		assertFalse(bucket.tryConsume(1));
	}
}

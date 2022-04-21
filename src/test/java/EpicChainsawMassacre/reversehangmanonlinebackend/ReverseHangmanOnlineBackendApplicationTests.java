package EpicChainsawMassacre.reversehangmanonlinebackend;

import EpicChainsawMassacre.reversehangmanonlinebackend.resources.WordResource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class ReverseHangmanOnlineBackendApplicationTests {

	@Test
	void existingWord() {
		WordResource wordResource = new WordResource();
		assertTrue(wordResource.wordExists("help"));
	}

	@Test
	void notExistingWord() {
		WordResource wordResource = new WordResource();
		assertFalse(wordResource.wordExists("marc"));
	}

}

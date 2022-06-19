package EpicChainsawMassacre.reversehangmanonlinebackend.resources;

import EpicChainsawMassacre.reversehangmanonlinebackend.models.Word;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@RestController
@CrossOrigin
@RequestMapping("/word")
public class WordResource {

    // this means
    Refill refillSeconds = Refill.intervally(1, Duration.ofSeconds(5)); // refill back to 1 token every 5 seconds
    Refill refillHours = Refill.intervally(50, Duration.ofHours(1)); // refill back to 50 tokens every 1 hour
    Refill refillDays = Refill.intervally(1000, Duration.ofDays(1)); // refill back to 1000 tokens every 1 day
    Bandwidth limitSeconds = Bandwidth.classic(1, refillSeconds); // every 5 seconds max 1 request
    Bandwidth limitHours = Bandwidth.classic(50, refillHours); // every 1 hour max 50 requests
    Bandwidth limitDays = Bandwidth.classic(1000, refillDays); // every 1 day max 1000 requests
    Bucket bucket = Bucket4j.builder()
            .addLimit(limitSeconds)
            .addLimit(limitHours)
            .addLimit(limitDays)
            .build();

    @RequestMapping("/koeieuier")
    Word koeieuier() {
        return new Word("koeieuier", false);
    }

    @RequestMapping("/test")
    @ResponseBody
    Word test(@RequestParam String word) {
        return new Word(word, false);
    }

    @RequestMapping("/check")
    @ResponseBody
    ResponseEntity<Word> checkIfWordExists(@RequestParam String word) {
        System.out.println("Parameter: " + word);
        if (bucket.tryConsume(1)) {
            if (wordExists(word)) {
                return ResponseEntity.ok(new Word(word, true));
            } else {
                return ResponseEntity.ok(new Word(word, false));
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    public boolean wordExists(String word){
            RestTemplate restTemplate = new RestTemplate();
            String jsonResult = restTemplate.getForObject("https://dictionaryapi.com/api/v3/references/sd2/json/" + word + System.getenv("API_KEY"), String.class);
            JSONArray json = new JSONArray(jsonResult);
            System.out.println("Result: " + jsonResult);
            String wordString;
            try {
                wordString = json.getJSONObject(0).getJSONObject("meta").getString("id");
            }
            catch (Exception e) {
                return false;
            }
            System.out.println("Result2: " + wordString);
            if (wordString.contains(":")) {
                wordString = wordString.substring(0, wordString.length() - 2);
            }
            System.out.println("Result3: " + wordString);
            return wordString.contains(word);
    }

    //1000 per dag
    //https://dictionaryapi.com/api/v3/references/sd2/json/PUT_WORD_HERE?key=4084046c-2911-430f-9666-a5ca6a3714b4

    @RequestMapping("/cool")
    Word doShit() {
        RestTemplate restTemplate = new RestTemplate();
        Word word = restTemplate.getForObject("http://localhost:8080/word/check?word=help", Word.class);
        return word;
    }
}

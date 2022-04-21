package EpicChainsawMassacre.reversehangmanonlinebackend.resources;

import EpicChainsawMassacre.reversehangmanonlinebackend.models.Word;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
@RequestMapping("/word")
public class wordResource {

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
    Word checkIfWordExists(@RequestParam String word) {
        System.out.println("Parameter: " + word);
        if (wordExists(word)) {
            return new Word(word, true);
        }
        else {
            return new Word(word, false);
        }
    }

    boolean wordExists(String word){
            RestTemplate restTemplate = new RestTemplate();
            String jsonResult = restTemplate.getForObject("https://dictionaryapi.com/api/v3/references/sd2/json/" + word + "?key=4084046c-2911-430f-9666-a5ca6a3714b4", String.class);
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

    //https://dictionaryapi.com/api/v3/references/sd2/json/PUT_WORD_HERE?key=4084046c-2911-430f-9666-a5ca6a3714b4

    @RequestMapping("/cool")
    Word doShit() {
        RestTemplate restTemplate = new RestTemplate();
        Word word = restTemplate.getForObject("http://localhost:8080/word/check?word=help", Word.class);
        return word;
    }
}

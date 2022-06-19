package EpicChainsawMassacre.reversehangmanonlinebackend.resources;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/app")
public class PingResource {

    @RequestMapping("/ping")
    String ping() {return new String("pong");}
}

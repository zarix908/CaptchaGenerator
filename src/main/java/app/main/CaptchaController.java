package app.main;

import app.model.CaptchaHandler;
import app.model.CaptchaCharacteristics;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CaptchaController {
    private final CaptchaHandler captchaHandler = new CaptchaHandler();

    @RequestMapping(value = "/generate", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> generateCaptcha() {
        return captchaHandler.getCaptchaGenerationResponse();
    }

    @RequestMapping(value = "/checkanswer", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> checkCaptchaAnswer(@RequestBody CaptchaCharacteristics captchaCharacteristics) {
        return captchaHandler.checkAnswer(captchaCharacteristics);
    }
}
package app.model;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashSet;
import java.util.UUID;

public class CaptchaHandler{
    private final CaptchaSaver captchaSaver = new CaptchaSaver();
    private CaptchaGenerator captchaGenerator;
    private final HashSet<UUID> unavailableCaptcha = new HashSet<>();

    public CaptchaHandler(){
        captchaGenerator = new CaptchaGenerator(captchaSaver);
    }

    public ResponseEntity<JSONObject> checkAnswer(CaptchaCharacteristics captchaCharacteristics) {
        JSONObject response = new JSONObject();

        if (captchaCharacteristics.getAnswer() == null || captchaCharacteristics.getId() == null) {
            response.put("response", "request should contain json with captcha id and answer");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        UUID id;

        try {
            id = UUID.fromString(captchaCharacteristics.getId());
        }catch (IllegalArgumentException ex){
            response.put("response", "id should have following form: " +
                    "HHHHHHHH-HHHH-HHHH-HHHH-HHHHHHHHHHHH, where H - hex digit");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("response", "wrong id");

        if (!captchaSaver.contain(id))
            return new ResponseEntity<>(response, HttpStatus.OK);

        Captcha captcha = captchaSaver.get(id);
        if (!captcha.isAlive() || unavailableCaptcha.contains(id))
            response.put("response", "captcha is unavailable");
        else if (!captcha.getAnswer().equals(captchaCharacteristics.getAnswer()))
            response.put("response", "wrong answer");
        else
            response.put("response", "accepted");

        unavailableCaptcha.add(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> getCaptchaGenerationResponse() {
        Captcha captcha = captchaGenerator.generate();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("RequestId", captcha.getId().toString());
        responseHeaders.set("CaptchaAnswer", captcha.getAnswer());

        byte[] response = Utils.convertImageToByteArray(captcha.getImage());

        return new ResponseEntity<>(response, responseHeaders, HttpStatus.CREATED);
    }


}
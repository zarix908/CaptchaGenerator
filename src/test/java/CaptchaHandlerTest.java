import app.model.CaptchaCharacteristics;
import app.model.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import app.model.CaptchaHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Timed;

import java.util.UUID;

public class CaptchaHandlerTest {
    private CaptchaHandler captchaHandler;
    
    @Before
    public void init(){
        Utils.CAPTCHA_LIFETIME = 3;
    }

    @Test
    public void creatingTest() {
        CaptchaHandler captchaHandler = new CaptchaHandler();
    }

    @Test
    public void getCaptchaGenerationResponseTest() {
        CaptchaHandler captchaHandler = new CaptchaHandler();
        ResponseEntity<?> response = captchaHandler.getCaptchaGenerationResponse();

        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        HttpHeaders headers = response.getHeaders();
        Assert.assertTrue(headers.containsKey("RequestId"));
        UUID.fromString(headers.get("RequestId").get(0));
        Assert.assertTrue(headers.containsKey("CaptchaAnswer"));
        Assert.assertEquals(headers.get("CaptchaAnswer").get(0).getClass(), String.class);

        Assert.assertTrue(response.hasBody());
        Assert.assertEquals(response.getBody().getClass(), byte[].class);
    }

    @Test
    public void checkCaptchaAnswerTest() {
        CaptchaHandler captchaHandler = new CaptchaHandler();
        ResponseEntity<?> response = captchaHandler.getCaptchaGenerationResponse();

        HttpHeaders headers = response.getHeaders();
        String requestId = headers.get("RequestId").get(0);
        String captchaAnswer = headers.get("CaptchaAnswer").get(0);

        CaptchaCharacteristics captchaCharacteristics = new CaptchaCharacteristics(requestId, captchaAnswer);
        String checkResult = captchaHandler.checkAnswer(captchaCharacteristics).getBody().toString();
        Assert.assertEquals(checkResult, "{\"response\":\"accepted\"}");
    }

    private CaptchaCharacteristics generateCaptcha(){
        ResponseEntity<?> response = captchaHandler.getCaptchaGenerationResponse();

        HttpHeaders headers = response.getHeaders();
        String requestId = headers.get("RequestId").get(0);
        String captchaAnswer = headers.get("CaptchaAnswer").get(0);

        return new CaptchaCharacteristics(requestId, captchaAnswer);
    }

    private String checkRequest(CaptchaCharacteristics captchaCharacteristics){
        return captchaHandler.checkAnswer(captchaCharacteristics).getBody().toString();
    }

    @Test
    public void InvalidRequestFormatTest(){
        captchaHandler = new CaptchaHandler();
        generateCaptcha();
        String checkResult = checkRequest(new CaptchaCharacteristics(null,null));
        Assert.assertEquals(checkResult,
                "{\"response\":\"request should contain json with captcha id and answer\"}");
    }

    @Test
    public void InvalidIDFormatTest(){
        captchaHandler = new CaptchaHandler();
        CaptchaCharacteristics captchaCharacteristics = generateCaptcha();
        String checkResult = checkRequest(new CaptchaCharacteristics("?#^4", captchaCharacteristics.getAnswer()));
        Assert.assertEquals(checkResult, "{\"response\":\"id should have following form: " +
                "HHHHHHHH-HHHH-HHHH-HHHH-HHHHHHHHHHHH, where H - hex digit\"}");
    }

    @Test
    public void WrongIDTest(){
        captchaHandler = new CaptchaHandler();
        CaptchaCharacteristics captchaCharacteristics = generateCaptcha();
        String checkResult = checkRequest(new CaptchaCharacteristics("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
                captchaCharacteristics.getAnswer()));
        Assert.assertEquals(checkResult, "{\"response\":\"wrong id\"}");
    }

    @Test
    public void WrongAnswerTest(){
        captchaHandler = new CaptchaHandler();
        CaptchaCharacteristics captchaCharacteristics = generateCaptcha();
        String checkResult = checkRequest(new CaptchaCharacteristics(captchaCharacteristics.getId(),
                "some answer"));
        Assert.assertEquals(checkResult, "{\"response\":\"wrong answer\"}");
    }

    @Test
    public void DoubleEnterCaptchaUnavailableTest(){
        captchaHandler = new CaptchaHandler();
        CaptchaCharacteristics captchaCharacteristics = generateCaptcha();
        String checkResult = null;
        for(int i=0; i<2; i++)
            checkResult = checkRequest(captchaCharacteristics);
        Assert.assertEquals(checkResult, "{\"response\":\"captcha is unavailable\"}");
    }

    @Test
    @Timed(millis = 4000)
    public void LifetimeCaptchaUnavailableTest() throws InterruptedException {
        Utils.CAPTCHA_LIFETIME = 0.05;
        captchaHandler = new CaptchaHandler();
        CaptchaCharacteristics captchaCharacteristics = generateCaptcha();
        Thread.sleep(3500);
        String checkResult = checkRequest(captchaCharacteristics);
        Assert.assertEquals(checkResult, "{\"response\":\"captcha is unavailable\"}");
    }
}

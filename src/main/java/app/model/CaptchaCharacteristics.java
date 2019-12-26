package app.model;

public class CaptchaCharacteristics {
    private String id;
    private String answer;

    public CaptchaCharacteristics(){} //for spring json deserializer

    public CaptchaCharacteristics(String id, String answer){
        this.id = id;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }
}

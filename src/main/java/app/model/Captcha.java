package app.model;

import java.awt.image.BufferedImage;
import java.util.UUID;

class Captcha {
    private UUID id;
    private String answer;
    private BufferedImage image;
    private long generatingTime;

    Captcha(String answer, BufferedImage image, UUID id, long generatingTime){
        this.id = id;
        this.answer = answer;
        this.image = image;
        this.generatingTime = generatingTime;
    }

    String getAnswer(){
        return answer;
    }

    BufferedImage getImage(){
        return image;
    }

    UUID getId(){
        return id;
    }

    Boolean isAlive(){
        return (System.nanoTime() - generatingTime) / Math.pow(10,9) / 60 < Utils.CAPTCHA_LIFETIME;
    }
}

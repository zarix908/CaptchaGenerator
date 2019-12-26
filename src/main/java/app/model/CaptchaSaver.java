package app.model;

import java.util.HashMap;
import java.util.UUID;

class CaptchaSaver {
    private final HashMap<UUID, Captcha> captcha = new HashMap<>();

    void save(UUID id, Captcha captcha){
        this.captcha.put(id, captcha);
    }

    Captcha get(UUID id){
        return captcha.get(id);
    }

    boolean contain(UUID id) {
        return captcha.containsKey(id);
    }
}

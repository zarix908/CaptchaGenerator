package app.model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;

class CaptchaGenerator {
    private static final int width = 300;
    private static final int height = 100;
    private static final int font_size = 48;
    private CaptchaSaver captchaSaver;

    CaptchaGenerator(CaptchaSaver captchaSaver){
        this.captchaSaver = captchaSaver;
    }

    Captcha generate() {
        String captchaAnswer = getCaptchaText();

        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics graphics = image.createGraphics();
        addNoise(image);
        addText(captchaAnswer, graphics);

        Captcha captcha = new Captcha(captchaAnswer, image, UUID.randomUUID(), System.nanoTime());
        captchaSaver.save(captcha.getId(), captcha);
        return captcha;
    }

    private String getCaptchaText() {
        StringBuilder captchaAnswer = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            char random_letter = (char) (random.nextInt(25) + 97);
            char random_digit = (char) (random.nextInt(10) + 48);

            captchaAnswer.append(random.nextBoolean() ? random_digit : random_letter);
        }

        return captchaAnswer.toString();
    }

    private void addNoise(BufferedImage image) {
        Random random = new Random();

        for (int x=0; x < width; x++)
            for (int y=0; y<height; y++)
                image.setRGB(x,y, random.nextInt());
    }

    private void addText(String text, Graphics graphics) {
        Font font = new Font("Georgia", Font.BOLD + Font.ITALIC, font_size);
        FontMetrics metrics = graphics.getFontMetrics(font);
        int font_height = metrics.getHeight();
        int font_width = metrics.stringWidth(text) / text.length() + 1;
        int text_width = metrics.stringWidth(text);
        graphics.setFont(font);

        Graphics2D graphics2D = (Graphics2D) graphics;

        Random random = new Random();

        int x = width / 2 - text_width / 2;
        int y = height / 2 + font_height / 4;

        for (int i = 0; i < text.length(); i++) {
            AffineTransform transform = graphics2D.getTransform();

            graphics.setColor(new Color(random.nextInt()));

            graphics2D.translate(x, y);
            int number = random.nextInt() % 10;
            graphics2D.rotate(Math.toRadians(number + 15*Math.signum(number)));

            graphics.drawString(String.valueOf(text.charAt(i)), 0, 0);
            x += font_width;

            graphics2D.setTransform(transform);

            graphics.drawLine(0,random.nextInt(100), 300, random.nextInt(100));
        }
    }
}
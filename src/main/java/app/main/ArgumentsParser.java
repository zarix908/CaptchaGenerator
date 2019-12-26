package app.main;

import app.model.Utils;

class ArgumentsParser {
    private String[] args;

    ArgumentsParser(String[] args) {
        this.args = args;
    }

    void parse() {
        String helpMessage = "Try \"java -jar CaptchaService-1.0.jar -h or --help\" to more information";

        if (args.length < 1)
            return;

        if (args.length == 1) {
            String argument = args[0];

            if (argument.equals("-h") || argument.equals("--help")) {
                System.out.println("Usage: java -jar CaptchaService.jar <CAPTCHA LIFETIME>\n" +
                        "CAPTCHA LIFETIME - captcha lifetime in minutes " +
                        "(possible fraction value, default value: 3 minutes");

                System.exit(0);
            }

            try {
                Utils.CAPTCHA_LIFETIME = Double.valueOf(argument);
            } catch (NullPointerException | NumberFormatException ex) {
                System.out.println("Invalid arguments format. " + helpMessage);
                System.exit(1);
            }

        } else {
            System.out.println("Too many arguments. " + helpMessage);
            System.exit(1);
        }
    }
}

package pl.pjatk.SOZ_Gastro.ObjectClasses;

import java.util.regex.Pattern;

public class InputStringMysqlValidator {

    private static final String FORBIDDEN_CHARACTERS_REGEX = "[;\"'--<>%&]";
    private static final Pattern FORBIDDEN_CHARACTERS_PATTERN = Pattern.compile(FORBIDDEN_CHARACTERS_REGEX);

    public static boolean containsForbiddenCharacters(String input) {
        if (input == null) {
            return false;
        }
        return FORBIDDEN_CHARACTERS_PATTERN.matcher(input).find();
    }
}

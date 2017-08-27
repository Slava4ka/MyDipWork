package make_enter_key.for_create_password;

import java.util.regex.Pattern;

/**
 * Created by Slavik on 07.12.2016.
 */
public class chek {

        public static final String PATTERN_PASSWORD = "^(?=.*[0-9])(?=.*[for_create_password.a-z])(?=.*[A-Z]).{10,50}$";

        public static final boolean checkRegEx(String word) {
            return Pattern.compile(PATTERN_PASSWORD).matcher(word).matches();
        }
    }


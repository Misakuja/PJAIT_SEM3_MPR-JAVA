package pl.edu.pjatk.MPR_Project.service;

import org.springframework.stereotype.Component;

@Component
public class StringService {
    public String lowercase(String s) {
        char[] charArray = s.toCharArray();

        for (int i = 1; i < charArray.length; i++) {
            charArray[i] = Character.toLowerCase(charArray[i]);
        }

        return new String(charArray);
    }
    public String uppercase(String s) {
        return s.toUpperCase();
    }
}

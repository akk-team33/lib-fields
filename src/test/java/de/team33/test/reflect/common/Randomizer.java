package de.team33.test.reflect.common;

import java.util.Random;

public class Randomizer {

    private final Random random = new Random();
    private final int stringMinLength = 3;
    private final int stringMaxLength = 16;
    private final char[] stringCharSet = "0123456789 abcdefghijklmnopqrstuvwxyzäöüß-ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ_"
            .toCharArray();

    public final String nextString() {
        return nextString(stringMinLength, stringMaxLength, stringCharSet);
    }

    public final String nextString(final int minLength, final int maxLength, final char[] charSet) {
        return nextString(minLength + random.nextInt(maxLength - minLength), charSet);
    }

    public final String nextString(final int length, final char[] charSet) {
        final char[] result = new char[length];
        for (int i = 0; i < length; ++i) {
            result[i] = charSet[random.nextInt(charSet.length)];
        }
        return new String(result);
    }
}

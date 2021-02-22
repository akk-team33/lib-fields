package de.team33.test.fields.common;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Level2c extends Level1c {

    long privateStaticFinalLong = 278L;
    List<String> privateStaticFinalListOfString = Collections.emptyList();
    Map<String, Integer> privateStaticFinalMapOfStringToInteger = Collections.emptyMap();
}

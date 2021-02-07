package de.team33.test.reflect.v4;

import de.team33.libs.reflect.v4.Fields;
import de.team33.libs.reflect.v4.Fields.Naming;
import de.team33.libs.reflect.v4.Fields.Streaming;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;


public class FieldsMappingTest {

    @Test
    public void significantFlat() {
        final Map<String, Field> result = Fields.Mapping.SIGNIFICANT_FLAT.apply(FieldsTest.Sub.class);
        assertEquals(Arrays.asList("privateFinalInt", "privateInt"),
                new ArrayList<>(new TreeSet<>(result.keySet())));
    }

    @Test
    public void significantDeep() {
        final Map<String, Field> result = Fields.Mapping.SIGNIFICANT_DEEP.apply(FieldsTest.Sub.class);
        assertEquals(Arrays.asList("..privateFinalInt",
                "..privateInt",
                ".privateFinalInt",
                ".privateInt",
                "privateFinalInt",
                "privateInt"), new ArrayList<>(new TreeSet<>(result.keySet())));
    }
}

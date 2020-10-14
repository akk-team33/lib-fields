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

    private static final Fields.Mapping ALL_WIDE = type -> Streaming.WIDE.apply(type)
                                                                         .collect(TreeMap::new,
                                                                                 (map, field) -> map.put(Fields.compactName(type, field), field), Map::putAll);
            //.collect(toMap(Naming.compact(type), field -> field));

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

    @Test
    public void allWide() {
        final Map<String, Field> result = ALL_WIDE.apply(FieldsTest.Sub.class);
        final List<String> expected = Arrays.asList(
                "de.team33.test.reflect.v4.FieldsTest.ISuper1.privateFinalInt",
                "de.team33.test.reflect.v4.FieldsTest.ISuper1.privateInt",
                "de.team33.test.reflect.v4.FieldsTest.ISuper1.privateStaticFinalInt",
                "de.team33.test.reflect.v4.FieldsTest.ISuper1.privateStaticInt",
                "de.team33.test.reflect.v4.FieldsTest.ISuper2.privateFinalInt",
                "de.team33.test.reflect.v4.FieldsTest.ISuper2.privateInt",
                "de.team33.test.reflect.v4.FieldsTest.ISuper2.privateStaticFinalInt",
                "de.team33.test.reflect.v4.FieldsTest.ISuper2.privateStaticInt",
                "de.team33.test.reflect.v4.FieldsTest.Inner.privateFinalInt",
                "de.team33.test.reflect.v4.FieldsTest.Inner.privateInt",
                "de.team33.test.reflect.v4.FieldsTest.Inner.privateStaticFinalInt",
                "de.team33.test.reflect.v4.FieldsTest.Inner.privateStaticInt",
                "de.team33.test.reflect.v4.FieldsTest.Super.privateFinalInt",
                "de.team33.test.reflect.v4.FieldsTest.Super.privateInt",
                "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticFinalInt",
                "de.team33.test.reflect.v4.FieldsTest.Super.privateStaticInt",
                "de.team33.test.reflect.v4.FieldsTest.Super.privateTransientInt",
                "privateFinalInt",
                "privateInt",
                "privateStaticFinalInt",
                "this$0");
        final ArrayList<String> actual = new ArrayList<>(new TreeSet<>(result.keySet()));
//        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }
}

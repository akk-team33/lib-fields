package de.team33.test.fields.v1a;

import de.team33.libs.fields.v1a.Fields;
import de.team33.test.fields.common.Level3;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class FieldsTest {

    private final Class<?> theClass = Level3.class;

    private static List<Field> listFlat(final Class<?> type) {
        return asList(type.getDeclaredFields());
    }

    private static List<Field> listDeep(final Class<?> type) {
        final List<Field> result = Optional.ofNullable(type.getSuperclass())
                                           .map(FieldsTest::listDeep)
                                           .orElseGet(ArrayList::new);
        result.addAll(asList(type.getDeclaredFields()));
        return result;
    }

    private static List<Field> listAll(final Class<?> type) {
        return new ArrayList<>(new HashSet<>(listAllRedundant(type)));
    }

    private static List<Field> listAllRedundant(final Class<?> type) {
        if (null == type) {
            return emptyList();
        }

        final List<Field> result = listAllRedundant(type.getInterfaces());
        result.addAll(listAllRedundant(type.getSuperclass()));
        result.addAll(listFlat(type));
        return result;
    }

    private static List<Field> listAllRedundant(final Class<?>[] interfaces) {
        final List<Field> result = new ArrayList<>(0);
        for (final Class<?> type: interfaces){
            result.addAll(listAllRedundant(type));
        }
        return result;
    }

    /**
     * Verifies certain basic assumptions about {@link Field}s:
     * <ul>
     *     <li>Two {@link Field} instances that represent the same field are {@link Field#equals(Object) equal}.</li>
     *     <li>Multiple accesses to the same field via the methods of a {@link Class} result in new,
     *     i.e. non-identical {@link Field} instances.</li>
     *     <li>If a property, e.g. the accessibility of a {@link Field} instance, is changed, this only affects this
     *     instance, not the field itself.</li>
     * </ul>
     */
    @Test
    public final void basicAssumptionsAboutFields() {
        // Getting two instances of the 'same' <Field> through a <Class> ...
        final Field first = theClass.getDeclaredFields()[0];
        final Field second = theClass.getDeclaredFields()[0];

        assertNotSame("Two instances of the 'same' <Field> are expected to be not identical", first, second);
        assertEquals("Two instances of the 'same' <Field> are expected to be equal", first, second);

        first.setAccessible(true);
        second.setAccessible(false);
        assertEquals("Even if two instances of the 'same' <Field> are modified in a different way, " +
                             "they are still expected to be equal", first, second);
        assertNotEquals("It is nevertheless expected that the accessibility of two instances, " +
                                "which is set to different values, is retained.",
                        first.isAccessible(), second.isAccessible());
    }

    @Test
    public final void basicAssumptionsAboutPreconditions() {
        final List<Field> flatList = listFlat(theClass);
        assertFalse("In the class that is used for the tests, fields must actually be declared, " +
                            "otherwise some of the following test results would not be significant.",
                    flatList.isEmpty());
        assertEquals("It is expected that each element of the result list is unique.",
                     flatList.size(), new HashSet<>(flatList).size());

        final List<Field> deepList = listDeep(theClass);
        assertTrue("The class that is used for the tests must be derived from a class that in turn actually " +
                           "declares fields, otherwise some of the subsequent test results would not be significant.",
                   deepList.size() > flatList.size());
        assertEquals("It is expected that each element of the result list is unique.",
                     deepList.size(), new HashSet<>(deepList).size());

        final List<Field> wideList = listAll(theClass);
        assertTrue("The class that is used for the tests must be derived from an interface that in turn actually " +
                           "declares fields, otherwise some of the subsequent test results would not be significant.",
                   wideList.size() > deepList.size());
        final List<Field> wideListRedundant = listAllRedundant(theClass);
        assertTrue("The class that is used for the tests must be redundantly derived from one or more interfaces, " +
                           "which in turn actually declare fields, otherwise some of the subsequent test results " +
                           "would not be significant.",
                   wideListRedundant.size() > wideList.size());
        assertEquals("It is expected that each element of the result list is unique.",
                     wideList.size(), new HashSet<>(wideList).size());
    }

    /**
     * It is expected that {@link Fields#stream(Class)} returns a {@link Stream} that consists of all the fields that
     * are declared directly in the context of the given class.
     */
    @Test
    public final void stream() {
        final List<Field> expected = listFlat(theClass);
        final List<Field> collected = Fields.stream(theClass).collect(toList());
        assertEquals("The result list is expected to have a definite size",
                     expected.size(), collected.size());
        assertEquals("The result list is expected to have a definite content",
                     new HashSet<>(expected), new HashSet<>(collected));
        // Remark: The result list is not expected to have a definite order!

        final List<Field> empty = Fields.stream(null).collect(toList());
        assertEquals("The result list is expected to be empty", emptyList(), empty);
    }

    @Test
    public final void streamDeep() {
        final List<Field> expected = listDeep(theClass);
        final List<Field> collected = Fields.streamDeep(theClass).collect(toList());
        assertEquals("The result list is expected to have a definite size",
                     expected.size(), collected.size());
        assertEquals("The result list is expected to have a definite content",
                     new HashSet<>(expected), new HashSet<>(collected));
        // Remark: The result list is not expected to have a definite order!

        final List<Field> empty = Fields.streamDeep(null).collect(toList());
        assertEquals("The result list is expected to be empty", emptyList(), empty);
    }

    @Test
    public final void streamAll() {
        final List<Field> expected = listAll(Level3.class);
        final List<Field> collected = Fields.streamAll(Level3.class).collect(toList());
        assertEquals("The result list is expected to have a definite size",
                     expected.size(), collected.size());
        assertEquals("The result list is expected to have a definite content",
                     new HashSet<>(expected), new HashSet<>(collected));
        // Remark: The result list is not expected to have a definite order!

        final List<Field> empty = Fields.streamAll(null).collect(toList());
        assertEquals("The result list is expected to be empty", emptyList(), empty);
    }
}

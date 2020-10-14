package de.team33.test.reflect.v4;

import de.team33.libs.reflect.v4.Fields;
import de.team33.test.reflect.common.Sample;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("JUnit4MethodNamingConvention")
public class FieldsFilterTest {

    private static Stream<Field> sampleFields() {
        return Fields.flatStreamOf(Sample.class);
    }

    private static Stream<Field> sampleFields(final Predicate<? super Field> filter) {
        return Fields.flatStreamOf(Sample.class).filter(filter);
    }

    private static Set<String> sampleFieldNames(final Stream<Field> stream) {
        return stream
                .map(Fields.Naming.SIMPLE)
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    @Test
    public final void ANY() {
        final Set<String> expected = sampleFieldNames(sampleFields());
        final Set<String> result = sampleFieldNames(sampleFields(Fields.Filter.ANY));
        assertEquals(expected, result);
    }

    @Test
    public final void PUBLIC() {
        sampleFields(Fields.Filter.PUBLIC)
                .forEach(field -> assertTrue(Modifier.isPublic(field.getModifiers())));
        sampleFields(Fields.Filter.PUBLIC.negate())
                .forEach(field -> assertFalse(Modifier.isPublic(field.getModifiers())));
    }

    @Test
    public final void PRIVATE() {
        sampleFields(Fields.Filter.PRIVATE)
                .forEach(field -> assertTrue(Modifier.isPrivate(field.getModifiers())));
        sampleFields(Fields.Filter.PRIVATE.negate())
                .forEach(field -> assertFalse(Modifier.isPrivate(field.getModifiers())));
    }

    @Test
    public final void PROTECTED() {
        sampleFields(Fields.Filter.PROTECTED)
                .forEach(field -> assertTrue(Modifier.isProtected(field.getModifiers())));
        sampleFields(Fields.Filter.PROTECTED.negate())
                .forEach(field -> assertFalse(Modifier.isProtected(field.getModifiers())));
    }

    @Test
    public final void STATIC() {
        sampleFields(Fields.Filter.STATIC)
                .forEach(field -> assertTrue(Modifier.isStatic(field.getModifiers())));
        sampleFields(Fields.Filter.STATIC.negate())
                .forEach(field -> assertFalse(Modifier.isStatic(field.getModifiers())));
    }

    @Test
    public final void FINAL() {
        sampleFields(Fields.Filter.FINAL)
                .forEach(field -> assertTrue(Modifier.isFinal(field.getModifiers())));
        sampleFields(Fields.Filter.FINAL.negate())
                .forEach(field -> assertFalse(Modifier.isFinal(field.getModifiers())));
    }

    @Test
    public final void TRANSIENT() {
        sampleFields(Fields.Filter.TRANSIENT)
                .forEach(field -> assertTrue(Modifier.isTransient(field.getModifiers())));
        sampleFields(Fields.Filter.TRANSIENT.negate())
                .forEach(field -> assertFalse(Modifier.isTransient(field.getModifiers())));
    }

    @Test
    public final void INSTANCE() {
        Fields.wideStreamOf(FieldsTest.ISuper2.class).forEach(field ->
                assertFalse(field.toString(), Fields.Filter.INSTANCE.test(field)));
    }

    @Test
    public final void SIGNIFICANT() {
        sampleFields(Fields.Filter.SIGNIFICANT)
                .forEach(field -> {
                    final int modifiers = field.getModifiers();
                    assertFalse(Modifier.isStatic(modifiers));
                    assertFalse(Modifier.isTransient(modifiers));
                });
        sampleFields(Fields.Filter.SIGNIFICANT.negate())
                .forEach(field -> {
                    final int modifiers = field.getModifiers();
                    assertTrue(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers));
                });
    }
}

package de.team33.libs.reflect.v4;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * <p>A tool that can copy instances of a certain type field by field or translate them into or from a map.</p>
 * <p>To get an instance use {@link #factory(Function)} or {@link #FACTORY} and {@link Factory#mapperFor(Class)}.</p>
 *
 * @param <T> the type of interest
 */
public final class FieldMapper<T> {

    /**
     * A default {@link Factory} to get typical {@link FieldMapper} instances that take into account all non-transient
     * instance fields of a given class and its superclasses (if any).
     */
    public static final Factory FACTORY = factory(Fields.Mapping.SIGNIFICANT_DEEP);

    private static final String CANNOT_GET_FIELD = "cannot get value of field <%s> of instance <%s>";
    private static final String CANNOT_SET_FIELD = "cannot Set field <%s> of instance <%s> to value <%s>";

    private final Map<String, Field> mapping;

    private FieldMapper(final Map<String, Field> mapping) {
        this.mapping = mapping;
    }

    /**
     * A method to get a {@link Factory} that differs from the {@link #FACTORY default factory}.
     */
    public static Factory factory(final Function<Class<?>, Map<String, Field>> mapping) {
        return new Factory() {
            @Override
            public <T> FieldMapper<T> mapperFor(final Class<T> subjectClass) {
                return new FieldMapper<T>(mapping.apply(subjectClass));
            }
        };
    }

    private static void set(final Field field, final Object target, final Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(format(CANNOT_SET_FIELD, field, target, value), e);
        }
    }

    private static Object get(final Field field, final Object origin) {
        try {
            return field.get(origin);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(format(CANNOT_GET_FIELD, field, origin), e);
        }
    }

    /**
     * Copies the fields of an original instance of the underlying type into a corresponding target instance.
     *
     * @return the target instance.
     */
    public final T copy(final T origin, final T target) {
        mapping.forEach((name, field) -> set(field, target, get(field, origin)));
        return target;
    }

    /**
     * Copies the fields of a source instance of the underlying type to a target map, which must be a mutable map.
     *
     * @return the target map.
     */
    public final <M extends Map<String, Object>> M map(final T origin, final M target) {
        mapping.forEach((name, field) -> target.put(name, get(field, origin)));
        return target;
    }

    /**
     * Copies the values of an original map into a corresponding target instance of the underlying type.
     *
     * @return the target instance.
     */
    public final T map(final Map<?, ?> origin, final T target) {
        mapping.forEach((name, field) -> set(field, target, origin.get(name)));
        return target;
    }

    /**
     * Abstracts a factory for {@link FieldMapper} instances.
     */
    public interface Factory {

        /**
         * Creates a new {@link FieldMapper} instance for a given type.
         */
        <T> FieldMapper<T> mapperFor(Class<T> subjectClass);
    }
}

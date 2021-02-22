package de.team33.libs.fields.v1a;

import java.lang.reflect.Field;
import java.util.stream.Stream;

public final class Fields {

    private Fields() {
    }

    private static <E> Stream<E> streamOfNullable(final E element) {
        return (null == element) ? Stream.empty() : Stream.of(element);
    }

    private static Stream<Field> stream(final Stream<Class<?>> types) {
        return types.map(Class::getDeclaredFields)
                    .flatMap(Stream::of);
    }

    private static Stream<Class<?>> lineageClasses(final Class<?> type) {
        return (null == type) ? Stream.empty() : Stream.concat(lineageClasses(type.getSuperclass()), Stream.of(type));
    }

    private static Stream<Class<?>> superior(final Class<?> type) {
        return Stream.concat(Stream.of(type.getInterfaces()), streamOfNullable(type.getSuperclass()));
    }

    private static Stream<Class<?>> lineage(final Stream<Class<?>> types) {
        return types.flatMap(Fields::lineage);
    }

    private static Stream<Class<?>> lineage(final Class<?> type) {
        return (null == type) ? Stream.empty() : Stream.concat(lineage(superior(type)), Stream.of(type));
    }

    /**
     * Streams all {@link Field}s straightly declared by a given {@link Class}.
     * <p>
     * There are no guarantees about the order of the fields in the resulting stream.
     * <p>
     * The {@link Class} may be {@code null}. Then the result is an empty stream.
     */
    public static Stream<Field> stream(final Class<?> type) {
        return stream(streamOfNullable(type));
    }

    /**
     * Streams all {@link Field}s declared by a given {@link Class} or any of its superclasses.
     * <p>
     * The {@link Class} may be {@code null}. Then the result is an empty stream.
     */
    public static Stream<Field> streamDeep(final Class<?> type) {
        return stream(lineageClasses(type));
    }

    /**
     * Streams all {@link Field}s declared by a given {@link Class}, any of its superclasses or any of its
     * superinterfaces.
     */
    public static Stream<Field> streamAll(final Class<?> type) {
        return stream(lineage(type).distinct());
    }
}

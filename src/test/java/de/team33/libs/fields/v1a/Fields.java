package de.team33.libs.fields.v1a;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Fields {

    private static final int SYNTHETIC = 0x00001000;
    private static final int NOT_INSTANCE = Modifier.STATIC | SYNTHETIC;
    private static final int NOT_SIGNIFICANT = Modifier.STATIC | Modifier.TRANSIENT | SYNTHETIC;

    private Fields() {
    }

    private static <E> Stream<E> streamOfNullable(final E element) {
        return (null == element) ? Stream.empty() : Stream.of(element);
    }

    private static Stream<Field> streamDeclaredOfAll(final Stream<Class<?>> types) {
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
     * Streams all {@link Field}s of a given {@link Class} according to a given {@link Strategy}.
     * <p>
     * There are no guarantees about the order of the fields in the resulting {@link Stream}.
     * <p>
     * The {@link Class} may be {@code null}. Then the result is an empty stream.
     */
    public static Stream<Field> stream(final Strategy strategy, final Class<?> type) {
        return strategy.streaming.apply(type);
    }

    /**
     * Tests the {@link Field#getModifiers() modifiers} of a given field.
     *
     * @param field      The field whose modifiers are to be tested.
     * @param required   The modifiers that must be present in the form of a bit pattern.
     * @param prohibited The modifiers, which must not be present, in the form of a bit pattern.
     * @return {@code true} if the modifiers of the field correspond to the expectations, otherwise false.
     * @see Field#getModifiers()
     * @see Modifier
     */
    public static boolean testModifiers(final Field field, final int required, final int prohibited) {
        final int modifiers = field.getModifiers();
        return (required == (required & modifiers)) && (0 == (prohibited & modifiers));
    }

    /**
     * Defines different strategies for accessing the fields of a class.
     */
    public enum Strategy {

        /**
         * The access should cover all {@link Field}s that were straightly declared in the {@link Class} concerned.
         */
        STRAIGHT(type -> streamDeclaredOfAll(streamOfNullable(type))),

        /**
         * The access should cover all {@link Field}s that were declared in the {@link Class} concerned or recursively
         * in one of its superclasses.
         */
        DEEP(type -> streamDeclaredOfAll(lineageClasses(type))),

        /**
         * The access should cover all {@link Field}s that were declared in the {@link Class} concerned or recursively
         * in one of its superclasses or its superinterfaces.
         */
        WIDE(type -> streamDeclaredOfAll(lineage(type).distinct())),

        /**
         * The access should cover all accessible public {@link Field}s that were declared anywhere in the lineage
         * hierarchy of the {@link Class} concerned.
         */
        PUBLIC(type -> Optional.ofNullable(type)
                               .map(Class::getFields)
                               .map(Stream::of)
                               .orElseGet(Stream::empty));

        private final Function<Class<?>, Stream<Field>> streaming;

        Strategy(final Function<Class<?>, Stream<Field>> streaming) {
            this.streaming = streaming;
        }
    }

    /**
     * Provides some predefined {@linkplain Predicate filters} for {@link Field Fields}.
     */
    @FunctionalInterface
    public interface Filter extends Predicate<Field> {

        /**
         * Defines a filter accepting all fields (including static fields).
         */
        Filter ANY = field -> true;

        /**
         * Defines a filter accepting all public fields.
         */
        Filter PUBLIC = field -> testModifiers(field, Modifier.PUBLIC, 0);

        /**
         * Defines a filter accepting all public static fields.
         */
        Filter PUBLIC_STATIC = field -> testModifiers(field, Modifier.PUBLIC | Modifier.STATIC, 0);

        /**
         * Defines a filter accepting all public static fields.
         */
        Filter PUBLIC_INSTANCE = field -> testModifiers(field, Modifier.PUBLIC, Modifier.STATIC | Fields.SYNTHETIC);

        /**
         * Defines a filter accepting all private fields.
         */
        Filter PRIVATE = field -> Modifier.PRIVATE == (field.getModifiers() & Modifier.PRIVATE);

        /**
         * Defines a filter accepting all protected fields.
         */
        Filter PROTECTED = field -> Modifier.PROTECTED == (field.getModifiers() & Modifier.PROTECTED);

        /**
         * Defines a filter accepting all static fields.
         */
        Filter STATIC = field -> Modifier.STATIC == (field.getModifiers() & Modifier.STATIC);

        /**
         * Defines a filter accepting all final fields.
         */
        Filter FINAL = field -> Modifier.FINAL == (field.getModifiers() & Modifier.FINAL);

        /**
         * Defines a filter accepting all transient fields.
         */
        Filter TRANSIENT = field -> Modifier.TRANSIENT == (field.getModifiers() & Modifier.TRANSIENT);

        /**
         * Defines a filter accepting all synthetic fields.
         * Caution: this filter uses an undocumented feature.
         */
        Filter SYNTHETIC = field -> Fields.SYNTHETIC == (field.getModifiers() & Fields.SYNTHETIC);

        /**
         * Defines a filter accepting all instance-fields (non-static fields).
         */
        Filter INSTANCE = field -> 0 == (field.getModifiers() & NOT_INSTANCE);

        /**
         * Defines a filter accepting all but static or transient fields.
         * Those fields should be significant for a type with value semantics.
         */
        Filter SIGNIFICANT = field -> 0 == (field.getModifiers() & NOT_SIGNIFICANT);
    }
}

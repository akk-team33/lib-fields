package de.team33.libs.reflect.v4;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;


/**
 * Utility for dealing with fields.
 */
public final class Fields {

    private static final String INITIAL_PREFIX = "";
    private static final Function<String, String> ADD_PREFIX = "."::concat;

    private Fields() {
    }

    /**
     * Streams all {@link Field}s straightly declared by a given {@link Class}
     */
    public static Stream<Field> flatStreamOf(final Class<?> type) {
        return streamOf(Classes.streamOf(type));
    }

    /**
     * Streams all {@link Field}s declared by a given {@link Class} or any of its superclasses.
     */
    public static Stream<Field> deepStreamOf(final Class<?> type) {
        return streamOf(Classes.deepStreamOf(type));
    }

    /**
     * Streams all {@link Field}s declared by a given {@link Class}, any of its superclasses or any of its
     * superinterfaces.
     */
    public static Stream<Field> wideStreamOf(final Class<?> type) {
        return streamOf(Classes.wideStreamOf(type));
    }

    /**
     * <p>Determines an extended field name that is as compact as possible but unique in the context of a certain class
     * and its class hierarchy.</p>
     *
     * <p>This takes into account the fact that different fields of the same name can be declared within a class
     * hierarchy.</p>
     *
     * <ul>
     *     <li>If the field is declared directly in the context class, the
     *     {@linkplain Field#getName() simple field name} results.</li>
     *     <li>If the field is declared in a super class of the context class, the name is preceded by as many dots
     *     ('.') as it corresponds to the hierarchy level of the declaring class compared to the context class.</li>
     *     <li>If the field is declared in a class that is not a direct or indirect superclass of the context class,
     *     the result is a fully qualified field name.</li>
     * </ul>
     */
    public static String compactName(final Class<?> contextClass, final Field field) {
        final Class<?> declaringClass = field.getDeclaringClass();
        return prefix(contextClass, declaringClass)
                .orElseGet(() -> declaringClass.getCanonicalName() + ".")
                .concat(field.getName());
    }

    private static Optional<String> prefix(final Class<?> contextClass, final Class<?> declaringClass) {
        return (null == contextClass)
                ? Optional.empty()
                : _prefix(contextClass, declaringClass);
    }

    private static Optional<String> _prefix(final Class<?> contextClass, final Class<?> declaringClass) {
        return contextClass.equals(declaringClass)
                ? Optional.of(INITIAL_PREFIX)
                : prefix(contextClass.getSuperclass(), declaringClass).map(ADD_PREFIX);
    }

    /**
     * Determines a canonical, fully qualified name for a given field.
     */
    public static String canonicalName(final Field field) {
        return field.getDeclaringClass().getCanonicalName() + "." + field.getName();
    }

    private static Stream<Field> streamOf(final Stream<Class<?>> classes) {
        return classes.map(Class::getDeclaredFields)
                .map(Stream::of)
                .reduce(Stream::concat)
                .orElseGet(Stream::empty);
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
        Filter PUBLIC = field -> Modifier.isPublic(field.getModifiers());

        /**
         * Defines a filter accepting all private fields.
         */
        Filter PRIVATE = field -> Modifier.isPrivate(field.getModifiers());

        /**
         * Defines a filter accepting all protected fields.
         */
        Filter PROTECTED = field -> Modifier.isProtected(field.getModifiers());

        /**
         * Defines a filter accepting all static fields.
         */
        Filter STATIC = field -> Modifier.isStatic(field.getModifiers());

        /**
         * Defines a filter accepting all final fields.
         */
        Filter FINAL = field -> Modifier.isFinal(field.getModifiers());

        /**
         * Defines a filter accepting all transient fields.
         */
        Filter TRANSIENT = field -> Modifier.isTransient(field.getModifiers());

        /**
         * Defines a filter accepting all synthetic fields.
         * Caution: this filter uses an undocumented feature.
         */
        Filter SYNTHETIC = field -> 0 != (0x00001000 & field.getModifiers());

        /**
         * Defines a filter accepting all instance-fields (non-static fields).
         */
        Filter INSTANCE = field -> STATIC.negate().test(field);

        /**
         * Defines a filter accepting all but static or transient fields.
         * Those fields should be significant for a type with value semantics.
         */
        Filter SIGNIFICANT = field -> STATIC.or(TRANSIENT).or(SYNTHETIC).negate().test(field);
    }

    /**
     * Defines some typical {@link Function}s that serve to find a name for a {@link Field}.
     */
    @FunctionalInterface
    public interface Naming extends Function<Field, String> {

        /**
         * A {@link Function} that simply returns the plain {@linkplain Field#getName() name} of a given {@link Field}.
         */
        Naming SIMPLE = Field::getName;

        /**
         * A {@link Function} that returns a canonical, full qualified name for a given {@link Field}.
         */
        Naming CANONICAL = Fields::canonicalName;

        /**
         * Returns a {@link Function} that retrieves a field name that is short but unique in the context of a
         * particular class.
         *
         * @see Fields#compactName(Class, Field)
         */
        static Naming compact(final Class<?> context) {
            return field -> compactName(context, field);
        }

        /**
         * Returns a {@link Function} that simply returns the plain {@linkplain Field#getName() name} of a
         * given {@link Field} if inquired in the context of the Field's declaring class. Otherwise it returns
         * a canonical, full qualified name.
         */
        static Naming conditional(final Class<?> context) {
            return field -> context.equals(field.getDeclaringClass())
                    ? field.getName()
                    : canonicalName(field);
        }
    }

    /**
     * Defines some typical {@link Function}s that serve to stream {@link Field}s of a given {@link Class}.
     */
    @FunctionalInterface
    public interface Streaming extends Function<Class<?>, Stream<Field>> {

        /**
         * Streams all {@link Field}s straightly declared by a given {@link Class}
         */
        Streaming FLAT = Fields::flatStreamOf;

        /**
         * Streams all {@link Field}s declared by a given {@link Class} or any of its superclasses.
         */
        Streaming DEEP = Fields::deepStreamOf;

        /**
         * Streams all {@link Field}s declared by a given {@link Class}, any of its superclasses or any of
         * its superinterfaces.
         */
        Streaming WIDE = Fields::wideStreamOf;

        /**
         * Streams all non-static {@link Field}s straightly declared by a given {@link Class}.
         */
        Streaming INSTANCE_FLAT = context -> flatStreamOf(context).filter(Filter.INSTANCE);

        /**
         * Streams all non-static {@link Field}s declared by a given {@link Class} or any of its
         * superclasses.
         */
        Streaming INSTANCE_DEEP = context -> deepStreamOf(context).filter(Filter.INSTANCE);

        /**
         * Streams all non-static/non-transient {@link Field}s straightly declared by a given {@link Class}.
         * Those fields should be significant for a simple type with value semantics.
         */
        Streaming SIGNIFICANT_FLAT = context -> flatStreamOf(context).filter(Filter.SIGNIFICANT);

        /**
         * Streams all non-static/non-transient {@link Field}s declared by a given {@link Class} or any of
         * its superclasses. Those fields should be significant for a type with value semantics.
         */
        Streaming SIGNIFICANT_DEEP = context -> deepStreamOf(context).filter(Filter.SIGNIFICANT);
    }

    /**
     * In essence, a {@link Mapping} is a {@link Function} to get a {@link Map} whose values represent
     * a subset of all {@link Field}s declared by a given {@link Class} or one of its superclasses.
     * The keys are to be understood as logical field names, but they do not necessarily have to match their
     * {@linkplain Field#getName() plain field name}.
     */
    @FunctionalInterface
    public interface Mapping extends Function<Class<?>, Map<String, Field>> {

        /**
         * Defines a {@link Mapping} that only considers the fields straightly declared by the underlying class,
         * which are neither static nor transient.
         */
        Mapping SIGNIFICANT_FLAT = type -> Streaming.SIGNIFICANT_FLAT.apply(type)
                .peek(field -> field.setAccessible(true))
                .collect(toMap(Naming.SIMPLE, field -> field));

        /**
         * Defines a {@link Mapping} that considers the fields declared by the underlying class or one of its
         * superclasses, which are neither static nor transient.
         */
        Mapping SIGNIFICANT_DEEP = type -> Streaming.SIGNIFICANT_DEEP.apply(type)
                .peek(field -> field.setAccessible(true))
                .collect(toMap(Naming.compact(type), field -> field));
    }

    private static class CompactNameException extends RuntimeException {
    }
}

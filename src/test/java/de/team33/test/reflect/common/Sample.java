package de.team33.test.reflect.common;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({
        "unused", "FieldMayBeStatic", "FieldMayBeFinal", "PublicField", "ProtectedField", "PackageVisibleField",
        "ConstantNamingConvention", "StaticVariableNamingConvention", "ClassWithTooManyFields",
        "RedundantStringConstructorCall"})
public class Sample {

    public static final String aPublicStaticFinalField = "Sample.aPublicStaticFinalField";
    private static String aPrivateStaticField = "Sample.aPrivateStaticField";

    public final String aPublicFinalField;
    public final transient String aPublicFinalTransientField;
    protected final String aProtectedFinalField;
    final String aPackageFinalField;
    private final String aPrivateFinalField;
    public String aPublicField;
    public transient String aPublicTransientField;
    protected String aProtectedField;
    String aPackageField;
    private String aPrivateField;

    public Sample() {
        aPublicFinalField = "Sample.aPublicFinalField";
        aPublicFinalTransientField = "Sample.aPublicFinalTransientField";
        aProtectedFinalField = "Sample.aProtectedFinalField";
        aPackageFinalField = "Sample.aPackageFinalField";
        aPrivateFinalField = "Sample.aPrivateFinalField";
        aPublicField = "Sample.aPublicField";
        aPublicTransientField = "Sample.aPublicTransientField";
        aProtectedField = "Sample.aProtectedField";
        aPackageField = "Sample.aPackageField";
        aPrivateField = "Sample.aPrivateField";
    }

    public Sample(final Randomizer rnd) {
        aPublicFinalField = rnd.nextString();
        aPublicFinalTransientField = rnd.nextString();
        aProtectedFinalField = rnd.nextString();
        aPackageFinalField = rnd.nextString();
        aPrivateFinalField = rnd.nextString();
        aPublicField = rnd.nextString();
        aPublicTransientField = rnd.nextString();
        aProtectedField = rnd.nextString();
        aPackageField = rnd.nextString();
        aPrivateField = rnd.nextString();
    }

    public static String getAPublicStaticFinalField() {
        return aPublicStaticFinalField;
    }

    public static String getAStaticField() {
        return aPrivateStaticField;
    }

    public static void setAStaticField(final String aStaticField) {
        Sample.aPrivateStaticField = aStaticField;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public static List<?> toList(final Sample subject) {
        return Arrays.asList(
                subject.aPrivateFinalField,
                subject.aProtectedFinalField,
                subject.aPackageFinalField,
                subject.aPublicFinalField,
                subject.aPrivateField,
                subject.aProtectedField,
                subject.aPackageField,
                subject.aPublicField
        );
    }

    public final String getAPrivateFinalField() {
        return aPrivateFinalField;
    }

    public final String getAProtectedFinalField() {
        return aProtectedFinalField;
    }

    public final String getAPackageFinalField() {
        return aPackageFinalField;
    }

    public final String getAPublicFinalField() {
        return aPublicFinalField;
    }

    public final String getAPublicFinalTransientField() {
        return aPublicFinalTransientField;
    }

    public final String getAPrivateField() {
        return aPrivateField;
    }

    public final Sample setAPrivateField(final String aPrivateField) {
        this.aPrivateField = aPrivateField;
        return this;
    }

    public final String getAProtectedField() {
        return aProtectedField;
    }

    public final Sample setAProtectedField(final String aProtectedField) {
        this.aProtectedField = aProtectedField;
        return this;
    }

    public final String getAPackageField() {
        return aPackageField;
    }

    public final Sample setAPackageField(final String aPackageField) {
        this.aPackageField = aPackageField;
        return this;
    }

    public final String getAPublicField() {
        return aPublicField;
    }

    public final Sample setAPublicField(final String aPublicField) {
        this.aPublicField = aPublicField;
        return this;
    }

    public final String getAPublicTransientField() {
        return aPublicTransientField;
    }

    public final Sample setAPublicTransientField(final String aPublicTransientField) {
        this.aPublicTransientField = aPublicTransientField;
        return this;
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public int hashCode() {
        return toList(this).hashCode();
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public boolean equals(final Object obj) {
        return this == obj || Sample.class == obj.getClass() && toList(this).equals(toList((Sample) obj));
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public String toString() {
        return toList(this).toString();
    }
}

package de.team33.test.reflect.common;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({
        "unused", "FieldMayBeStatic", "FieldMayBeFinal", "PublicField", "ProtectedField", "PackageVisibleField",
        "ConstantNamingConvention", "StaticVariableNamingConvention", "ClassWithTooManyFields",
        "FieldNameHidesFieldInSuperclass"})
public class SampleEx extends Sample {

    public static final String aPublicStaticFinalField = "SampleEx.aPublicStaticFinalField";
    private static String aPrivateStaticField = "SampleEx.aPrivateStaticField";

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

    public SampleEx() {
        aPublicFinalField = "SampleEx.aPublicFinalField";
        aPublicFinalTransientField = "SampleEx.aPublicFinalTransientField";
        aProtectedFinalField = "SampleEx.aProtectedFinalField";
        aPackageFinalField = "SampleEx.aPackageFinalField";
        aPrivateFinalField = "SampleEx.aPrivateFinalField";
        aPublicField = "SampleEx.aPublicField";
        aPublicTransientField = "SampleEx.aPublicTransientField";
        aProtectedField = "SampleEx.aProtectedField";
        aPackageField = "SampleEx.aPackageField";
        aPrivateField = "SampleEx.aPrivateField";
    }

    public SampleEx(final Randomizer rnd) {
        super(rnd);
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

    @SuppressWarnings("TypeMayBeWeakened")
    public static List<?> toList(final SampleEx subject) {
        return Arrays.asList(
                Sample.toList(subject),
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

    @SuppressWarnings("DesignForExtension")
    @Override
    public int hashCode() {
        return toList(this).hashCode();
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((SampleEx.class == obj.getClass()) && toList(this).equals(toList((SampleEx) obj)));
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public String toString() {
        return toList(this).toString();
    }
}

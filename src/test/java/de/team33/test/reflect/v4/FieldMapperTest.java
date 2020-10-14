package de.team33.test.reflect.v4;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.reflect.v4.FieldMapper;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class FieldMapperTest {

    private Random random = new Random();
    private FieldMapper<Sample> subject = FieldMapper.FACTORY.mapperFor(Sample.class);

    @Test
    public void copy() {
        final Sample origin = new Sample();
        final Sample result = subject.copy(origin, new Sample());
        assertEquals(origin, result);
    }

    @Test
    public void mapToMap() {
        final Sample origin = new Sample();
        final TreeMap<String, Object> stage = subject.map(origin, new TreeMap<>());
        final Sample result = subject.map(stage, new Sample());
        assertEquals(origin, result);

    }

    @Test
    public void mapFromMap() {
        final Map<?, ?> origin = new TreeMap<>(ImmutableMap.builder()
                                                           .put(".privateFinalInt", anyInt())
                                                           .put(".privateFinalDouble", anyDouble())
                                                           .put("privateFinalInt", anyInt())
                                                           .put("privateFinalDouble", anyDouble())
                                                           .put("privateFinalString", anyString())
                                                           .put("privateFinalDate", anyDate())
                                                           .build());
        final Sample stage = subject.map(origin, new Sample());
        final Map<?, ?> result = subject.map(stage, new TreeMap<>());
        assertEquals(origin, result);
    }

    private int anyInt() {
        return random.nextInt();
    }

    private Double anyDouble() {
        return random.nextDouble();
    }

    private String anyString() {
        return new BigInteger(128, random).toString(Character.MAX_RADIX);
    }

    private Date anyDate() {
        return new Date(random.nextInt());
    }

    private static List<Object> superSampleFields(final SuperSample sample) {
        return Arrays.asList(
                sample.privateFinalInt,
                sample.privateFinalDouble);
    }

    private static List<Object> sampleFields(final Sample sample) {
        return Arrays.asList(
                superSampleFields(sample),
                sample.privateFinalInt,
                sample.privateFinalDouble,
                sample.privateFinalString,
                sample.privateFinalDate);
    }

    private class SuperSample {

        private final int privateFinalInt;
        private final Double privateFinalDouble;
        private final transient String privateFinalString;

        private SuperSample() {
            privateFinalInt = anyInt();
            privateFinalDouble = anyDouble();
            this.privateFinalString = anyString();
        }
    }

    private class Sample extends SuperSample {

        private final int privateFinalInt;
        private final Double privateFinalDouble;
        private final String privateFinalString;
        private final Date privateFinalDate;

        private Sample() {
            this.privateFinalInt = anyInt();
            this.privateFinalDouble = anyDouble();
            this.privateFinalString = anyString();
            this.privateFinalDate = anyDate();
        }

        @Override
        public int hashCode() {
            return sampleFields(this).hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || (obj instanceof Sample && sampleFields(this).equals(sampleFields((Sample) obj)));
        }

        @Override
        public String toString() {
            return sampleFields(this).toString();
        }
    }
}

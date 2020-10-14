package de.team33.test.reflect.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;


@SuppressWarnings("unused")
public class Level3 extends Level2 {

  private final int privateFinalInt;
  private final String privateFinalString;
  private final Date privateFinalDate;

  public Level3(final int privateFinalInt, final String privateFinalString, final Date privateFinalDate) {
    super(7, Arrays.asList("g", "h"), Collections.emptyMap());
    this.privateFinalInt = privateFinalInt;
    this.privateFinalString = privateFinalString;
    this.privateFinalDate = privateFinalDate;
  }

  public final int getPrivateFinalInt() {
    return privateFinalInt;
  }

  public final String getPrivateFinalString() {
    return privateFinalString;
  }

  public final Date getPrivateFinalDate() {
    return privateFinalDate;
  }

  @Override
  final String getAbstractString()
  {
    throw new UnsupportedOperationException("not yet implemented");
  }

  @Override
  protected final Date getProtectedAbstractDate()
  {
    throw new UnsupportedOperationException("not yet implemented");
  }
}

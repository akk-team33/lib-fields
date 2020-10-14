package de.team33.test.reflect.common;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


abstract class Level2 extends Level1 {

  private final long privateFinalLong;
  private final List<String> privateFinalListOfString;
  private final Map<String, Integer> privateFinalMapOfStringToInteger;

  public Level2(final long privateFinalLong,
                final List<String> privateFinalListOfString,
                final Map<String, Integer> privateFinalMapOfStringToInteger)
  {
    super(true, new HashSet<>(0));
    this.privateFinalLong = privateFinalLong;
    this.privateFinalListOfString = privateFinalListOfString;
    this.privateFinalMapOfStringToInteger = privateFinalMapOfStringToInteger;
  }

  abstract String getAbstractString();

  protected abstract Date getProtectedAbstractDate();

  public final long getPrivateFinalLong()
  {
    return privateFinalLong;
  }

  public final List<String> getPrivateFinalListOfString()
  {
    return privateFinalListOfString;
  }

  public final Map<String, Integer> getPrivateFinalMapOfStringToInteger()
  {
    return privateFinalMapOfStringToInteger;
  }
}

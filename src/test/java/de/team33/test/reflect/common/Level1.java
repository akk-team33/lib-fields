package de.team33.test.reflect.common;

import java.util.Set;


public class Level1
{
  private final Boolean privateFinalBoolean;
  private final Set<Integer> privateFinalSetOfInteger;

  public Level1(final Boolean privateFinalBoolean, final Set<Integer> privateFinalSetOfInteger)
  {
    this.privateFinalBoolean = privateFinalBoolean;
    this.privateFinalSetOfInteger = privateFinalSetOfInteger;
  }

  public Boolean getPrivateFinalBoolean()
  {
    return privateFinalBoolean;
  }

  public Set<Integer> getPrivateFinalSetOfInteger()
  {
    return privateFinalSetOfInteger;
  }
}

package com.anadi.prabhupadalectures.android.base.navigation;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.annotation.processing.Generated;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class RouterImpl_Factory implements Factory<RouterImpl> {
  @Override
  public RouterImpl get() {
    return newInstance();
  }

  public static RouterImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RouterImpl newInstance() {
    return new RouterImpl();
  }

  private static final class InstanceHolder {
    private static final RouterImpl_Factory INSTANCE = new RouterImpl_Factory();
  }
}

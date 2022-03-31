package com.anadi.prabhupadalectures.android.base.navigation;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class RouterViewModel_Factory implements Factory<RouterViewModel> {
  private final Provider<Router> routerProvider;

  public RouterViewModel_Factory(Provider<Router> routerProvider) {
    this.routerProvider = routerProvider;
  }

  @Override
  public RouterViewModel get() {
    return newInstance(routerProvider.get());
  }

  public static RouterViewModel_Factory create(Provider<Router> routerProvider) {
    return new RouterViewModel_Factory(routerProvider);
  }

  public static RouterViewModel newInstance(Router router) {
    return new RouterViewModel(router);
  }
}

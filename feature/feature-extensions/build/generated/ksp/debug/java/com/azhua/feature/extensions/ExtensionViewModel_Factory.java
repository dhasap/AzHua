package com.azhua.feature.extensions;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ExtensionViewModel_Factory implements Factory<ExtensionViewModel> {
  @Override
  public ExtensionViewModel get() {
    return newInstance();
  }

  public static ExtensionViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ExtensionViewModel newInstance() {
    return new ExtensionViewModel();
  }

  private static final class InstanceHolder {
    private static final ExtensionViewModel_Factory INSTANCE = new ExtensionViewModel_Factory();
  }
}

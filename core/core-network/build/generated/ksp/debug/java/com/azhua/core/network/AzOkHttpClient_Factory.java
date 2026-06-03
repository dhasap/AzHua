package com.azhua.core.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AzOkHttpClient_Factory implements Factory<AzOkHttpClient> {
  @Override
  public AzOkHttpClient get() {
    return newInstance();
  }

  public static AzOkHttpClient_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AzOkHttpClient newInstance() {
    return new AzOkHttpClient();
  }

  private static final class InstanceHolder {
    private static final AzOkHttpClient_Factory INSTANCE = new AzOkHttpClient_Factory();
  }
}

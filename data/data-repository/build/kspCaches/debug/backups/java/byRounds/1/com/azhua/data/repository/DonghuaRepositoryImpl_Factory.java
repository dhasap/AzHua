package com.azhua.data.repository;

import com.azhua.core.database.dao.DonghuaDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DonghuaRepositoryImpl_Factory implements Factory<DonghuaRepositoryImpl> {
  private final Provider<DonghuaDao> donghuaDaoProvider;

  public DonghuaRepositoryImpl_Factory(Provider<DonghuaDao> donghuaDaoProvider) {
    this.donghuaDaoProvider = donghuaDaoProvider;
  }

  @Override
  public DonghuaRepositoryImpl get() {
    return newInstance(donghuaDaoProvider.get());
  }

  public static DonghuaRepositoryImpl_Factory create(Provider<DonghuaDao> donghuaDaoProvider) {
    return new DonghuaRepositoryImpl_Factory(donghuaDaoProvider);
  }

  public static DonghuaRepositoryImpl newInstance(DonghuaDao donghuaDao) {
    return new DonghuaRepositoryImpl(donghuaDao);
  }
}

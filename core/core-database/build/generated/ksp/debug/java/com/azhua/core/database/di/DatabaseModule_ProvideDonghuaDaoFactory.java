package com.azhua.core.database.di;

import com.azhua.core.database.AzHuaDatabase;
import com.azhua.core.database.dao.DonghuaDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DatabaseModule_ProvideDonghuaDaoFactory implements Factory<DonghuaDao> {
  private final Provider<AzHuaDatabase> dbProvider;

  public DatabaseModule_ProvideDonghuaDaoFactory(Provider<AzHuaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public DonghuaDao get() {
    return provideDonghuaDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideDonghuaDaoFactory create(Provider<AzHuaDatabase> dbProvider) {
    return new DatabaseModule_ProvideDonghuaDaoFactory(dbProvider);
  }

  public static DonghuaDao provideDonghuaDao(AzHuaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDonghuaDao(db));
  }
}

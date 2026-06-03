package com.azhua.core.database.di;

import com.azhua.core.database.AzHuaDatabase;
import com.azhua.core.database.dao.EpisodeDao;
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
public final class DatabaseModule_ProvideEpisodeDaoFactory implements Factory<EpisodeDao> {
  private final Provider<AzHuaDatabase> dbProvider;

  public DatabaseModule_ProvideEpisodeDaoFactory(Provider<AzHuaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public EpisodeDao get() {
    return provideEpisodeDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideEpisodeDaoFactory create(Provider<AzHuaDatabase> dbProvider) {
    return new DatabaseModule_ProvideEpisodeDaoFactory(dbProvider);
  }

  public static EpisodeDao provideEpisodeDao(AzHuaDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEpisodeDao(db));
  }
}

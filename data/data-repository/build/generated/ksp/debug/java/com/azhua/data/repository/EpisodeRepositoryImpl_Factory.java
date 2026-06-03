package com.azhua.data.repository;

import com.azhua.core.database.dao.EpisodeDao;
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
public final class EpisodeRepositoryImpl_Factory implements Factory<EpisodeRepositoryImpl> {
  private final Provider<EpisodeDao> episodeDaoProvider;

  public EpisodeRepositoryImpl_Factory(Provider<EpisodeDao> episodeDaoProvider) {
    this.episodeDaoProvider = episodeDaoProvider;
  }

  @Override
  public EpisodeRepositoryImpl get() {
    return newInstance(episodeDaoProvider.get());
  }

  public static EpisodeRepositoryImpl_Factory create(Provider<EpisodeDao> episodeDaoProvider) {
    return new EpisodeRepositoryImpl_Factory(episodeDaoProvider);
  }

  public static EpisodeRepositoryImpl newInstance(EpisodeDao episodeDao) {
    return new EpisodeRepositoryImpl(episodeDao);
  }
}

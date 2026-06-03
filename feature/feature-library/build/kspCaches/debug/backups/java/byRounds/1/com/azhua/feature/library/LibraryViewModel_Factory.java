package com.azhua.feature.library;

import com.azhua.data.repository.DonghuaRepository;
import com.azhua.data.repository.EpisodeRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class LibraryViewModel_Factory implements Factory<LibraryViewModel> {
  private final Provider<DonghuaRepository> donghuaRepositoryProvider;

  private final Provider<EpisodeRepository> episodeRepositoryProvider;

  public LibraryViewModel_Factory(Provider<DonghuaRepository> donghuaRepositoryProvider,
      Provider<EpisodeRepository> episodeRepositoryProvider) {
    this.donghuaRepositoryProvider = donghuaRepositoryProvider;
    this.episodeRepositoryProvider = episodeRepositoryProvider;
  }

  @Override
  public LibraryViewModel get() {
    return newInstance(donghuaRepositoryProvider.get(), episodeRepositoryProvider.get());
  }

  public static LibraryViewModel_Factory create(
      Provider<DonghuaRepository> donghuaRepositoryProvider,
      Provider<EpisodeRepository> episodeRepositoryProvider) {
    return new LibraryViewModel_Factory(donghuaRepositoryProvider, episodeRepositoryProvider);
  }

  public static LibraryViewModel newInstance(DonghuaRepository donghuaRepository,
      EpisodeRepository episodeRepository) {
    return new LibraryViewModel(donghuaRepository, episodeRepository);
  }
}

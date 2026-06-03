package com.azhua.feature.detail;

import androidx.lifecycle.SavedStateHandle;
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
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<DonghuaRepository> donghuaRepositoryProvider;

  private final Provider<EpisodeRepository> episodeRepositoryProvider;

  public DetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<DonghuaRepository> donghuaRepositoryProvider,
      Provider<EpisodeRepository> episodeRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.donghuaRepositoryProvider = donghuaRepositoryProvider;
    this.episodeRepositoryProvider = episodeRepositoryProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), donghuaRepositoryProvider.get(), episodeRepositoryProvider.get());
  }

  public static DetailViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<DonghuaRepository> donghuaRepositoryProvider,
      Provider<EpisodeRepository> episodeRepositoryProvider) {
    return new DetailViewModel_Factory(savedStateHandleProvider, donghuaRepositoryProvider, episodeRepositoryProvider);
  }

  public static DetailViewModel newInstance(SavedStateHandle savedStateHandle,
      DonghuaRepository donghuaRepository, EpisodeRepository episodeRepository) {
    return new DetailViewModel(savedStateHandle, donghuaRepository, episodeRepository);
  }
}

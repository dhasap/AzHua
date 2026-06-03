package com.azhua.feature.player;

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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<DonghuaRepository> donghuaRepositoryProvider;

  private final Provider<EpisodeRepository> episodeRepositoryProvider;

  public PlayerViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<DonghuaRepository> donghuaRepositoryProvider,
      Provider<EpisodeRepository> episodeRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.donghuaRepositoryProvider = donghuaRepositoryProvider;
    this.episodeRepositoryProvider = episodeRepositoryProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(savedStateHandleProvider.get(), donghuaRepositoryProvider.get(), episodeRepositoryProvider.get());
  }

  public static PlayerViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<DonghuaRepository> donghuaRepositoryProvider,
      Provider<EpisodeRepository> episodeRepositoryProvider) {
    return new PlayerViewModel_Factory(savedStateHandleProvider, donghuaRepositoryProvider, episodeRepositoryProvider);
  }

  public static PlayerViewModel newInstance(SavedStateHandle savedStateHandle,
      DonghuaRepository donghuaRepository, EpisodeRepository episodeRepository) {
    return new PlayerViewModel(savedStateHandle, donghuaRepository, episodeRepository);
  }
}

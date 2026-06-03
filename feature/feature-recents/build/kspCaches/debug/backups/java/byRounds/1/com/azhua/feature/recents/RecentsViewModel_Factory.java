package com.azhua.feature.recents;

import com.azhua.data.repository.DonghuaRepository;
import com.azhua.data.repository.EpisodeRepository;
import com.azhua.data.repository.HistoryRepository;
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
public final class RecentsViewModel_Factory implements Factory<RecentsViewModel> {
  private final Provider<DonghuaRepository> donghuaRepositoryProvider;

  private final Provider<EpisodeRepository> episodeRepositoryProvider;

  private final Provider<HistoryRepository> historyRepositoryProvider;

  public RecentsViewModel_Factory(Provider<DonghuaRepository> donghuaRepositoryProvider,
      Provider<EpisodeRepository> episodeRepositoryProvider,
      Provider<HistoryRepository> historyRepositoryProvider) {
    this.donghuaRepositoryProvider = donghuaRepositoryProvider;
    this.episodeRepositoryProvider = episodeRepositoryProvider;
    this.historyRepositoryProvider = historyRepositoryProvider;
  }

  @Override
  public RecentsViewModel get() {
    return newInstance(donghuaRepositoryProvider.get(), episodeRepositoryProvider.get(), historyRepositoryProvider.get());
  }

  public static RecentsViewModel_Factory create(
      Provider<DonghuaRepository> donghuaRepositoryProvider,
      Provider<EpisodeRepository> episodeRepositoryProvider,
      Provider<HistoryRepository> historyRepositoryProvider) {
    return new RecentsViewModel_Factory(donghuaRepositoryProvider, episodeRepositoryProvider, historyRepositoryProvider);
  }

  public static RecentsViewModel newInstance(DonghuaRepository donghuaRepository,
      EpisodeRepository episodeRepository, HistoryRepository historyRepository) {
    return new RecentsViewModel(donghuaRepository, episodeRepository, historyRepository);
  }
}

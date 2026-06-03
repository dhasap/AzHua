package com.azhua.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.azhua.core.database.dao.CategoryDao;
import com.azhua.core.database.dao.CategoryDao_Impl;
import com.azhua.core.database.dao.DonghuaDao;
import com.azhua.core.database.dao.DonghuaDao_Impl;
import com.azhua.core.database.dao.DownloadDao;
import com.azhua.core.database.dao.DownloadDao_Impl;
import com.azhua.core.database.dao.EpisodeDao;
import com.azhua.core.database.dao.EpisodeDao_Impl;
import com.azhua.core.database.dao.HistoryDao;
import com.azhua.core.database.dao.HistoryDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AzHuaDatabase_Impl extends AzHuaDatabase {
  private volatile DonghuaDao _donghuaDao;

  private volatile EpisodeDao _episodeDao;

  private volatile CategoryDao _categoryDao;

  private volatile HistoryDao _historyDao;

  private volatile DownloadDao _downloadDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `donghua_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `source_id` TEXT NOT NULL, `source_url` TEXT NOT NULL, `title` TEXT NOT NULL, `title_alt` TEXT, `cover_url` TEXT, `synopsis` TEXT, `genres` TEXT NOT NULL, `status` TEXT NOT NULL, `studio` TEXT, `year` INTEGER, `rating` REAL NOT NULL, `total_episodes` INTEGER NOT NULL, `in_library` INTEGER NOT NULL, `favorite_order` INTEGER NOT NULL, `date_added` INTEGER NOT NULL, `last_updated` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `episode_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `donghua_id` INTEGER NOT NULL, `source_episode_id` TEXT NOT NULL, `episode_number` REAL NOT NULL, `title` TEXT, `thumbnail_url` TEXT, `duration_ms` INTEGER NOT NULL, `date_upload` INTEGER NOT NULL, `last_watch_ms` INTEGER NOT NULL, `is_watched` INTEGER NOT NULL, `is_downloaded` INTEGER NOT NULL, `download_path` TEXT, FOREIGN KEY(`donghua_id`) REFERENCES `donghua_table`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_episode_table_donghua_id` ON `episode_table` (`donghua_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `category_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `sort_order` INTEGER NOT NULL, `is_default` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `donghua_category_table` (`donghua_id` INTEGER NOT NULL, `category_id` INTEGER NOT NULL, PRIMARY KEY(`donghua_id`, `category_id`), FOREIGN KEY(`donghua_id`) REFERENCES `donghua_table`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`category_id`) REFERENCES `category_table`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `history_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `donghua_id` INTEGER NOT NULL, `episode_id` INTEGER NOT NULL, `watch_at` INTEGER NOT NULL, `duration_ms` INTEGER NOT NULL, FOREIGN KEY(`donghua_id`) REFERENCES `donghua_table`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`episode_id`) REFERENCES `episode_table`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_history_table_donghua_id` ON `history_table` (`donghua_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_history_table_episode_id` ON `history_table` (`episode_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_history_table_watch_at` ON `history_table` (`watch_at`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `download_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `episode_id` INTEGER NOT NULL, `status` TEXT NOT NULL, `progress` REAL NOT NULL, `file_path` TEXT, `file_size` INTEGER NOT NULL, `error_msg` TEXT, `created_at` INTEGER NOT NULL, FOREIGN KEY(`episode_id`) REFERENCES `episode_table`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_download_table_episode_id` ON `download_table` (`episode_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bc09711f1dc294d9633be7f2a003d6a9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `donghua_table`");
        db.execSQL("DROP TABLE IF EXISTS `episode_table`");
        db.execSQL("DROP TABLE IF EXISTS `category_table`");
        db.execSQL("DROP TABLE IF EXISTS `donghua_category_table`");
        db.execSQL("DROP TABLE IF EXISTS `history_table`");
        db.execSQL("DROP TABLE IF EXISTS `download_table`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDonghuaTable = new HashMap<String, TableInfo.Column>(17);
        _columnsDonghuaTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("source_id", new TableInfo.Column("source_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("source_url", new TableInfo.Column("source_url", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("title_alt", new TableInfo.Column("title_alt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("cover_url", new TableInfo.Column("cover_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("synopsis", new TableInfo.Column("synopsis", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("genres", new TableInfo.Column("genres", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("studio", new TableInfo.Column("studio", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("year", new TableInfo.Column("year", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("rating", new TableInfo.Column("rating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("total_episodes", new TableInfo.Column("total_episodes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("in_library", new TableInfo.Column("in_library", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("favorite_order", new TableInfo.Column("favorite_order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("date_added", new TableInfo.Column("date_added", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaTable.put("last_updated", new TableInfo.Column("last_updated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDonghuaTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDonghuaTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDonghuaTable = new TableInfo("donghua_table", _columnsDonghuaTable, _foreignKeysDonghuaTable, _indicesDonghuaTable);
        final TableInfo _existingDonghuaTable = TableInfo.read(db, "donghua_table");
        if (!_infoDonghuaTable.equals(_existingDonghuaTable)) {
          return new RoomOpenHelper.ValidationResult(false, "donghua_table(com.azhua.core.database.entity.DonghuaEntity).\n"
                  + " Expected:\n" + _infoDonghuaTable + "\n"
                  + " Found:\n" + _existingDonghuaTable);
        }
        final HashMap<String, TableInfo.Column> _columnsEpisodeTable = new HashMap<String, TableInfo.Column>(12);
        _columnsEpisodeTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("donghua_id", new TableInfo.Column("donghua_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("source_episode_id", new TableInfo.Column("source_episode_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("episode_number", new TableInfo.Column("episode_number", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("thumbnail_url", new TableInfo.Column("thumbnail_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("duration_ms", new TableInfo.Column("duration_ms", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("date_upload", new TableInfo.Column("date_upload", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("last_watch_ms", new TableInfo.Column("last_watch_ms", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("is_watched", new TableInfo.Column("is_watched", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("is_downloaded", new TableInfo.Column("is_downloaded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEpisodeTable.put("download_path", new TableInfo.Column("download_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEpisodeTable = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysEpisodeTable.add(new TableInfo.ForeignKey("donghua_table", "CASCADE", "NO ACTION", Arrays.asList("donghua_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesEpisodeTable = new HashSet<TableInfo.Index>(1);
        _indicesEpisodeTable.add(new TableInfo.Index("index_episode_table_donghua_id", false, Arrays.asList("donghua_id"), Arrays.asList("ASC")));
        final TableInfo _infoEpisodeTable = new TableInfo("episode_table", _columnsEpisodeTable, _foreignKeysEpisodeTable, _indicesEpisodeTable);
        final TableInfo _existingEpisodeTable = TableInfo.read(db, "episode_table");
        if (!_infoEpisodeTable.equals(_existingEpisodeTable)) {
          return new RoomOpenHelper.ValidationResult(false, "episode_table(com.azhua.core.database.entity.EpisodeEntity).\n"
                  + " Expected:\n" + _infoEpisodeTable + "\n"
                  + " Found:\n" + _existingEpisodeTable);
        }
        final HashMap<String, TableInfo.Column> _columnsCategoryTable = new HashMap<String, TableInfo.Column>(4);
        _columnsCategoryTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryTable.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryTable.put("sort_order", new TableInfo.Column("sort_order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategoryTable.put("is_default", new TableInfo.Column("is_default", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategoryTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategoryTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategoryTable = new TableInfo("category_table", _columnsCategoryTable, _foreignKeysCategoryTable, _indicesCategoryTable);
        final TableInfo _existingCategoryTable = TableInfo.read(db, "category_table");
        if (!_infoCategoryTable.equals(_existingCategoryTable)) {
          return new RoomOpenHelper.ValidationResult(false, "category_table(com.azhua.core.database.entity.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategoryTable + "\n"
                  + " Found:\n" + _existingCategoryTable);
        }
        final HashMap<String, TableInfo.Column> _columnsDonghuaCategoryTable = new HashMap<String, TableInfo.Column>(2);
        _columnsDonghuaCategoryTable.put("donghua_id", new TableInfo.Column("donghua_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonghuaCategoryTable.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDonghuaCategoryTable = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysDonghuaCategoryTable.add(new TableInfo.ForeignKey("donghua_table", "CASCADE", "NO ACTION", Arrays.asList("donghua_id"), Arrays.asList("id")));
        _foreignKeysDonghuaCategoryTable.add(new TableInfo.ForeignKey("category_table", "CASCADE", "NO ACTION", Arrays.asList("category_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDonghuaCategoryTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDonghuaCategoryTable = new TableInfo("donghua_category_table", _columnsDonghuaCategoryTable, _foreignKeysDonghuaCategoryTable, _indicesDonghuaCategoryTable);
        final TableInfo _existingDonghuaCategoryTable = TableInfo.read(db, "donghua_category_table");
        if (!_infoDonghuaCategoryTable.equals(_existingDonghuaCategoryTable)) {
          return new RoomOpenHelper.ValidationResult(false, "donghua_category_table(com.azhua.core.database.entity.DonghuaCategoryEntity).\n"
                  + " Expected:\n" + _infoDonghuaCategoryTable + "\n"
                  + " Found:\n" + _existingDonghuaCategoryTable);
        }
        final HashMap<String, TableInfo.Column> _columnsHistoryTable = new HashMap<String, TableInfo.Column>(5);
        _columnsHistoryTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryTable.put("donghua_id", new TableInfo.Column("donghua_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryTable.put("episode_id", new TableInfo.Column("episode_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryTable.put("watch_at", new TableInfo.Column("watch_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryTable.put("duration_ms", new TableInfo.Column("duration_ms", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHistoryTable = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysHistoryTable.add(new TableInfo.ForeignKey("donghua_table", "CASCADE", "NO ACTION", Arrays.asList("donghua_id"), Arrays.asList("id")));
        _foreignKeysHistoryTable.add(new TableInfo.ForeignKey("episode_table", "CASCADE", "NO ACTION", Arrays.asList("episode_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesHistoryTable = new HashSet<TableInfo.Index>(3);
        _indicesHistoryTable.add(new TableInfo.Index("index_history_table_donghua_id", false, Arrays.asList("donghua_id"), Arrays.asList("ASC")));
        _indicesHistoryTable.add(new TableInfo.Index("index_history_table_episode_id", false, Arrays.asList("episode_id"), Arrays.asList("ASC")));
        _indicesHistoryTable.add(new TableInfo.Index("index_history_table_watch_at", false, Arrays.asList("watch_at"), Arrays.asList("ASC")));
        final TableInfo _infoHistoryTable = new TableInfo("history_table", _columnsHistoryTable, _foreignKeysHistoryTable, _indicesHistoryTable);
        final TableInfo _existingHistoryTable = TableInfo.read(db, "history_table");
        if (!_infoHistoryTable.equals(_existingHistoryTable)) {
          return new RoomOpenHelper.ValidationResult(false, "history_table(com.azhua.core.database.entity.HistoryEntity).\n"
                  + " Expected:\n" + _infoHistoryTable + "\n"
                  + " Found:\n" + _existingHistoryTable);
        }
        final HashMap<String, TableInfo.Column> _columnsDownloadTable = new HashMap<String, TableInfo.Column>(8);
        _columnsDownloadTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadTable.put("episode_id", new TableInfo.Column("episode_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadTable.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadTable.put("progress", new TableInfo.Column("progress", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadTable.put("file_path", new TableInfo.Column("file_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadTable.put("file_size", new TableInfo.Column("file_size", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadTable.put("error_msg", new TableInfo.Column("error_msg", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadTable.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDownloadTable = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysDownloadTable.add(new TableInfo.ForeignKey("episode_table", "CASCADE", "NO ACTION", Arrays.asList("episode_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDownloadTable = new HashSet<TableInfo.Index>(1);
        _indicesDownloadTable.add(new TableInfo.Index("index_download_table_episode_id", false, Arrays.asList("episode_id"), Arrays.asList("ASC")));
        final TableInfo _infoDownloadTable = new TableInfo("download_table", _columnsDownloadTable, _foreignKeysDownloadTable, _indicesDownloadTable);
        final TableInfo _existingDownloadTable = TableInfo.read(db, "download_table");
        if (!_infoDownloadTable.equals(_existingDownloadTable)) {
          return new RoomOpenHelper.ValidationResult(false, "download_table(com.azhua.core.database.entity.DownloadEntity).\n"
                  + " Expected:\n" + _infoDownloadTable + "\n"
                  + " Found:\n" + _existingDownloadTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bc09711f1dc294d9633be7f2a003d6a9", "11f3bb0603610ffd50831b6d6b61661a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "donghua_table","episode_table","category_table","donghua_category_table","history_table","download_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `donghua_table`");
      _db.execSQL("DELETE FROM `episode_table`");
      _db.execSQL("DELETE FROM `category_table`");
      _db.execSQL("DELETE FROM `donghua_category_table`");
      _db.execSQL("DELETE FROM `history_table`");
      _db.execSQL("DELETE FROM `download_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DonghuaDao.class, DonghuaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EpisodeDao.class, EpisodeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HistoryDao.class, HistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DownloadDao.class, DownloadDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DonghuaDao donghuaDao() {
    if (_donghuaDao != null) {
      return _donghuaDao;
    } else {
      synchronized(this) {
        if(_donghuaDao == null) {
          _donghuaDao = new DonghuaDao_Impl(this);
        }
        return _donghuaDao;
      }
    }
  }

  @Override
  public EpisodeDao episodeDao() {
    if (_episodeDao != null) {
      return _episodeDao;
    } else {
      synchronized(this) {
        if(_episodeDao == null) {
          _episodeDao = new EpisodeDao_Impl(this);
        }
        return _episodeDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public HistoryDao historyDao() {
    if (_historyDao != null) {
      return _historyDao;
    } else {
      synchronized(this) {
        if(_historyDao == null) {
          _historyDao = new HistoryDao_Impl(this);
        }
        return _historyDao;
      }
    }
  }

  @Override
  public DownloadDao downloadDao() {
    if (_downloadDao != null) {
      return _downloadDao;
    } else {
      synchronized(this) {
        if(_downloadDao == null) {
          _downloadDao = new DownloadDao_Impl(this);
        }
        return _downloadDao;
      }
    }
  }
}

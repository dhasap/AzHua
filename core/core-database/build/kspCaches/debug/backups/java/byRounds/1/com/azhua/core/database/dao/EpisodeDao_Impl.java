package com.azhua.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.azhua.core.database.entity.EpisodeEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EpisodeDao_Impl implements EpisodeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EpisodeEntity> __insertionAdapterOfEpisodeEntity;

  private final EntityDeletionOrUpdateAdapter<EpisodeEntity> __deletionAdapterOfEpisodeEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateWatchProgress;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDownloadStatus;

  public EpisodeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEpisodeEntity = new EntityInsertionAdapter<EpisodeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `episode_table` (`id`,`donghua_id`,`source_episode_id`,`episode_number`,`title`,`thumbnail_url`,`duration_ms`,`date_upload`,`last_watch_ms`,`is_watched`,`is_downloaded`,`download_path`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EpisodeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDonghuaId());
        statement.bindString(3, entity.getSourceEpisodeId());
        statement.bindDouble(4, entity.getEpisodeNumber());
        if (entity.getTitle() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTitle());
        }
        if (entity.getThumbnailUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getThumbnailUrl());
        }
        statement.bindLong(7, entity.getDurationMs());
        statement.bindLong(8, entity.getDateUpload());
        statement.bindLong(9, entity.getLastWatchMs());
        final int _tmp = entity.isWatched() ? 1 : 0;
        statement.bindLong(10, _tmp);
        final int _tmp_1 = entity.isDownloaded() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        if (entity.getDownloadPath() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getDownloadPath());
        }
      }
    };
    this.__deletionAdapterOfEpisodeEntity = new EntityDeletionOrUpdateAdapter<EpisodeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `episode_table` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EpisodeEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateWatchProgress = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE episode_table SET last_watch_ms = ?, is_watched = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateDownloadStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE episode_table SET is_downloaded = ?, download_path = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrUpdate(final EpisodeEntity episode,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfEpisodeEntity.insertAndReturnId(episode);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<EpisodeEntity> episodes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEpisodeEntity.insert(episodes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final EpisodeEntity episode, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfEpisodeEntity.handle(episode);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWatchProgress(final long episodeId, final long positionMs,
      final boolean isWatched, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateWatchProgress.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, positionMs);
        _argIndex = 2;
        final int _tmp = isWatched ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, episodeId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateWatchProgress.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDownloadStatus(final long episodeId, final boolean isDownloaded,
      final String path, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDownloadStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isDownloaded ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (path == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, path);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, episodeId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateDownloadStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EpisodeEntity>> getEpisodesByDonghua(final long donghuaId) {
    final String _sql = "SELECT * FROM episode_table WHERE donghua_id = ? ORDER BY episode_number ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, donghuaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"episode_table"}, new Callable<List<EpisodeEntity>>() {
      @Override
      @NonNull
      public List<EpisodeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDonghuaId = CursorUtil.getColumnIndexOrThrow(_cursor, "donghua_id");
          final int _cursorIndexOfSourceEpisodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_episode_id");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episode_number");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_url");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfDateUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "date_upload");
          final int _cursorIndexOfLastWatchMs = CursorUtil.getColumnIndexOrThrow(_cursor, "last_watch_ms");
          final int _cursorIndexOfIsWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "is_watched");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfDownloadPath = CursorUtil.getColumnIndexOrThrow(_cursor, "download_path");
          final List<EpisodeEntity> _result = new ArrayList<EpisodeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EpisodeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDonghuaId;
            _tmpDonghuaId = _cursor.getLong(_cursorIndexOfDonghuaId);
            final String _tmpSourceEpisodeId;
            _tmpSourceEpisodeId = _cursor.getString(_cursorIndexOfSourceEpisodeId);
            final float _tmpEpisodeNumber;
            _tmpEpisodeNumber = _cursor.getFloat(_cursorIndexOfEpisodeNumber);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final long _tmpDateUpload;
            _tmpDateUpload = _cursor.getLong(_cursorIndexOfDateUpload);
            final long _tmpLastWatchMs;
            _tmpLastWatchMs = _cursor.getLong(_cursorIndexOfLastWatchMs);
            final boolean _tmpIsWatched;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWatched);
            _tmpIsWatched = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadPath;
            if (_cursor.isNull(_cursorIndexOfDownloadPath)) {
              _tmpDownloadPath = null;
            } else {
              _tmpDownloadPath = _cursor.getString(_cursorIndexOfDownloadPath);
            }
            _item = new EpisodeEntity(_tmpId,_tmpDonghuaId,_tmpSourceEpisodeId,_tmpEpisodeNumber,_tmpTitle,_tmpThumbnailUrl,_tmpDurationMs,_tmpDateUpload,_tmpLastWatchMs,_tmpIsWatched,_tmpIsDownloaded,_tmpDownloadPath);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getEpisodeById(final long id,
      final Continuation<? super EpisodeEntity> $completion) {
    final String _sql = "SELECT * FROM episode_table WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EpisodeEntity>() {
      @Override
      @Nullable
      public EpisodeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDonghuaId = CursorUtil.getColumnIndexOrThrow(_cursor, "donghua_id");
          final int _cursorIndexOfSourceEpisodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_episode_id");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episode_number");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_url");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfDateUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "date_upload");
          final int _cursorIndexOfLastWatchMs = CursorUtil.getColumnIndexOrThrow(_cursor, "last_watch_ms");
          final int _cursorIndexOfIsWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "is_watched");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfDownloadPath = CursorUtil.getColumnIndexOrThrow(_cursor, "download_path");
          final EpisodeEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDonghuaId;
            _tmpDonghuaId = _cursor.getLong(_cursorIndexOfDonghuaId);
            final String _tmpSourceEpisodeId;
            _tmpSourceEpisodeId = _cursor.getString(_cursorIndexOfSourceEpisodeId);
            final float _tmpEpisodeNumber;
            _tmpEpisodeNumber = _cursor.getFloat(_cursorIndexOfEpisodeNumber);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final long _tmpDateUpload;
            _tmpDateUpload = _cursor.getLong(_cursorIndexOfDateUpload);
            final long _tmpLastWatchMs;
            _tmpLastWatchMs = _cursor.getLong(_cursorIndexOfLastWatchMs);
            final boolean _tmpIsWatched;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWatched);
            _tmpIsWatched = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadPath;
            if (_cursor.isNull(_cursorIndexOfDownloadPath)) {
              _tmpDownloadPath = null;
            } else {
              _tmpDownloadPath = _cursor.getString(_cursorIndexOfDownloadPath);
            }
            _result = new EpisodeEntity(_tmpId,_tmpDonghuaId,_tmpSourceEpisodeId,_tmpEpisodeNumber,_tmpTitle,_tmpThumbnailUrl,_tmpDurationMs,_tmpDateUpload,_tmpLastWatchMs,_tmpIsWatched,_tmpIsDownloaded,_tmpDownloadPath);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<EpisodeEntity> getEpisodeByIdFlow(final long id) {
    final String _sql = "SELECT * FROM episode_table WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"episode_table"}, new Callable<EpisodeEntity>() {
      @Override
      @Nullable
      public EpisodeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDonghuaId = CursorUtil.getColumnIndexOrThrow(_cursor, "donghua_id");
          final int _cursorIndexOfSourceEpisodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_episode_id");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episode_number");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_url");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfDateUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "date_upload");
          final int _cursorIndexOfLastWatchMs = CursorUtil.getColumnIndexOrThrow(_cursor, "last_watch_ms");
          final int _cursorIndexOfIsWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "is_watched");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfDownloadPath = CursorUtil.getColumnIndexOrThrow(_cursor, "download_path");
          final EpisodeEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDonghuaId;
            _tmpDonghuaId = _cursor.getLong(_cursorIndexOfDonghuaId);
            final String _tmpSourceEpisodeId;
            _tmpSourceEpisodeId = _cursor.getString(_cursorIndexOfSourceEpisodeId);
            final float _tmpEpisodeNumber;
            _tmpEpisodeNumber = _cursor.getFloat(_cursorIndexOfEpisodeNumber);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final long _tmpDateUpload;
            _tmpDateUpload = _cursor.getLong(_cursorIndexOfDateUpload);
            final long _tmpLastWatchMs;
            _tmpLastWatchMs = _cursor.getLong(_cursorIndexOfLastWatchMs);
            final boolean _tmpIsWatched;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWatched);
            _tmpIsWatched = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadPath;
            if (_cursor.isNull(_cursorIndexOfDownloadPath)) {
              _tmpDownloadPath = null;
            } else {
              _tmpDownloadPath = _cursor.getString(_cursorIndexOfDownloadPath);
            }
            _result = new EpisodeEntity(_tmpId,_tmpDonghuaId,_tmpSourceEpisodeId,_tmpEpisodeNumber,_tmpTitle,_tmpThumbnailUrl,_tmpDurationMs,_tmpDateUpload,_tmpLastWatchMs,_tmpIsWatched,_tmpIsDownloaded,_tmpDownloadPath);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<EpisodeEntity>> getNewEpisodesInLibrary(final int limit) {
    final String _sql = "\n"
            + "        SELECT e.* FROM episode_table e\n"
            + "        INNER JOIN donghua_table d ON e.donghua_id = d.id\n"
            + "        WHERE d.in_library = 1 AND e.is_watched = 0\n"
            + "        ORDER BY e.date_upload DESC\n"
            + "        LIMIT ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"episode_table",
        "donghua_table"}, new Callable<List<EpisodeEntity>>() {
      @Override
      @NonNull
      public List<EpisodeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDonghuaId = CursorUtil.getColumnIndexOrThrow(_cursor, "donghua_id");
          final int _cursorIndexOfSourceEpisodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_episode_id");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episode_number");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_url");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfDateUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "date_upload");
          final int _cursorIndexOfLastWatchMs = CursorUtil.getColumnIndexOrThrow(_cursor, "last_watch_ms");
          final int _cursorIndexOfIsWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "is_watched");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfDownloadPath = CursorUtil.getColumnIndexOrThrow(_cursor, "download_path");
          final List<EpisodeEntity> _result = new ArrayList<EpisodeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EpisodeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDonghuaId;
            _tmpDonghuaId = _cursor.getLong(_cursorIndexOfDonghuaId);
            final String _tmpSourceEpisodeId;
            _tmpSourceEpisodeId = _cursor.getString(_cursorIndexOfSourceEpisodeId);
            final float _tmpEpisodeNumber;
            _tmpEpisodeNumber = _cursor.getFloat(_cursorIndexOfEpisodeNumber);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final long _tmpDateUpload;
            _tmpDateUpload = _cursor.getLong(_cursorIndexOfDateUpload);
            final long _tmpLastWatchMs;
            _tmpLastWatchMs = _cursor.getLong(_cursorIndexOfLastWatchMs);
            final boolean _tmpIsWatched;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWatched);
            _tmpIsWatched = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadPath;
            if (_cursor.isNull(_cursorIndexOfDownloadPath)) {
              _tmpDownloadPath = null;
            } else {
              _tmpDownloadPath = _cursor.getString(_cursorIndexOfDownloadPath);
            }
            _item = new EpisodeEntity(_tmpId,_tmpDonghuaId,_tmpSourceEpisodeId,_tmpEpisodeNumber,_tmpTitle,_tmpThumbnailUrl,_tmpDurationMs,_tmpDateUpload,_tmpLastWatchMs,_tmpIsWatched,_tmpIsDownloaded,_tmpDownloadPath);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<EpisodeEntity>> getContinueWatching() {
    final String _sql = "\n"
            + "        SELECT e.* FROM episode_table e\n"
            + "        INNER JOIN donghua_table d ON e.donghua_id = d.id\n"
            + "        WHERE d.in_library = 1 AND e.last_watch_ms > 0 AND e.is_watched = 0\n"
            + "        ORDER BY e.last_watch_ms DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"episode_table",
        "donghua_table"}, new Callable<List<EpisodeEntity>>() {
      @Override
      @NonNull
      public List<EpisodeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDonghuaId = CursorUtil.getColumnIndexOrThrow(_cursor, "donghua_id");
          final int _cursorIndexOfSourceEpisodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_episode_id");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episode_number");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_url");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_ms");
          final int _cursorIndexOfDateUpload = CursorUtil.getColumnIndexOrThrow(_cursor, "date_upload");
          final int _cursorIndexOfLastWatchMs = CursorUtil.getColumnIndexOrThrow(_cursor, "last_watch_ms");
          final int _cursorIndexOfIsWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "is_watched");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "is_downloaded");
          final int _cursorIndexOfDownloadPath = CursorUtil.getColumnIndexOrThrow(_cursor, "download_path");
          final List<EpisodeEntity> _result = new ArrayList<EpisodeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EpisodeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDonghuaId;
            _tmpDonghuaId = _cursor.getLong(_cursorIndexOfDonghuaId);
            final String _tmpSourceEpisodeId;
            _tmpSourceEpisodeId = _cursor.getString(_cursorIndexOfSourceEpisodeId);
            final float _tmpEpisodeNumber;
            _tmpEpisodeNumber = _cursor.getFloat(_cursorIndexOfEpisodeNumber);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final long _tmpDateUpload;
            _tmpDateUpload = _cursor.getLong(_cursorIndexOfDateUpload);
            final long _tmpLastWatchMs;
            _tmpLastWatchMs = _cursor.getLong(_cursorIndexOfLastWatchMs);
            final boolean _tmpIsWatched;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWatched);
            _tmpIsWatched = _tmp != 0;
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final String _tmpDownloadPath;
            if (_cursor.isNull(_cursorIndexOfDownloadPath)) {
              _tmpDownloadPath = null;
            } else {
              _tmpDownloadPath = _cursor.getString(_cursorIndexOfDownloadPath);
            }
            _item = new EpisodeEntity(_tmpId,_tmpDonghuaId,_tmpSourceEpisodeId,_tmpEpisodeNumber,_tmpTitle,_tmpThumbnailUrl,_tmpDurationMs,_tmpDateUpload,_tmpLastWatchMs,_tmpIsWatched,_tmpIsDownloaded,_tmpDownloadPath);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

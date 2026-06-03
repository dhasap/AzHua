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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.azhua.core.database.entity.DonghuaEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class DonghuaDao_Impl implements DonghuaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DonghuaEntity> __insertionAdapterOfDonghuaEntity;

  private final EntityDeletionOrUpdateAdapter<DonghuaEntity> __deletionAdapterOfDonghuaEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLibraryStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastUpdated;

  public DonghuaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDonghuaEntity = new EntityInsertionAdapter<DonghuaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `donghua_table` (`id`,`source_id`,`source_url`,`title`,`title_alt`,`cover_url`,`synopsis`,`genres`,`status`,`studio`,`year`,`rating`,`total_episodes`,`in_library`,`favorite_order`,`date_added`,`last_updated`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DonghuaEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSourceId());
        statement.bindString(3, entity.getSourceUrl());
        statement.bindString(4, entity.getTitle());
        if (entity.getTitleAlt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTitleAlt());
        }
        if (entity.getCoverUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCoverUrl());
        }
        if (entity.getSynopsis() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getSynopsis());
        }
        statement.bindString(8, entity.getGenres());
        statement.bindString(9, entity.getStatus());
        if (entity.getStudio() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getStudio());
        }
        if (entity.getYear() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getYear());
        }
        statement.bindDouble(12, entity.getRating());
        statement.bindLong(13, entity.getTotalEpisodes());
        final int _tmp = entity.getInLibrary() ? 1 : 0;
        statement.bindLong(14, _tmp);
        statement.bindLong(15, entity.getFavoriteOrder());
        statement.bindLong(16, entity.getDateAdded());
        statement.bindLong(17, entity.getLastUpdated());
      }
    };
    this.__deletionAdapterOfDonghuaEntity = new EntityDeletionOrUpdateAdapter<DonghuaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `donghua_table` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DonghuaEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateLibraryStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE donghua_table SET in_library = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastUpdated = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE donghua_table SET last_updated = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrUpdate(final DonghuaEntity donghua,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDonghuaEntity.insertAndReturnId(donghua);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final DonghuaEntity donghua, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfDonghuaEntity.handle(donghua);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLibraryStatus(final long id, final boolean inLibrary,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLibraryStatus.acquire();
        int _argIndex = 1;
        final int _tmp = inLibrary ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfUpdateLibraryStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastUpdated(final long id, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastUpdated.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfUpdateLastUpdated.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DonghuaEntity>> getLibraryDonghua() {
    final String _sql = "SELECT * FROM donghua_table WHERE in_library = 1 ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"donghua_table"}, new Callable<List<DonghuaEntity>>() {
      @Override
      @NonNull
      public List<DonghuaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_id");
          final int _cursorIndexOfSourceUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "source_url");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTitleAlt = CursorUtil.getColumnIndexOrThrow(_cursor, "title_alt");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "cover_url");
          final int _cursorIndexOfSynopsis = CursorUtil.getColumnIndexOrThrow(_cursor, "synopsis");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStudio = CursorUtil.getColumnIndexOrThrow(_cursor, "studio");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfTotalEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "total_episodes");
          final int _cursorIndexOfInLibrary = CursorUtil.getColumnIndexOrThrow(_cursor, "in_library");
          final int _cursorIndexOfFavoriteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite_order");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "date_added");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final List<DonghuaEntity> _result = new ArrayList<DonghuaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DonghuaEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSourceId;
            _tmpSourceId = _cursor.getString(_cursorIndexOfSourceId);
            final String _tmpSourceUrl;
            _tmpSourceUrl = _cursor.getString(_cursorIndexOfSourceUrl);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpTitleAlt;
            if (_cursor.isNull(_cursorIndexOfTitleAlt)) {
              _tmpTitleAlt = null;
            } else {
              _tmpTitleAlt = _cursor.getString(_cursorIndexOfTitleAlt);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final String _tmpSynopsis;
            if (_cursor.isNull(_cursorIndexOfSynopsis)) {
              _tmpSynopsis = null;
            } else {
              _tmpSynopsis = _cursor.getString(_cursorIndexOfSynopsis);
            }
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpStudio;
            if (_cursor.isNull(_cursorIndexOfStudio)) {
              _tmpStudio = null;
            } else {
              _tmpStudio = _cursor.getString(_cursorIndexOfStudio);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final float _tmpRating;
            _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
            final int _tmpTotalEpisodes;
            _tmpTotalEpisodes = _cursor.getInt(_cursorIndexOfTotalEpisodes);
            final boolean _tmpInLibrary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfInLibrary);
            _tmpInLibrary = _tmp != 0;
            final int _tmpFavoriteOrder;
            _tmpFavoriteOrder = _cursor.getInt(_cursorIndexOfFavoriteOrder);
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new DonghuaEntity(_tmpId,_tmpSourceId,_tmpSourceUrl,_tmpTitle,_tmpTitleAlt,_tmpCoverUrl,_tmpSynopsis,_tmpGenres,_tmpStatus,_tmpStudio,_tmpYear,_tmpRating,_tmpTotalEpisodes,_tmpInLibrary,_tmpFavoriteOrder,_tmpDateAdded,_tmpLastUpdated);
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
  public Flow<List<DonghuaEntity>> getLibraryByLastUpdated() {
    final String _sql = "SELECT * FROM donghua_table WHERE in_library = 1 ORDER BY last_updated DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"donghua_table"}, new Callable<List<DonghuaEntity>>() {
      @Override
      @NonNull
      public List<DonghuaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_id");
          final int _cursorIndexOfSourceUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "source_url");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTitleAlt = CursorUtil.getColumnIndexOrThrow(_cursor, "title_alt");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "cover_url");
          final int _cursorIndexOfSynopsis = CursorUtil.getColumnIndexOrThrow(_cursor, "synopsis");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStudio = CursorUtil.getColumnIndexOrThrow(_cursor, "studio");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfTotalEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "total_episodes");
          final int _cursorIndexOfInLibrary = CursorUtil.getColumnIndexOrThrow(_cursor, "in_library");
          final int _cursorIndexOfFavoriteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite_order");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "date_added");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final List<DonghuaEntity> _result = new ArrayList<DonghuaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DonghuaEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSourceId;
            _tmpSourceId = _cursor.getString(_cursorIndexOfSourceId);
            final String _tmpSourceUrl;
            _tmpSourceUrl = _cursor.getString(_cursorIndexOfSourceUrl);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpTitleAlt;
            if (_cursor.isNull(_cursorIndexOfTitleAlt)) {
              _tmpTitleAlt = null;
            } else {
              _tmpTitleAlt = _cursor.getString(_cursorIndexOfTitleAlt);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final String _tmpSynopsis;
            if (_cursor.isNull(_cursorIndexOfSynopsis)) {
              _tmpSynopsis = null;
            } else {
              _tmpSynopsis = _cursor.getString(_cursorIndexOfSynopsis);
            }
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpStudio;
            if (_cursor.isNull(_cursorIndexOfStudio)) {
              _tmpStudio = null;
            } else {
              _tmpStudio = _cursor.getString(_cursorIndexOfStudio);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final float _tmpRating;
            _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
            final int _tmpTotalEpisodes;
            _tmpTotalEpisodes = _cursor.getInt(_cursorIndexOfTotalEpisodes);
            final boolean _tmpInLibrary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfInLibrary);
            _tmpInLibrary = _tmp != 0;
            final int _tmpFavoriteOrder;
            _tmpFavoriteOrder = _cursor.getInt(_cursorIndexOfFavoriteOrder);
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new DonghuaEntity(_tmpId,_tmpSourceId,_tmpSourceUrl,_tmpTitle,_tmpTitleAlt,_tmpCoverUrl,_tmpSynopsis,_tmpGenres,_tmpStatus,_tmpStudio,_tmpYear,_tmpRating,_tmpTotalEpisodes,_tmpInLibrary,_tmpFavoriteOrder,_tmpDateAdded,_tmpLastUpdated);
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
  public Flow<DonghuaEntity> getDonghuaById(final long id) {
    final String _sql = "SELECT * FROM donghua_table WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"donghua_table"}, new Callable<DonghuaEntity>() {
      @Override
      @Nullable
      public DonghuaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_id");
          final int _cursorIndexOfSourceUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "source_url");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTitleAlt = CursorUtil.getColumnIndexOrThrow(_cursor, "title_alt");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "cover_url");
          final int _cursorIndexOfSynopsis = CursorUtil.getColumnIndexOrThrow(_cursor, "synopsis");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStudio = CursorUtil.getColumnIndexOrThrow(_cursor, "studio");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfTotalEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "total_episodes");
          final int _cursorIndexOfInLibrary = CursorUtil.getColumnIndexOrThrow(_cursor, "in_library");
          final int _cursorIndexOfFavoriteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite_order");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "date_added");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final DonghuaEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSourceId;
            _tmpSourceId = _cursor.getString(_cursorIndexOfSourceId);
            final String _tmpSourceUrl;
            _tmpSourceUrl = _cursor.getString(_cursorIndexOfSourceUrl);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpTitleAlt;
            if (_cursor.isNull(_cursorIndexOfTitleAlt)) {
              _tmpTitleAlt = null;
            } else {
              _tmpTitleAlt = _cursor.getString(_cursorIndexOfTitleAlt);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final String _tmpSynopsis;
            if (_cursor.isNull(_cursorIndexOfSynopsis)) {
              _tmpSynopsis = null;
            } else {
              _tmpSynopsis = _cursor.getString(_cursorIndexOfSynopsis);
            }
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpStudio;
            if (_cursor.isNull(_cursorIndexOfStudio)) {
              _tmpStudio = null;
            } else {
              _tmpStudio = _cursor.getString(_cursorIndexOfStudio);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final float _tmpRating;
            _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
            final int _tmpTotalEpisodes;
            _tmpTotalEpisodes = _cursor.getInt(_cursorIndexOfTotalEpisodes);
            final boolean _tmpInLibrary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfInLibrary);
            _tmpInLibrary = _tmp != 0;
            final int _tmpFavoriteOrder;
            _tmpFavoriteOrder = _cursor.getInt(_cursorIndexOfFavoriteOrder);
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _result = new DonghuaEntity(_tmpId,_tmpSourceId,_tmpSourceUrl,_tmpTitle,_tmpTitleAlt,_tmpCoverUrl,_tmpSynopsis,_tmpGenres,_tmpStatus,_tmpStudio,_tmpYear,_tmpRating,_tmpTotalEpisodes,_tmpInLibrary,_tmpFavoriteOrder,_tmpDateAdded,_tmpLastUpdated);
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
  public Object getDonghuaByIdOnce(final long id,
      final Continuation<? super DonghuaEntity> $completion) {
    final String _sql = "SELECT * FROM donghua_table WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DonghuaEntity>() {
      @Override
      @Nullable
      public DonghuaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_id");
          final int _cursorIndexOfSourceUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "source_url");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTitleAlt = CursorUtil.getColumnIndexOrThrow(_cursor, "title_alt");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "cover_url");
          final int _cursorIndexOfSynopsis = CursorUtil.getColumnIndexOrThrow(_cursor, "synopsis");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStudio = CursorUtil.getColumnIndexOrThrow(_cursor, "studio");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfTotalEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "total_episodes");
          final int _cursorIndexOfInLibrary = CursorUtil.getColumnIndexOrThrow(_cursor, "in_library");
          final int _cursorIndexOfFavoriteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite_order");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "date_added");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final DonghuaEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSourceId;
            _tmpSourceId = _cursor.getString(_cursorIndexOfSourceId);
            final String _tmpSourceUrl;
            _tmpSourceUrl = _cursor.getString(_cursorIndexOfSourceUrl);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpTitleAlt;
            if (_cursor.isNull(_cursorIndexOfTitleAlt)) {
              _tmpTitleAlt = null;
            } else {
              _tmpTitleAlt = _cursor.getString(_cursorIndexOfTitleAlt);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final String _tmpSynopsis;
            if (_cursor.isNull(_cursorIndexOfSynopsis)) {
              _tmpSynopsis = null;
            } else {
              _tmpSynopsis = _cursor.getString(_cursorIndexOfSynopsis);
            }
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpStudio;
            if (_cursor.isNull(_cursorIndexOfStudio)) {
              _tmpStudio = null;
            } else {
              _tmpStudio = _cursor.getString(_cursorIndexOfStudio);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final float _tmpRating;
            _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
            final int _tmpTotalEpisodes;
            _tmpTotalEpisodes = _cursor.getInt(_cursorIndexOfTotalEpisodes);
            final boolean _tmpInLibrary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfInLibrary);
            _tmpInLibrary = _tmp != 0;
            final int _tmpFavoriteOrder;
            _tmpFavoriteOrder = _cursor.getInt(_cursorIndexOfFavoriteOrder);
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _result = new DonghuaEntity(_tmpId,_tmpSourceId,_tmpSourceUrl,_tmpTitle,_tmpTitleAlt,_tmpCoverUrl,_tmpSynopsis,_tmpGenres,_tmpStatus,_tmpStudio,_tmpYear,_tmpRating,_tmpTotalEpisodes,_tmpInLibrary,_tmpFavoriteOrder,_tmpDateAdded,_tmpLastUpdated);
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
  public Flow<List<DonghuaEntity>> searchLibrary(final String query) {
    final String _sql = "SELECT * FROM donghua_table WHERE title LIKE '%' || ? || '%' AND in_library = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"donghua_table"}, new Callable<List<DonghuaEntity>>() {
      @Override
      @NonNull
      public List<DonghuaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "source_id");
          final int _cursorIndexOfSourceUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "source_url");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTitleAlt = CursorUtil.getColumnIndexOrThrow(_cursor, "title_alt");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "cover_url");
          final int _cursorIndexOfSynopsis = CursorUtil.getColumnIndexOrThrow(_cursor, "synopsis");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStudio = CursorUtil.getColumnIndexOrThrow(_cursor, "studio");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfTotalEpisodes = CursorUtil.getColumnIndexOrThrow(_cursor, "total_episodes");
          final int _cursorIndexOfInLibrary = CursorUtil.getColumnIndexOrThrow(_cursor, "in_library");
          final int _cursorIndexOfFavoriteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite_order");
          final int _cursorIndexOfDateAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "date_added");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "last_updated");
          final List<DonghuaEntity> _result = new ArrayList<DonghuaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DonghuaEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSourceId;
            _tmpSourceId = _cursor.getString(_cursorIndexOfSourceId);
            final String _tmpSourceUrl;
            _tmpSourceUrl = _cursor.getString(_cursorIndexOfSourceUrl);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpTitleAlt;
            if (_cursor.isNull(_cursorIndexOfTitleAlt)) {
              _tmpTitleAlt = null;
            } else {
              _tmpTitleAlt = _cursor.getString(_cursorIndexOfTitleAlt);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final String _tmpSynopsis;
            if (_cursor.isNull(_cursorIndexOfSynopsis)) {
              _tmpSynopsis = null;
            } else {
              _tmpSynopsis = _cursor.getString(_cursorIndexOfSynopsis);
            }
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpStudio;
            if (_cursor.isNull(_cursorIndexOfStudio)) {
              _tmpStudio = null;
            } else {
              _tmpStudio = _cursor.getString(_cursorIndexOfStudio);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final float _tmpRating;
            _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
            final int _tmpTotalEpisodes;
            _tmpTotalEpisodes = _cursor.getInt(_cursorIndexOfTotalEpisodes);
            final boolean _tmpInLibrary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfInLibrary);
            _tmpInLibrary = _tmp != 0;
            final int _tmpFavoriteOrder;
            _tmpFavoriteOrder = _cursor.getInt(_cursorIndexOfFavoriteOrder);
            final long _tmpDateAdded;
            _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            _item = new DonghuaEntity(_tmpId,_tmpSourceId,_tmpSourceUrl,_tmpTitle,_tmpTitleAlt,_tmpCoverUrl,_tmpSynopsis,_tmpGenres,_tmpStatus,_tmpStudio,_tmpYear,_tmpRating,_tmpTotalEpisodes,_tmpInLibrary,_tmpFavoriteOrder,_tmpDateAdded,_tmpLastUpdated);
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
  public Object deleteByIds(final List<Long> ids, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("DELETE FROM donghua_table WHERE id IN (");
        final int _inputSize = ids.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (long _item : ids) {
          _stmt.bindLong(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

package com.momokotle.agent.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.momokotle.agent.database.entity.SystemUssdFlowEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SystemUssdFlowDao_Impl implements SystemUssdFlowDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SystemUssdFlowEntity> __insertionAdapterOfSystemUssdFlowEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public SystemUssdFlowDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSystemUssdFlowEntity = new EntityInsertionAdapter<SystemUssdFlowEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `system_ussd_flows` (`flowKey`,`name`,`ussdCode`,`flowJson`,`updatedAt`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemUssdFlowEntity entity) {
        statement.bindString(1, entity.getFlowKey());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getUssdCode());
        statement.bindString(4, entity.getFlowJson());
        statement.bindLong(5, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM system_ussd_flows";
        return _query;
      }
    };
  }

  @Override
  public Object upsertAll(final List<SystemUssdFlowEntity> flows,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSystemUssdFlowEntity.insert(flows);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getByFlowKey(final String flowKey,
      final Continuation<? super SystemUssdFlowEntity> $completion) {
    final String _sql = "SELECT * FROM system_ussd_flows WHERE flowKey = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, flowKey);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SystemUssdFlowEntity>() {
      @Override
      @Nullable
      public SystemUssdFlowEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFlowKey = CursorUtil.getColumnIndexOrThrow(_cursor, "flowKey");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfUssdCode = CursorUtil.getColumnIndexOrThrow(_cursor, "ussdCode");
          final int _cursorIndexOfFlowJson = CursorUtil.getColumnIndexOrThrow(_cursor, "flowJson");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final SystemUssdFlowEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpFlowKey;
            _tmpFlowKey = _cursor.getString(_cursorIndexOfFlowKey);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpUssdCode;
            _tmpUssdCode = _cursor.getString(_cursorIndexOfUssdCode);
            final String _tmpFlowJson;
            _tmpFlowJson = _cursor.getString(_cursorIndexOfFlowJson);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new SystemUssdFlowEntity(_tmpFlowKey,_tmpName,_tmpUssdCode,_tmpFlowJson,_tmpUpdatedAt);
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
  public Object getAll(final Continuation<? super List<SystemUssdFlowEntity>> $completion) {
    final String _sql = "SELECT * FROM system_ussd_flows";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SystemUssdFlowEntity>>() {
      @Override
      @NonNull
      public List<SystemUssdFlowEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFlowKey = CursorUtil.getColumnIndexOrThrow(_cursor, "flowKey");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfUssdCode = CursorUtil.getColumnIndexOrThrow(_cursor, "ussdCode");
          final int _cursorIndexOfFlowJson = CursorUtil.getColumnIndexOrThrow(_cursor, "flowJson");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<SystemUssdFlowEntity> _result = new ArrayList<SystemUssdFlowEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SystemUssdFlowEntity _item;
            final String _tmpFlowKey;
            _tmpFlowKey = _cursor.getString(_cursorIndexOfFlowKey);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpUssdCode;
            _tmpUssdCode = _cursor.getString(_cursorIndexOfUssdCode);
            final String _tmpFlowJson;
            _tmpFlowJson = _cursor.getString(_cursorIndexOfFlowJson);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new SystemUssdFlowEntity(_tmpFlowKey,_tmpName,_tmpUssdCode,_tmpFlowJson,_tmpUpdatedAt);
            _result.add(_item);
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
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM system_ussd_flows";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

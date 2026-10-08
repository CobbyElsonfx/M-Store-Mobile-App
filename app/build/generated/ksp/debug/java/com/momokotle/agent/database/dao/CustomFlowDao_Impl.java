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
import com.momokotle.agent.database.entity.CustomFlowEntity;
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
public final class CustomFlowDao_Impl implements CustomFlowDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CustomFlowEntity> __insertionAdapterOfCustomFlowEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public CustomFlowDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCustomFlowEntity = new EntityInsertionAdapter<CustomFlowEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `custom_flows` (`id`,`name`,`ussdCode`,`step1`,`step2`,`step3`,`useDynamicPhone`,`useDynamicAmount`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CustomFlowEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getUssdCode());
        statement.bindString(4, entity.getStep1());
        statement.bindString(5, entity.getStep2());
        statement.bindString(6, entity.getStep3());
        final int _tmp = entity.getUseDynamicPhone() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.getUseDynamicAmount() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM custom_flows WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CustomFlowEntity flow, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCustomFlowEntity.insertAndReturnId(flow);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CustomFlowEntity>> observeAll() {
    final String _sql = "SELECT * FROM custom_flows ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"custom_flows"}, new Callable<List<CustomFlowEntity>>() {
      @Override
      @NonNull
      public List<CustomFlowEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfUssdCode = CursorUtil.getColumnIndexOrThrow(_cursor, "ussdCode");
          final int _cursorIndexOfStep1 = CursorUtil.getColumnIndexOrThrow(_cursor, "step1");
          final int _cursorIndexOfStep2 = CursorUtil.getColumnIndexOrThrow(_cursor, "step2");
          final int _cursorIndexOfStep3 = CursorUtil.getColumnIndexOrThrow(_cursor, "step3");
          final int _cursorIndexOfUseDynamicPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "useDynamicPhone");
          final int _cursorIndexOfUseDynamicAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "useDynamicAmount");
          final List<CustomFlowEntity> _result = new ArrayList<CustomFlowEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CustomFlowEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpUssdCode;
            _tmpUssdCode = _cursor.getString(_cursorIndexOfUssdCode);
            final String _tmpStep1;
            _tmpStep1 = _cursor.getString(_cursorIndexOfStep1);
            final String _tmpStep2;
            _tmpStep2 = _cursor.getString(_cursorIndexOfStep2);
            final String _tmpStep3;
            _tmpStep3 = _cursor.getString(_cursorIndexOfStep3);
            final boolean _tmpUseDynamicPhone;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUseDynamicPhone);
            _tmpUseDynamicPhone = _tmp != 0;
            final boolean _tmpUseDynamicAmount;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfUseDynamicAmount);
            _tmpUseDynamicAmount = _tmp_1 != 0;
            _item = new CustomFlowEntity(_tmpId,_tmpName,_tmpUssdCode,_tmpStep1,_tmpStep2,_tmpStep3,_tmpUseDynamicPhone,_tmpUseDynamicAmount);
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
  public Object getById(final long id, final Continuation<? super CustomFlowEntity> $completion) {
    final String _sql = "SELECT * FROM custom_flows WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CustomFlowEntity>() {
      @Override
      @Nullable
      public CustomFlowEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfUssdCode = CursorUtil.getColumnIndexOrThrow(_cursor, "ussdCode");
          final int _cursorIndexOfStep1 = CursorUtil.getColumnIndexOrThrow(_cursor, "step1");
          final int _cursorIndexOfStep2 = CursorUtil.getColumnIndexOrThrow(_cursor, "step2");
          final int _cursorIndexOfStep3 = CursorUtil.getColumnIndexOrThrow(_cursor, "step3");
          final int _cursorIndexOfUseDynamicPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "useDynamicPhone");
          final int _cursorIndexOfUseDynamicAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "useDynamicAmount");
          final CustomFlowEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpUssdCode;
            _tmpUssdCode = _cursor.getString(_cursorIndexOfUssdCode);
            final String _tmpStep1;
            _tmpStep1 = _cursor.getString(_cursorIndexOfStep1);
            final String _tmpStep2;
            _tmpStep2 = _cursor.getString(_cursorIndexOfStep2);
            final String _tmpStep3;
            _tmpStep3 = _cursor.getString(_cursorIndexOfStep3);
            final boolean _tmpUseDynamicPhone;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUseDynamicPhone);
            _tmpUseDynamicPhone = _tmp != 0;
            final boolean _tmpUseDynamicAmount;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfUseDynamicAmount);
            _tmpUseDynamicAmount = _tmp_1 != 0;
            _result = new CustomFlowEntity(_tmpId,_tmpName,_tmpUssdCode,_tmpStep1,_tmpStep2,_tmpStep3,_tmpUseDynamicPhone,_tmpUseDynamicAmount);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

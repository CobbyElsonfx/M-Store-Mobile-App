package com.momokotle.agent.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.momokotle.agent.database.entity.ProfitRuleEntity;
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
public final class ProfitRuleDao_Impl implements ProfitRuleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProfitRuleEntity> __insertionAdapterOfProfitRuleEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllByType;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public ProfitRuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProfitRuleEntity = new EntityInsertionAdapter<ProfitRuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `profit_rules` (`id`,`ruleType`,`minAmount`,`maxAmount`,`fixedCommission`,`profitPercentage`,`useFixedCommission`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProfitRuleEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getRuleType());
        statement.bindDouble(3, entity.getMinAmount());
        statement.bindDouble(4, entity.getMaxAmount());
        statement.bindDouble(5, entity.getFixedCommission());
        statement.bindDouble(6, entity.getProfitPercentage());
        final int _tmp = entity.getUseFixedCommission() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__preparedStmtOfDeleteAllByType = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM profit_rules WHERE ruleType = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM profit_rules WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ProfitRuleEntity rule, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProfitRuleEntity.insertAndReturnId(rule);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllByType(final String ruleType,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllByType.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, ruleType);
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
          __preparedStmtOfDeleteAllByType.release(_stmt);
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
  public Flow<List<ProfitRuleEntity>> observeByType(final String ruleType) {
    final String _sql = "SELECT * FROM profit_rules WHERE ruleType = ? ORDER BY minAmount ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, ruleType);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"profit_rules"}, new Callable<List<ProfitRuleEntity>>() {
      @Override
      @NonNull
      public List<ProfitRuleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRuleType = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleType");
          final int _cursorIndexOfMinAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "minAmount");
          final int _cursorIndexOfMaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "maxAmount");
          final int _cursorIndexOfFixedCommission = CursorUtil.getColumnIndexOrThrow(_cursor, "fixedCommission");
          final int _cursorIndexOfProfitPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "profitPercentage");
          final int _cursorIndexOfUseFixedCommission = CursorUtil.getColumnIndexOrThrow(_cursor, "useFixedCommission");
          final List<ProfitRuleEntity> _result = new ArrayList<ProfitRuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProfitRuleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRuleType;
            _tmpRuleType = _cursor.getString(_cursorIndexOfRuleType);
            final double _tmpMinAmount;
            _tmpMinAmount = _cursor.getDouble(_cursorIndexOfMinAmount);
            final double _tmpMaxAmount;
            _tmpMaxAmount = _cursor.getDouble(_cursorIndexOfMaxAmount);
            final double _tmpFixedCommission;
            _tmpFixedCommission = _cursor.getDouble(_cursorIndexOfFixedCommission);
            final double _tmpProfitPercentage;
            _tmpProfitPercentage = _cursor.getDouble(_cursorIndexOfProfitPercentage);
            final boolean _tmpUseFixedCommission;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUseFixedCommission);
            _tmpUseFixedCommission = _tmp != 0;
            _item = new ProfitRuleEntity(_tmpId,_tmpRuleType,_tmpMinAmount,_tmpMaxAmount,_tmpFixedCommission,_tmpProfitPercentage,_tmpUseFixedCommission);
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
  public Object getAllByType(final String ruleType,
      final Continuation<? super List<ProfitRuleEntity>> $completion) {
    final String _sql = "SELECT * FROM profit_rules WHERE ruleType = ? ORDER BY minAmount ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, ruleType);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ProfitRuleEntity>>() {
      @Override
      @NonNull
      public List<ProfitRuleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRuleType = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleType");
          final int _cursorIndexOfMinAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "minAmount");
          final int _cursorIndexOfMaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "maxAmount");
          final int _cursorIndexOfFixedCommission = CursorUtil.getColumnIndexOrThrow(_cursor, "fixedCommission");
          final int _cursorIndexOfProfitPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "profitPercentage");
          final int _cursorIndexOfUseFixedCommission = CursorUtil.getColumnIndexOrThrow(_cursor, "useFixedCommission");
          final List<ProfitRuleEntity> _result = new ArrayList<ProfitRuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProfitRuleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpRuleType;
            _tmpRuleType = _cursor.getString(_cursorIndexOfRuleType);
            final double _tmpMinAmount;
            _tmpMinAmount = _cursor.getDouble(_cursorIndexOfMinAmount);
            final double _tmpMaxAmount;
            _tmpMaxAmount = _cursor.getDouble(_cursorIndexOfMaxAmount);
            final double _tmpFixedCommission;
            _tmpFixedCommission = _cursor.getDouble(_cursorIndexOfFixedCommission);
            final double _tmpProfitPercentage;
            _tmpProfitPercentage = _cursor.getDouble(_cursorIndexOfProfitPercentage);
            final boolean _tmpUseFixedCommission;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUseFixedCommission);
            _tmpUseFixedCommission = _tmp != 0;
            _item = new ProfitRuleEntity(_tmpId,_tmpRuleType,_tmpMinAmount,_tmpMaxAmount,_tmpFixedCommission,_tmpProfitPercentage,_tmpUseFixedCommission);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

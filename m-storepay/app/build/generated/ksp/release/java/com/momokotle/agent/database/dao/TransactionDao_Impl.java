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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.momokotle.agent.database.entity.TransactionEntity;
import java.lang.Class;
import java.lang.Double;
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
public final class TransactionDao_Impl implements TransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransactionEntity> __insertionAdapterOfTransactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatusAndCommission;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public TransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransactionEntity = new EntityInsertionAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transactions` (`id`,`type`,`amount`,`profit`,`commission`,`commissionRuleUsed`,`phoneNumber`,`flowName`,`status`,`note`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getType());
        statement.bindDouble(3, entity.getAmount());
        statement.bindDouble(4, entity.getProfit());
        statement.bindDouble(5, entity.getCommission());
        if (entity.getCommissionRuleUsed() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCommissionRuleUsed());
        }
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPhoneNumber());
        }
        if (entity.getFlowName() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFlowName());
        }
        statement.bindString(9, entity.getStatus());
        if (entity.getNote() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNote());
        }
        statement.bindLong(11, entity.getTimestamp());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE transactions SET status = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStatusAndCommission = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE transactions SET status = ?, profit = ?, commission = ?,\n"
                + "        commissionRuleUsed = ? WHERE id = ?\n"
                + "        ";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transactions WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TransactionEntity transaction,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTransactionEntity.insertAndReturnId(transaction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final long id, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
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
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatusAndCommission(final long id, final String status, final double profit,
      final double commission, final String commissionRuleUsed,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatusAndCommission.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, profit);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, commission);
        _argIndex = 4;
        if (commissionRuleUsed == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, commissionRuleUsed);
        }
        _argIndex = 5;
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
          __preparedStmtOfUpdateStatusAndCommission.release(_stmt);
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
  public Flow<List<TransactionEntity>> observeAll() {
    final String _sql = "SELECT * FROM transactions ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCommission = CursorUtil.getColumnIndexOrThrow(_cursor, "commission");
          final int _cursorIndexOfCommissionRuleUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "commissionRuleUsed");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfFlowName = CursorUtil.getColumnIndexOrThrow(_cursor, "flowName");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final double _tmpCommission;
            _tmpCommission = _cursor.getDouble(_cursorIndexOfCommission);
            final String _tmpCommissionRuleUsed;
            if (_cursor.isNull(_cursorIndexOfCommissionRuleUsed)) {
              _tmpCommissionRuleUsed = null;
            } else {
              _tmpCommissionRuleUsed = _cursor.getString(_cursorIndexOfCommissionRuleUsed);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpFlowName;
            if (_cursor.isNull(_cursorIndexOfFlowName)) {
              _tmpFlowName = null;
            } else {
              _tmpFlowName = _cursor.getString(_cursorIndexOfFlowName);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpProfit,_tmpCommission,_tmpCommissionRuleUsed,_tmpPhoneNumber,_tmpFlowName,_tmpStatus,_tmpNote,_tmpTimestamp);
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
  public Flow<List<TransactionEntity>> observeByType(final String type) {
    final String _sql = "SELECT * FROM transactions WHERE type = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCommission = CursorUtil.getColumnIndexOrThrow(_cursor, "commission");
          final int _cursorIndexOfCommissionRuleUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "commissionRuleUsed");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfFlowName = CursorUtil.getColumnIndexOrThrow(_cursor, "flowName");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final double _tmpCommission;
            _tmpCommission = _cursor.getDouble(_cursorIndexOfCommission);
            final String _tmpCommissionRuleUsed;
            if (_cursor.isNull(_cursorIndexOfCommissionRuleUsed)) {
              _tmpCommissionRuleUsed = null;
            } else {
              _tmpCommissionRuleUsed = _cursor.getString(_cursorIndexOfCommissionRuleUsed);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpFlowName;
            if (_cursor.isNull(_cursorIndexOfFlowName)) {
              _tmpFlowName = null;
            } else {
              _tmpFlowName = _cursor.getString(_cursorIndexOfFlowName);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpProfit,_tmpCommission,_tmpCommissionRuleUsed,_tmpPhoneNumber,_tmpFlowName,_tmpStatus,_tmpNote,_tmpTimestamp);
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
  public Flow<List<TransactionEntity>> observeRecent(final int limit) {
    final String _sql = "SELECT * FROM transactions ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCommission = CursorUtil.getColumnIndexOrThrow(_cursor, "commission");
          final int _cursorIndexOfCommissionRuleUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "commissionRuleUsed");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfFlowName = CursorUtil.getColumnIndexOrThrow(_cursor, "flowName");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final double _tmpCommission;
            _tmpCommission = _cursor.getDouble(_cursorIndexOfCommission);
            final String _tmpCommissionRuleUsed;
            if (_cursor.isNull(_cursorIndexOfCommissionRuleUsed)) {
              _tmpCommissionRuleUsed = null;
            } else {
              _tmpCommissionRuleUsed = _cursor.getString(_cursorIndexOfCommissionRuleUsed);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpFlowName;
            if (_cursor.isNull(_cursorIndexOfFlowName)) {
              _tmpFlowName = null;
            } else {
              _tmpFlowName = _cursor.getString(_cursorIndexOfFlowName);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpProfit,_tmpCommission,_tmpCommissionRuleUsed,_tmpPhoneNumber,_tmpFlowName,_tmpStatus,_tmpNote,_tmpTimestamp);
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
  public Object getById(final long id, final Continuation<? super TransactionEntity> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TransactionEntity>() {
      @Override
      @Nullable
      public TransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCommission = CursorUtil.getColumnIndexOrThrow(_cursor, "commission");
          final int _cursorIndexOfCommissionRuleUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "commissionRuleUsed");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfFlowName = CursorUtil.getColumnIndexOrThrow(_cursor, "flowName");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final TransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final double _tmpCommission;
            _tmpCommission = _cursor.getDouble(_cursorIndexOfCommission);
            final String _tmpCommissionRuleUsed;
            if (_cursor.isNull(_cursorIndexOfCommissionRuleUsed)) {
              _tmpCommissionRuleUsed = null;
            } else {
              _tmpCommissionRuleUsed = _cursor.getString(_cursorIndexOfCommissionRuleUsed);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpFlowName;
            if (_cursor.isNull(_cursorIndexOfFlowName)) {
              _tmpFlowName = null;
            } else {
              _tmpFlowName = _cursor.getString(_cursorIndexOfFlowName);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _result = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpProfit,_tmpCommission,_tmpCommissionRuleUsed,_tmpPhoneNumber,_tmpFlowName,_tmpStatus,_tmpNote,_tmpTimestamp);
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
  public Object findRecentByPhoneNumbers(final List<String> phoneNumbers, final long cutoffTime,
      final Continuation<? super TransactionEntity> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("\n");
    _stringBuilder.append("        SELECT * FROM transactions");
    _stringBuilder.append("\n");
    _stringBuilder.append("        WHERE phoneNumber IN (");
    final int _inputSize = phoneNumbers.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    _stringBuilder.append("\n");
    _stringBuilder.append("          AND timestamp >= ");
    _stringBuilder.append("?");
    _stringBuilder.append("\n");
    _stringBuilder.append("        ORDER BY timestamp DESC");
    _stringBuilder.append("\n");
    _stringBuilder.append("        LIMIT 1");
    _stringBuilder.append("\n");
    _stringBuilder.append("        ");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : phoneNumbers) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    _argIndex = 1 + _inputSize;
    _statement.bindLong(_argIndex, cutoffTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TransactionEntity>() {
      @Override
      @Nullable
      public TransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCommission = CursorUtil.getColumnIndexOrThrow(_cursor, "commission");
          final int _cursorIndexOfCommissionRuleUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "commissionRuleUsed");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfFlowName = CursorUtil.getColumnIndexOrThrow(_cursor, "flowName");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final TransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final double _tmpCommission;
            _tmpCommission = _cursor.getDouble(_cursorIndexOfCommission);
            final String _tmpCommissionRuleUsed;
            if (_cursor.isNull(_cursorIndexOfCommissionRuleUsed)) {
              _tmpCommissionRuleUsed = null;
            } else {
              _tmpCommissionRuleUsed = _cursor.getString(_cursorIndexOfCommissionRuleUsed);
            }
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpFlowName;
            if (_cursor.isNull(_cursorIndexOfFlowName)) {
              _tmpFlowName = null;
            } else {
              _tmpFlowName = _cursor.getString(_cursorIndexOfFlowName);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _result = new TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpProfit,_tmpCommission,_tmpCommissionRuleUsed,_tmpPhoneNumber,_tmpFlowName,_tmpStatus,_tmpNote,_tmpTimestamp);
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
  public Object countByPhoneNumbers(final List<String> phoneNumbers,
      final Continuation<? super Integer> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("\n");
    _stringBuilder.append("        SELECT COUNT(*) FROM transactions");
    _stringBuilder.append("\n");
    _stringBuilder.append("        WHERE phoneNumber IN (");
    final int _inputSize = phoneNumbers.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    _stringBuilder.append("\n");
    _stringBuilder.append("        ");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : phoneNumbers) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
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

  @Override
  public Object findFrequentPhonesMatching(final String query, final int minCount, final int limit,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "\n"
            + "        SELECT phoneNumber FROM transactions\n"
            + "        WHERE phoneNumber IS NOT NULL\n"
            + "          AND phoneNumber != ''\n"
            + "          AND phoneNumber LIKE '%' || ? || '%'\n"
            + "        GROUP BY phoneNumber\n"
            + "        HAVING COUNT(*) >= ?\n"
            + "        ORDER BY MAX(timestamp) DESC\n"
            + "        LIMIT ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindLong(_argIndex, minCount);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Object sumSuccessfulCommissionSince(final String type, final long startMillis,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(commission), 0) FROM transactions\n"
            + "        WHERE status = 'SUCCESSFUL' AND timestamp >= ? AND type = ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startMillis);
    _argIndex = 2;
    _statement.bindString(_argIndex, type);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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

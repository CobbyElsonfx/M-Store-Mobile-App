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
import com.momokotle.agent.database.entity.FormSubmissionEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class FormSubmissionDao_Impl implements FormSubmissionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FormSubmissionEntity> __insertionAdapterOfFormSubmissionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByUserId;

  public FormSubmissionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFormSubmissionEntity = new EntityInsertionAdapter<FormSubmissionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `form_submissions` (`submissionId`,`userId`,`applicantName`,`phoneNumber`,`formType`,`formDataJson`,`documentsJson`,`status`,`adminNote`,`requiresPayment`,`paymentAmount`,`paymentStatus`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FormSubmissionEntity entity) {
        statement.bindString(1, entity.getSubmissionId());
        statement.bindString(2, entity.getUserId());
        statement.bindString(3, entity.getApplicantName());
        statement.bindString(4, entity.getPhoneNumber());
        statement.bindString(5, entity.getFormType());
        statement.bindString(6, entity.getFormDataJson());
        statement.bindString(7, entity.getDocumentsJson());
        statement.bindString(8, entity.getStatus());
        if (entity.getAdminNote() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getAdminNote());
        }
        final int _tmp = entity.getRequiresPayment() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindDouble(11, entity.getPaymentAmount());
        if (entity.getPaymentStatus() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPaymentStatus());
        }
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfDeleteByUserId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM form_submissions WHERE userId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FormSubmissionEntity submission,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFormSubmissionEntity.insert(submission);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<FormSubmissionEntity> submissions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFormSubmissionEntity.insert(submissions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByUserId(final String userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByUserId.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfDeleteByUserId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FormSubmissionEntity>> observeByUserId(final String userId) {
    final String _sql = "SELECT * FROM form_submissions WHERE userId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"form_submissions"}, new Callable<List<FormSubmissionEntity>>() {
      @Override
      @NonNull
      public List<FormSubmissionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSubmissionId = CursorUtil.getColumnIndexOrThrow(_cursor, "submissionId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfApplicantName = CursorUtil.getColumnIndexOrThrow(_cursor, "applicantName");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfFormType = CursorUtil.getColumnIndexOrThrow(_cursor, "formType");
          final int _cursorIndexOfFormDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "formDataJson");
          final int _cursorIndexOfDocumentsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "documentsJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAdminNote = CursorUtil.getColumnIndexOrThrow(_cursor, "adminNote");
          final int _cursorIndexOfRequiresPayment = CursorUtil.getColumnIndexOrThrow(_cursor, "requiresPayment");
          final int _cursorIndexOfPaymentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentAmount");
          final int _cursorIndexOfPaymentStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentStatus");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<FormSubmissionEntity> _result = new ArrayList<FormSubmissionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FormSubmissionEntity _item;
            final String _tmpSubmissionId;
            _tmpSubmissionId = _cursor.getString(_cursorIndexOfSubmissionId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpApplicantName;
            _tmpApplicantName = _cursor.getString(_cursorIndexOfApplicantName);
            final String _tmpPhoneNumber;
            _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            final String _tmpFormType;
            _tmpFormType = _cursor.getString(_cursorIndexOfFormType);
            final String _tmpFormDataJson;
            _tmpFormDataJson = _cursor.getString(_cursorIndexOfFormDataJson);
            final String _tmpDocumentsJson;
            _tmpDocumentsJson = _cursor.getString(_cursorIndexOfDocumentsJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAdminNote;
            if (_cursor.isNull(_cursorIndexOfAdminNote)) {
              _tmpAdminNote = null;
            } else {
              _tmpAdminNote = _cursor.getString(_cursorIndexOfAdminNote);
            }
            final boolean _tmpRequiresPayment;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRequiresPayment);
            _tmpRequiresPayment = _tmp != 0;
            final double _tmpPaymentAmount;
            _tmpPaymentAmount = _cursor.getDouble(_cursorIndexOfPaymentAmount);
            final String _tmpPaymentStatus;
            if (_cursor.isNull(_cursorIndexOfPaymentStatus)) {
              _tmpPaymentStatus = null;
            } else {
              _tmpPaymentStatus = _cursor.getString(_cursorIndexOfPaymentStatus);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new FormSubmissionEntity(_tmpSubmissionId,_tmpUserId,_tmpApplicantName,_tmpPhoneNumber,_tmpFormType,_tmpFormDataJson,_tmpDocumentsJson,_tmpStatus,_tmpAdminNote,_tmpRequiresPayment,_tmpPaymentAmount,_tmpPaymentStatus,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getById(final String submissionId,
      final Continuation<? super FormSubmissionEntity> $completion) {
    final String _sql = "SELECT * FROM form_submissions WHERE submissionId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, submissionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FormSubmissionEntity>() {
      @Override
      @Nullable
      public FormSubmissionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSubmissionId = CursorUtil.getColumnIndexOrThrow(_cursor, "submissionId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfApplicantName = CursorUtil.getColumnIndexOrThrow(_cursor, "applicantName");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfFormType = CursorUtil.getColumnIndexOrThrow(_cursor, "formType");
          final int _cursorIndexOfFormDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "formDataJson");
          final int _cursorIndexOfDocumentsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "documentsJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAdminNote = CursorUtil.getColumnIndexOrThrow(_cursor, "adminNote");
          final int _cursorIndexOfRequiresPayment = CursorUtil.getColumnIndexOrThrow(_cursor, "requiresPayment");
          final int _cursorIndexOfPaymentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentAmount");
          final int _cursorIndexOfPaymentStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentStatus");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final FormSubmissionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSubmissionId;
            _tmpSubmissionId = _cursor.getString(_cursorIndexOfSubmissionId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpApplicantName;
            _tmpApplicantName = _cursor.getString(_cursorIndexOfApplicantName);
            final String _tmpPhoneNumber;
            _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            final String _tmpFormType;
            _tmpFormType = _cursor.getString(_cursorIndexOfFormType);
            final String _tmpFormDataJson;
            _tmpFormDataJson = _cursor.getString(_cursorIndexOfFormDataJson);
            final String _tmpDocumentsJson;
            _tmpDocumentsJson = _cursor.getString(_cursorIndexOfDocumentsJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAdminNote;
            if (_cursor.isNull(_cursorIndexOfAdminNote)) {
              _tmpAdminNote = null;
            } else {
              _tmpAdminNote = _cursor.getString(_cursorIndexOfAdminNote);
            }
            final boolean _tmpRequiresPayment;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRequiresPayment);
            _tmpRequiresPayment = _tmp != 0;
            final double _tmpPaymentAmount;
            _tmpPaymentAmount = _cursor.getDouble(_cursorIndexOfPaymentAmount);
            final String _tmpPaymentStatus;
            if (_cursor.isNull(_cursorIndexOfPaymentStatus)) {
              _tmpPaymentStatus = null;
            } else {
              _tmpPaymentStatus = _cursor.getString(_cursorIndexOfPaymentStatus);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new FormSubmissionEntity(_tmpSubmissionId,_tmpUserId,_tmpApplicantName,_tmpPhoneNumber,_tmpFormType,_tmpFormDataJson,_tmpDocumentsJson,_tmpStatus,_tmpAdminNote,_tmpRequiresPayment,_tmpPaymentAmount,_tmpPaymentStatus,_tmpCreatedAt,_tmpUpdatedAt);
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

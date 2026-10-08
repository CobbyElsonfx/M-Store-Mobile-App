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
import com.momokotle.agent.database.entity.StoreProductEntity;
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
public final class StoreProductDao_Impl implements StoreProductDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StoreProductEntity> __insertionAdapterOfStoreProductEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public StoreProductDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStoreProductEntity = new EntityInsertionAdapter<StoreProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `store_products` (`productId`,`productName`,`productDescription`,`category`,`price`,`profit`,`referralPercent`,`systemPercent`,`stockQuantity`,`productImageUrl`,`isAvailable`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StoreProductEntity entity) {
        statement.bindString(1, entity.getProductId());
        statement.bindString(2, entity.getProductName());
        statement.bindString(3, entity.getProductDescription());
        statement.bindString(4, entity.getCategory());
        statement.bindDouble(5, entity.getPrice());
        statement.bindDouble(6, entity.getProfit());
        statement.bindDouble(7, entity.getReferralPercent());
        statement.bindDouble(8, entity.getSystemPercent());
        statement.bindLong(9, entity.getStockQuantity());
        if (entity.getProductImageUrl() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getProductImageUrl());
        }
        final int _tmp = entity.isAvailable() ? 1 : 0;
        statement.bindLong(11, _tmp);
        statement.bindLong(12, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM store_products";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<StoreProductEntity> products,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStoreProductEntity.insert(products);
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
  public Flow<List<StoreProductEntity>> observeAll() {
    final String _sql = "SELECT * FROM store_products ORDER BY productName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"store_products"}, new Callable<List<StoreProductEntity>>() {
      @Override
      @NonNull
      public List<StoreProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfProductDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "productDescription");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfReferralPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "referralPercent");
          final int _cursorIndexOfSystemPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "systemPercent");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfProductImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "productImageUrl");
          final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<StoreProductEntity> _result = new ArrayList<StoreProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StoreProductEntity _item;
            final String _tmpProductId;
            _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            final String _tmpProductDescription;
            _tmpProductDescription = _cursor.getString(_cursorIndexOfProductDescription);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final double _tmpReferralPercent;
            _tmpReferralPercent = _cursor.getDouble(_cursorIndexOfReferralPercent);
            final double _tmpSystemPercent;
            _tmpSystemPercent = _cursor.getDouble(_cursorIndexOfSystemPercent);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final String _tmpProductImageUrl;
            if (_cursor.isNull(_cursorIndexOfProductImageUrl)) {
              _tmpProductImageUrl = null;
            } else {
              _tmpProductImageUrl = _cursor.getString(_cursorIndexOfProductImageUrl);
            }
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StoreProductEntity(_tmpProductId,_tmpProductName,_tmpProductDescription,_tmpCategory,_tmpPrice,_tmpProfit,_tmpReferralPercent,_tmpSystemPercent,_tmpStockQuantity,_tmpProductImageUrl,_tmpIsAvailable,_tmpUpdatedAt);
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
  public Flow<List<StoreProductEntity>> observeByCategory(final String category) {
    final String _sql = "\n"
            + "        SELECT * FROM store_products\n"
            + "        WHERE category = ? AND isAvailable = 1\n"
            + "        ORDER BY productName ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"store_products"}, new Callable<List<StoreProductEntity>>() {
      @Override
      @NonNull
      public List<StoreProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfProductDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "productDescription");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfReferralPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "referralPercent");
          final int _cursorIndexOfSystemPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "systemPercent");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfProductImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "productImageUrl");
          final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<StoreProductEntity> _result = new ArrayList<StoreProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StoreProductEntity _item;
            final String _tmpProductId;
            _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            final String _tmpProductDescription;
            _tmpProductDescription = _cursor.getString(_cursorIndexOfProductDescription);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final double _tmpReferralPercent;
            _tmpReferralPercent = _cursor.getDouble(_cursorIndexOfReferralPercent);
            final double _tmpSystemPercent;
            _tmpSystemPercent = _cursor.getDouble(_cursorIndexOfSystemPercent);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final String _tmpProductImageUrl;
            if (_cursor.isNull(_cursorIndexOfProductImageUrl)) {
              _tmpProductImageUrl = null;
            } else {
              _tmpProductImageUrl = _cursor.getString(_cursorIndexOfProductImageUrl);
            }
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StoreProductEntity(_tmpProductId,_tmpProductName,_tmpProductDescription,_tmpCategory,_tmpPrice,_tmpProfit,_tmpReferralPercent,_tmpSystemPercent,_tmpStockQuantity,_tmpProductImageUrl,_tmpIsAvailable,_tmpUpdatedAt);
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
  public Object getById(final String productId,
      final Continuation<? super StoreProductEntity> $completion) {
    final String _sql = "SELECT * FROM store_products WHERE productId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, productId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<StoreProductEntity>() {
      @Override
      @Nullable
      public StoreProductEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfProductDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "productDescription");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfReferralPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "referralPercent");
          final int _cursorIndexOfSystemPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "systemPercent");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfProductImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "productImageUrl");
          final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final StoreProductEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpProductId;
            _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            final String _tmpProductDescription;
            _tmpProductDescription = _cursor.getString(_cursorIndexOfProductDescription);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final double _tmpReferralPercent;
            _tmpReferralPercent = _cursor.getDouble(_cursorIndexOfReferralPercent);
            final double _tmpSystemPercent;
            _tmpSystemPercent = _cursor.getDouble(_cursorIndexOfSystemPercent);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final String _tmpProductImageUrl;
            if (_cursor.isNull(_cursorIndexOfProductImageUrl)) {
              _tmpProductImageUrl = null;
            } else {
              _tmpProductImageUrl = _cursor.getString(_cursorIndexOfProductImageUrl);
            }
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new StoreProductEntity(_tmpProductId,_tmpProductName,_tmpProductDescription,_tmpCategory,_tmpPrice,_tmpProfit,_tmpReferralPercent,_tmpSystemPercent,_tmpStockQuantity,_tmpProductImageUrl,_tmpIsAvailable,_tmpUpdatedAt);
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

package com.momokotle.agent.database;

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
import com.momokotle.agent.database.dao.CustomFlowDao;
import com.momokotle.agent.database.dao.CustomFlowDao_Impl;
import com.momokotle.agent.database.dao.FormSubmissionDao;
import com.momokotle.agent.database.dao.FormSubmissionDao_Impl;
import com.momokotle.agent.database.dao.ProfitRuleDao;
import com.momokotle.agent.database.dao.ProfitRuleDao_Impl;
import com.momokotle.agent.database.dao.SavedContactDao;
import com.momokotle.agent.database.dao.SavedContactDao_Impl;
import com.momokotle.agent.database.dao.StoreOrderDao;
import com.momokotle.agent.database.dao.StoreOrderDao_Impl;
import com.momokotle.agent.database.dao.TransactionDao;
import com.momokotle.agent.database.dao.TransactionDao_Impl;
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
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TransactionDao _transactionDao;

  private volatile SavedContactDao _savedContactDao;

  private volatile ProfitRuleDao _profitRuleDao;

  private volatile CustomFlowDao _customFlowDao;

  private volatile StoreOrderDao _storeOrderDao;

  private volatile FormSubmissionDao _formSubmissionDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(7) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `amount` REAL NOT NULL, `profit` REAL NOT NULL, `phoneNumber` TEXT, `flowName` TEXT, `status` TEXT NOT NULL, `note` TEXT, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `saved_contacts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `contactName` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, `network` TEXT NOT NULL, `isPorted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_saved_contacts_phoneNumber` ON `saved_contacts` (`phoneNumber`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `profit_rules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ruleType` TEXT NOT NULL, `amount` REAL NOT NULL, `profitPercentage` REAL NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_profit_rules_ruleType_amount` ON `profit_rules` (`ruleType`, `amount`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `custom_flows` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `ussdCode` TEXT NOT NULL, `step1` TEXT NOT NULL, `step2` TEXT NOT NULL, `step3` TEXT NOT NULL, `useDynamicPhone` INTEGER NOT NULL, `useDynamicAmount` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `store_orders` (`orderId` TEXT NOT NULL, `userId` TEXT NOT NULL, `itemName` TEXT NOT NULL, `quantity` INTEGER NOT NULL, `totalAmount` REAL NOT NULL, `phoneNumber` TEXT NOT NULL, `location` TEXT NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`orderId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `form_submissions` (`submissionId` TEXT NOT NULL, `userId` TEXT NOT NULL, `formType` TEXT NOT NULL, `formDataJson` TEXT NOT NULL, `documentsJson` TEXT NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`submissionId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ac4204fc70ee5f42232f5b644b70cf19')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        db.execSQL("DROP TABLE IF EXISTS `saved_contacts`");
        db.execSQL("DROP TABLE IF EXISTS `profit_rules`");
        db.execSQL("DROP TABLE IF EXISTS `custom_flows`");
        db.execSQL("DROP TABLE IF EXISTS `store_orders`");
        db.execSQL("DROP TABLE IF EXISTS `form_submissions`");
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
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(9);
        _columnsTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("profit", new TableInfo.Column("profit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("flowName", new TableInfo.Column("flowName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(com.momokotle.agent.database.entity.TransactionEntity).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsSavedContacts = new HashMap<String, TableInfo.Column>(7);
        _columnsSavedContacts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedContacts.put("contactName", new TableInfo.Column("contactName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedContacts.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedContacts.put("network", new TableInfo.Column("network", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedContacts.put("isPorted", new TableInfo.Column("isPorted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedContacts.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedContacts.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSavedContacts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSavedContacts = new HashSet<TableInfo.Index>(1);
        _indicesSavedContacts.add(new TableInfo.Index("index_saved_contacts_phoneNumber", true, Arrays.asList("phoneNumber"), Arrays.asList("ASC")));
        final TableInfo _infoSavedContacts = new TableInfo("saved_contacts", _columnsSavedContacts, _foreignKeysSavedContacts, _indicesSavedContacts);
        final TableInfo _existingSavedContacts = TableInfo.read(db, "saved_contacts");
        if (!_infoSavedContacts.equals(_existingSavedContacts)) {
          return new RoomOpenHelper.ValidationResult(false, "saved_contacts(com.momokotle.agent.database.entity.SavedContactEntity).\n"
                  + " Expected:\n" + _infoSavedContacts + "\n"
                  + " Found:\n" + _existingSavedContacts);
        }
        final HashMap<String, TableInfo.Column> _columnsProfitRules = new HashMap<String, TableInfo.Column>(4);
        _columnsProfitRules.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfitRules.put("ruleType", new TableInfo.Column("ruleType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfitRules.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfitRules.put("profitPercentage", new TableInfo.Column("profitPercentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProfitRules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProfitRules = new HashSet<TableInfo.Index>(1);
        _indicesProfitRules.add(new TableInfo.Index("index_profit_rules_ruleType_amount", true, Arrays.asList("ruleType", "amount"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoProfitRules = new TableInfo("profit_rules", _columnsProfitRules, _foreignKeysProfitRules, _indicesProfitRules);
        final TableInfo _existingProfitRules = TableInfo.read(db, "profit_rules");
        if (!_infoProfitRules.equals(_existingProfitRules)) {
          return new RoomOpenHelper.ValidationResult(false, "profit_rules(com.momokotle.agent.database.entity.ProfitRuleEntity).\n"
                  + " Expected:\n" + _infoProfitRules + "\n"
                  + " Found:\n" + _existingProfitRules);
        }
        final HashMap<String, TableInfo.Column> _columnsCustomFlows = new HashMap<String, TableInfo.Column>(8);
        _columnsCustomFlows.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomFlows.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomFlows.put("ussdCode", new TableInfo.Column("ussdCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomFlows.put("step1", new TableInfo.Column("step1", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomFlows.put("step2", new TableInfo.Column("step2", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomFlows.put("step3", new TableInfo.Column("step3", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomFlows.put("useDynamicPhone", new TableInfo.Column("useDynamicPhone", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomFlows.put("useDynamicAmount", new TableInfo.Column("useDynamicAmount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCustomFlows = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCustomFlows = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCustomFlows = new TableInfo("custom_flows", _columnsCustomFlows, _foreignKeysCustomFlows, _indicesCustomFlows);
        final TableInfo _existingCustomFlows = TableInfo.read(db, "custom_flows");
        if (!_infoCustomFlows.equals(_existingCustomFlows)) {
          return new RoomOpenHelper.ValidationResult(false, "custom_flows(com.momokotle.agent.database.entity.CustomFlowEntity).\n"
                  + " Expected:\n" + _infoCustomFlows + "\n"
                  + " Found:\n" + _existingCustomFlows);
        }
        final HashMap<String, TableInfo.Column> _columnsStoreOrders = new HashMap<String, TableInfo.Column>(9);
        _columnsStoreOrders.put("orderId", new TableInfo.Column("orderId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoreOrders.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoreOrders.put("itemName", new TableInfo.Column("itemName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoreOrders.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoreOrders.put("totalAmount", new TableInfo.Column("totalAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoreOrders.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoreOrders.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoreOrders.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStoreOrders.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStoreOrders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStoreOrders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStoreOrders = new TableInfo("store_orders", _columnsStoreOrders, _foreignKeysStoreOrders, _indicesStoreOrders);
        final TableInfo _existingStoreOrders = TableInfo.read(db, "store_orders");
        if (!_infoStoreOrders.equals(_existingStoreOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "store_orders(com.momokotle.agent.database.entity.StoreOrderEntity).\n"
                  + " Expected:\n" + _infoStoreOrders + "\n"
                  + " Found:\n" + _existingStoreOrders);
        }
        final HashMap<String, TableInfo.Column> _columnsFormSubmissions = new HashMap<String, TableInfo.Column>(7);
        _columnsFormSubmissions.put("submissionId", new TableInfo.Column("submissionId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormSubmissions.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormSubmissions.put("formType", new TableInfo.Column("formType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormSubmissions.put("formDataJson", new TableInfo.Column("formDataJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormSubmissions.put("documentsJson", new TableInfo.Column("documentsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormSubmissions.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormSubmissions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFormSubmissions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFormSubmissions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFormSubmissions = new TableInfo("form_submissions", _columnsFormSubmissions, _foreignKeysFormSubmissions, _indicesFormSubmissions);
        final TableInfo _existingFormSubmissions = TableInfo.read(db, "form_submissions");
        if (!_infoFormSubmissions.equals(_existingFormSubmissions)) {
          return new RoomOpenHelper.ValidationResult(false, "form_submissions(com.momokotle.agent.database.entity.FormSubmissionEntity).\n"
                  + " Expected:\n" + _infoFormSubmissions + "\n"
                  + " Found:\n" + _existingFormSubmissions);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ac4204fc70ee5f42232f5b644b70cf19", "e12c6532cbd19a04f4d3b90f409da621");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "transactions","saved_contacts","profit_rules","custom_flows","store_orders","form_submissions");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `transactions`");
      _db.execSQL("DELETE FROM `saved_contacts`");
      _db.execSQL("DELETE FROM `profit_rules`");
      _db.execSQL("DELETE FROM `custom_flows`");
      _db.execSQL("DELETE FROM `store_orders`");
      _db.execSQL("DELETE FROM `form_submissions`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
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
    _typeConvertersMap.put(TransactionDao.class, TransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SavedContactDao.class, SavedContactDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProfitRuleDao.class, ProfitRuleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CustomFlowDao.class, CustomFlowDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StoreOrderDao.class, StoreOrderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FormSubmissionDao.class, FormSubmissionDao_Impl.getRequiredConverters());
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
  public TransactionDao transactionDao() {
    if (_transactionDao != null) {
      return _transactionDao;
    } else {
      synchronized(this) {
        if(_transactionDao == null) {
          _transactionDao = new TransactionDao_Impl(this);
        }
        return _transactionDao;
      }
    }
  }

  @Override
  public SavedContactDao savedContactDao() {
    if (_savedContactDao != null) {
      return _savedContactDao;
    } else {
      synchronized(this) {
        if(_savedContactDao == null) {
          _savedContactDao = new SavedContactDao_Impl(this);
        }
        return _savedContactDao;
      }
    }
  }

  @Override
  public ProfitRuleDao profitRuleDao() {
    if (_profitRuleDao != null) {
      return _profitRuleDao;
    } else {
      synchronized(this) {
        if(_profitRuleDao == null) {
          _profitRuleDao = new ProfitRuleDao_Impl(this);
        }
        return _profitRuleDao;
      }
    }
  }

  @Override
  public CustomFlowDao customFlowDao() {
    if (_customFlowDao != null) {
      return _customFlowDao;
    } else {
      synchronized(this) {
        if(_customFlowDao == null) {
          _customFlowDao = new CustomFlowDao_Impl(this);
        }
        return _customFlowDao;
      }
    }
  }

  @Override
  public StoreOrderDao storeOrderDao() {
    if (_storeOrderDao != null) {
      return _storeOrderDao;
    } else {
      synchronized(this) {
        if(_storeOrderDao == null) {
          _storeOrderDao = new StoreOrderDao_Impl(this);
        }
        return _storeOrderDao;
      }
    }
  }

  @Override
  public FormSubmissionDao formSubmissionDao() {
    if (_formSubmissionDao != null) {
      return _formSubmissionDao;
    } else {
      synchronized(this) {
        if(_formSubmissionDao == null) {
          _formSubmissionDao = new FormSubmissionDao_Impl(this);
        }
        return _formSubmissionDao;
      }
    }
  }
}

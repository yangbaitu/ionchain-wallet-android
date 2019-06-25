package org.ionc.wallet.greendaogen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import org.ionc.wallet.bean.TxRecordBeanOutHelper;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TX_RECORD_BEAN_OUT_HELPER".
*/
public class TxRecordBeanOutHelperDao extends AbstractDao<TxRecordBeanOutHelper, Long> {

    public static final String TABLENAME = "TX_RECORD_BEAN_OUT_HELPER";

    /**
     * Properties of entity TxRecordBeanOutHelper.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property IndexMax = new Property(1, Long.class, "indexMax", false, "INDEX_MAX");
        public final static Property PublicKey = new Property(2, String.class, "publicKey", false, "PUBLIC_KEY");
    }


    public TxRecordBeanOutHelperDao(DaoConfig config) {
        super(config);
    }
    
    public TxRecordBeanOutHelperDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TX_RECORD_BEAN_OUT_HELPER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"INDEX_MAX\" INTEGER," + // 1: indexMax
                "\"PUBLIC_KEY\" TEXT);"); // 2: publicKey
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TX_RECORD_BEAN_OUT_HELPER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TxRecordBeanOutHelper entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long indexMax = entity.getIndexMax();
        if (indexMax != null) {
            stmt.bindLong(2, indexMax);
        }
 
        String publicKey = entity.getPublicKey();
        if (publicKey != null) {
            stmt.bindString(3, publicKey);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TxRecordBeanOutHelper entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long indexMax = entity.getIndexMax();
        if (indexMax != null) {
            stmt.bindLong(2, indexMax);
        }
 
        String publicKey = entity.getPublicKey();
        if (publicKey != null) {
            stmt.bindString(3, publicKey);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TxRecordBeanOutHelper readEntity(Cursor cursor, int offset) {
        TxRecordBeanOutHelper entity = new TxRecordBeanOutHelper( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // indexMax
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // publicKey
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TxRecordBeanOutHelper entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIndexMax(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setPublicKey(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TxRecordBeanOutHelper entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TxRecordBeanOutHelper entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TxRecordBeanOutHelper entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

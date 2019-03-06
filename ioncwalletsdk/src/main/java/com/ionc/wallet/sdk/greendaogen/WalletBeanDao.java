package com.ionc.wallet.sdk.greendaogen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ionc.wallet.sdk.bean.WalletBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WALLET_BEAN".
*/
public class WalletBeanDao extends AbstractDao<WalletBean, Long> {

    public static final String TABLENAME = "WALLET_BEAN";

    /**
     * Properties of entity WalletBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PrivateKey = new Property(1, String.class, "privateKey", false, "PRIVATE_KEY");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Address = new Property(3, String.class, "address", false, "ADDRESS");
        public final static Property Publickey = new Property(4, String.class, "publickey", false, "PUBLICKEY");
        public final static Property Balance = new Property(5, String.class, "balance", false, "BALANCE");
        public final static Property Keystore = new Property(6, String.class, "keystore", false, "KEYSTORE");
        public final static Property Password = new Property(7, String.class, "password", false, "PASSWORD");
        public final static Property MIconIdex = new Property(8, int.class, "mIconIdex", false, "M_ICON_IDEX");
        public final static Property Mnemonic = new Property(9, String.class, "mnemonic", false, "MNEMONIC");
        public final static Property Choosen = new Property(10, boolean.class, "choosen", false, "CHOOSEN");
        public final static Property IsMainWallet = new Property(11, boolean.class, "isMainWallet", false, "IS_MAIN_WALLET");
    }


    public WalletBeanDao(DaoConfig config) {
        super(config);
    }
    
    public WalletBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WALLET_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"PRIVATE_KEY\" TEXT," + // 1: privateKey
                "\"NAME\" TEXT," + // 2: name
                "\"ADDRESS\" TEXT," + // 3: address
                "\"PUBLICKEY\" TEXT," + // 4: publickey
                "\"BALANCE\" TEXT," + // 5: balance
                "\"KEYSTORE\" TEXT," + // 6: keystore
                "\"PASSWORD\" TEXT," + // 7: password
                "\"M_ICON_IDEX\" INTEGER NOT NULL ," + // 8: mIconIdex
                "\"MNEMONIC\" TEXT," + // 9: mnemonic
                "\"CHOOSEN\" INTEGER NOT NULL ," + // 10: choosen
                "\"IS_MAIN_WALLET\" INTEGER NOT NULL );"); // 11: isMainWallet
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WALLET_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, WalletBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String privateKey = entity.getPrivateKey();
        if (privateKey != null) {
            stmt.bindString(2, privateKey);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
 
        String publickey = entity.getPublickey();
        if (publickey != null) {
            stmt.bindString(5, publickey);
        }
 
        String balance = entity.getBalance();
        if (balance != null) {
            stmt.bindString(6, balance);
        }
 
        String keystore = entity.getKeystore();
        if (keystore != null) {
            stmt.bindString(7, keystore);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(8, password);
        }
        stmt.bindLong(9, entity.getMIconIdex());
 
        String mnemonic = entity.getMnemonic();
        if (mnemonic != null) {
            stmt.bindString(10, mnemonic);
        }
        stmt.bindLong(11, entity.getChoosen() ? 1L: 0L);
        stmt.bindLong(12, entity.getIsMainWallet() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, WalletBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String privateKey = entity.getPrivateKey();
        if (privateKey != null) {
            stmt.bindString(2, privateKey);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
 
        String publickey = entity.getPublickey();
        if (publickey != null) {
            stmt.bindString(5, publickey);
        }
 
        String balance = entity.getBalance();
        if (balance != null) {
            stmt.bindString(6, balance);
        }
 
        String keystore = entity.getKeystore();
        if (keystore != null) {
            stmt.bindString(7, keystore);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(8, password);
        }
        stmt.bindLong(9, entity.getMIconIdex());
 
        String mnemonic = entity.getMnemonic();
        if (mnemonic != null) {
            stmt.bindString(10, mnemonic);
        }
        stmt.bindLong(11, entity.getChoosen() ? 1L: 0L);
        stmt.bindLong(12, entity.getIsMainWallet() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public WalletBean readEntity(Cursor cursor, int offset) {
        WalletBean entity = new WalletBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // privateKey
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // address
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // publickey
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // balance
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // keystore
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // password
            cursor.getInt(offset + 8), // mIconIdex
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // mnemonic
            cursor.getShort(offset + 10) != 0, // choosen
            cursor.getShort(offset + 11) != 0 // isMainWallet
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, WalletBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPrivateKey(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPublickey(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBalance(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setKeystore(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPassword(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setMIconIdex(cursor.getInt(offset + 8));
        entity.setMnemonic(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setChoosen(cursor.getShort(offset + 10) != 0);
        entity.setIsMainWallet(cursor.getShort(offset + 11) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(WalletBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(WalletBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(WalletBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

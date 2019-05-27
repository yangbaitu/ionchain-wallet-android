package org.ionc.wallet.greendaogen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import org.ionc.wallet.bean.WalletBeanNew;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WALLET_BEAN_NEW".
*/
public class WalletBeanNewDao extends AbstractDao<WalletBeanNew, Long> {

    public static final String TABLENAME = "WALLET_BEAN_NEW";

    /**
     * Properties of entity WalletBeanNew.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PrivateKey = new Property(1, String.class, "privateKey", false, "PRIVATE_KEY");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Address = new Property(3, String.class, "address", false, "ADDRESS");
        public final static Property Public_key = new Property(4, String.class, "public_key", false, "PUBLIC_KEY");
        public final static Property Balance = new Property(5, String.class, "balance", false, "BALANCE");
        public final static Property Keystore = new Property(6, String.class, "keystore", false, "KEYSTORE");
        public final static Property Password = new Property(7, String.class, "password", false, "PASSWORD");
        public final static Property MIconIndex = new Property(8, Integer.class, "mIconIndex", false, "M_ICON_INDEX");
        public final static Property Mnemonic = new Property(9, String.class, "mnemonic", false, "MNEMONIC");
        public final static Property Chosen = new Property(10, boolean.class, "chosen", false, "CHOSEN");
        public final static Property IsMainWallet = new Property(11, boolean.class, "isMainWallet", false, "IS_MAIN_WALLET");
        public final static Property Rmb = new Property(12, String.class, "rmb", false, "RMB");
        public final static Property Light = new Property(13, boolean.class, "light", false, "LIGHT");
    }


    public WalletBeanNewDao(DaoConfig config) {
        super(config);
    }
    
    public WalletBeanNewDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WALLET_BEAN_NEW\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PRIVATE_KEY\" TEXT," + // 1: privateKey
                "\"NAME\" TEXT," + // 2: name
                "\"ADDRESS\" TEXT," + // 3: address
                "\"PUBLIC_KEY\" TEXT," + // 4: public_key
                "\"BALANCE\" TEXT," + // 5: balance
                "\"KEYSTORE\" TEXT," + // 6: keystore
                "\"PASSWORD\" TEXT," + // 7: password
                "\"M_ICON_INDEX\" INTEGER," + // 8: mIconIndex
                "\"MNEMONIC\" TEXT," + // 9: mnemonic
                "\"CHOSEN\" INTEGER NOT NULL ," + // 10: chosen
                "\"IS_MAIN_WALLET\" INTEGER NOT NULL ," + // 11: isMainWallet
                "\"RMB\" TEXT," + // 12: rmb
                "\"LIGHT\" INTEGER NOT NULL );"); // 13: light
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WALLET_BEAN_NEW\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, WalletBeanNew entity) {
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
 
        String public_key = entity.getPublic_key();
        if (public_key != null) {
            stmt.bindString(5, public_key);
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
 
        Integer mIconIndex = entity.getMIconIndex();
        if (mIconIndex != null) {
            stmt.bindLong(9, mIconIndex);
        }
 
        String mnemonic = entity.getMnemonic();
        if (mnemonic != null) {
            stmt.bindString(10, mnemonic);
        }
        stmt.bindLong(11, entity.getChosen() ? 1L: 0L);
        stmt.bindLong(12, entity.getIsMainWallet() ? 1L: 0L);
 
        String rmb = entity.getRmb();
        if (rmb != null) {
            stmt.bindString(13, rmb);
        }
        stmt.bindLong(14, entity.getLight() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, WalletBeanNew entity) {
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
 
        String public_key = entity.getPublic_key();
        if (public_key != null) {
            stmt.bindString(5, public_key);
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
 
        Integer mIconIndex = entity.getMIconIndex();
        if (mIconIndex != null) {
            stmt.bindLong(9, mIconIndex);
        }
 
        String mnemonic = entity.getMnemonic();
        if (mnemonic != null) {
            stmt.bindString(10, mnemonic);
        }
        stmt.bindLong(11, entity.getChosen() ? 1L: 0L);
        stmt.bindLong(12, entity.getIsMainWallet() ? 1L: 0L);
 
        String rmb = entity.getRmb();
        if (rmb != null) {
            stmt.bindString(13, rmb);
        }
        stmt.bindLong(14, entity.getLight() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public WalletBeanNew readEntity(Cursor cursor, int offset) {
        WalletBeanNew entity = new WalletBeanNew( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // privateKey
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // address
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // public_key
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // balance
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // keystore
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // password
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // mIconIndex
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // mnemonic
            cursor.getShort(offset + 10) != 0, // chosen
            cursor.getShort(offset + 11) != 0, // isMainWallet
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // rmb
            cursor.getShort(offset + 13) != 0 // light
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, WalletBeanNew entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPrivateKey(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPublic_key(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBalance(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setKeystore(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPassword(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setMIconIndex(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setMnemonic(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setChosen(cursor.getShort(offset + 10) != 0);
        entity.setIsMainWallet(cursor.getShort(offset + 11) != 0);
        entity.setRmb(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setLight(cursor.getShort(offset + 13) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(WalletBeanNew entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(WalletBeanNew entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(WalletBeanNew entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

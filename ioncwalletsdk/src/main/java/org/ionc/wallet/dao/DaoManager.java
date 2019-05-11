package org.ionc.wallet.dao;


import org.ionc.wallet.daohelper.MyOpenHelper;
import org.ionc.wallet.greendaogen.DaoMaster;
import org.ionc.wallet.greendaogen.DaoSession;
import org.ionc.wallet.sdk.IONCWalletSDK;

import static org.ionc.wallet.constant.ConstanParams.DB_NAME;

public class DaoManager {
    private static DaoManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DaoManager() {
        MyOpenHelper devOpenHelper = new MyOpenHelper(IONCWalletSDK.appContext, DB_NAME, null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public static DaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new DaoManager();
        }
        return mInstance;
    }
}

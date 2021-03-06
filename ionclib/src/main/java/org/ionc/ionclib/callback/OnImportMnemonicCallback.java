package org.ionc.ionclib.callback;


import org.ionc.ionclib.bean.WalletBeanNew;

/**
*
* 导出助记词的回调接口
*/
public interface OnImportMnemonicCallback {

    /**
     * 导出成功
     *
     * @param walletBean 钱包
     */
    void onImportMnemonicSuccess(WalletBeanNew walletBean);


    /**
     * 导出失败
     *
     * @param error 失败原因
     */
    void onImportMnemonicFailure(String error);

}

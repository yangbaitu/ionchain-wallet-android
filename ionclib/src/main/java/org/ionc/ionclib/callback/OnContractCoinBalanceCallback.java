package org.ionc.ionclib.callback;

public interface OnContractCoinBalanceCallback {
    /**
     * @param position
     * @param balance 余额
     */
    void onContractCoinBalanceSuccess(int position, String balance);

    /**
     * @param position
     * @param error 失败
     */
    void onContractCoinBalanceFailure(int position, String error);

}

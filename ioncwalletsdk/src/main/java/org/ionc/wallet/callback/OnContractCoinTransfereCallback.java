package org.ionc.wallet.callback;

public interface OnContractCoinTransfereCallback {
    /**
     * @param position
     * @param balance  余额
     */
    void onContractCoinTransferSuccess(String balance);

    /**
     * @param position
     * @param error    失败
     */
    void onContractCoinTransferFailure(String error);

}

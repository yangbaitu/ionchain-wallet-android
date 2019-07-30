package org.ionchain.wallet.view.fragment.txrecord;

import org.ionc.wallet.bean.TxRecordBean;
import org.ionc.wallet.callback.OnTxRecordFromNodeCallback;
import org.ionchain.wallet.view.fragment.AssetFragment;

public class TxRecordDoneFragment extends AbsTxRecordBaseFragment implements OnTxRecordFromNodeCallback,
        AssetFragment.OnPullToRefreshCallback {


    @Override
    protected int getType() {
        return TYPE_DONE;
    }


    /**
     * @param txRecordBean 有新的交易记录的时候
     */
    @Override
    public void onNewTxRecordByTx(TxRecordBean txRecordBean) {
        mListDone.add(0, txRecordBean);
        super.onNewTxRecordByTx(txRecordBean);
    }
}
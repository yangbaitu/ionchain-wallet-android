package org.ionchain.wallet.mvp.model.txrecoder;


import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.ionc.wallet.utils.LoggerUtils;
import org.ionchain.wallet.App;
import org.ionchain.wallet.R;
import org.ionchain.wallet.bean.TxRecordBeanTemp;
import org.ionchain.wallet.mvp.callback.OnTxRecordCallback;
import org.ionchain.wallet.utils.NetUtils;

import static org.ionchain.wallet.utils.UrlUtils.getTxRecordUrl;

/**
 * AUTHOR binny
 * <p>
 * <p>
 * TIME 2018/11/13 16:15
 */
public class TxRecordModel implements ITxRecoderModel {
    final HttpParams params = new HttpParams();


    @Override
    public void getTxRecord(boolean isLoadMore, String type, String key, String pageNumber, String pageSize, final OnTxRecordCallback callback) {
        params.clear();
        params.put("type", type);
        params.put("key", key);
        params.put("pageNumber", pageNumber);
        params.put("pageSize", pageSize);
        NetUtils.get(getTxRecordUrl(), params, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String json = response.body();
                LoggerUtils.i(json);
                TxRecordBeanTemp txRecordBeanTemp = NetUtils.gsonToBean(json, TxRecordBeanTemp.class);
                if (txRecordBeanTemp == null || txRecordBeanTemp.getData() == null || txRecordBeanTemp.getData().getData() == null) {
                    callback.onTxRecordFailure(App.mContext.getResources().getString(R.string.error_data_parase));
                    return;
                }
                TxRecordBeanTemp.DataBean beans = txRecordBeanTemp.getData();
                if (!isLoadMore) {
                    callback.onTxRecordRefreshSuccess(beans);
                } else {
                    callback.onTxRecordLoadMoreSuccess(beans);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                callback.onLoadFinish();
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                callback.onLoadStart();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                callback.onTxRecordFailure(App.mContext.getString(R.string.error_net_tx_recorder));
            }
        }, callback);
    }
}

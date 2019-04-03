package org.ionchain.wallet.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.ionc.wallet.sdk.IONCWalletSDK;
import org.ionc.wallet.adapter.CommonAdapter;
import org.ionc.wallet.bean.WalletBean;
import org.ionc.wallet.callback.OnBalanceCallback;
import org.ionc.wallet.utils.Logger;
import org.ionc.wallet.utils.StringUtils;
import org.ionchain.wallet.App;
import org.ionchain.wallet.R;
import org.ionchain.wallet.adapter.device.DeviceViewHelper;
import org.ionchain.wallet.adapter.morewallet.MoreWalletViewHelper;
import org.ionchain.wallet.bean.DeviceBean;
import org.ionchain.wallet.mvp.callback.OnBindDeviceCallback;
import org.ionchain.wallet.mvp.callback.OnDeviceListCallback;
import org.ionchain.wallet.mvp.callback.OnUnbindDeviceButtonClickedListener;
import org.ionchain.wallet.mvp.callback.OnUnbindDeviceCallback;
import org.ionchain.wallet.mvp.presenter.Presenter;
import org.ionchain.wallet.mvp.view.activity.ShowAddressActivity;
import org.ionchain.wallet.mvp.view.activity.createwallet.CreateWalletActivity;
import org.ionchain.wallet.mvp.view.activity.importmode.SelectImportModeActivity;
import org.ionchain.wallet.mvp.view.activity.modify.ModifyAndExportWalletActivity;
import org.ionchain.wallet.mvp.view.activity.sdk.SDKCreateActivity;
import org.ionchain.wallet.mvp.view.activity.sdk.SDKSelectCreateModeWalletActivity;
import org.ionchain.wallet.mvp.view.activity.transaction.TxActivity;
import org.ionchain.wallet.mvp.view.activity.transaction.TxRecoderActivity;
import org.ionchain.wallet.mvp.view.base.AbsBaseFragment;
import org.ionchain.wallet.utils.SoftKeyboardUtil;
import org.ionchain.wallet.utils.ToastUtil;
import org.ionchain.wallet.widget.DialogBindDevice;
import org.ionchain.wallet.widget.PopupWindowBuilder;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static org.ionc.wallet.constant.ConstantUrl.ETH_CHAIN_NODE;
import static org.ionc.wallet.constant.ConstantUrl.IONC_CHAIN_NODE;
import static org.ionchain.wallet.App.SDK_Debug;
import static org.ionchain.wallet.constant.ConstantParams.SERIALIZABLE_DATA;


public class HomeFragment extends AbsBaseFragment implements
        PopupWindowBuilder.OnItemBuilder,
        OnRefreshListener,
        OnBindDeviceCallback,
        OnDeviceListCallback,
        OnUnbindDeviceButtonClickedListener,
        OnUnbindDeviceCallback,
        OnBalanceCallback {


    private SmartRefreshLayout mRefresh;
    private WalletBean mCurrentWallet;
    private TextView walletNameTx;
    private ImageView moreWallet;
    private RelativeLayout modifyWallet;
    private TextView walletBalanceTx;
    private TextView walletBalanceTxETH;
    private TextView tx_recoder;
    private PopupWindowBuilder mBuilder;
    private ImageView addDevice;
    private ImageView wallet_logo;

    private CommonAdapter mAdapterMore;
    private List<WalletBean> mMoreWallets = new ArrayList<>();
    private List<WalletBean> mMoreWalletsTemp = new ArrayList<>();
    private ListView mMoreWalletListView;//更过钱包

    private CommonAdapter mAdapterDeviceLv;

    private List<DeviceBean.DataBean> mDataBeans = new ArrayList<>();

    private Presenter mPresenter;


    private LinearLayout tx_out_ll;
    private LinearLayout tx_in_ll;

    private DialogBindDevice mDialogBindCardWithWallet;//绑定设备的弹窗
    private int mUnbindPos = 0;

    private void initListViewHeaderViews(View rootView) {
        walletNameTx = rootView.findViewById(R.id.wallet_name_tv);
        moreWallet = rootView.findViewById(R.id.wallet_list);
        modifyWallet = rootView.findViewById(R.id.modify_wallet);
        walletBalanceTx = rootView.findViewById(R.id.wallet_balance_tx);
        walletBalanceTxETH = rootView.findViewById(R.id.wallet_balance_tx_eth);
        addDevice = rootView.findViewById(R.id.add_device);
        wallet_logo = rootView.findViewById(R.id.wallet_logo);
        tx_in_ll = rootView.findViewById(R.id.tx_in_ll);
        tx_out_ll = rootView.findViewById(R.id.tx_out_ll);
        tx_recoder = rootView.findViewById(R.id.tx_recoder_tv);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mImmersionBar
                    .statusBarColor(R.color.blue_top)
                    .statusBarDarkFont(false).execute();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SoftKeyboardUtil.hideSoftKeyboard(getActivity());
        mCurrentWallet = IONCWalletSDK.getInstance().getMainWallet();
        if (mCurrentWallet == null) {
            ToastUtil.showLong("您还没有钱包,请您先创建或导入钱包");
//            跳转到钱包创建或者导入界面
            return;
        }
        walletNameTx.setText(mCurrentWallet.getName());
//        if (!StringUtils.isEmpty(mCurrentWallet.getBalance())) {
//            walletBalanceTx.setText(mCurrentWallet.getBalance());// 钱包金额
//        } else {
//            walletBalanceTx.setText("0.0000");// 钱包金额
//        }
        int id = mCurrentWallet.getMIconIdex();
        wallet_logo.setImageResource(App.sRandomHeader[id]);
        IONCWalletSDK.getInstance().getDoubleWalletBalance(mCurrentWallet.getAddress(), this);
        getDeviceList();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    /**
     * @param view 实例化的ivew
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void initView(View view) {

        //设备列表
        ListView mDevicesLv = (ListView) view.findViewById(R.id.devices_items);
        //lv头部
        View lvHeader = LayoutInflater.from(getActivity()).inflate(R.layout.header_lv_home_page, null);
        initListViewHeaderViews(lvHeader);

        mDevicesLv.addHeaderView(lvHeader);

        mRefresh = view.findViewById(R.id.refresh_asset);
        mRefresh.setOnRefreshListener(this);
        mBuilder = new PopupWindowBuilder(getActivity(), R.layout.item_popup_list_layout, this);

        mAdapterDeviceLv = new CommonAdapter(getActivity(), mDataBeans, R.layout.item_devices_layout, new DeviceViewHelper(this));
        mDevicesLv.setAdapter(mAdapterDeviceLv);



        /*
         * 更多钱包
         * */
        moreWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int screenHeight = metrics.heightPixels;
                int screenWidth = metrics.widthPixels;
                mBuilder.setAnimationStyle(R.style.push_left_in_out)
                        .setWidth((int) (screenWidth * 0.6))
                        .setHeight(screenHeight)
                        .setLocation(mRefresh, Gravity.END | Gravity.TOP, 0, 0)
                        .build()
                        .show();
                mMoreWalletListView.smoothScrollToPosition(0);

                mMoreWalletsTemp = IONCWalletSDK.getInstance().getAllWallet();
                if (mMoreWalletsTemp != null && mMoreWalletsTemp.size() > 0) {
                    mMoreWallets.clear();
                    mMoreWallets.addAll(mMoreWalletsTemp);
                    mAdapterMore.notifyDataSetChanged();
                }

            }

        });
        /*
         * 修改钱包
         * */
        wallet_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(ModifyAndExportWalletActivity.class, SERIALIZABLE_DATA, mCurrentWallet);
            }
        });
        /*
         * 绑定设备
         * */
        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 10);

            }
        });

        /*
         * 转账
         * */
        tx_out_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(mCurrentWallet.getBalance())||"0.0000".equals(mCurrentWallet.getBalance())) {
                    ToastUtil.showLong("此钱包余不足！");
                    return;
                }
                skip(TxActivity.class, "wallet", mCurrentWallet);
            }
        });
        /*
         * 转账
         * */
        tx_in_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAddressActivity.class);
                intent.putExtra("msg", mCurrentWallet.getAddress());
                skip(intent);
            }
        });
        /*
         * 交易记录
         * */
        tx_recoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TxRecoderActivity.class);
                intent.putExtra("address", mCurrentWallet.getAddress());
                skip(intent);
            }
        });

    }


    @Override
    protected void initData() {
        mPresenter = new Presenter();
        mPresenter.initHomePageModel();

    }

    private void getDeviceList() {
        mPresenter.getCurrentWalletDevicesList(mCurrentWallet, this);
    }


    @Override
    protected void initImmersionBar() {
        mImmersionBar
                .statusBarColor(R.color.blue_top)
                .execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10) {
            final String result = data.getStringExtra("result");
            Logger.i("result", result);

        }
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 10) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    mDialogBindCardWithWallet = new DialogBindDevice(getActivity());
                    mDialogBindCardWithWallet.setMessageText(result);
                    mDialogBindCardWithWallet.setLeftBtnClickedListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialogBindCardWithWallet.dismiss();
                        }
                    });
                    mDialogBindCardWithWallet.setRightBtnClickedListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialogBindCardWithWallet.dismiss();
                            String address = mCurrentWallet.getAddress();
                            mPresenter.bindDeviceToWallet(address, result, HomeFragment.this);
                        }
                    });
                    mDialogBindCardWithWallet.show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtil.showLong("解析二维码失败");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initItems(final PopupWindow instance, View contentView) {
        mMoreWalletsTemp = IONCWalletSDK.getInstance().getAllWallet();
        if (mMoreWalletsTemp != null && mMoreWalletsTemp.size() > 0) {
            mMoreWallets.clear();
//            Collections.reverse(mMoreWalletsTemp);
            mMoreWallets.addAll(mMoreWalletsTemp);
        }
        mMoreWalletListView = contentView.findViewById(R.id.data_list);
        LinearLayout scan_popu = contentView.findViewById(R.id.scan_popu);
        LinearLayout new_ll = contentView.findViewById(R.id.new_ll);
        mAdapterMore = new CommonAdapter(getActivity(), mMoreWallets, R.layout.item_popup_list, new MoreWalletViewHelper());
        mMoreWalletListView.setAdapter(mAdapterMore);
        scan_popu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.SDK_Debug) {
                    skip(SDKSelectCreateModeWalletActivity.class);
                } else {
                    skip(SelectImportModeActivity.class);//
                }
                instance.dismiss();
            }
        });
        new_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SDK_Debug) {
                    skip(SDKCreateActivity.class);//
                } else {
                    skip(CreateWalletActivity.class);
                }
                instance.dismiss();
            }
        });
        mMoreWalletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.i(TAG, "onItemClick: ");

                for (int i = 0; i < mMoreWallets.size(); i++) {
                    if (i!=position) {
                        mMoreWallets.get(i).setIsMainWallet(false);
                    }else {
                        mMoreWallets.get(i).setIsMainWallet(true);
                    }
                    IONCWalletSDK.getInstance().saveWallet(mMoreWallets.get(i));
                }
                mCurrentWallet = mMoreWallets.get(position);
                walletNameTx.setText(mCurrentWallet.getName());
//                if (!StringUtils.isEmpty(mCurrentWallet.getBalance())) {
//                    walletBalanceTx.setText(mCurrentWallet.getBalance());// 钱包金额
//                } else {
//                    walletBalanceTx.setText("0.0000");// 钱包金额
//                }
                int ids = mCurrentWallet.getMIconIdex();
                wallet_logo.setImageResource(App.sRandomHeader[ids]);
                IONCWalletSDK.getInstance().getDoubleWalletBalance(mCurrentWallet.getAddress(), HomeFragment.this);
                mDataBeans.clear();
                mAdapterDeviceLv.notifyDataSetChanged();
                getDeviceList();
                instance.dismiss();
            }
        });

    }


    @Override
    public void onLoadStart() {
        Logger.i(TAG, "onLoadStart: ");
    }

    @Override
    public void onLoadFinish() {
        Logger.i(TAG, "onLoadFinish: ");
        mRefresh.finishRefresh();
    }


    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        IONCWalletSDK.getInstance().getDoubleWalletBalance(mCurrentWallet.getAddress(), HomeFragment.this);
        getDeviceList();
    }


    /**
     * @param dataBean 成功
     */
    @Override
    public void onBindSuccess(DeviceBean.DataBean dataBean) {
        DeviceBean.DataBean bean = new DeviceBean.DataBean();
        bean.setCksn(dataBean.getCksn());
        bean.setCreated_at(dataBean.getCreated_at());
        bean.setId(dataBean.getId());
        bean.setImage_url(dataBean.getImage_url());
        bean.setName(dataBean.getName());
        bean.setSystem(dataBean.getSystem());
        mDataBeans.add(bean);
        mAdapterDeviceLv.notifyDataSetChanged();
    }

    /**
     * @param result 绑定失败
     */
    @Override
    public void onBindFailure(String result) {
        Logger.i(TAG, "onBindFailure: " + result);
    }

    /**
     * @param list 设备列表
     */
    @Override
    public void onDeviceListSuccess(List<DeviceBean.DataBean> list) {
        Logger.i(TAG, "onDeviceListSuccess: " + list.toString());
        mDataBeans.clear();
        mDataBeans.addAll(list);
        mAdapterDeviceLv.notifyDataSetChanged();
    }

    /**
     * @param errorMessage 失败信息 获取设备列表失败
     */
    @Override
    public void onDeviceListFailure(String errorMessage) {
        Logger.i(TAG, "onDeviceListFailure: " + errorMessage);
    }

    /**
     * 解绑设备
     *
     * @param cksn
     * @param position
     */
    @Override
    public void onUnbindButtonClick(final String cksn, int position) {
        Logger.i(TAG, "onUnbindButtonClick: " + cksn);
        mUnbindPos = position;
        mDialogBindCardWithWallet = new DialogBindDevice(getActivity());
        mDialogBindCardWithWallet.setMessageText(cksn);
        mDialogBindCardWithWallet.setTitleText("确定解绑设备?");
        mDialogBindCardWithWallet.setLeftBtnClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogBindCardWithWallet.dismiss();
            }
        });
        mDialogBindCardWithWallet.setRightBtnClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogBindCardWithWallet.dismiss();
                mPresenter.unbindDeviceToWallet(mCurrentWallet.getAddress(), cksn, HomeFragment.this);
            }
        });
        mDialogBindCardWithWallet.show();


    }

    /**
     * 解绑定成功
     */
    @Override
    public void onUnbindSuccess(DeviceBean.DataBean dataBean) {
        mDataBeans.remove(mUnbindPos);
        mAdapterDeviceLv.notifyDataSetChanged();
    }

    /**
     * @param result 解绑定失败
     */
    @Override
    public void onUnbindFailure(String result) {
        Logger.i(TAG, "onUnbindFailure: " + result);
    }

    /**
     * @param ballance 余额 获取成功
     * @param nodeUrlTag
     */
    @Override
    public void onBalanceSuccess(String ballance, String nodeUrlTag) {
        switch (nodeUrlTag){
            case IONC_CHAIN_NODE:
                walletBalanceTx.setText(ballance);
                break;
            case ETH_CHAIN_NODE:
                walletBalanceTxETH.setText(ballance);
                break;
        }
    }

    /**
     * @param error 失败信息 获取余额失败
     */
    @Override
    public void onBalanceFailure(String error) {
        Logger.i(TAG, "onCreateFailure: " + error);
        ToastUtil.showToastLonger(error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBuilder.release();
    }
}

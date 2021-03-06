package org.ionc.wallet.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lzy.okgo.OkGo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.ionc.ionclib.bean.TxRecordBean;
import org.ionc.ionclib.bean.WalletBeanNew;
import org.ionc.ionclib.callback.OnBalanceCallback;
import org.ionc.ionclib.callback.OnTxRecordFromNodeCallback;
import org.ionc.ionclib.utils.ToastUtil;
import org.ionc.ionclib.web3j.IONCSDKTransfers;
import org.ionc.ionclib.web3j.IONCSDKWallet;
import org.ionc.wallet.App;
import org.ionc.wallet.bean.NodeBean;
import org.ionc.wallet.bean.USDExRmb;
import org.ionc.wallet.callback.OnIONCNodeCallback;
import org.ionc.wallet.constant.ConstantParams;
import org.ionc.wallet.model.ioncprice.callbcak.OnUSDExRateRMBCallback;
import org.ionc.wallet.model.ioncprice.callbcak.OnUSDPriceCallback;
import org.ionc.wallet.presenter.ioncrmb.PricePresenter;
import org.ionc.wallet.utils.LoggerUtils;
import org.ionc.wallet.utils.SPUtils;
import org.ionc.wallet.utils.SoftKeyboardUtil;
import org.ionc.wallet.view.activity.address.ShowAddressActivity;
import org.ionc.wallet.view.activity.create.CreateWalletActivity;
import org.ionc.wallet.view.activity.imports.SelectImportModeActivity;
import org.ionc.wallet.view.activity.modify.ModifyWalletActivity;
import org.ionc.wallet.view.activity.transaction.TxOutActivity;
import org.ionc.wallet.view.base.AbsBaseFragment;
import org.ionc.wallet.view.fragment.txrecord.TxRecordAllFragment;
import org.ionc.wallet.view.fragment.txrecord.TxRecordDoingFragment;
import org.ionc.wallet.view.fragment.txrecord.TxRecordDoneFragment;
import org.ionc.wallet.view.fragment.txrecord.TxRecordFailureFragment;
import org.ionc.wallet.view.fragment.txrecord.TxRecordPagerAdapter;
import org.ionc.wallet.view.widget.dialog.callback.OnDialogCheck12MnemonicCallbcak;
import org.ionc.wallet.view.widget.dialog.export.DialogTextMessage;
import org.ionc.wallet.view.widget.dialog.mnemonic.DialogCheckMnemonic;
import org.ionc.wallet.view.widget.dialog.mnemonic.DialogMnemonicShow;
import org.ionc.wallet.view.widget.dialog.more.MoreWalletDialog;
import org.ionchain.wallet.BuildConfig;
import org.ionchain.wallet.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.ionc.wallet.constant.ConstantActivitySkipTag.INTENT_FROM_MAIN_ACTIVITY;
import static org.ionc.wallet.constant.ConstantActivitySkipTag.INTENT_FROM_WHERE_TAG;
import static org.ionc.wallet.constant.ConstantCoinType.COIN_TYPE_CNY;
import static org.ionc.wallet.constant.ConstantCoinType.COIN_TYPE_IDR;
import static org.ionc.wallet.constant.ConstantCoinType.COIN_TYPE_KRW;
import static org.ionc.wallet.constant.ConstantCoinType.COIN_TYPE_USD;
import static org.ionc.wallet.constant.ConstantIntentParam.INTENT_PARAM_CURRENT_WALLET;
import static org.ionc.wallet.constant.ConstantNetCancelTag.NET_CANCEL_TAG_NODE;
import static org.ionc.wallet.constant.ConstantNetCancelTag.NET_CANCEL_TAG_USD_PRICE;
import static org.ionc.wallet.constant.ConstantNetCancelTag.NET_CANCEL_TAG_USD_RMB_RATE;
import static org.ionc.wallet.constant.ConstantParams.CURRENT_ADDRESS;
import static org.ionc.wallet.constant.ConstantParams.CURRENT_KSP;
import static org.ionc.wallet.constant.ConstantParams.DEFAULT_TRANSCATION_HASH_NULL;
import static org.ionc.wallet.constant.ConstantParams.INTENT_PARAME_WALLET_ADDRESS;
import static org.ionc.wallet.constant.ConstantParams.PARCELABLE_WALLET_BEAN;
import static org.ionc.wallet.constant.ConstantParams.TX_ACTIVITY_FOR_RESULT_CODE;
import static org.ionc.wallet.constant.ConstantParams.TX_ACTIVITY_RESULT;
import static org.ionc.wallet.utils.UrlUtils.getHostNode;
import static org.ionc.wallet.web3j.IONCTxRecords.saveTxRecordBean;


public class AssetFragment extends AbsBaseFragment implements
        OnRefreshListener,
        OnBalanceCallback,
        DialogMnemonicShow.OnSavedMnemonicCallback,
        DialogTextMessage.OnBtnClickedListener,
        OnDialogCheck12MnemonicCallbcak,
        OnUSDPriceCallback,
        OnUSDExRateRMBCallback,
        OnIONCNodeCallback, OnTxRecordFromNodeCallback, OnLoadMoreListener, MoreWalletDialog.OnMoreWalletDialogItemClickedListener {


    /**
     * 刷新容器
     */
    private SmartRefreshLayout mRefresh;
    /**
     * 当前展示的钱包
     */
    private WalletBeanNew mCurrentWallet;
    /**
     * 当前展示的钱包
     */
    private WalletBeanNew mOldWallet;
    /**
     * 钱包名字
     */
    private TextView mWalletNameTx;

    /**
     * 更多钱包入口
     */
    private ImageView mMoreWallet;

    /**
     * 当前钱包的余额
     */
    private TextView mIoncBalanceTx;

    /**
     * 离子比余额
     */
    private BigDecimal mIONCBalance;
    /**
     * 离子比美元总价格
     */
    private double mUSDPrice;
    /**
     * 离子比美元总价格
     */
    private BigDecimal mTotalUSDPrice;
    /**
     * 人民币余额
     */
    private TextView mRmbTx;
    private TextView mRmbIcon;

    /**
     * 提示用户备份钱包,在用户备份钱包的情况下
     */
    private TextView mPleaseBackupWallet;
    /**
     * 钱包的图标
     */
    private ImageView mWalletLogo;

    /**
     * 更多钱包
     */
    private List<WalletBeanNew> mMoreWallets = new ArrayList<>();
    /**
     * 更过钱包缓存
     */
    private List<WalletBeanNew> mMoreWalletsTemp = new ArrayList<>();


    /**
     * 转出
     */
    private LinearLayout mTxOutLl;
    /**
     * 转入
     */
    private LinearLayout mTxInLl;

    /**
     * 引导用户备份,助记词
     */
    private DialogMnemonicShow dialogMnemonic;

    /**
     * 币价信息
     */
    private PricePresenter mPricePresenter;
    private String mNodeIONC = getHostNode();

    private ClassicsHeader mRefreshHeader;

    /**
     * 交易记录的分类页面
     */
    private TabLayout tabLayout;
    /**
     * 交易记录的容器
     */
    private ViewPager viewPager;

    private List<AbsBaseFragment> mFragmentListTxRecord = new ArrayList<>();

    private TxRecordDoingFragment mTxRecordDoingFragment;
    private TxRecordAllFragment mTxRecordAllFragment;
    private TxRecordDoneFragment mTxRecordDoneFragment;
    private TxRecordFailureFragment mTxRecordFailureFragment;

    private String mOldAddress = "";
    private double mCNY = 0;
    private double mUS = 0;
    private double mKRW = 0;
    private double mIDR = 0;
    public static int NEW_WALLET_FOR_RESULT_CODE = 11;
    private MoreWalletDialog mMoreWalletDialog;
    private int mBalanceScale = 4;

    private String split = "    ";
    /**
     * 实例化资产页头部
     *
     * @param header
     */
    private void initListViewHeaderViews(View header) {
        mWalletNameTx = header.findViewById(R.id.wallet_name_tv);
        mMoreWallet = header.findViewById(R.id.wallet_list);
        mIoncBalanceTx = header.findViewById(R.id.ionc_balance_tx);
        mRmbTx = header.findViewById(R.id.rmb_balance_tx);
        mRmbIcon = header.findViewById(R.id.rmb_icon);
        mWalletLogo = header.findViewById(R.id.wallet_logo);
        mTxInLl = header.findViewById(R.id.tx_in_ll);
        mTxOutLl = header.findViewById(R.id.tx_out_ll);
        mPleaseBackupWallet = header.findViewById(R.id.please_backup_wallet);
        mIoncBalanceTx.setText("0000");
        mRmbTx.setText("0000");
    }

    /**
     * 主页fragment切换时调用
     * <p>
     * 更新钱包为主钱包
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void handleShow() {
        LoggerUtils.i("method", "handleShow" + "   AssetFragment");
        mImmersionBar.statusBarColor(R.color.main_color).statusBarDarkFont(false).execute();
        mCurrentWallet = IONCSDKWallet.getMainWallet();
        if (mCurrentWallet == null) {
            ToastUtil.showLong(getResources().getString(R.string.wallet_null_toast));
//            跳转到钱包创建或者导入界面
            return;
        }
        if (!mOldAddress.equals(mCurrentWallet.getAddress())) {
            setBackupTag();
            mOldAddress = mCurrentWallet.getAddress();
            mWalletNameTx.setText(mCurrentWallet.getName());
            Integer id = mCurrentWallet.getMIconIndex();
            mWalletLogo.setImageResource(App.sRandomHeader[id]);
            balance(); //回到前台 handleShow
            mTxRecordAllFragment.onAddressChanged(mCurrentWallet);
        }
        mRmbIcon.setText(split+ SPUtils.getInstance().getCoinType());
    }

    @Override
    protected void handleHidden() {
        LoggerUtils.i("method", "handleHidden" + "   AssetFragment");
        OkGo.cancelTag(OkGo.getInstance().getOkHttpClient(), NET_CANCEL_TAG_USD_PRICE);
        OkGo.cancelTag(OkGo.getInstance().getOkHttpClient(), NET_CANCEL_TAG_NODE);
        OkGo.cancelTag(OkGo.getInstance().getOkHttpClient(), NET_CANCEL_TAG_USD_RMB_RATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        SoftKeyboardUtil.hideSoftKeyboard(Objects.requireNonNull(mActivity));
        mCurrentWallet = IONCSDKWallet.getMainWallet();
        balance();
        mTxRecordAllFragment.initRecordWalletBean(mCurrentWallet);
        setBackupTag();
        mWalletNameTx.setText(mCurrentWallet.getName());
        Integer id = mCurrentWallet.getMIconIndex();
        mWalletLogo.setImageResource(App.sRandomHeader[id]);
        if (!TextUtils.isEmpty(mOldAddress) && !mOldAddress.equals(mCurrentWallet.getAddress())) {
            mTxRecordAllFragment.onAddressChanged(mCurrentWallet);
        }
        mOldAddress = mCurrentWallet.getAddress();

        setBalance();
    }

    /**
     * 如果用户备份了助记词,则助记词会从本机数据库消失,
     * 则不显示提示用户备份的信息,否则显示
     */
    private void setBackupTag() {
        if (!TextUtils.isEmpty(mCurrentWallet.getMnemonic())) {
            mPleaseBackupWallet.setVisibility(View.VISIBLE);
        } else {
            mPleaseBackupWallet.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_asset;
    }

    /**
     * @param view 实例化的ivew
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void initView(View view) {
        //lv头部
        View header = view.findViewById(R.id.header_lv_home_page);

        initListViewHeaderViews(header);
        mRefresh = view.findViewById(R.id.refresh_asset);
        mRefresh.setHeaderHeight(40);
        mRefresh.setFooterHeight(40);
        mRefresh.setHeaderMaxDragRate(2);//最大显示下拉高度/Header标准高度
        mRefresh.setFooterMaxDragRate(2);//最大显示下拉高度/Footer标准高度
        mRefresh.setHeaderTriggerRate(1);//触发刷新距离 与 HeaderHeight 的比率1.0.4
        mRefresh.setFooterTriggerRate(1);//触发加载距离 与 FooterHeight 的比率1.0.4
        mRefresh.setEnableAutoLoadMore(false);
        mRefreshHeader = view.findViewById(R.id.refresh_header);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setOnLoadMoreListener(this);
//        mRefresh.setEnableLoadMore(false);
        tabLayout = view.findViewById(R.id.tx_record_tabs);
        viewPager = view.findViewById(R.id.tx_record_content);

    }

    @Override
    protected void setListener() {
        super.setListener();


        /*
         * 修改钱包
         * */
        mWalletLogo.setOnClickListener(v -> {
            if (pleaseBackupWallet()) return;
            /*
             *  将币价信息携带过去
             */
            cancelGetNode();// 修改钱包
            skip(ModifyWalletActivity.class, PARCELABLE_WALLET_BEAN, mCurrentWallet);
        });


        /*
         * 转账
         * */
        mTxOutLl.setOnClickListener(v -> {
            if (pleaseBackupWallet()) return;
            Intent intent = new Intent(mActivity, TxOutActivity.class);
            intent.putExtra(INTENT_PARAM_CURRENT_WALLET, mCurrentWallet);
            intent.putExtra(CURRENT_ADDRESS, mCurrentWallet.getAddress());
            intent.putExtra(CURRENT_KSP, mCurrentWallet.getKeystore());
            cancelGetNode();//转账
            startActivityForResult(intent, TX_ACTIVITY_FOR_RESULT_CODE);
        });
        /*
         * 转入
         * */
        mTxInLl.setOnClickListener(v -> {
            if (pleaseBackupWallet()) return;
            Intent intent = new Intent(mActivity, ShowAddressActivity.class);
            intent.putExtra(INTENT_PARAME_WALLET_ADDRESS, mCurrentWallet.getAddress());
            cancelGetNode();  //转入
            skip(intent);
        });

        /*
         * 备份钱包
         * */
        mPleaseBackupWallet.setOnClickListener(v -> {
            String[] mnemonics = mCurrentWallet.getMnemonic().split(" ");
            dialogMnemonic = new DialogMnemonicShow(mActivity, mnemonics, AssetFragment.this);
            dialogMnemonic.show();
        });
        /*
         * 更多钱包
         * */
        mMoreWallet.setOnClickListener(v -> {

            mOldWallet = mCurrentWallet;//缓存切换前的钱包
            mMoreWalletDialog = new MoreWalletDialog(mActivity);
//            mMoreWalletsTemp = IONCSDKWallet.getAllWalletNew();
//            if (mMoreWalletsTemp != null && mMoreWalletsTemp.size() > 0) {
//                mMoreWallets.clear();
////            Collections.reverse(mMoreWalletsTemp);
//                mMoreWallets.addAll(mMoreWalletsTemp);
//            }
            mMoreWalletDialog.setMoreWalletListItemClickedLisenter(AssetFragment.this);

            mMoreWalletDialog.show();
        });
    }

    @Override
    public void onMoreWalletDialogItemClick(WalletBeanNew wallet, int position) {
        LoggerUtils.i(TAG, "onItemClick: ");

        //修改主钱包
        if (!mOldWallet.getAddress().endsWith(wallet.getAddress())) {
            mOldWallet.setIsMainWallet(false);
            IONCSDKWallet.updateWallet(mOldWallet);
            wallet.setIsMainWallet(true);
            IONCSDKWallet.updateWallet(wallet);
        }

        mCurrentWallet =wallet;
        mWalletNameTx.setText(mCurrentWallet.getName());
        setBackupTag();
        Integer ids = mCurrentWallet.getMIconIndex();
        mWalletLogo.setImageResource(App.sRandomHeader[ids]);
        mIoncBalanceTx.setText(mCurrentWallet.getBalance());
        mRmbTx.setText(mCurrentWallet.getRmb()); //切换时读取余额
        mMoreWalletDialog.dismiss();
        LoggerUtils.i("钱包切换 mOldAddress = " + mOldAddress);
        LoggerUtils.i("钱包切换 mNewAddress = " + mCurrentWallet.getAddress());
        if (!mOldAddress.equals(mCurrentWallet.getAddress())) {
            balance();//切换
            changeWallet();
        }
        mOldAddress = mCurrentWallet.getAddress();
        cancelGetNode();//切换
    }

    @Override
    public void onMoreWalletDialogImportBtnClick(MoreWalletDialog moreWalletDialog) {
        moreWalletDialog.dismiss();
        Intent intent = new Intent(mActivity, SelectImportModeActivity.class);
        startActivityForResult(intent, NEW_WALLET_FOR_RESULT_CODE);
    }

    @Override
    public void onMoreWalletDialogCreateBtnClick(MoreWalletDialog moreWalletDialog) {
        moreWalletDialog.dismiss();
        Intent intent = new Intent(mActivity, CreateWalletActivity.class);
        intent.putExtra(INTENT_FROM_WHERE_TAG, INTENT_FROM_MAIN_ACTIVITY);
        startActivityForResult(intent, NEW_WALLET_FOR_RESULT_CODE);
    }

    /**
     *
     * 这里有一个切换界面，导入或者创建钱包，导致地址变化时，更新交易记录数据的一个判断
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tabLayout.selectTab(tabLayout.getTabAt(0));
        if (requestCode == NEW_WALLET_FOR_RESULT_CODE) {
            if (data != null) {
                mCurrentWallet = data.getParcelableExtra(ConstantParams.SERIALIZABLE_DATA_WALLET_BEAN);
                LoggerUtils.i("requestCode", "requestCode = " + requestCode + "resultCode = " + resultCode + "address = " + mCurrentWallet.getAddress());
                if (!mOldAddress.equals(mCurrentWallet.getAddress())) {
                    mTxRecordAllFragment.onAddressChanged(mCurrentWallet);
                } else {
                    LoggerUtils.i("requestCode", "requestCode2 = " + requestCode + "resultCode = " + resultCode + "address = " + mCurrentWallet.getAddress());
                }
            }

        } else if (requestCode == TX_ACTIVITY_FOR_RESULT_CODE) {
            LoggerUtils.i("requestCode", "requestCode = " + requestCode + "resultCode = " + resultCode + "address = " + mCurrentWallet.getAddress());
            TxRecordBean t;
            if (data != null) {
                t = data.getParcelableExtra(TX_ACTIVITY_RESULT);
                if (t == null) {
                    return;
                }
                if (DEFAULT_TRANSCATION_HASH_NULL.equals(t.getHash())) {
                    //交易失败
                    mTxRecordAllFragment.onNewTxRecordByTx(t);
                    saveTxRecordBean(t);
                    return;
                }
                //交易成功
                //刷刷新余额
//                //请求交易区块等信息
                IONCSDKTransfers.getEthTransactionFromNode(mNodeIONC
                        , t.getHash()
                        , t
                        , this);
            }
        }
    }


    /**
     * 钱包没有备份,则提示用户去备份钱包
     *
     * @return ..
     */
    private boolean pleaseBackupWallet() {
        if (BuildConfig.APP_DEBUG) {
            return false;
        }
        if (mPleaseBackupWallet.getVisibility() == View.VISIBLE) {
            ToastUtil.showToastLonger(getResources().getString(R.string.toast_please_backup_wallet));
            return true;
        }
        return false;
    }

    @Override
    protected void initData() {
        if (mCurrentWallet == null) {
            mCurrentWallet = IONCSDKWallet.getMainWallet();
        }
        mTxRecordDoingFragment = new TxRecordDoingFragment();
        mTxRecordDoneFragment = new TxRecordDoneFragment();
        mTxRecordDoingFragment.setSuccessCallback(mTxRecordDoneFragment);
        mTxRecordAllFragment = new TxRecordAllFragment();
        mTxRecordFailureFragment = new TxRecordFailureFragment();
        mFragmentListTxRecord.add(mTxRecordAllFragment);
        mFragmentListTxRecord.add(mTxRecordDoneFragment);
        mFragmentListTxRecord.add(mTxRecordDoingFragment);
        mFragmentListTxRecord.add(mTxRecordFailureFragment);
        List<String> titles = new ArrayList<>();
        LoggerUtils.e("tab",getAppString(R.string.tab_tx_all));
        titles.add(getAppString(R.string.tab_tx_all));
        titles.add(getAppString(R.string.tab_tx_done));
        titles.add(getAppString(R.string.tab_tx_doing));
        titles.add(getAppString(R.string.tab_tx_failure));
        viewPager.setAdapter(new TxRecordPagerAdapter(getChildFragmentManager(), mFragmentListTxRecord, titles));
        viewPager.setPageTransformer(true,new DepthPageTransformer());
//        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        tabLayout.setupWithViewPager(viewPager);
    }


    /**
     * 钱包切换时，将记录面板切换到地换全部记录面板
     * 同时并通知它
     */
    private void changeWallet() {
        tabLayout.selectTab(tabLayout.getTabAt(0));
        mTxRecordAllFragment.onAddressChanged(mCurrentWallet);
        mTxRecordDoneFragment.onAddressChanged(mCurrentWallet);
        mTxRecordDoingFragment.onAddressChanged(mCurrentWallet);
    }


    /**
     * 余额获取失败，刷新获取主链节点
     * 1、获取余额
     * 2、获取交易记录
     *
     * @param refreshLayout
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        balance();
        if (mTxRecordAllFragment != null) {
            mTxRecordAllFragment.onPullToDown(mCurrentWallet, mRefresh);
        }
        if (mTxRecordDoneFragment != null) {
            mTxRecordDoneFragment.onPullToDown(mCurrentWallet, mRefresh);
        }
        if (mTxRecordDoingFragment != null) {
            mTxRecordDoingFragment.onPullToDown(mCurrentWallet, mRefresh);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mTxRecordAllFragment.onPullToUp(mCurrentWallet, mRefresh);
        mTxRecordDoingFragment.onPullToUp(mCurrentWallet, mRefresh);
        mTxRecordDoneFragment.onPullToUp(mCurrentWallet, mRefresh);
        mRefresh.finishLoadMore(500);
    }

    @Override
    public void onSaveMnemonicSure() {
        new DialogTextMessage(Objects.requireNonNull(mActivity)).setTitle(getResources().getString(R.string.attention))
                .setMessage(getResources().getString(R.string.key_store_to_save))
                .setBtnText(getResources().getString(R.string.i_know))
                .setHintMsg("")
                .setCancelByBackKey(true)
                .setTag("")
                .setCopyBtnClickedListener(AssetFragment.this).show();
    }

    @Override
    public void onSaveMnemonicCancel(DialogMnemonicShow dialogMnemonic) {
        dialogMnemonic.dismiss();
    }

    @Override
    public void onDialogTextMessageBtnClicked(DialogTextMessage dialogTextMessage) {
        dialogMnemonic.dismiss();
        dialogTextMessage.dismiss();
        //保存准状态
        //去测试一下助记词
        new DialogCheckMnemonic(requireActivity(), this).show();
    }

    /**
     * @param s                   回传数据 ,测试助记词是否正确
     * @param editTextList        助记词编辑框列表
     * @param dialogCheckMnemonic 助记词检查对话框
     */
    @Override
    public void onDialogCheckMnemonics12(String[] s, List<AppCompatEditText> editTextList, DialogCheckMnemonic dialogCheckMnemonic) {
        String[] mnemonics = mCurrentWallet.getMnemonic().split(" ");
        if (s.length != mnemonics.length) {
            ToastUtil.showToastLonger(getResources().getString(R.string.error_mnemonics));
            return;
        }
        int count = mnemonics.length;
        for (int i = 0; i < count; i++) {
            if (!mnemonics[i].equals(s[i])) {
                String index = String.valueOf((i + 1));
                ToastUtil.showToastLonger(getResources().getString(R.string.error_index_mnemonics, index));
                editTextList.get(i).setTextColor(Color.RED);
                return;
            }
        }
        //更新
        mCurrentWallet.setMnemonic("");
        IONCSDKWallet.updateWallet(mCurrentWallet);
        ToastUtil.showToastLonger(getResources().getString(R.string.authentication_successful));
        dialogCheckMnemonic.dismiss();
//        skip(MainActivity.class);
        //隐藏备份提示布局
        mPleaseBackupWallet.setVisibility(View.GONE);
    }

    @Override
    public void onIONCNodeStart() {
        LoggerUtils.i("正在离子链节点......");
    }

    @Override
    public void onIONCNodeSuccess(List<NodeBean.DataBean> dataBean) {
        //取出主链节点
        mNodeIONC = dataBean.get(0).getIonc_node();
        LoggerUtils.i("node", mNodeIONC);
        //获取主链成功后,获取离子币余额
        balance();//onIONCNodeSuccess
    }

    /**
     * 从中心服务器 获取节点失败，使用内置的备用节点
     *
     * @param error 失败
     */
    @Override
    public void onIONCNodeError(String error) {
        LoggerUtils.e("node error", " 获取离子链节点失败......" + ("".equals(error) ? "数据解析失败" : error));
        //获取主链成功后,获取余额
        ToastUtil.showToastLonger(getAppString(R.string.error_net_node));
    }

    /**
     * 获取余额
     */
    private void balance() {
        IONCSDKWallet.getIONCWalletBalance(mNodeIONC, mCurrentWallet.getAddress(), this);
    }


    @Override
    public void onIONCNodeFinish() {
        LoggerUtils.i("节点请求结束");
    }

    /**
     * @param balanceBigDecimal 原始形式
     * @param nodeUrlTag        节点
     */
    @Override
    public void onBalanceSuccess(BigDecimal balanceBigDecimal, String nodeUrlTag) {
        LoggerUtils.i("离子币余额获取成功:" + balanceBigDecimal.toPlainString());
        mIoncBalanceTx.setTextColor(Color.BLACK);
        mIONCBalance = balanceBigDecimal;
        //正常显示离子币余额
        mIoncBalanceTx.setText(balanceBigDecimal.toPlainString());

        mCurrentWallet.setBalance(balanceBigDecimal.toPlainString());  //缓存余额
        IONCSDKWallet.updateWallet(mCurrentWallet);//更新余额到数据库

        //获取美元价格
        mPricePresenter = new PricePresenter();
        mPricePresenter.getUSDPrice(this);
    }

    /**
     * 获取余额失败，不再去获取离子币对应的美元价格
     * 显示余额为灰色
     *
     * @param error 失败信息 获取余额失败
     */
    @Override
    public void onBalanceFailure(String error) {
        mRefresh.finishRefresh();

        LoggerUtils.e("离子币余额获取失败:", error);
        ToastUtil.showToastLonger(error);
        String balance = mCurrentWallet.getBalance();
        String rmb = mCurrentWallet.getRmb();
        if (TextUtils.isEmpty(balance)) {
            balance = "0000";
        }
        if (TextUtils.isEmpty(rmb)) {
            rmb = "0000";
        }
        mRmbTx.setTextColor(Color.GRAY);
        mIoncBalanceTx.setTextColor(Color.GRAY);

        mIoncBalanceTx.setText(balance);
        setBalance();
    }

    @Override
    public void onUSDPriceStart() {
        LoggerUtils.i("正在美元价格......");
    }

    /**
     * 美元价格获取成功
     *
     * @param usdPrice IONC 的美元价格
     */
    @Override
    public void onUSDPriceSuccess(double usdPrice) {
        //获取人民币汇率
        mUSDPrice = usdPrice;
        mTotalUSDPrice = mIONCBalance.multiply(BigDecimal.valueOf(mUSDPrice));
        //计算美元价格
        IONCSDKWallet.updateWallet(mCurrentWallet);

        /*
         * 请求汇率
         * 如果用户需要显示总的美元价格  就不要去请求汇率了
         * 设置美元价格
         *  mRmbTx.setText(mCurrentWallet.getBalance());
         *  mRmbTx.setTextColor(Color.WHITE);
         */
        mPricePresenter.getUSDExchangeRateRMB(this);
    }

    /**
     * 获取美元汇率失败 ，影响价格转换，但不影响IONC的数量显示
     *
     * @param error 请求usd价格失败
     */
    @Override
    public void onUSDPriceFailure(String error) {
        ToastUtil.showToastLonger(getAppString(R.string.error_net_getting_usd));
        LoggerUtils.e("美元价格获取失败:" + error);
        mRmbTx.setTextColor(Color.GRAY);
        if (TextUtils.isEmpty(mCurrentWallet.getRmb())) {
            mRmbTx.setText("0000"); //切换时读取余额
        } else {
            setBalance();
        }
    }

    @Override
    public void onUSDPriceFinish() {
        LoggerUtils.i("美元价格请求结束");
        mRefresh.finishRefresh();
    }

    @Override
    public void onUSDExRateRMBStart() {
        LoggerUtils.i("正在获取人民币队美元汇率......");
    }

    @Override
    public void onUSDExRateRMBSuccess(USDExRmb.DataBean dataBean) {
        //转换为人民币
        mRmbTx.setTextColor(Color.GRAY);
        mCNY = dataBean.getCNY();
        mUS = dataBean.getUSD();
        mIDR = dataBean.getIDR();
        mKRW = dataBean.getKRW();
        LoggerUtils.i("balance = ");
        setBalance();
        IONCSDKWallet.updateWallet(mCurrentWallet); //更新到数据库
        mRefreshHeader.setLastUpdateText(new SimpleDateFormat("上次更新:yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
    }

    @Override
    public void onUSDExRateRMBFailure(String error) {
        LoggerUtils.e(error);
        mRmbTx.setTextColor(Color.GRAY);
        if (TextUtils.isEmpty(mCurrentWallet.getRmb())) {
            mRmbTx.setText("0000"); //切换时读取余额
        } else {
            setBalance();
        }
        ToastUtil.showToastLonger(getAppString(R.string.error_net_getting_rate_rmb));
    }

    @Override
    public void onUSDExRateRMBFinish() {
        LoggerUtils.i("汇率请求结束");
        mRefresh.finishRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        mOldAddress = mCurrentWallet.getAddress();
        cancelGetNode();//pause
    }

    @Override
    public void onDataParseError() {
        ToastUtil.showToastLonger(getAppString(R.string.error_data_parse));
    }

    private void cancelGetNode() {
        OkGo.cancelTag(OkGo.getInstance().getOkHttpClient(), NET_CANCEL_TAG_USD_RMB_RATE);
        OkGo.cancelTag(OkGo.getInstance().getOkHttpClient(), NET_CANCEL_TAG_USD_PRICE);
    }

    private void txRecordHelper(TxRecordBean txRecordBean) {
        txRecordBean.setPublicKey(mCurrentWallet.getPublic_key());

       saveTxRecordBean(txRecordBean);
    }

    /**
     * 直接通过 web3j Api查询交易结果
     *
     * @param txRecordBean
     */
    @Override
    public void OnTxRecordNodeSuccess(TxRecordBean txRecordBean) {
        hideProgress();
        LoggerUtils.i("syncBrowser", "OnTxRecordNodeSuccess" + "   AssetFragment" + txRecordBean.toString());
        //取出块的哈希值，获取交易完成时间
//        IONCSDKWallet.ethTransactiontimestamp(mNodeIONC,txRecordBean,this);
        mTxRecordAllFragment.onNewTxRecordByTx(txRecordBean);
        saveTxRecordBean(txRecordBean);
        balance();
    }

    @Override
    public void onTxRecordNodeFailure(String error, TxRecordBean recordBean) {
        hideProgress();
        mTxRecordAllFragment.onNewTxRecordByTx(recordBean);
//        mTxRecordDoneFragment.onNewTxRecordByTx(recordBean);
        recordBean.setPublicKey(mCurrentWallet.getPublic_key());
        saveTxRecordBean(recordBean);
    }

    @Override
    public void onTxRecordNodeStart() {
        LoggerUtils.i("4444444444444444");
        showProgress(getAppString(R.string.please_wait));
    }




    public interface OnPullToRefreshCallback {
        /**
         * 下拉刷新
         */
        void onPullToDown(WalletBeanNew walletBeanNew, SmartRefreshLayout refresh);

        /**
         * 上拉加载更多
         */
        void onPullToUp(WalletBeanNew walletBeanNew, SmartRefreshLayout refresh);

        /**
         * @param currentWallet 地址发生改变的时候
         */
        void onAddressChanged(WalletBeanNew currentWallet);

        /**
         * @param txRecordBean 有新的交易记录的时候
         */
        void onNewTxRecordByTx(TxRecordBean txRecordBean);

    }

    @SuppressLint("SetTextI18n")
    private void setBalance() {

        String balance = "0000";
        if (mTotalUSDPrice == null) {
            mRmbTx.setText(balance);
            mRmbIcon.setText(split + SPUtils.getInstance().getCoinType());
            return;
        }
        BigDecimal us = mTotalUSDPrice.multiply(BigDecimal.valueOf(mUS));
        BigDecimal rmb = mTotalUSDPrice.multiply(BigDecimal.valueOf(mCNY));
        BigDecimal krw = mTotalUSDPrice.multiply(BigDecimal.valueOf(mKRW));
        BigDecimal idr = mTotalUSDPrice.multiply(BigDecimal.valueOf(mIDR));
        String balance_us = us.setScale(mBalanceScale, ROUND_HALF_UP).toPlainString();
        String balance_rmb = rmb.setScale(mBalanceScale, ROUND_HALF_UP).toPlainString();
        String balance_krw = krw.setScale(mBalanceScale, ROUND_HALF_UP).toPlainString();
        String balance_idr = idr.setScale(mBalanceScale, ROUND_HALF_UP).toPlainString();
        mCurrentWallet.setRmb(balance_rmb);
        mCurrentWallet.setUs(balance_us);
        mCurrentWallet.setKrw(balance_krw);
        mCurrentWallet.setIdr(balance_idr);
        mRmbIcon.setText(split+ SPUtils.getInstance().getCoinType());
        switch (SPUtils.getInstance().getCoinType()) {
            case COIN_TYPE_CNY:
                mRmbTx.setText(balance_rmb);
                break;
            case COIN_TYPE_IDR:
                mRmbTx.setText(balance_idr);
                break;
            case COIN_TYPE_KRW:
                mRmbTx.setText(balance_krw);
                break;
            case COIN_TYPE_USD:
                mRmbTx.setText(balance_us);
                break;
        }
        LoggerUtils.i("balance = COIN_TYPE_IDR ", balance);
        IONCSDKWallet.updateWallet(mCurrentWallet);
    }
}

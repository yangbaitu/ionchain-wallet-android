package org.ionc.wallet.view.activity.manager;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.widget.AppCompatEditText;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.ionc.ionclib.bean.WalletBeanNew;
import org.ionc.ionclib.utils.ToastUtil;
import org.ionc.ionclib.web3j.IONCSDKWallet;
import org.ionc.wallet.adapter.CommonAdapter;
import org.ionc.wallet.adapter.walletmanager.ManagerWalletHelper;
import org.ionc.wallet.view.activity.create.CreateWalletActivity;
import org.ionc.wallet.view.activity.imports.SelectImportModeActivity;
import org.ionc.wallet.view.activity.modify.ModifyWalletActivity;
import org.ionc.wallet.view.base.AbsBaseActivityTitleTwo;
import org.ionc.wallet.view.widget.dialog.callback.OnDialogCheck12MnemonicCallbcak;
import org.ionc.wallet.view.widget.dialog.export.DialogTextMessage;
import org.ionc.wallet.view.widget.dialog.mnemonic.DialogCheckMnemonic;
import org.ionc.wallet.view.widget.dialog.mnemonic.DialogMnemonicShow;
import org.ionchain.wallet.R;

import java.util.ArrayList;
import java.util.List;

import static org.ionc.wallet.constant.ConstantActivitySkipTag.INTENT_FROM_MANAGER_ACTIVITY;
import static org.ionc.wallet.constant.ConstantActivitySkipTag.INTENT_FROM_WHERE_TAG;
import static org.ionc.wallet.constant.ConstantParams.PARCELABLE_WALLET_BEAN;
import static org.ionc.wallet.utils.ViewUtils.setViewAlphaAnimation;

public class ManageWalletActivity extends AbsBaseActivityTitleTwo implements
        ManagerWalletHelper.OnWalletManagerItemClickedListener,
        DialogMnemonicShow.OnSavedMnemonicCallback,
        DialogTextMessage.OnBtnClickedListener,
        OnDialogCheck12MnemonicCallbcak,
        OnRefreshLoadMoreListener {


    private SmartRefreshLayout srl;
    private ListView listview;
    private Button importBtn;
    private Button createBtn;
    private CommonAdapter mAdapter;
    private List<WalletBeanNew> mWalletBeans = new ArrayList<>();
    private DialogMnemonicShow dialogMnemonic;
    private WalletBeanNew mCurrentWallet;

    private void findViews() {
        srl = findViewById(R.id.srl);
        listview = findViewById(R.id.listview);
        importBtn = findViewById(R.id.importBtn);
        createBtn = findViewById(R.id.createBtn);
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        mWalletBeans = IONCSDKWallet.getAllWalletNew();
        mAdapter = new CommonAdapter(this, mWalletBeans, R.layout.item_wallet_manager_layout, new ManagerWalletHelper(this));
        listview.setAdapter(mAdapter);
    }

    @Override
    protected String getTitleName() {
        return getAppString(R.string.title_manage_wallet);
    }

    @Override
    protected int getTitleNameColor() {
        return super.getTitleNameColor();
    }

    @Override
    protected void setListener() {
        super.setListener();
        srl.setOnRefreshLoadMoreListener(this);
        createBtn.setOnClickListener(v -> {
            setViewAlphaAnimation(createBtn);
            skip(CreateWalletActivity.class, INTENT_FROM_WHERE_TAG, INTENT_FROM_MANAGER_ACTIVITY);
        });

        importBtn.setOnClickListener(v -> {
            setViewAlphaAnimation(importBtn);
            skip(SelectImportModeActivity.class, INTENT_FROM_WHERE_TAG, INTENT_FROM_MANAGER_ACTIVITY);//
        });

    }

    @Override
    protected void initView() {
        findViews();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_manage;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        srl.finishRefresh();
        mWalletBeans.clear();
        mWalletBeans.addAll(IONCSDKWallet.getAllWalletNew());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWalletBeans.clear();
        mWalletBeans.addAll(IONCSDKWallet.getAllWalletNew());
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClicked(int position) {
        mCurrentWallet = (WalletBeanNew) mAdapter.getItem(position);
        if (!TextUtils.isEmpty(mCurrentWallet.getMnemonic())) {
            ToastUtil.showToastLonger(getResources().getString(R.string.toast_please_backup_wallet));
            String[] mnemonics = mCurrentWallet.getMnemonic().split(" ");
            dialogMnemonic = new DialogMnemonicShow(this, mnemonics, this);
            dialogMnemonic.show();
            return;
        }
        skip(ModifyWalletActivity.class, PARCELABLE_WALLET_BEAN, mCurrentWallet);
    }

    @Override
    public void onSaveMnemonicSure() {
        new DialogTextMessage(this).setTitle(getResources().getString(R.string.attention))
                .setMessage(getResources().getString(R.string.key_store_to_save))
                .setBtnText(getResources().getString(R.string.i_know))
                .setHintMsg("")
                .setCancelByBackKey(true)
                .setTag("")
                .setCopyBtnClickedListener(this).show();
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
        new DialogCheckMnemonic(this, this).show();
    }

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
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        srl.finishLoadMore();
    }
}

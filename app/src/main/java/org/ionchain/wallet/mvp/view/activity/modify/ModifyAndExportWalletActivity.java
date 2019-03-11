package org.ionchain.wallet.mvp.view.activity.modify;

import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ionc.wallet.sdk.IONCWalletSDK;
import org.ionc.wallet.sdk.bean.WalletBean;
import org.ionc.wallet.sdk.callback.OnBalanceCallback;
import org.ionc.wallet.sdk.callback.OnDeletefinishCallback;
import org.ionc.wallet.sdk.callback.OnImportPrivateKeyCallback;
import org.ionc.wallet.sdk.callback.OnSimulateTimeConsume;
import org.ionc.wallet.sdk.utils.Logger;
import org.ionc.wallet.sdk.utils.StringUtils;

import org.ionchain.wallet.R;
import org.ionchain.wallet.mvp.view.activity.createwallet.CreateWalletSelectActivity;
import org.ionchain.wallet.mvp.view.base.AbsBaseActivity;
import org.ionchain.wallet.utils.SoftKeyboardUtil;
import org.ionchain.wallet.utils.ToastUtil;
import org.ionchain.wallet.widget.DialogImport;
import org.ionchain.wallet.widget.DialogPasswordCheck;
import org.ionchain.wallet.widget.IONCTitleBar;
import org.web3j.utils.Files;

import java.io.File;
import java.io.IOException;

import static org.ionc.wallet.sdk.utils.StringUtils.chechPwd;
import static org.ionchain.wallet.constant.ConstantParams.REQUEST_MODIFY_WALLET_PWD;
import static org.ionchain.wallet.constant.ConstantParams.SERIALIZABLE_DATA;
import static org.ionchain.wallet.constant.ConstantParams.SERIALIZABLE_DATA_WALLET_BEAN;
import static org.ionchain.wallet.utils.AnimationUtils.setViewAlphaAnimation;

/**
 * 修改钱包：钱包名、修改密码、导出私钥
 */
public class ModifyAndExportWalletActivity extends AbsBaseActivity implements
        OnBalanceCallback,
        OnImportPrivateKeyCallback,
        View.OnClickListener,
        OnSimulateTimeConsume, OnDeletefinishCallback {


    WalletBean mWallet;


    private Button delBtn;
    private TextView walletBalanceTv;
    private TextView walletAddressTv;
    private AppCompatEditText walletNameEt;
    private RelativeLayout modifyPwdLayout;
    private RelativeLayout importLayout;
    private RelativeLayout import_mnemonic_layout;
    private RelativeLayout import_key_store_layout;
    private boolean bShowMnemonicRl;//是否显示导出助记词条目
    private boolean bShowKSRl;//是否显示导出keystore

    private int importFlag = 0;//默认导出助记词

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-09-26 14:47:05 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        delBtn = (Button) findViewById(R.id.delBtn);
        walletBalanceTv = (TextView) findViewById(R.id.walletBalanceTv);
        walletAddressTv = (TextView) findViewById(R.id.walletAddressTv);
        walletNameEt = (AppCompatEditText) findViewById(R.id.walletNameEt);
        modifyPwdLayout = (RelativeLayout) findViewById(R.id.modifyPwdLayout);
        importLayout = (RelativeLayout) findViewById(R.id.import_pri_layout);
        import_mnemonic_layout = (RelativeLayout) findViewById(R.id.import_mnemonic_layout);
        import_key_store_layout = (RelativeLayout) findViewById(R.id.import_key_store_layout);
        if (!bShowMnemonicRl) {
            import_mnemonic_layout.setVisibility(View.GONE);
        }
        if (!bShowKSRl) {
            import_key_store_layout.setVisibility(View.GONE);
        }
        if (IONCWalletSDK.getInstance().getAllWallet().size() == 1) {
            delBtn.setVisibility(View.GONE);
        }
        import_mnemonic_layout.setOnClickListener(this);
        delBtn.setOnClickListener(this);
        modifyPwdLayout.setOnClickListener(this);
        import_key_store_layout.setOnClickListener(this);
        importLayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        if (!TextUtils.isEmpty(mWallet.getAddress())) {
            walletAddressTv.setText(mWallet.getAddress());
        }

        if (!TextUtils.isEmpty(mWallet.getName())) {
            walletNameEt.setText(mWallet.getName());
        }

        IONCWalletSDK.getInstance().getAccountBalance(mWallet, this);

    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mWallet = (WalletBean) intent.getSerializableExtra(SERIALIZABLE_DATA);
        bShowMnemonicRl = !StringUtils.isEmpty(mWallet.getMnemonic());
        bShowKSRl = !StringUtils.isEmpty(mWallet.getKeystore());
    }

    @Override
    protected void initView() {
        findViews();
        final IONCTitleBar ioncTitleBar = findViewById(R.id.ionc_title_bar);
        getMImmersionBar().titleView(ioncTitleBar).statusBarDarkFont(true).execute();
        ioncTitleBar.setTitle(mWallet.getName());
        ioncTitleBar.setLeftImgRes(R.mipmap.arrow_back_white);
        ioncTitleBar.setLeftBtnCLickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftKeyboardUtil.hideSoftKeyboard(ModifyAndExportWalletActivity.this);
                finish();
            }
        });
        ioncTitleBar.setRightTextCLickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletNameEt.getText() != null && !StringUtils.isEmpty(walletNameEt.getText().toString())) {
                    mWallet.setName(walletNameEt.getText().toString());
                    ioncTitleBar.setTitle(walletNameEt.getText().toString());
                    SoftKeyboardUtil.hideSoftKeyboard(ModifyAndExportWalletActivity.this);
                    IONCWalletSDK.getInstance().removeWalletPrivateKey(mWallet);
                } else {
                    ToastUtil.showShort("钱包名称不可为空！");
                }

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_modify;
    }



    /**
     * 导出私钥
     *
     * @param privateKey
     */
    private void showImportPrivateKeyDialog(final String privateKey) {

        new DialogImport(this).setTitle("导出私钥").setMessage(privateKey)
                .setCopyBtnClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //复制
                        StringUtils.copy(ModifyAndExportWalletActivity.this, privateKey);
                        ToastUtil.showToastLonger("已复制私钥");
                    }
                }).show();

    }

    /**
     * 删钱包
     */
    private void delwallet() {

        final DialogPasswordCheck dialogPasswordCheck = new DialogPasswordCheck(this);
        dialogPasswordCheck.setLeftBtnText("取消")
                .setRightBtnText("确定")
                .setTitleText("请输入该钱包的密码")
                .setLeftBtnClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPasswordCheck.dismiss();
                    }
                })
                .setRightBtnClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtils.isEmpty(dialogPasswordCheck.getPasswordEt().getText().toString())) {
                            ToastUtil.showToastLonger("请输入密码！");
                            return;
                        }
                        Logger.i(getTAG(), "onClick: " + mWallet.getPassword());
                        String p_dao = mWallet.getPassword();
                        String p_input = dialogPasswordCheck.getPasswordEt().getText().toString();
                        if (!chechPwd(p_dao,p_input)) {
                            ToastUtil.showToastLonger("您输入的密码有误！");
                            return;
                        }
//                        if (WalletDaoTools.getAllWallet().size() == 1) {
//                            ToastUtil.showToastLonger("不能删除该钱包，您必须至少有一个钱包");
//                            return;
//                        }

                        IONCWalletSDK.getInstance().deleteWallet(mWallet, ModifyAndExportWalletActivity.this);

                        finish();
                    }
                }).show();

    }

    @Override
    public void onBalanceSuccess(WalletBean walletBean) {
        walletBalanceTv.setText(walletBean.getBalance());
    }

    @Override
    public void onBalanceFailure(String error) {
        walletBalanceTv.setText("0.0000");
    }

    @Override
    public void onImportPriKeySuccess(String privateKey) {
        showImportPrivateKeyDialog(privateKey);
        hideProgress();
    }

    @Override
    public void onImportPriKeyFailure(String erroe) {
        hideProgress();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        setViewAlphaAnimation(v);
        final DialogPasswordCheck d = new DialogPasswordCheck(this);

        switch (v.getId()) {
            case R.id.copyBtn://复制
                StringUtils.copy(this, mWallet.getPrivateKey());
                ToastUtil.showShortToast("已复制到剪切板");
                break;
            case R.id.delBtn://删除钱包
                delwallet();
                break;
            case R.id.modifyPwdLayout://修改密码
                intent = new Intent(getMActivity(), ModifyWalletPwdActivity.class);
                intent.putExtra(SERIALIZABLE_DATA_WALLET_BEAN, mWallet);
                startActivityForResult(intent, REQUEST_MODIFY_WALLET_PWD);
                break;
            case R.id.import_pri_layout://导出私钥

                d.setTitleText("导出私钥");
                d.setLeftBtnClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.setRightBtnClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*比对密码是否正确*/
                        String pwd_dao = mWallet.getPassword();
                        String pwd1 = d.getPasswordEt().getText().toString();
                        if (StringUtils.chechPwd(pwd_dao,pwd1)) {
                            d.dismiss();
                            showProgress("正在导出请稍候");
                            IONCWalletSDK.getInstance().exportPrivateKey(mWallet.getKeystore(), pwd_dao, ModifyAndExportWalletActivity.this);
                        } else {
                            ToastUtil.showToastLonger("请检查密码是否正确！");
                        }
                    }
                });
                d.show();
                break;
            case R.id.import_mnemonic_layout://导出助记词
                importFlag = 0;
                d.setLeftBtnClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.setRightBtnClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*比对密码是否正确*/
                        String pwd = mWallet.getPassword();//保存的密码
                        String pwd1 = d.getPasswordEt().getText().toString();//输入的密码
                        if (chechPwd(pwd,pwd1)) {
                            d.dismiss();
                            showProgress("正在导出请稍候");
                            IONCWalletSDK.getInstance().simulateTimeConsuming(ModifyAndExportWalletActivity.this);
                        } else {
                            ToastUtil.showToastLonger("请检查密码是否正确！");
                        }
                    }
                });
                d.show();
                break;
            case R.id.import_key_store_layout://导出KS
                importFlag = 1;
                final DialogPasswordCheck dialog1 = new DialogPasswordCheck(this);
                dialog1.setLeftBtnClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.setRightBtnClickedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*比对密码是否正确*/
                        String pwd = mWallet.getPassword();
                        String pwd1 = dialog1.getPasswordEt().getText().toString();
                        if (chechPwd(pwd,pwd1)) {
                            dialog1.dismiss();
                            showProgress("正在导出请稍候");
                            IONCWalletSDK.getInstance().simulateTimeConsuming(ModifyAndExportWalletActivity.this);
                        } else {
                            ToastUtil.showToastLonger("请检查密码是否正确！");
                        }
                    }
                });
                dialog1.show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(getTAG(), "onActivityResult1: " + mWallet.getKeystore());
        if (requestCode == REQUEST_MODIFY_WALLET_PWD && data != null) {
            mWallet = (WalletBean) data.getSerializableExtra(SERIALIZABLE_DATA_WALLET_BEAN);
        }
        Log.i(getTAG(), "onActivityResult2: " + mWallet.getKeystore());
    }

    @Override
    public void onSimulateFinish() {
        hideProgress();
        if (importFlag == 0) {

            new DialogImport(this).setMessage(mWallet.getMnemonic())
                    .setTitle("导出助记词")
                    .setCopyBtnClickedListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //复制
                            StringUtils.copy(ModifyAndExportWalletActivity.this, mWallet.getMnemonic());
                            ToastUtil.showToastLonger("已复制助记词");
                        }
                    }).show();
        } else if (importFlag == 1) {

            try {
                String path = mWallet.getKeystore();
                final String json = Files.readString(new File(path));
                Logger.i(json);
//                ImportMnemonicOrKSDialog dialog = new ImportMnemonicOrKSDialog(this).setMnemonicOrKS(json);
//                dialog.show();
                new DialogImport(this).setMessage(json)
                        .setTitle("导出KeyStory")
                        .setCopyBtnClickedListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //复制
                                StringUtils.copy(ModifyAndExportWalletActivity.this, json);
                                ToastUtil.showToastLonger("已复制 KeyStory");
                            }
                        }).show();
            } catch (IOException e) {
                Logger.e(e.getMessage());
            }

        }

    }

    /**
     * 删除完成:检查数据库是否还有钱包,如果有将数据库第一个钱包设置为首页展示钱包
     */
    @Override
    public void onDeleteFinish() {
        //检查是否还有钱包
        if (IONCWalletSDK.getInstance().getAllWallet() == null || IONCWalletSDK.getInstance().getAllWallet().size() > 0) {
            IONCWalletSDK.getInstance().getAllWallet().get(0).setIsMainWallet(true);
            IONCWalletSDK.getInstance().saveWallet(IONCWalletSDK.getInstance().getAllWallet().get(0));
        }else {
            //去创建钱包
            Intent intent1 = new Intent(this, CreateWalletSelectActivity.class);
            startActivity(intent1);
            finish();
        }
    }
}

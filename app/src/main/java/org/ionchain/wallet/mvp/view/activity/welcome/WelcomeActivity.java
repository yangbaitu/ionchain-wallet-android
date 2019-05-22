package org.ionchain.wallet.mvp.view.activity.welcome;import android.content.Intent;import android.view.animation.AlphaAnimation;import android.view.animation.Animation;import android.widget.ProgressBar;import android.widget.RelativeLayout;import android.widget.TextView;import com.lzy.okgo.model.Progress;import org.ionc.wallet.bean.WalletBean;import org.ionc.wallet.sdk.IONCWalletSDK;import org.ionc.wallet.utils.Logger;import org.ionchain.wallet.App;import org.ionchain.wallet.R;import org.ionchain.wallet.bean.UpdateBean;import org.ionchain.wallet.constant.ConstantParams;import org.ionchain.wallet.mvp.model.update.OnCheckUpdateInfoCallback;import org.ionchain.wallet.mvp.presenter.update.UpdatePresenter;import org.ionchain.wallet.mvp.view.activity.MainActivity;import org.ionchain.wallet.mvp.view.activity.create.CreateWalletSelectActivity;import org.ionchain.wallet.mvp.view.base.AbsBaseActivity;import org.ionchain.wallet.utils.AppUtil;import org.ionchain.wallet.utils.ToastUtil;import org.ionchain.wallet.widget.dialog.download.DownloadDialog;import org.ionchain.wallet.widget.dialog.version.VersionInfoDialog;import java.util.List;import java.util.Objects;import static org.ionchain.wallet.constant.ConstantParams.DOWNLOAD_MUST_UPDATE_NO;import static org.ionchain.wallet.constant.ConstantParams.DOWNLOAD_MUST_UPDATE_YES;/** * Created by siberiawolf on 17/4/28. */public class WelcomeActivity extends AbsBaseActivity implements        Animation.AnimationListener,        OnCheckUpdateInfoCallback,        VersionInfoDialog.OnVersionDialogBtnClickedListener, DownloadDialog.DownloadCallback {    RelativeLayout welcome_layout;    ProgressBar progressBar;    TextView progressValueTv;    private UpdatePresenter mUpdatePresenter;    RelativeLayout progressRl;    private String must_update = DOWNLOAD_MUST_UPDATE_NO; // 1为必须更新    @Override    protected void initData() {        mUpdatePresenter = new UpdatePresenter();        String version_name = AppUtil.getVersionName(this);        TextView v = findViewById(R.id.version_name);        v.setText(getString(R.string.version, version_name));        progressValueTv = findViewById(R.id.download_progress_value);        progressBar = findViewById(R.id.download_progress);        progressRl = findViewById(R.id.progressRL);        AlphaAnimation aAnimation = new AlphaAnimation(0.0f, 1.0f);        aAnimation.setDuration(2000);        aAnimation.setAnimationListener(this);        welcome_layout.startAnimation(aAnimation);    }    @Override    protected void setListener() {    }    @Override    protected void handleIntent(Intent intent) {    }    /**     * @param requestCode 授予权限     * @param list        权限列表     */    @Override    public void onPermissionsGranted(int requestCode, List<String> list) {        super.onPermissionsGranted(requestCode, list);        mUpdatePresenter.checkForUpdate(this);    }    /**     * @param requestCode 权限被拒绝     * @param list        权限列表     */    @Override    public void onPermissionsDenied(int requestCode, List<String> list) {        super.onPermissionsDenied(requestCode, list);        ToastUtil.showToastLonger(getAppString(R.string.permission_storage));        skipToNext();    }    @Override    protected void initView() {        welcome_layout = findViewById(R.id.welcome_layout);    }    @Override    protected int getLayoutId() {        return R.layout.activity_welcome;    }    @Override    public void onAnimationStart(Animation animation) {    }    @Override    public void onAnimationEnd(Animation animation) {        //检查就表是否存在        if (IONCWalletSDK.getInstance().checkOldDataBaseExist()) {            List<WalletBean> allWalletOld = IONCWalletSDK.getInstance().getAllWalletOld();            IONCWalletSDK.getInstance().migrateDb(allWalletOld);        }        if (requestStoragePermissions()) {            mUpdatePresenter.checkForUpdate(this);        }    }    @Override    public void onAnimationRepeat(Animation animation) {    }    /**     * 跳转到     */    private void skipToNext() {        if (IONCWalletSDK.getInstance().getAllWalletNew() != null && IONCWalletSDK.getInstance().getAllWalletNew().size() > 0) {            skip(MainActivity.class);        } else {            Intent intent1 = new Intent(mActivity, CreateWalletSelectActivity.class);            startActivity(intent1);            finish();        }    }    /**     * 开始检查更新     */    @Override    public void onCheckForUpdateStart() {        Logger.i("checkForUpdate", "onCheckForUpdateStart");//        showProgress(getString(R.string.updating));    }    /**     * 检查更新成功     */    @Override    public void onCheckForUpdateSuccess() {//        hideProgress();    }    /**     * @param error 检查更新失败     */    @Override    public void onCheckForUpdateError(String error) {        Logger.i("checkForUpdate", "onCheckForUpdateError  == " + error);        hideProgress();        ToastUtil.showToastLonger(getString(R.string.check_update_net_error_code));        skipToNext();    }    /**     * @param updateBean  检查结果 需要更新     * @param must_update 是否强制更新     */    @Override    public void onCheckForUpdateNeedUpdate(UpdateBean updateBean, String must_update) {        try {            String update_info;            this.must_update = must_update;            UpdateBean.DataBean dataBean_CN = null, dataBean_EN = null;            for (int i = 0; i < 2; i++) {                if (updateBean.getData().get(i).getLanguage().equals("0")) {                    //中文                    dataBean_CN = updateBean.getData().get(i);                } else {                    dataBean_EN = updateBean.getData().get(i);                }            }            if (App.isCurrentLanguageZN()) {                update_info = Objects.requireNonNull(dataBean_CN).getUpdate_info();            } else {                update_info = Objects.requireNonNull(dataBean_EN).getUpdate_info();            }            /*             * 更新当前版本信息对话框的展示内容             *             * */            VersionInfoDialog versionInfoDialog = new VersionInfoDialog(mActivity, updateBean.getData().get(0).getUrl(), this)                    .setTitleName(mActivity.getAppString(R.string.version_info_title))                    .setSureBtnName(getString(R.string.dialog_btn_download))                    .setCancelableBydBackKey(false)                    .setVersionInfo(update_info);            versionInfoDialog.setType(ConstantParams.VERSION_TAG_DOWNLOAD);            versionInfoDialog.show();        } catch (NullPointerException e) {            ToastUtil.showToastLonger(mActivity.getAppString(R.string.data_parase_error));        }    }    /**     * 检查结果没有新版本     */    @Override    public void onCheckForUpdateNoNewVersion() {        skipToNext();    }    /**     * 欢迎页的 检查更新对话框     *     * @param dialog       信息展示对话框     * @param type         对话框的类型:下载,展示(展示对话框有检查更新文字)     * @param download_url 下载地址     */    @Override    public void onVersionDialogRightBtnClicked(VersionInfoDialog dialog, char type, String download_url) {        //下载对话框        dialog.dismiss();        new DownloadDialog(mActivity, download_url, this,must_update).setCancelableBydBackKey(false).show();    }    /**     * @param dialog 对话框     */    @Override    public void onVersionDialogLeftBtnClicked(VersionInfoDialog dialog) {        if (DOWNLOAD_MUST_UPDATE_YES.equals(must_update)) {            ToastUtil.showShort(getAppString(R.string.must_update));        } else {            dialog.dismiss();            skipToNext();        }    }    /**     * @param progress 开始下载     */    @Override    public void onDownloadStart(Progress progress) {    }    /**     * @param progress 下载出错     */    @Override    public void onDownloadError(Progress progress) {        ToastUtil.showShortToast(getAppString(R.string.download_error));        skipToNext();    }    /**     * 下载取消     */    @Override    public void onDownloadCancel() {        ToastUtil.showToastLonger(getAppString(R.string.download_cancel));        skipToNext();    }}
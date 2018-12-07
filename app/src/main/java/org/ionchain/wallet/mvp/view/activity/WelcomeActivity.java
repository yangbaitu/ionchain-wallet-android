package org.ionchain.wallet.mvp.view.activity;import android.content.DialogInterface;import android.content.Intent;import android.net.Uri;import android.os.Build;import android.support.v4.content.FileProvider;import android.support.v7.app.AlertDialog;import android.view.View;import android.view.animation.AlphaAnimation;import android.view.animation.Animation;import android.widget.ProgressBar;import android.widget.RelativeLayout;import android.widget.TextView;import com.ionc.wallet.sdk.IONCWalletSDK;import com.ionc.wallet.sdk.dao.WalletDaoTools;import com.lzy.okgo.callback.StringCallback;import com.lzy.okgo.model.HttpParams;import com.lzy.okgo.model.Progress;import com.lzy.okgo.model.Response;import com.lzy.okserver.download.DownloadListener;import com.lzy.okserver.download.DownloadTask;import com.orhanobut.logger.Logger;import org.ionchain.wallet.BuildConfig;import org.ionchain.wallet.R;import org.ionchain.wallet.bean.UpdateBean;import org.ionchain.wallet.mvp.view.activity.createwallet.CreateWalletSelectActivity;import org.ionchain.wallet.mvp.view.base.AbsBaseActivity;import org.ionchain.wallet.utils.AppUtil;import org.ionchain.wallet.utils.NetUtils;import java.io.File;import java.math.BigDecimal;import static org.ionchain.wallet.constant.ConstantParams.FROM_WELCOME;import static org.ionchain.wallet.constant.ConstantUrl.UPDATE_APK;/** * Created by siberiawolf on 17/4/28. */public class WelcomeActivity extends AbsBaseActivity implements Animation.AnimationListener {    RelativeLayout welcome_layout;    DownloadTask task;    ProgressBar progressBar;    TextView progressValueTv;    RelativeLayout progressRl;    private String downloadUrl = "https://www.ionchain.org/download/ionchain.apk";    @Override    protected void initData() {        String version_name = AppUtil.getVersionName(this);        TextView v = findViewById(R.id.version_name);        v.setText("当前版本：" + version_name);        progressValueTv = findViewById(R.id.download_progress_value);        progressBar = findViewById(R.id.download_progress);        progressRl = findViewById(R.id.progressRL);        Animation aAnimation = new AlphaAnimation(0.0f, 1.0f);        aAnimation.setDuration(2000);        aAnimation.setAnimationListener(this);        welcome_layout.startAnimation(aAnimation);    }    @Override    protected void setListener() {    }    @Override    protected void handleIntent(Intent intent) {    }    @Override    protected void initView() {        welcome_layout = findViewById(R.id.welcome_layout);    }    @Override    protected int getLayoutId() {        return R.layout.activity_welcome;    }    @Override    public void onAnimationStart(Animation animation) {    }    @Override    public void onAnimationEnd(Animation animation) {        //检查是否有更新        HttpParams params = new HttpParams();        params.clear();        params.put("platform", "Android");        NetUtils.get(UPDATE_APK, params, new StringCallback() {            @Override            public void onSuccess(Response<String> response) {                String json = response.body();                UpdateBean updateBean = NetUtils.gsonToBean(json, UpdateBean.class);                if (updateBean != null && updateBean.getData() != null && updateBean.getData().getVersion() != null) {                    int new_code = Integer.parseInt(updateBean.getData().getVersion().replace(".", ""));                    int old_code = Integer.parseInt(AppUtil.getVersionName(mActivity).replace(".", ""));                    if (new_code > old_code) {                        //询问用户是否下载？                        downloadUrl = updateBean.getData().getVersion();                        showDialog(downloadUrl,updateBean.getData().getChangelog(), updateBean.getData().getVersion());                    } else {                        IONCWalletSDK.getInstance().initWeb3j(mActivity.getApplicationContext());                        if (WalletDaoTools.getAllWallet() != null && WalletDaoTools.getAllWallet().size() > 0) {                            skip(MainActivity.class);                        } else {                            Intent intent1 = new Intent(getMActivity(), CreateWalletSelectActivity.class);                            intent1.putExtra(FROM_WELCOME, true);                            startActivity(intent1);                            finish();                        }                    }                }else {                    IONCWalletSDK.getInstance().initWeb3j(mActivity.getApplicationContext());                    if (WalletDaoTools.getAllWallet() != null && WalletDaoTools.getAllWallet().size() > 0) {                        skip(MainActivity.class);                    } else {                        Intent intent1 = new Intent(getMActivity(), CreateWalletSelectActivity.class);                        intent1.putExtra(FROM_WELCOME, true);                        startActivity(intent1);                        finish();                    }                }            }        }, "update");    }    @Override    public void onAnimationRepeat(Animation animation) {    }    public void showDialog(final String downloadUrl, String msg, String new_code) {        AlertDialog.Builder builder = new AlertDialog.Builder(this);        builder.setTitle("新版本 ：" + new_code + " ，请更新后使用！")                .setMessage(msg)                .setCancelable(false)                .setPositiveButton("下载", new DialogInterface.OnClickListener() {                    @Override                    public void onClick(DialogInterface dialog, int which) {                        task = NetUtils.downloader(downloadUrl, new AppDownloadListener("apk_download"));                        task.start();                    }                })                .setNegativeButton("取消", new DialogInterface.OnClickListener() {                    @Override                    public void onClick(DialogInterface dialog, int which) {                        finish();                    }                })                .show();    }    class AppDownloadListener extends DownloadListener {        public AppDownloadListener(Object tag) {            super(tag);        }        @Override        public void onStart(Progress progress) {            //开始            com.orhanobut.logger.Logger.i("开始下载......");        }        @Override        public void onProgress(Progress progress) {            //下载进度            int p = (int) scale(progress.fraction);            Logger.i("当前进度  " + p + "%");            progressValueTv.setText(p + "%");            progressRl.setVisibility(View.VISIBLE);            progressBar.setProgress(p);        }        @Override        public void onError(Progress progress) {            progressRl.setVisibility(View.GONE);            progressBar.setProgress(0);            Logger.e("下载出错：" + progress.exception.getMessage());        }        @Override        public void onFinish(File file, Progress progress) {            progressRl.setVisibility(View.GONE);            progressBar.setProgress(0);            finish();            Intent intent = new Intent(Intent.ACTION_VIEW);            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                /* Android N 写法*/                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);                Uri contentUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".fileProvider", new File(progress.filePath));                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");            } else {                /* Android N之前的老版本写法*/                intent.setDataAndType(Uri.fromFile(new File(progress.filePath)), "application/vnd.android.package-archive");                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);            }            startActivity(intent);        }        @Override        public void onRemove(Progress progress) {        }    }    /**     * 保留两位小数 四舍五入     *     * @param f 原始浮点型数     * @return 两位小数     */    private float scale(float f) {        BigDecimal b = new BigDecimal(f);        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100;    }}
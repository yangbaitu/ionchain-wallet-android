package org.ionchain.wallet.comm.api;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fast.lib.logger.Logger;
import com.fast.lib.okhttp.callback.ResultCallback;
import com.fast.lib.okhttp.request.OkHttpRequest;

import org.ionchain.wallet.App;
import org.ionchain.wallet.bean.WalletBean;
import org.ionchain.wallet.callback.OnCreateWalletCallback;
import org.ionchain.wallet.comm.api.conf.ApiConfig;
import org.ionchain.wallet.comm.api.constant.ApiConstant;
import org.ionchain.wallet.comm.api.myweb3j.MnemonicUtils;
import org.ionchain.wallet.comm.api.myweb3j.SecureRandomUtils;
import org.ionchain.wallet.comm.api.resphonse.ResponseModel;
import org.ionchain.wallet.dao.WalletDaoTools;
import org.json.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import okhttp3.Request;

import static org.web3j.crypto.Hash.sha256;


public class ApiWalletManager {

    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom(); //"https://ropsten.etherscan.io/token/0x92e831bbbb22424e0f22eebb8beb126366fa07ce"
    private static final String DEF_WALLET_ADDRESS = "http://192.168.23.149:8545";
    private static final String DEF_CONTRACT_ADDRESS = "0x92e831bbbb22424e0f22eebb8beb126366fa07ce";
    public static final String DEF_WALLET_PATH = Environment.getExternalStorageDirectory().toString() + "/ionchain/wallet";
    private static final String DEF_WALLET_WORDS_LIST_NAME = "en-mnemonic-word-list";
    private static final BigDecimal DEF_WALLET_DECIMALS = new BigDecimal("1000000000000000000");
    private static final int DEF_WALLET_DECIMALS_UNIT = 4;


    private WalletBean mainWallet;
    private String contractAddress;
    private String walletIndexUrl;
    private Web3j web3;
    private static ApiWalletManager instance;
    private Boolean isInit;//是否初始化了
    private Context mContext;


    public WalletBean getMainWallet() {
        return mainWallet;
    }

    public void setMainWallet(WalletBean myWallet) {
        this.mainWallet = myWallet;
    }

    public Boolean getInit() {
        return isInit;
    }

    private ApiWalletManager(Context mContext) {
//        this.mainWallet = wallet;
        this.contractAddress = null;
        this.walletIndexUrl = null;
        this.web3 = null;
        this.isInit = false;
        this.mContext = mContext;
        //偷懒了 初始化 词库 写这边了
        if (null == MnemonicUtils.WORD_LIST) {
            String info = readAssetsTxt(this.mContext, DEF_WALLET_WORDS_LIST_NAME);
            String[] a = info.split("\n");
            printtest(a.length + "");
            List<String> arr = new ArrayList<>();
            for (String word :
                    a) {
                arr.add(word);
            }
            MnemonicUtils.WORD_LIST = arr;
        }

    }

    public static ApiWalletManager getInstance() {
        return instance;
    }

    public static ApiWalletManager getInstance(Context mContext) {
        if (null == instance) {
            instance = new ApiWalletManager(mContext);
        }
        return instance;
    }

    public static ApiWalletManager reloadInstance(Context mContext) {
        instance = new ApiWalletManager(mContext);
        return instance;
    }

    /**
     * 初始化工具类
     *
     * @param handler
     */
    public void init(final Handler handler, WalletBean wallet) {
        mainWallet = wallet;
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //这里回头要从接口获取 现在先写死
//                    contractAddress = DEF_CONTRACT_ADDRESS;
                    walletIndexUrl = DEF_WALLET_ADDRESS;
                    web3 = Web3jFactory.build(new HttpService(walletIndexUrl));
                    try {
                        String url = ApiConfig.API_BASE_URL + ApiConstant.ApiUri.URI_SYS_INFO.getDesc();
                        printtest(url);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("1", "1");
                        new OkHttpRequest.Builder().url(url).params(map).headers(null).post(new ResultCallback<String>() {
                            @Override
                            public void onError(Request request, Exception e) {
                                printtest(e.getMessage());
                                sendResult(handler, ApiConstant.WalletManagerType.MANAGER_INIT.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                            }

                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject json = new JSONObject(response);
                                    int code = json.getInt("code");
                                    if (code == 0) {
                                        String contractAddress = json.getJSONObject("data").getString("contractAddress");
                                        String providerUrl = json.getJSONObject("data").getString("providerUrl");
                                        ApiWalletManager.this.contractAddress = contractAddress;
                                        ApiWalletManager.this.walletIndexUrl = providerUrl;
                                        isInit = true;

                                        sendResult(handler, ApiConstant.WalletManagerType.MANAGER_INIT.getDesc(), ApiConstant.WalletManagerErrCode.SUCCESS.name(), null, null);
                                    } else {
                                        sendResult(handler, ApiConstant.WalletManagerType.MANAGER_INIT.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                                    }

                                } catch (Exception e) {
                                    printtest(e.getMessage());
                                    sendResult(handler, ApiConstant.WalletManagerType.MANAGER_INIT.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                                }

                            }
                        });

                    } catch (Throwable e) {
                        printtest(e.getMessage());
                        sendResult(handler, ApiConstant.WalletManagerType.MANAGER_INIT.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                    }

                } catch (Exception e) {
                    printtest(e.getMessage());
                    sendResult(handler, ApiConstant.WalletManagerType.MANAGER_INIT.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                }


            }
        }.start();
    }

    /**
     * 创建钱包
     *
     * @param handler
     */
    public void createWallet(final WalletBean wallet, final Handler handler) {

        if (!isInit) {
            sendResult(handler, ApiConstant.WalletManagerType.WALLET_CREATE.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    String filePath = DEF_WALLET_PATH;
                    Bip39Wallet bip39Wallet = generateBip39Wallet(wallet.getPassword(), new File(filePath));
                    printtest(bip39Wallet.toString());
                    String keystore = filePath + "/" + bip39Wallet.getFilename();
                    wallet.setKeystore(keystore);
                    loadWalletBaseInfo(wallet);

                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_CREATE.getDesc(), ApiConstant.WalletManagerErrCode.SUCCESS.name(), null, null);
                } catch (Exception e) {
                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_CREATE.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                }

            }
        }.start();


    }

    /**
     * 导入钱包
     *
     * @param handler
     */
    public void importWallet(final WalletBean wallet, final Handler handler) {
        if (!isInit) {
            sendResult(handler, ApiConstant.WalletManagerType.WALLET_IMPORT.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    importWallt(wallet, null);
                    loadWalletBaseInfo(wallet);
                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_IMPORT.getDesc(), ApiConstant.WalletManagerErrCode.SUCCESS.name(), null, null);
                } catch (Exception e) {
                    Log.e("wallet", "", e);
                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_IMPORT.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                }

            }
        }.start();
    }

    /**
     * 读取钱包余额
     *
     * @param handler
     */
    public void getBlance(final WalletBean wallet, final Handler handler) {
        if (!isInit) {
            sendResult(handler, ApiConstant.WalletManagerType.WALLET_BALANCE.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    getBlance(wallet);
                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_BALANCE.getDesc(), ApiConstant.WalletManagerErrCode.SUCCESS.name(), null, null);
                } catch (Exception e) {
                    Log.e("wallet", "", e);
                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_BALANCE.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                }

            }
        }.start();
    }
    /**
     * 读取钱包余额
     *
     * @param callback
     */
    public void getBalance(final WalletBean wallet, final OnCreateWalletCallback callback) {
        if (!isInit) {
            callback.onCreateFailure("请先初始化！");
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
//                    wallet.setAddress("0x018673c699738c569d88d31167be1d2cb97c443e");//测试地址
                    String methodName = "balanceOf";
                    List<Type> inputParameters = new ArrayList<>();
                    List<TypeReference<?>> outputParameters = new ArrayList<>();
                    Address address = new Address(wallet.getAddress());
                    inputParameters.add(address);
                    TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
                    };
                    outputParameters.add(typeReference);
                    Function function = new Function(methodName, inputParameters, outputParameters);


                    String dealString = FunctionEncoder.encode(function);//交易串
//        String contractAddress = "0xbc647aad10114b89564c0a7aabe542bd0cf2c5af";//代币地址
                    Transaction transaction = Transaction.createEthCallTransaction(wallet.getAddress(), contractAddress, dealString);

                    EthCall ethCall;
                    BigInteger balanceValue;
                    ethCall = web3.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
                    Future<EthCall> s = web3.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync();
                    s.get().getValue();
                    String value = ethCall.getValue();
                    List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
                    if (results != null && results.size() > 0) {
                        balanceValue = (BigInteger) results.get(0).getValue();
                        BigDecimal bigDecimal = new BigDecimal(balanceValue);
                        BigDecimal balance = bigDecimal.divide(DEF_WALLET_DECIMALS).setScale(DEF_WALLET_DECIMALS_UNIT, BigDecimal.ROUND_DOWN);
                        wallet.setBalance(balance.toString());
                    }
                    App.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCreateSuccess(wallet);
                        }
                    });
                } catch (final Exception e) {
                    App.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCreateFailure(e.getMessage());
                        }
                    });
                }

            }
        }.start();
    }

    /**
     * 没有所谓的 修改密码 修改的密码实现是 利用私匙重新生成一个keystore
     *
     * @param newPassWord
     * @param handler
     */
    public void editPassWord(final WalletBean wallet, final String newPassWord, final Handler handler) {
        if (!isInit) {
            sendResult(handler, ApiConstant.WalletManagerType.WALLET_EDIT_PASS.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    Credentials credentials = WalletUtils.loadCredentials(wallet.getPassword(), wallet.getKeystore());
                    if (null == credentials || !credentials.getAddress().equals(wallet.getAddress())) {
                        sendResult(handler, ApiConstant.WalletManagerType.WALLET_EDIT_PASS.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                        return;
                    }
                    wallet.setPrivateKey(credentials.getEcKeyPair().getPrivateKey().toString(16));
                    importWallt(wallet, newPassWord);
                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_EDIT_PASS.getDesc(), ApiConstant.WalletManagerErrCode.SUCCESS.name(), null, wallet);
                } catch (Exception e) {
                    Log.e("wallet", "", e);
                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_EDIT_PASS.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                }

            }
        }.start();
    }

    /**
     * 异步获取 私钥
     *
     * @param keystore
     * @param password
     * @param handler
     */
    public void exportPrivateKey(final String keystore, final String password, final Handler handler) {
        if (!isInit) {
            sendResult(handler, ApiConstant.WalletManagerType.WALLET_EDIT_PASS.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    Credentials credentials = WalletUtils.loadCredentials(password, keystore);
                    String keyStroe = credentials.getEcKeyPair().getPrivateKey().toString(16);
                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_EXPORT_PRIVATEKEY.getDesc(), ApiConstant.WalletManagerErrCode.SUCCESS.name(), null, keyStroe);
                } catch (Exception e) {
                    Log.e("wallet", "", e);
                    sendResult(handler, ApiConstant.WalletManagerType.WALLET_EXPORT_PRIVATEKEY.getDesc(), ApiConstant.WalletManagerErrCode.FAIL.name(), null, null);
                }

            }
        }.start();
    }

    private void getBlance(WalletBean wallet) throws IOException {
        if (null == wallet) {
            wallet = mainWallet;
        }
        wallet.setAddress("0xf7171d1EA98F698e22EF0EbFb5498d0C2CA83890");//测试地址
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(wallet.getAddress());
        inputParameters.add(address);
        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);


        String dealString = FunctionEncoder.encode(function);//交易串
//        String contractAddress = "0xbc647aad10114b89564c0a7aabe542bd0cf2c5af";//代币地址
        Transaction transaction = Transaction.createEthCallTransaction(wallet.getAddress(), contractAddress, dealString);

        EthCall ethCall;
        BigInteger balanceValue;
        ethCall = web3.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
        Future<EthCall> s = web3.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync();
        try {
            s.get().getValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String value = ethCall.getValue();
        List<Type> results = FunctionReturnDecoder.decode(value, function.getOutputParameters());
        if (results != null && results.size() > 0) {
            balanceValue = (BigInteger) results.get(0).getValue();
            BigDecimal bigDecimal = new BigDecimal(balanceValue);
            BigDecimal balance = bigDecimal.divide(DEF_WALLET_DECIMALS).setScale(DEF_WALLET_DECIMALS_UNIT, BigDecimal.ROUND_DOWN);
            wallet.setBalance(balance.toString());
        }
    }


    private void importWallt(WalletBean wallet, String passWord) throws CipherException, IOException {
        if (null == wallet) wallet = mainWallet;
        if (null == passWord) passWord = wallet.getPassword();
        String keystore;
        String key = wallet.getPrivateKey();
        Log.i("key", "importWallt: " + key);
        BigInteger privateKeyBig = new BigInteger(key, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(privateKeyBig);
        keystore = WalletUtils.generateWalletFile(passWord, ecKeyPair, new File(DEF_WALLET_PATH), false);
        keystore = DEF_WALLET_PATH + "/" + keystore;

        Logger.i("new keystore ==>" + keystore);

        //发生更换了
        if (null != wallet.getKeystore() && !wallet.getKeystore().equals(keystore)) {
            String old = wallet.getKeystore();
            //删除旧的keystore
            File file = new File(old);
            if (file.exists()) {
                file.delete();
            }
        }
        wallet.setKeystore(keystore);
        wallet.setPassword(passWord);
    }

    /**
     * 为用户创建钱包凭证
     * <p>
     * Credentials ：包含钱包地址，address
     * <p>
     * 包含公私钥 ECKeyPair
     */
    private void loadWalletBaseInfo(WalletBean wallet) throws IOException, CipherException {
        Credentials credentials = WalletUtils.loadCredentials(wallet.getPassword(), wallet.getKeystore());
        wallet.setAddress(credentials.getAddress());
        wallet.setPrivateKey(credentials.getEcKeyPair().getPrivateKey().toString(16));
        wallet.setPublickey(credentials.getEcKeyPair().getPublicKey().toString(16));
        printtest("address " + credentials.getAddress());
        printtest("xx  " + credentials.getEcKeyPair().getPublicKey().toString(16));
        printtest("oo  " + credentials.getEcKeyPair().getPrivateKey().toString(16));
        WalletDaoTools.saveWallet(wallet);
    }


    private String readAssetsTxt(Context context, String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName + ".txt");
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }

    private List<String> readAllLines(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        List<String> data = new ArrayList<String>();
        for (String line; (line = br.readLine()) != null; ) {
            data.add(line);
        }
        return data;
    }

    private Bip39Wallet generateBip39Wallet(String password, File destinationDirectory)
            throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);//产红一个随机数

        //生成助记词
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        Log.i("mnemonic", "generateBip39Wallet: " + mnemonic);
        //根据助记词生成种子
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
        //根据种子生成秘钥对
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
        //完成钱包的创建
        String walletFile = WalletUtils.generateWalletFile(password, privateKey, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }
    /*public ResponseModel<String> createWallet(WalletCreateRquest request) {
        ResponseModel<String> responseModel = new ResponseModel<String>();
        responseModel.data = "123123123";
        responseModel.code = String.valueOf(ApiConstant.ApiErrCode.SUCCESS.getDesc());
        responseModel.msg = String.valueOf(ApiConstant.ApiErrMsg.SUCCESS.getDesc());
        return responseModel;
    }*/

    /**
     * 发送消息
     *
     * @param handler 处理消息的 对象
     * @param type
     * @param code
     * @param msg
     * @param data
     */
    private void sendResult(Handler handler, int type, String code, String msg, Object data) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.data = data;
        responseModel.code = code;
        responseModel.msg = msg;
        aidsendMessage(handler, type, responseModel);
    }

    private void aidsendMessage(Handler handler, int what, Object obj) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);

    }

    public static void printtest(String info) {
        Logger.d(info);
    }
}

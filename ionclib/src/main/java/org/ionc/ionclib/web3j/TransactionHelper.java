package org.ionc.ionclib.web3j;

import org.ionc.ionclib.bean.WalletBeanNew;
import org.ionc.ionclib.utils.LoggerUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * describe: 交易的辅助类
 * 交易参数的封装,封装交易参数
 *
 * @author xubinbin
 * @date 2019/03/26
 */
public class TransactionHelper {
    /**
     * 转出钱包
     */
    private WalletBeanNew walletBeanTx;
    /**
     * 转入地址
     */
    private String toAddress;
    /**
     * gas价格,以Gwei为单位
     */
    private BigDecimal gasPrice;
    /**
     * 转账金额
     */
    private String txValue;
    private BigInteger gasLimit;

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public TransactionHelper setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
        return this;
    }

    /**
     * 转出钱包,泳衣获取转出所需相关的必备参数
     *
     * @param walletBeanTx
     * @return
     */
    public TransactionHelper setWalletBeanTx(WalletBeanNew walletBeanTx) {
        this.walletBeanTx = walletBeanTx;
        return this;
    }

    /**
     * 转出地址
     *
     * @param toAddress
     * @return
     */
    public TransactionHelper setToAddress(String toAddress) {
        this.toAddress = toAddress;
        return this;
    }

    /**
     * @param gasPrice
     * @return
     */
    public TransactionHelper setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
        return this;
    }

    public TransactionHelper setTxValue(String txValue) {
        this.txValue = txValue;
        return this;
    }

    public WalletBeanNew getWalletBeanTx() {
        return walletBeanTx;
    }

    public String getToAddress() {
        return toAddress;
    }

    /**
     * 将 getGasPriceETH 转为 wei,再转为 BigInteger类型
     *
     * @return 当前的 getGasPriceETH 单位 wei
     */
    public BigInteger getGasPrice() {
        return gasPrice.toBigInteger();
    }

    /**
     * 将 用户设置的转账金额转为以太坊接口中所需要的数据格式
     *
     * @return 转账金额
     */
    public BigInteger getTxValue() {
        BigInteger value = Convert.toWei(BigDecimal.valueOf(Double.parseDouble(txValue)), Convert.Unit.ETHER).toBigInteger();
        LoggerUtils.i("value", String.valueOf(value));
        return value;
    }

    @Override
    public String toString() {
        return "TransactionHelper{" +
                "walletBeanTx=" + walletBeanTx +
                ", toAddress='" + toAddress + '\'' +
                ", gasPrice=" + gasPrice +
                ", txValue='" + txValue + '\'' +
                ", gasLimit=" + gasLimit +
                '}';
    }
}

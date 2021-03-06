package org.ionc.ionclib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.math.BigInteger;

/**
 * 交易记录
 */
@Entity
public class TxRecordBean implements Parcelable, Comparable<TxRecordBean> {
    @Id(autoincrement = true)
    private Long id;
    private String hash;
    private String nonce;
    private String blockHash;
    private String blockNumber;
    private String transactionIndex;
    private String from;
    private String to;
    private String value;
    private String gasPrice;
    private String gas;
    private String input;
    private String creates;
    private String publicKey;
    private String raw;
    private String r;
    private String s;
    private Integer v;  // see https://github.com/web3j/web3j/issues/44

    /**
     * 本地交易的时时间戳
     */
    private String tc_in_out;


    @Override
    public int compareTo(TxRecordBean txRecordBean) {
        return new BigInteger(txRecordBean.getTc_in_out()).compareTo(new BigInteger(this.getTc_in_out()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getCreates() {
        return creates;
    }

    public void setCreates(String creates) {
        this.creates = creates;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getTc_in_out() {
        return tc_in_out;
    }

    public void setTc_in_out(String tc_in_out) {
        this.tc_in_out = tc_in_out;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.hash);
        dest.writeString(this.nonce);
        dest.writeString(this.blockHash);
        dest.writeString(this.blockNumber);
        dest.writeString(this.transactionIndex);
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeString(this.value);
        dest.writeString(this.gasPrice);
        dest.writeString(this.gas);
        dest.writeString(this.input);
        dest.writeString(this.creates);
        dest.writeString(this.publicKey);
        dest.writeString(this.raw);
        dest.writeString(this.r);
        dest.writeString(this.s);
        dest.writeValue(this.v);
        dest.writeString(this.tc_in_out);
    }

    public TxRecordBean() {
    }

    protected TxRecordBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.hash = in.readString();
        this.nonce = in.readString();
        this.blockHash = in.readString();
        this.blockNumber = in.readString();
        this.transactionIndex = in.readString();
        this.from = in.readString();
        this.to = in.readString();
        this.value = in.readString();
        this.gasPrice = in.readString();
        this.gas = in.readString();
        this.input = in.readString();
        this.creates = in.readString();
        this.publicKey = in.readString();
        this.raw = in.readString();
        this.r = in.readString();
        this.s = in.readString();
        this.v = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tc_in_out = in.readString();
    }

    @Generated(hash = 217395210)
    public TxRecordBean(Long id, String hash, String nonce, String blockHash, String blockNumber,
            String transactionIndex, String from, String to, String value, String gasPrice, String gas,
            String input, String creates, String publicKey, String raw, String r, String s, Integer v,
            String tc_in_out) {
        this.id = id;
        this.hash = hash;
        this.nonce = nonce;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.transactionIndex = transactionIndex;
        this.from = from;
        this.to = to;
        this.value = value;
        this.gasPrice = gasPrice;
        this.gas = gas;
        this.input = input;
        this.creates = creates;
        this.publicKey = publicKey;
        this.raw = raw;
        this.r = r;
        this.s = s;
        this.v = v;
        this.tc_in_out = tc_in_out;
    }

    public static final Creator<TxRecordBean> CREATOR = new Creator<TxRecordBean>() {
        @Override
        public TxRecordBean createFromParcel(Parcel source) {
            return new TxRecordBean(source);
        }

        @Override
        public TxRecordBean[] newArray(int size) {
            return new TxRecordBean[size];
        }
    };

    @Override
    public String toString() {
        return "TxRecordBean{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", nonce='" + nonce + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", blockNumber='" + blockNumber + '\'' +
                ", transactionIndex='" + transactionIndex + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", value='" + value + '\'' +
                ", getGasPriceETH='" + gasPrice + '\'' +
                ", gas='" + gas + '\'' +
                ", input='" + input + '\'' +
                ", creates='" + creates + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", raw='" + raw + '\'' +
                ", r='" + r + '\'' +
                ", s='" + s + '\'' +
                ", v=" + v +
                ", tc_in_out='" + tc_in_out + '\'' +
                '}';
    }
}

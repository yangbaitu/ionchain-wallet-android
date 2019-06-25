package org.ionc.wallet.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TxRecordBeanHelper {
    @Id(autoincrement = true)
    private Long id;
    private Long indexMax;
    private String publicKey;

    @Generated(hash = 1496552963)
    public TxRecordBeanHelper(Long id, Long indexMax, String publicKey) {
        this.id = id;
        this.indexMax = indexMax;
        this.publicKey = publicKey;
    }

    @Generated(hash = 409002779)
    public TxRecordBeanHelper() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndexMax() {
        return indexMax;
    }

    public void setIndexMax(Long indexMax) {
        this.indexMax = indexMax;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "TxRecordBeanHelper{" +
                "id=" + id +
                ", indexMax=" + indexMax +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}

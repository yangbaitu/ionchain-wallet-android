package org.ionc.wallet.bean;

import java.io.Serializable;
import java.util.List;

public class NodeBean {

    /**
     * code : 0
     * msg : 操作成功!
     * data : [{"ionc_node":"xxx-test"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * ionc_node : xxx-test
         */

        private String ionc_node;

        public String getIonc_node() {
            return ionc_node;
        }

        public void setIonc_node(String ionc_node) {
            this.ionc_node = ionc_node;
        }
    }
}

package org.asname.integration.ws1;

import java.io.Serializable;

public class ResultType implements Serializable {

    private static final long serialVersionUID = 6719441844429518939L;

    private int result;

    public int getResult() {
        return result;
    }

    public void setResult(int value) {
        this.result = value;
    }

}

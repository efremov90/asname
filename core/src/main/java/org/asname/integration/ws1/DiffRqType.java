package org.asname.integration.ws1;

import java.io.Serializable;

public class DiffRqType implements Serializable {

    private static final long serialVersionUID = 8097440018557706571L;

    private int param1;
    private int param2;

    public int getParam1() {
        return param1;
    }

    public void setParam1(int value) {
        this.param1 = value;
    }

    public int getParam2() {
        return param2;
    }

    public void setParam2(int value) {
        this.param2 = value;
    }

}

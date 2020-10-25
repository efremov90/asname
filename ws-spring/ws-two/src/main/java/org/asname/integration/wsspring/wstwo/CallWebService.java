package org.asname.integration.wsspring.wstwo;

public interface CallWebService {

    public AddRsType add(AddRqType addRq);

    public ResultType diff(DiffRqType diffRq);
}
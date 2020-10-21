package org.asname.integration.wsspring;

public interface CallWebService {

    public AddRsType add(AddRqType addRq);

    public ResultType diff(DiffRqType diffRq);
}
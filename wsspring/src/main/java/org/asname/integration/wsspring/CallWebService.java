package org.asname.integration.wsspring;

public interface CallWebService {

    public ResultType add(AddRqType addRq);

    public ResultType diff(DiffRqType diffRq);
}
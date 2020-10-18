package org.asname.integration.ws1;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface CalWebService {

    @WebMethod
    public ResultType add(
                    AddRqType addRq);

    @WebMethod
    public ResultType diff(
                    DiffRqType diffRq);

}

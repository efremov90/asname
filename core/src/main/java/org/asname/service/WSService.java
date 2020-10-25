package org.asname.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dto.ErrorDTO;
import org.asname.dto.ErrorResponseDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WSService {
    public String getExceptionString(Exception e) throws IOException {
        return e.toString() + "\n" +
                Arrays.stream(e.getStackTrace()).map(x -> x.toString()).collect(Collectors.joining("\n"));
    }
}

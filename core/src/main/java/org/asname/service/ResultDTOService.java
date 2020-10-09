package org.asname.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asname.dto.ErrorDTO;
import org.asname.dto.ErrorResponseDTO;
import org.asname.dto.ResultDTO;
import org.asname.dto.ResultResponseDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ResultDTOService {
    public static void writer(HttpServletResponse resp, String code, String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println(mapper.writeValueAsString(new ResultResponseDTO(new ResultDTO(code, message))));
        out.close();
    }
}

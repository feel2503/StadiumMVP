package com.thiscat.stadiumamp.system.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Data
@NoArgsConstructor
public class InputRequestBodyWriteFile {
    Logger logger = LoggerFactory.getLogger(InputRequestBodyWriteFile.class);

    public void writeRequestBodyToFile(ServletRequestAttributes attr) {
        HttpServletRequest request = attr.getRequest();
        String method=request.getMethod();
        String url=request.getRequestURI().replaceAll("/","_");
        String saveLogFile=method+url;

        String inputString = request.getAttribute("requestBody").toString();
        // 캐리지 리턴후 저장

       // MDC.put("urlLog", saveLogFile);
        logger.debug("[["+request.getRequestURI()+" ]]");
        logger.debug(inputString);
        //MDC.clear();

    }
}

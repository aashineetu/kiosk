package com.kiosk.kiosk;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class KioskPrintCardController {

    @RequestMapping(method = RequestMethod.POST, value = "/kiosk/printCard",
            consumes = "text/plain", produces = "application/json")

    public ResponseEntity<Boolean> vinCheckerWithRequestParam(
            @RequestParam("operationName") String operationName) {


        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.OK);
        return responseEntity;
    }
    
}

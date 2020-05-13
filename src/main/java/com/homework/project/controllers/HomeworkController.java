package com.homework.project.controllers;

import com.homework.project.services.DigitService;
import com.homework.project.validation.DigitException;
import com.homework.project.validation.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeworkController {

    @Autowired
    private DigitService digitService;

    private Logger log = LogManager.getLogger(HomeworkController.class);

    /**
     * @param number to process calculation based on homework assignment
     * @return calculated result
     */
    @RequestMapping(value = "/{number}", method = RequestMethod.GET)
    public ResponseEntity<Integer> calculateNumber(@PathVariable int number) throws DigitException {
        log.info("receive=[GET]/{number}, action=getCalculateNumberStart, callType=REST, number=" + number);
        int result = digitService.calculate(number);
        log.info("action=getCalculateNumberEnd, result=" + result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(DigitException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);
    }
}

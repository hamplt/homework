package com.homework.project.services;

import com.homework.project.validation.DigitException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DigitService {

    private Logger log = LogManager.getLogger(DigitService.class);

    /**
     * process calculation of given number
     *
     * @param number to be calculated
     * @return calculated number
     * @throws DigitException in case of bad input that would cause division by 0
     */
    public int calculate(int number) throws DigitException {
        log.debug("method=calculateStart, number=" + number);
        List<Integer> digits = getDigits(number);
        validateDigits(digits);
        moveDigits(digits);
        multiplyDigits(digits);
        correctDigits(digits);
        removeDigits(digits);
        int result = divideByNumberOfEvenDigits(digits);
        log.debug("method=calculateEnd, result=" + result);
        return result;
    }

    /**
     * divide the resulting number by number of even digits
     * example: 45326181 / 4 => 11331545
     */
    private int divideByNumberOfEvenDigits(List<Integer> digits) {
        log.debug("method=divideByNumberOfEvenDigitsStart, digits=" + digits);
        int result = convertToNumber(digits) / getNumberOfEvenDigits(digits);
        log.debug("method=divideByNumberOfEvenDigitsEnd, result=" + result);
        return result;
    }

    /**
     * @return number of even digits
     */
    private int getNumberOfEvenDigits(List<Integer> digits) {
        log.debug("method=getNumberOfEvenDigitsStart, digits=" + digits);
        int result = 0;
        for (int digit : digits) {
            if (digit % 2 == 0) {
                result = result + 1;
            }
        }
        log.debug("method=getNumberOfEvenDigitsEnd, result=" + result);
        return result;
    }

    /**
     * remove all digits of value 7
     */
    private void removeDigits(List<Integer> digits) {
        log.debug("method=removeDigitsStart, digits=" + digits);
        digits.removeIf(s -> (s == 7));
        log.debug("method=removeDigitsEnd, digits=" + digits);
    }

    /**
     * all digits of value 8 or 9 multiply by 2
     * example: 45326791 => 453267181
     */
    private void multiplyDigits(List<Integer> digits) {
        log.debug("method=multiplyDigitsStart, digits=" + digits);
        for (int i = 0; i < digits.size(); i++) {
            if (digits.get(i) == 8 || digits.get(i) == 9) {
                digits.set(i, digits.get(i) * 2);
            }
        }
        log.debug("method=multiplyDigitsEnd, digits=" + digits);
    }

    /**
     * move digits that are equal to or less than 3 to the right by 1 position
     * example: 43256791 => 45326791
     */
    private void moveDigits(List<Integer> digits) {
        log.debug("method=moveDigitsStart, digits=" + digits);
        int tmp;
        for (int i = digits.size() - 1; i >= 0; i--) {
            if (digits.get(i) <= 3 && i != digits.size() - 1) {
                tmp = digits.get(i + 1);
                digits.set(i + 1, digits.get(i));
                digits.set(i, tmp);
            }
        }
        log.debug("method=moveDigitsEnd, digits=" + digits);
    }

    /**
     * @return list of digits collected from number
     */
    private List<Integer> getDigits(int number) {
        log.debug("method=getDigitsStart, number=" + number);
        List<Integer> digits = new ArrayList<>();
        collectDigits(number, digits);
        log.debug("method=getDigitsEnd, digits=" + digits);
        return digits;
    }

    /**
     * fill the list of digits from given number
     */
    private void collectDigits(int number, List<Integer> digits) {
        if (number / 10 > 0) {
            collectDigits(number / 10, digits);
        }
        digits.add(number % 10);
    }

    /**
     * @return converted number from list of digits
     */
    private int convertToNumber(List<Integer> digits) {
        log.debug("method=convertToNumberStart, digits=" + digits);
        int result = 0;
        for (int digit : digits) {
            if (digit >= 10) {
                result = result * 10 + digit / 10;
                result = result * 10 + digit % 10;
            } else {
                result = result * 10 + digit;
            }
        }
        log.debug("method=convertToNumberEnd, result=" + result);
        return result;
    }

    /**
     * correct the digits after multiplying numbers 8 and 9 (split 16 to 1, 6 or 18 to 1, 8)
     */
    private void correctDigits(List<Integer> digits) {
        log.debug("method=correcrDigitsStart, digits=" + digits);
        int number = convertToNumber(digits);
        digits.clear();
        digits.addAll(getDigits(number));
        log.debug("method=correctDigitsEnd, digits=" + digits);
    }

    /**
     * validate digits so that there is at least one even digit or value 9 (that will be doubled)
     * for further division based on even numbers
     *
     * @throws DigitException if there is no even digit or value 9
     */
    private void validateDigits(List<Integer> digits) throws DigitException {
        int validDigits = 0;
        for (int digit : digits) {
            if (digit % 2 == 0 || digit == 9) {
                validDigits = validDigits + 1;
            }
        }
        if (validDigits == 0) {
            int number = convertToNumber(digits);
            throw new DigitException("Invalid number: " + number + " that would cause division by 0");
        }
    }
}

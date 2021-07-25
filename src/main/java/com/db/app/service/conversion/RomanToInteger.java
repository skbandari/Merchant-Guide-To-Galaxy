package com.db.app.service.conversion;

public class RomanToInteger {

    /**
     * Method to convert roman numeric to it's equivalent decimal
     *
     * @param romanNumber roman literal in String format
     * @return decimal value for given roman literal
     */
    public static float romanToDecimal(java.lang.String romanNumber) {

        float decimal = 0;
        float lastNumber = 0;
        char[] romanNumeral = romanNumber.toUpperCase().toCharArray();

        //Operation to be performed on upper cases even if user enters Roman values in lower case chars
        for (int i = romanNumeral.length- 1; i >= 0 ; i--) {
            Character romanCharacter = romanNumeral[i];

            switch (romanCharacter) {
                case 'M':
                    decimal = processDecimal(1000, lastNumber, decimal);
                    lastNumber = 1000;
                    break;

                case 'D':
                    decimal = processDecimal(500, lastNumber, decimal);
                    lastNumber = 500;
                    break;

                case 'C':
                    decimal = processDecimal(100, lastNumber, decimal);
                    lastNumber = 100;
                    break;

                case 'L':
                    decimal = processDecimal(50, lastNumber, decimal);
                    lastNumber = 50;
                    break;

                case 'X':
                    decimal = processDecimal(10, lastNumber, decimal);
                    lastNumber = 10;
                    break;

                case 'V':
                    decimal = processDecimal(5, lastNumber, decimal);
                    lastNumber = 5;
                    break;

                case 'I':
                    decimal = processDecimal(1, lastNumber, decimal);
                    lastNumber = 1;
                    break;
            }
        }
        return decimal;
    }

    /**
     * processDecimal() applied all subtraction logic and returns the result
     * @param decimal
     * @param lastNumber
     * @param lastDecimal
     * @return
     */
    public static float processDecimal(float decimal, float lastNumber, float lastDecimal) {
        if (lastNumber > decimal) {
            return lastDecimal - decimal;
        }
        else {
            return lastDecimal + decimal;
        }
    }
}

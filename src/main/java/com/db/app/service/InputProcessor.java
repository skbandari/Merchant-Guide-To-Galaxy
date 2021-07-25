package com.db.app.service;

import com.db.app.service.conversion.RomanToInteger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Service class to process input file
 */
public class InputProcessor {

    // regular expression copied from online to validate roman numerals.
     public static String romanNumberValidator = "^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";

     public static Map<String, String> questionsMapping = new HashMap<>(); // line ends with ?
     public static Map<String, String> metalRomanValuesMapping = new HashMap<>(); // Ex: glob is I
     public static ArrayList<String> hiddenValues = new ArrayList<>(); // Ex: glob glob is 34 Credits
     public static Map<String, Float> metalValuesMapping = new HashMap<>(); // Ex: Gold = 14450.0

     static Predicate<String> endsWithQuestionMark = i -> (i.equals("?"));
     static Predicate<String> endsWithCredits = i -> (i.equals("Credits"));
     static Predicate<String> validRomanNumeral = i -> (i.matches(romanNumberValidator));



    /**
     * Provided input file is picked up to process by default. If no file is provided then
     * default input file from resources is picked up for processing.
     *
     * @param filePath input file to process
     * @throws IOException
     */
    public static void processFile(String filePath) throws IOException {
        BufferedReader bufferedReader = null;
        if (filePath == null){
            // process file from resources
            InputStream in = InputProcessor.class.getResourceAsStream("Input");
            bufferedReader =new BufferedReader(new InputStreamReader(in));
        }
        else{
            // process file from arguments
            FileReader fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
        }
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            processLine(line);
        }

//        System.out.println("Questions Mapping:  " + questionsMapping);
//        System.out.println("MetalRoman: " + metalRomanValuesMapping);
//        System.out.println("Hiden Values: " +  hiddenValues);
        bufferedReader.close();
    }

    /**
     * Map metals to their integral values
     */
    public static void mapMetaltoIntegerValue() {
        Iterator it = metalRomanValuesMapping.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry metal = (Map.Entry)it.next();

            String romanValue = metal.getValue().toString();

            if (!validRomanNumeral.test(romanValue)) {
                System.err.println(romanValue + " is not a valid numeral. Ignoring " + metal.getKey());
                System.exit(0);
//                continue;
            }

            // find integer value for given roman literal
            float integerValue = RomanToInteger.romanToDecimal(romanValue);

            // insert integer value into metal values map
            metalValuesMapping.put(metal.getKey().toString(), integerValue);
        }

        mapMissingEntities();

//        System.out.println("Metal Values Mapping: " + metalValuesMapping);

    }

    /**
     * FInds the value of elements by decoding queries like [glob glob Silver is 34 Credits]
     */
    private static void mapMissingEntities(){
        for (int i = 0; i < hiddenValues.size(); i++) {
            processhiddenValues(hiddenValues.get(i));
        }
    }

    /**
     * Calculates the values of various elements and appends the same to map elementValueList.
     * elementValueList :{Gold=14450.0, Iron=195.5, Silver=17.0}
     * @param query
     */
    private static void processhiddenValues(String query){
        String array[] = query.split(" ");
        int splitIndex = 0;
        int creditValue = 0; String element= null; String[] valueofElement = null;
        for (int i = 0; i < array.length; i++) {
            if(array[i].toLowerCase().equals("credits")){
                creditValue = Integer.parseInt(array[i-1]);
            }
            if(array[i].toLowerCase().equals("is")){
                splitIndex = i-1;
                element = array[i-1];
            }
            valueofElement = java.util.Arrays.copyOfRange(array, 0, splitIndex);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < valueofElement.length; j++) {
            stringBuilder.append(metalRomanValuesMapping.get(valueofElement[j]));
        }
        float valueOfElementInDecimal = RomanToInteger.romanToDecimal(stringBuilder.toString());
        metalValuesMapping.put(element, creditValue/valueOfElementInDecimal);
    }




    /**
     * Process each line to store information from the input file into various collections
     * <p>
     * It is assumed that each line from input will fall in one of the three following categories:
     * <p>
     * 1. Line will end with question mark
     * 2. Metals or Materials are defined using format: <i><b>Metal<b> is <b>roman numeral<b></i>. Example: glob is I
     * 3. Line ends with the word <b>Credits<b> to indicate that a metal or material is worth mentioned credits. Example: glob glob is 34 Credits
     *
     * @param line each line from input file
     */
    private static void processLine(String line){
        //TODO - check below reg Expression (copied from online)
        String[] wordsInTheLine = line.split("((?<=:)|(?=:))|( )");

        String lastWord = wordsInTheLine[wordsInTheLine.length -1];

        //TODO - if-else can be replaced with DSL ?
        if (endsWithQuestionMark.test(lastWord)){
            questionsMapping.put(line,"");
        }
        else if (wordsInTheLine.length == 3 && wordsInTheLine[1].equalsIgnoreCase("is")){
            metalRomanValuesMapping.put(wordsInTheLine[0], lastWord);
        }
        else if(endsWithCredits.test(lastWord)){
            hiddenValues.add(line);
        }
    }

}

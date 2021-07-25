package com.db.app.service;

import com.db.app.service.InputProcessor;
import com.db.app.service.conversion.RomanToInteger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputProcessor extends InputProcessor {

        /**
         * processReplyForQuestion() itertates over the <p>questionsMapping</p> map that contain all the valid queries as keys.
         * It further invokes processReply() on each key for processing the response.
         */
        public static void processReplyForQuestion(){
            Map<String, String> map = questionsMapping;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                processReply(entry.getKey());
            }
        }

        private static void processReply(String query){
            if (query.startsWith("how much")){
                findValueOfRoman(query);
            }
            else if (query.startsWith("how many")){
                findValueOfElement(query);
            }

        }

        /**
         * Processes the queries seeking the decimal equivalent of any RomanNumeral and prints the result.
         *
         * @param query question that is passed. Example: how much is pish tegj glob glob ?
         */
        private static void findValueOfRoman(String query){
            if (isValidinput(query)){
                ArrayList<String> tokenValueToRoman = new ArrayList<>(); // Ex: XIV
                ArrayList<String> tokenValue = splitQuery(query);
                for (String s : tokenValue) {
                    tokenValueToRoman.add(metalRomanValuesMapping.get(s));
                }
                float value = RomanToInteger.romanToDecimal(tokenValueToRoman.toString());
                tokenValue.add("is");tokenValue.add(Float.toString(value));
                System.out.println(query + " " + outputFormatter(tokenValue));
            }
            else{
                System.err.println(query+" : I have no idea what you are talking about");
            }
        }


        /**
         * Processes the queries seeking the Credit value of any quantity of elements and prints the result.
         *
         * @param query question that is passed. Example: how many Credits is glob prok Gold ?
         */
        private static void findValueOfElement(String query){
            if (isValidinput(query)){
                ArrayList<String> tokenValue = splitQuery(query); // Ex: glob prok Gold
                ArrayList<String> tokenValueToRoman = new ArrayList<>(); // Ex: IV
                String element = null;
                for (String s : tokenValue) {
                    if (metalRomanValuesMapping.get(s) != null) {
                        tokenValueToRoman.add(metalRomanValuesMapping.get(s));
                    } else if (metalValuesMapping.get(s) != null) {
                        element = s;
                    } else {
                        System.err.println(query + " : I have no idea what you are talking about");
                    }
                }
                float elementValue = (RomanToInteger.romanToDecimal(tokenValueToRoman.toString()) * metalValuesMapping.get(element));
                tokenValue.add("is");tokenValue.add(Float.toString(elementValue));tokenValue.add("Credits");
                System.out.println(query + " "+ outputFormatter(tokenValue));
            }
            else{
                System.err.println(query+" : I have no idea what you are talking about");
            }
        }

        /**
         * Formats the response to a query and displays it in readable format
         * @param output
         * @return
         */
        private static String outputFormatter(ArrayList<String> output){
            return output.toString().replace(",", "").replace("[", "").replace("]", "");
        }

        // Applies regex on each input in the file to figure out the valid ones.
        private static boolean isValidinput(String query){
            Pattern regex = Pattern.compile("[$&+,:;=@#|]");
            Matcher matcher = regex.matcher(query);
            if (matcher.find()){
                return false;
            } else {
                return true;
            }

        }

        /**
         * Splits the query and returns an ArrayList containing only Roman numerals and elements
         * @param query
         * @return
         */
        private static ArrayList<String> splitQuery(String query){
//            ArrayList<String> queryArray = new ArrayList<>(Arrays.asList(query.split(" ")));
            ArrayList<String> queryArray = new ArrayList<>(Arrays.asList(query.split("((?<=:)|(?=:))|( )")));
            int startIndex = 0, endIndex = 0;
            for (int i = 0; i < queryArray.size(); i++) {
                if(queryArray.get(i).toLowerCase().equals("is")){
                    startIndex = i+1;
                }
                else if(queryArray.get(i).toLowerCase().equals("?")){
                    endIndex = i;

                }
            }
            String[] array = queryArray.toArray(new String[query.length()]);
            return new ArrayList<>(Arrays.asList(java.util.Arrays.copyOfRange(array, startIndex, endIndex)));

        }

    }

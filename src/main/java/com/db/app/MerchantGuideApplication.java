package com.db.app;

import com.db.app.service.InputProcessor;
import com.db.app.service.OutputProcessor;

import java.io.IOException;

/**
 * Main entry point into Merchant-Guide-To-Galaxy Application
 * <p>
 * It is assumed that the application is case sensitive i.e. glob is not same as Glob
 */
public class MerchantGuideApplication {

    /**
     * If no argument is provided then the input file present in the resources folder is picked as default
     * <p>
     * It is assumed that if input passed via arguments then only one argument i.e. args[0] is passed.
     * <p>
     * It is further assumed that through out the program, we use System out logs instead of any Logger for simplicity purpose
     *
     * @param args file that can act as an input
     */
    public static void main(String[] args) {
        String filePath = null;
        if (args.length != 0)
            filePath = args[0];
        try{
            // process input file
            InputProcessor.processFile(filePath);


            // map roman to integral values
            InputProcessor.mapMetaltoIntegerValue();

            // process questions
            OutputProcessor.processReplyForQuestion();
        }
        catch(RuntimeException | IOException e){
            System.out.println("Failed to process Merchant Guide Application. Error: " + e);
        }
    }
}

package com.db.app;

import com.db.app.service.OutputProcessor;
import com.db.app.service.InputProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class MerchantGuideApplicationTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    protected String filePath;

    @Before
    public void setUpStreams() {
        filePath = null;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void testProgram() throws IOException {
        InputProcessor.processFile(filePath);
        InputProcessor.mapMetaltoIntegerValue();
        OutputProcessor.processReplyForQuestion();
        Assert.assertEquals(
                        "how many Credits is glob prok Iron ? glob prok Iron is 782.0 Credits\r\n" +
                                "how much is pish tegj glob glob ? pish tegj glob glob is 42.0\r\n" +
                                "how many Credits is glob prok Gold ? glob prok Gold is 57800.0 Credits\r\n" +
                                "how many Credits is glob prok Silver ? glob prok Silver is 68.0 Credits\r\n"
                , outContent.toString());
    }


}
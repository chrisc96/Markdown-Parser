package com;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {

    @Parameter(names={"--inputFile", "-i"})
    String inputFile;

    @Parameter(names={"--format", "-f"})
    String outputFormat;

    public static void main(String ... argv) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    public void run() {
        System.out.printf("%s %s", inputFile, outputFormat);
    }
}
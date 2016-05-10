import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Daniel Reisel && Ari Zellner on 19/04/16.
 *              304953243       201524089
 * nand2tetris project 07
 *
 */
public class VMtranslator {

    public static void main(String[] args) {
        File[] files = new File[1];
        File output;
        if (args.length < 1) {
            System.out.println("Must provide input file");
            return;
        }
        String fileName = args[0].trim();
        File file = new File(fileName);
        if(file.isFile()){
            files[0] = file;
            String outputPath = file.getAbsolutePath().replace(".vm", ".asm");
            output = new File(outputPath);
        } else if (file.isDirectory()){
            files = file.listFiles();
            output = new File(file.getAbsolutePath() + "/" + file.getName() + ".asm");
        } else {
            System.out.println("Input file invalid");
            return;
        }
        if(output.exists()) {
            output.delete();
        }
        PrintWriter outputWriter = null;
       try {
           OutputStream os = new FileOutputStream(output);
           outputWriter = new PrintWriter(os,true); // Not ascii?
           CodeTranslator translator = new CodeTranslator("null");
           outputWriter.println(translator.initCode());
           for(File input : files){
               if(input.getAbsolutePath().endsWith(".vm")) {
                   Parser parser = new Parser(input, outputWriter, input.getName());
                   parser.parse();
               }
           }
       } catch (Exception e){
           e.printStackTrace();
           System.out.println("Can't open output File");

       } finally {
            if(outputWriter != null) {
                outputWriter.flush();
                outputWriter.close();
            }
       }
    }
}
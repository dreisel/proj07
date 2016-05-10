import java.io.*;

/**
 * created by Daniel Reisel && Ari Zellner on 19/04/16.
 *              304953243       201524089
 * nand2tetris project 07
 *
 */
public class Parser {
    File input;
    PrintWriter writer;
    CodeTranslator codeTranslator;
    String fileName;

    public Parser(File input ,PrintWriter outputWriter ,String fileName) {
        this.input = input;
        this.writer = outputWriter;
        this.fileName = fileName;
        this.codeTranslator = new CodeTranslator(fileName);

    }

    void parse(){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(input));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains("//"))
                    line = line.substring(0,line.indexOf("//"));

                line = line.trim();
                if(line.length() == 0)
                    continue;

                CodeTranslator.CommandTypes type = codeTranslator.commandType(line);
                String command = codeTranslator.parseCommand(line,type);
                writer.println(command);
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.print("Error while parsing");
        } finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException ignore) {}
            }
        }
    }
}

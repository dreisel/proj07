import java.io.*;

/**
 * created by Daniel Reisel && Ari Zellner on 19/04/16.
 *              304953243       201524089
 * nand2tetris project 07
 *
 */
public class Parser {
    File input;
    File output;
    CodeTranslator codeTranslator;
    String fileName;

    public Parser(File file , String fileName) {
        this.input = file;
        this.fileName = fileName;
        this.codeTranslator = new CodeTranslator(fileName);

    }

    void parse(){
        PrintWriter writer = null;
        BufferedReader br = null;
        try {
            String outputPath = input.getAbsolutePath().replace(".vm", ".asm");
            output = new File(outputPath);
            br = new BufferedReader(new FileReader(input));
            String line;

            writer = new PrintWriter(output); // Not ascii?

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
            writer.println(codeTranslator.getEndLoop());
        } catch (Exception e){
            System.err.print("Error while parsing");
        } finally {
            if(writer != null) {
                writer.flush();
                writer.close();
            }
            if(br != null){
                try {
                    br.close();
                } catch (IOException ignore) {}
            }
        }
    }
}

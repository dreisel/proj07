import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * ret
 * created by Daniel Reisel on 19/04/16.
 */
public class Parser {
    File input;
    File output;
    CodeTranslator codeTranslator;
    String fileName;
    Set arithmeticSet;
    final String arithmetics = "add,sub,neg,eq,gt,lt,and,or,not";
    public enum CommandTypes{
        C_ARITHMETIC("arithmetic"),
        C_POP("pop"),
        C_PUSH("push"),
        C_LABLE("lable"),
        C_GOTO("goto"),
        C_IF("if"),
        C_FUNCTION("function"),
        C_RETURN("return"),
        C_CALL("call");
        private String type;

        CommandTypes(String type) {
            this.type = type;

        }

    }

    public Parser(File file , String fileName) {
        this.arithmeticSet = new HashSet(Arrays.asList(arithmetics.split(",")));
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

                CommandTypes type = commandType(line);
                String command = parseCommand(line,type);
                writer.println(command);
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

    CommandTypes commandType(String command){

        String type = command.split(" ")[0];
        if(arithmeticSet.contains(type)){
            return CommandTypes.C_ARITHMETIC;
        }
        try {
            return CommandTypes.valueOf(type);
        } catch (Exception ignored){
            throw new IllegalArgumentException("Illegal command type");
        }


    }

    String parseCommand(String command, CommandTypes type){
        String[] commandArray = command.split(" ");
        int index;
        switch (type){
            case C_ARITHMETIC:
                return codeTranslator.translateArithmetic(commandArray[0]);
            case C_PUSH:
                index = Integer.parseInt(commandArray[2]);
                return codeTranslator.translatePush(commandArray[1], index);
            case C_POP:
                index = Integer.parseInt(commandArray[2]);
                return codeTranslator.translatePop(commandArray[1],index);
            case C_LABLE:
            case C_GOTO:
            case C_IF:
            case C_FUNCTION:
            case C_RETURN:
            case C_CALL:
            default:
                throw new IllegalArgumentException("un supported command type");
        }
    }
}
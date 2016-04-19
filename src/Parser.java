import java.io.*;

/**
 * ret
 * created by Daniel Reisel on 19/04/16.
 */
public class Parser {
    File input;
    File output;
    CodeTranslator codeTranslator = new CodeTranslator();
    public enum CommandTypes{
        C_ARITHMETIC,
        C_POP,
        C_PUSH,
        C_LABLE,
        C_GOTO,
        C_IF,
        C_FUNCTION,
        C_RETURN,
        C_CALL
    }

    public Parser(File file) {
            this.input = file;
    }

    void parse(){
        PrintWriter writer = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(input));
            String line;
            writer = new PrintWriter(output, "UTF-8");

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
        } catch (Exception e){
            System.err.print("Something Went Wrong");
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
        if (command.startsWith("pop")) return CommandTypes.C_POP ;
        else if (command.startsWith("push")) return CommandTypes.C_PUSH ;
        else return CommandTypes.C_ARITHMETIC ;
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
            default:
                throw new IllegalArgumentException("un supported command type");
        }

    }




}

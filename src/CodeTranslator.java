import java.util.*;

/**
 * created by Daniel Reisel && Ari Zellner on 19/04/16.
 *              304953243       201524089
 * nand2tetris project 07
 *
 */
public class CodeTranslator {
    String fileName;
    Map<String,String> arithmeticMap;
    Map<String,String> regMap;
    Map<String,CommandTypes> cmdMap;
    Set arithmeticSet;
    final String arithmetics = "add,sub,neg,eq,gt,lt,and,or,not";
    String endLoop = "(ENDLOOP)\n" + "@ENDLOOP\n" + "0;JMP";
    int commandCounter = 0;
    final String pushFormat = //reg D will be initialized per command
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n";

    final String popFormat =   //reg D will be initialized per command
                    "@R13\n" +
                    "M=D\n" + //r13 = pop destination (temp)
                    "@SP\n" +
                    "AM=M-1\n" + //a=*SP--
                    "D=M\n" + //d is the poped value
                    "@R13\n" +
                    "A=M\n" +
                    "M=D\n" ;

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

    public CodeTranslator(String fileName) {
        this.arithmeticSet = new HashSet(Arrays.asList(arithmetics.split(",")));
        fillArithmeticMap();
        regMap = new HashMap<>();
        regMap.put("local","LCL");
        regMap.put("argument","ARG");
        regMap.put("this","THIS");
        regMap.put("that","THAT");
        regMap.put("temp","R5");
        regMap.put("static","5");
        this.fileName = fileName;

        cmdMap = new HashMap<>();
        cmdMap.put("pop",CommandTypes.C_POP);
        cmdMap.put("push",CommandTypes.C_PUSH);
        cmdMap.put("goto",CommandTypes.C_GOTO);
        cmdMap.put("if",CommandTypes.C_IF);
        cmdMap.put("function",CommandTypes.C_FUNCTION);
        cmdMap.put("return",CommandTypes.C_RETURN);
        cmdMap.put("call",CommandTypes.C_CALL);
    }

    CommandTypes commandType(String command){
        String type = command.split(" ")[0];
        if(arithmeticSet.contains(type)){
            return CommandTypes.C_ARITHMETIC;
        }
        try {
            return cmdMap.get(type);
        } catch (Exception ignored){
            throw new IllegalArgumentException("Illegal command type");
        }
    }

    public String getEndLoop() {
        return endLoop;
    }

    String translateArithmetic(String command){
        return this.arithmeticMap.get(command)
                .replace("<%TRUE%>", "TRUE" + commandCounter)
                .replace("<%END%>", "END" + commandCounter++);
    }

    String translatePop(String segment, int index){
        //handel's this, that, argument, temp and local
        String command = regMap.get(segment);
        if(command != null){
            String setDFormat = "@%s\n" + "D=M\n" + "@%d\n" + "D=D+A\n" + "%s";
            return String.format(setDFormat,command,index,popFormat);
        }
        if (segment.equals("pointer")){
            String setDFormat = "" ;
            switch (index) {
                case 1:
                    setDFormat = "@SP\n" + "M=M-1\n" + "A=M\n" + "D=M\n" + "@THAT\n" + "M=D\n";
                    break;
                case 0:
                    setDFormat = "@SP\n" + "M=M-1\n" + "A=M\n" + "D=M\n" + "@THIS\n" + "M=D\n";
                    break;
            }
            return setDFormat;
        }
        throw new IllegalArgumentException("unsupported segment");
    }

    String translatePush(String segment, int index){

        //handel's this, that, argument, temp and local
        String command = regMap.get(segment);
        if(command != null){
            String setDFormat = "@%s\n" + "D=M\n" + "@%d\n" + "A=D+A\n" + "D=M\n" + "%s";
            return String.format(setDFormat,command,index,pushFormat);
        }
        if (segment.equals("constant")) {
            String setDFormat = "@%d\n" + "D=A\n" + "%s";
            return String.format(setDFormat, index,pushFormat);
        }
        if (segment.equals("pointer")){
            String setDFormat = "" ;
            switch (index) {
                case 1:
                    setDFormat = "@THAT\nD=M\n%s";
                    break;
                case 0:
                    setDFormat = "@THIS\nD=M\n%s";
                    break;
            }
            return String.format(setDFormat,pushFormat);
        }
        throw new IllegalArgumentException("unsupported segment");

    }

    String parseCommand(String command, CommandTypes type){
        String[] commandArray = command.split(" ");
        int index;
        switch (type){
            case C_ARITHMETIC:
                return translateArithmetic(commandArray[0]);
            case C_PUSH:
                index = Integer.parseInt(commandArray[2]);
                return translatePush(commandArray[1], index);
            case C_POP:
                index = Integer.parseInt(commandArray[2]);
                return translatePop(commandArray[1],index);
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

    private void fillArithmeticMap() {
        arithmeticMap = new HashMap<>();
        arithmeticMap.put("add", "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "M=D+M\n");
        arithmeticMap.put("sub", "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "M=M-D\n");
        arithmeticMap.put("neg", "@SP\n" +
                "A=M-1\n" +
                "D=0\n" +
                "M=D-M\n");
        arithmeticMap.put("eq", "@SP\n" +
                "AM=M-1\n" +  //SP-- m= top of stack\n" +
                "D=M\n" +
                "A=A-1\n" +  // M= second of stack\n" +
                "D=D-M\n" +
                "@<%TRUE%>\n" +
                "D;JEQ\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=0\n" +
                "@<%END%>\n" +
                "0;JMP\n" +
                "(<%TRUE%>)\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=-1\n" +
                "(<%END%>)\n" +
                "D=D\n"); // nop
        arithmeticMap.put("gt", "@SP\n" +
                "AM=M-1\n" + // //SP-- m= top of stack\n" +
                "D=M\n" +
                "A=A-1\n" + // M= second of stack\n" +
                "D=M-D\n" +
                "@<%TRUE%>\n" +
                "D;JGT\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=0\n" +
                "@<%END%>\n" +
                "0;JMP\n" +
                "(<%TRUE%>)\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=-1\n" +
                "(<%END%>)\n" +
                "D=D\n");// nop
        arithmeticMap.put("lt", "@SP\n" +
                "AM=M-1\n" +  //SP-- m= top of stack\n" +
                "D=M\n" +
                "A=A-1\n" + // M= second of stack\n" +
                "D=M-D\n" +
                "@<%TRUE%>\n" +
                "D;JLT\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=0\n" +
                "@<%END%>\n" +
                "0;JMP\n" +
                "(<%TRUE%>)\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=-1\n" +
                "(<%END%>)\n" +
                "D=D\n"); // nope
        arithmeticMap.put("and", "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "M=D&M\n");
        arithmeticMap.put("or", "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "M=D|M\n");
        arithmeticMap.put("not", "@SP\n" +
                "A=M-1\n" +
                "M=!M\n");
    }

}

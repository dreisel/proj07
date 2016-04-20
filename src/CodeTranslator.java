import java.util.HashMap;
import java.util.Map;

/**
 * created by Daniel Reisel on 19/04/16.
 */
public class CodeTranslator {
    Map<String,String> arithmeticMap;
    String mathFormat =
            "@SP\n" +
            "AM=M-1\n" +
            "D=M\n" +
            "A=A-1\n";

    final String pushLocalFormat =
        "@LCL\n" +
        "D=M\n"+
        "@%d\n" +
        "A=D+A\n" +
        "D=M\n" +
        "@SP\n" +
        "A=M\n" +
        "M=D\n" +
        "@SP\n" +
        "M=M+1\n";
    final String pushConstantFormat =
        "@%d\n" +
        "D=A\n" +
        "@SP\n" +
        "A=M\n" +
        "M=D\n" +
        "@SP\n" +
        "M=M+1\n";
    String popLocalFormat =
        "@LCL\n" +
        "D=M\n" +
        "@index\n" +
        "D=D+A\n" + //d = pop destination (local var)
        "@R13\n" +
        "M=D\n" + //r13 = pop destination (temp)
        "@SP\n" +
        "AM=M-1\n" + //a=*sp--
        "D=M\n" + //d is the poped value
        "@R13\n" +
        "A=M\n" +
        "M=D\n" ;
    Map<String,String> pushMap;


    public CodeTranslator() {
        arithmeticMap = new HashMap<>();
        arithmeticMap.put("add","M=M+D\n");
        arithmeticMap.put("sub","M=M-D\n");
        arithmeticMap.put("neg","D=0\\n@SP\\nA=M-1\\nM=D-M\\n");
        arithmeticMap.put("eq","JNE");
        arithmeticMap.put("gt","JLE");
        arithmeticMap.put("lt","JGE");
        arithmeticMap.put("and","M=M&D\n");
        arithmeticMap.put("or","M=M|D\n");
        arithmeticMap.put("not","@SP\nA=M-1\nM=!M\n");

        pushMap = new HashMap<>();
        pushMap.put("push_constant",pushConstantFormat );
        pushMap.put("push_local",pushLocalFormat);
        pushMap.put("pop_local",popLocalFormat);

    }

    String translateArithmetic(String command){
        return null;
    }

    String translatePop(String segment, int index){
        return null;
    }

    String translatePush(String segment, int index){
        return null;
    }
    /*
    and:
        @SP\n
        AM=M-1\n
        D=M\n
        A=A-1\n
        M=D&M\n
    or:
        @SP\n
        AM=M-1\n
        D=M\n
        A=A-1\n
        M=D|M\n
    add:
        @SP\n
        AM=M-1\n
        D=M\n
        A=A-1\n
        M=D+M\n
    sub:
        @SP\n
        AM=M-1\n
        D=M\n
        A=A-1\n
        M=D-M\n
    neg:
        @sp\n
        A=M-1\n
        D=0\n
        M=D-M\n
    not:
        @sp\n
        A=M-1\n
        M=!M\n
    eq?:
        @SP\n
        AM=M-1\n //sp-- m= top of stack
        D=M\n
        A=A-1\n // M= second of stack
        D=D-M\n
        @TRUE\n
        D;JNE\n
        @SP\n
        A=M-1\n
        M=0\n
        @END\n
        0;JMP\n
        (TRUE)\n
        @SP\n
        A=M-1\n
        M=1\n
        (END)\n
        D=D\n // nop
    gt?
        @SP\n
        AM=M-1\n //sp-- m= top of stack
        D=M\n
        A=A-1\n M= second of stack
        D=D-M\n
        @TRUE\n
        D;JGT\n
        @SP\n
        A=M-1\n
        M=0\n
        @END\n
        0;JMP\n
        (TRUE)\n
        @SP\n
        A=M-1\n
        M=1\n
        (END)\n
        D=D\n // nop

    lt?:
        @SP\n
        AM=M-1\n //sp-- m= top of stack
        D=M\n
        A=A-1\n M= second of stack
        D=D-M\n
        @TRUE\n
        D;JLT\n
        @SP\n
        A=M-1\n
        M=0\n
        @END\n
        0;JMP\n
        (TRUE)\n
        @SP\n
        A=M-1\n
        M=1\n
        (END)\n
        D=D\n // nop
     */
}

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
   /* String compareFormat =
            "@SP\n" +
            "AM=M-1\n" +
            "D=M\n" +
            "A=A-1\n" +
            "D=M-D\n" +
            "@FALSE" + arthJumpFlag + "\n" +
            "D;" + type + "\n" +
            "@SP\n" +
            "A=M-1\n" +
            "M=-1\n" +
            "@CONTINUE" + arthJumpFlag + "\n" +
            "0;JMP\n" +
            "(FALSE" + arthJumpFlag + ")\n" +
            "@SP\n" +
            "A=M-1\n" +
            "M=0\n" +
            "(CONTINUE" + arthJumpFlag + ")\n";*/
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
        "@LCL" +
        "D=M" +
        "@index" +
        "D=D+A" + //d = pop destination (local var)
        "@R13" +
        "M=D" + //r13 = pop destination (temp)
        "@SP" +
        "AM=M-1" + //a=*sp--
        "D=M" + //d is the poped value
        "@R13" +
        "A=M" +
        "M=D" ;
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
        @SP
        AM=M-1
        D=M
        A=A-1
        M=D&M
    or:
        @SP
        AM=M-1
        D=M
        A=A-1
        M=D|M
    add:
        @SP
        AM=M-1
        D=M
        A=A-1
        M=D+M
    sub:
        @SP
        AM=M-1
        D=M
        A=A-1
        M=D-M
    neg:
        @sp
        A=M-1
        D=0
        M=D-M
    not:
        @sp
        A=M-1
        M=!M
    eq?:
        @SP
        AM=M-1 //sp-- m= top of stack
        D=M
        A=A-1 M= second of stack
        D=D-M
        @TRUE
        D;JNE
        @SP
        A=M-1
        M=0
        @END
        0;JMP
        (TRUE)
        @SP
        A=M-1
        M=1
        (END)
        D=D // nop
    gt?
        @SP
        AM=M-1 //sp-- m= top of stack
        D=M
        A=A-1 M= second of stack
        D=D-M
        @TRUE
        D;JGT
        @SP
        A=M-1
        M=0
        @END
        0;JMP
        (TRUE)
        @SP
        A=M-1
        M=1
        (END)
        D=D // nop

    lt?:
        @SP
        AM=M-1 //sp-- m= top of stack
        D=M
        A=A-1 M= second of stack
        D=D-M
        @TRUE
        D;JLT
        @SP
        A=M-1
        M=0
        @END
        0;JMP
        (TRUE)
        @SP
        A=M-1
        M=1
        (END)
        D=D // nop
     */
}

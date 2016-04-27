import java.io.File;
/**
 * created by Daniel Reisel && Ari Zellner on 19/04/16.
 *              304953243       201524089
 * nand2tetris project 07
 *
 */
public class VMtranslator {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Must provide input file");
            return;
        }
        String fileName = args[0].trim();
        if (!fileName.endsWith(".vm")) {
            System.out.println("All vm Files must be of vm type (.vm)");
            return;
        }
        File inputFile = new File(fileName);
        if (!inputFile.isFile()) {
            System.out.println("Input file invalid");
            return;
        }

        Parser parser = new Parser(inputFile,fileName.substring(0,fileName.length() - 3));
        parser.parse();
    }
}
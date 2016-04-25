class main {
    public static void main(String[] args) {
        if (args.length != 1)
            System.out.println("Must provide input file");

        Parser parser = new Parser(new File(args[0]));
    }
}

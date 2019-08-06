package dexter.frontend;

import dexter.FilePaths;

import java.io.File;

/**
 * Created by Maaz Ahmad on 6/26/19.
 */
public class CppFrontend {
    public static void run () {
        try {
            System.out.println("Running C++ Frontend (VC Generator) ...");
            ProcessBuilder pb = new ProcessBuilder("./vcgen", FilePaths.cppFile(), FilePaths.dslFile());
            pb.directory(new File(System.getProperty("user.dir") + "/clang/"));
            pb.inheritIO().start().waitFor();
        }
        catch ( Exception e ) {
            System.err.println("Clang front-end crashed unexpectedly.");
            e.printStackTrace();
        }
    }
}
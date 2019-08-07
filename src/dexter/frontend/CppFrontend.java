package dexter.frontend;

import dexter.Paths;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Maaz Ahmad on 6/26/19.
 */
public class CppFrontend {
  static FileInputStream fis;

  public static void run () {
    try {
      System.out.println("");
      ProcessBuilder pb = new ProcessBuilder("./run.sh", Paths.cppFile(), Paths.dslFile());
      pb.directory(new File(Paths.cppFrontendDir()));
      pb.inheritIO().start().waitFor();
    }
    catch ( Exception e ) {
      System.err.println("Clang front-end crashed unexpectedly.");
      e.printStackTrace();
    }
  }

  /*private static Pipeline loadDAG() throws IOException {
    System.out.println("Loading DAG...");
    fis = new FileInputStream(FilePaths.analysisFile());
    Pipeline p = new Pipeline(Json.createReader(fis).readObject());
    fis.close();

    return p;
  }*/
}
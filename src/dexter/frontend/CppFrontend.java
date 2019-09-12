package dexter.frontend;

import dexter.Files;
import dexter.Preferences;
import dexter.dag.Pipeline;

import javax.json.Json;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maaz Ahmad on 6/26/19.
 */
public class CppFrontend {
  public static void run () {
    try {
      ProcessBuilder pb = new ProcessBuilder("./run.sh", Files.cppFilePath(), Files.userDslFilePath(), Files.cppFrontendDirPath(), Integer.toString(Preferences.Global.verbosity));
      pb.directory(Files.cppFrontendDir());
      pb.inheritIO().start().waitFor();
    }
    catch ( Exception e ) {
      System.err.println("Clang front-end crashed unexpectedly.");
      e.printStackTrace();
    }
  }

  public static Set<CodeBlock> getIntentionalBlocks() throws IOException {
    Set<CodeBlock> codeBlocks = new HashSet<>();

    if (!Files.tempDir().exists())
      return codeBlocks;

    for (File file : Files.tempDir().listFiles()) {
      if (file.getName().matches(".*\\.json")) {
        String name = Files.FileName(file.getAbsolutePath());
        Pipeline dag = loadDAG(Files.analysisFilePath(name));

        CodeBlock cb = new CodeBlock()
            .setName(name)
            .setDag(dag);

        codeBlocks.add(cb);
      }
    }

    return codeBlocks;
  }

  private static Pipeline loadDAG(String path) throws IOException {
    FileInputStream fis = new FileInputStream(path);
    Pipeline p = new Pipeline(Json.createReader(fis).readObject());
    fis.close();
    return p;
  }
}
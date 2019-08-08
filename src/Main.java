import dexter.CLIParser;
import dexter.Compiler;
import dexter.Files;
import dexter.Preferences;

import java.io.IOException;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Main {
  public static void main (String[] args) throws IOException, InterruptedException {
    CLIParser parser = new CLIParser();

    try {
      parser.parseArguments(args);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Preferences.init(parser);
    Files.init(parser);

    Compiler.run();
  }
}
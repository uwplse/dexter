package dexter;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class CLIParser
{
  @Option(
      name="--log",
      aliases="-l",
      required=false,
      usage="Generate log files for cegis runs"
  )
  private boolean log = false;

  @Option(
      name="--verbose",
      aliases="-v",
      required=false,
      usage="Level of verbosity"
  )
  private int verbosity = 0;

  @Option(
      name="--use-output-logs",
      aliases="-ul",
      required=false,
      usage="Use saved output logs instead of running the synthesizer"
  )
  private boolean use_output_logs = false;

  @Option(
      name="--skip-frontend",
      aliases="-nf",
      required=false,
      usage="Do not run the Clang frontend (VC Generator)"
  )
  private boolean skip_frontend = false;

  @Option(
      name="--generate-intermediate-files",
      aliases={"-i", "--gen-intermediate"},
      required=false,
      usage="Generate intermediate files"
  )
  private boolean generate_intermediate_files = false;

  @Option(
      name="--output",
      aliases="-o",
      required=false,
      metaVar="<output-file-path>",
      usage="Write output to this file"
  )
  private File out;

  @Option(
      name="--dsl",
      required=false,
      metaVar="<output-file-path>",
      usage="Write output to this file"
  )
  private File dsl;

  @Option(
      name="--source",
      aliases={"-s", "--src"},
      required=true,
      metaVar="<input-file-path>",
      usage="Source C++ file to translate"
  )
  private File source;

  @Option(
      name="--cpp-frontend-dir",
      aliases={"-cf"},
      required=true,
      metaVar="<cpp-frontend-dir>",
      usage="Root directory of the Clang frontend"
  )
  private File frontend;

  public boolean log() { return log; }
  public int verbosity() { return verbosity; }
  public boolean use_output_logs() { return use_output_logs; }
  public boolean skip_frontend() { return skip_frontend; }
  public boolean generate_intermediate_files() { return generate_intermediate_files; }
  public File out() { return out; }
  public File dsl() { return dsl; }
  public File source() { return source; }
  public File frontend() { return frontend; }

  @SuppressWarnings( "deprecation" )
  public void parseArguments (String[] args) throws IOException {
    CmdLineParser parser = new CmdLineParser(this);

    try {
      parser.parseArgument(args);

      if ( !this.source.isFile() )
        throw new CmdLineException(parser,
            "--source file not found (" + this.source.getAbsolutePath() + ")");

      if ( !getFileExtension(this.source).matches("cpp|c") )
        throw new CmdLineException(parser,
            "--source must be a C++ file.");

      if ( this.dsl != null ) {
        if ( !this.dsl.isFile() )
          throw new CmdLineException(parser,
              "--dsl file not found (" + this.dsl.getAbsolutePath() + ")");
        if ( !getFileExtension(this.dsl).equals("ir") )
          throw new CmdLineException(parser,
              "--dsl must be a Dexter IR (.ir) file.");
      }

      if ( this.out == null )
        this.out = new File(this.source.getAbsoluteFile() + ".out");

      if ( !this.frontend.exists() )
        throw new CmdLineException(parser,
            "--cpp-frontend-dir does not exist (" + this.frontend.getAbsolutePath() + ")");

      if ( !this.frontend.isDirectory() )
        throw new CmdLineException(parser,
            "--cpp-frontend-dir is not a directory (" + this.frontend.getAbsolutePath() + ")");

    } catch ( CmdLineException e ) {
      parser.printUsage(System.err);
      System.err.println();
      System.err.println(e.getMessage());
      return;
    }

    if ( this.verbosity < 1 )
      return;

    System.out.println("--verbosity set to " + this.verbosity);

    if ( this.log )
      System.out.println("--log flag is set");

    if ( this.generate_intermediate_files )
      System.out.println("--generate-intermediate-files flag is set");

    if ( this.skip_frontend )
      System.out.println("--skip-frontend flag is set");

    if ( this.use_output_logs )
      System.out.println("--use-output-logs flag is set");

    if ( this.dsl != null )
      System.out.println("Using DSL file: " + this.dsl.getAbsolutePath());

    System.out.println("Input file-path: " + this.source.getAbsolutePath());
    System.out.println("Output file-path: " + this.out.getAbsolutePath() + "\n");
  }

  private static String getFileExtension (File file) {
    String fileName = file.getName();
    if( fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0 )
      return fileName.substring(fileName.lastIndexOf(".")+1);
    else return "";
  }
}
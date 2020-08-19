# Dexter: A Compiler for Lifting C++ Code to Halide
Dexter is a proof-of-concept compiler prototype designed to rejuvenate legacy image and array processing C++ code by re-writing it to the [Halide DSL](https://halide-lang.org/), thus enabling cross-platform optimization and portability. Given an operation implemented in C++, Dexter uses Verified Lifting, a combination of program synthesis and verification, to discover the algorithm hidden in the low-level code. Once discovered, the algorithm is then compiled to new hardware using Halide, avoiding manual reimplementation.

Visit our [project website](http://dexter.uwplse.org) for more information.

## Building Dexter

### Building the Java backend
The Dexter backend is implemented in Java and can be compiled using IntelliJ. All dependencies needed to build the backend can be found in the `lib` directory. You can also find the latest pre-built jars under `build/artifacts`.

### Building the Clang front-end
The Dexter front-end is implemented in C++ using Clang. To compile the front-end, you will need `Clang v7.1.0`, `llvm-7` and `llvm-7-tools`. Once all dependencies are installed, `cd` to `src/dexter/frontend/clang` directory and run the `make` command. The latest pre-built binary for the C++ front-end can be found in `src/dexter/frontend/clang/bin`.

## Running Dexter
Instructions on how to use Dexter can be found by running `java -jar /path/to/Dexter.jar` as shown below.

```
$ java -jar build/artifacts/dexter.jar
 --cpp-frontend-dir (-cf)               : Root directory of the Clang frontend
 <cpp-frontend-dir>                        
 --dsl <output-file-path>               : Write output to this file
 --generate-intermediate-files (-i,     : Generate intermediate files (default:
 --gen-intermediate)                      false)
 --log (-l)                             : Generate log files for cegis runs
                                          (default: false)
 --mode (-m) VAL                        : Mode to run the compiler in (Options:
                                          legacy and intentional). Legacy mode
                                          allows more language features such as
                                          pointers but does suffers from a
                                          lower success-rate. (default:
                                          intentional)
 --output (-o) <output-file-path>       : Write output to this file
 --skip-frontend (-nf)                  : Do not run the Clang frontend (VC
                                          Generator) (default: false)
 --source (-s, --src) <input-file-path> : Source C++ file to translate
 --use-output-logs (-ul)                : Use saved output logs instead of
                                          running the synthesizer (default:
                                          false)
 --verbose (-v) N                       : Level of verbosity (default: 0)
```
To compile the sample code provided in `test/basic/blend.cpp`, you can use the following command in the root directory of this repository:
```
$ java -jar build/artifacts/dexter.jar --cpp-frontend-dir src/dexter/frontend/clang/ --src test/basic/blend.cpp -v 1
```

## Contact
Casper is written by [Maaz Bin Safeer Ahmad](http://homes.cs.washington.edu/~maazsaf/) at the [University of Washington](http://www.washington.edu/). Please get in touch if you have any questions or feedback!

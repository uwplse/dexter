package dexter.frontend;

import dexter.dag.Pipeline;

/**
 * Created by Maaz Ahmad on 8/6/19.
 */
public class CodeBlock {
  String name;
  Pipeline dag;

  public String name() {
    return name;
  }

  public Pipeline asDAG() {
    return dag;
  }

  public CodeBlock setName(String name) {
    this.name = name;
    return this;
  }

  public CodeBlock setDag(Pipeline dag) {
    this.dag = dag;
    return this;
  }
}
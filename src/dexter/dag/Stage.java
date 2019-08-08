package dexter.dag;

import dexter.frontend.CodeAnalysis;
import dexter.ir.Expr;
import dexter.ir.bool.VarExpr;

import java.util.Set;

public class Stage {
  int _id;
  boolean _forks;
  boolean _containsLoop;
  boolean _isEmpty;

  Expr _cond;
  int _consStageId;
  int _altrStageId;
  int _nextStageId;

  CodeAnalysis _analysis;

  public Stage(int id, boolean f, boolean cl, boolean ie, Expr c, int cid, int aid, int nid, CodeAnalysis a)
  {
    this._id = id;
    this._forks = f;
    this._containsLoop = cl;
    this._isEmpty = ie;
    this._cond = c;
    this._consStageId = cid;
    this._altrStageId = aid;
    this._nextStageId = nid;
    this._analysis = a;
  }

  public int id() { return _id; }

  public Set<VarExpr> getOutputArrVars() {
    return _analysis.getOutputArrVars();
  }

  public Set<VarExpr> getOutputBuffers() {
    return _analysis.getOutputBuffers();
  }

  public CodeAnalysis analysis() {
    return _analysis;
  }

  public boolean isEmpty() {
    return _isEmpty;
  }

  public boolean forks() {
    return _forks;
  }

  public boolean containsLoop() {
    return _containsLoop;
  }

  public int nextStageId() {
    return _nextStageId;
  }

  public int consStageId() {
    return _consStageId;
  }

  public int altrStageId() {
    return _altrStageId;
  }

  public Expr cond() { return _cond; }
}

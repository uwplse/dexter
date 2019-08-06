/*
 * VCGenerator.h
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#ifndef IR_SEMANTICS_VCGENERATOR_H_
#define IR_SEMANTICS_VCGENERATOR_H_

#include "ir/bool/VarExpr.h"
#include "ir/bool/Program.h"

namespace Dexter  {

class VCGenerator
{
public:
  VCGenerator ();
  VCGenerator (VCGenerator * other);

  Dexter::Expr * vc ();
  void vc (Dexter::Expr * vc);

  void pc (std::vector<VarExpr *> &vars, std::vector<VarExpr *> &outVars);
  Dexter::Expr * invariant (std::vector<VarExpr *> &vars, std::vector<VarExpr *> &outVars);
  Dexter::Program * createProgram (std::vector<VarExpr *> vars, std::vector<VarExpr *> &outVars);

  void mergeDecls (VCGenerator * other);
  void mergePrior (VCGenerator * other);
  void loop (Dexter::Expr * cond, Dexter::Expr * invariant, Dexter::Expr * continuation, std::vector<VarExpr *> &outVars);
  void ploop (Dexter::Expr * cond, Dexter::Expr * invariant, Dexter::Expr * continuation, std::vector<VarExpr *> &outVars, Dexter::Expr * outerInv);
  void conditional (Dexter::Expr * cond, VCGenerator * consVC, VCGenerator * altVC);
  void replace (Dexter::Expr * from, Dexter::Expr * to);
  void queueReplace (Dexter::Expr * from, Dexter::Expr * to);
  void runQueue ();

  void addAssumption (Dexter::Expr * e);
  void registerBounds (Dexter::VarExpr * v, Dexter::Expr * w, Dexter::Expr * h);

  jobject obj () const { return this->mObj; }
protected:
  jobject mObj;
};

}

#endif /* IR_SEMANTICS_VCGENERATOR_H_ */
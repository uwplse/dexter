/*
 * GenerateOutputs.h
 *
 *  Created on: Oct 13, 2017
 *      Author: maazsaf
 */

#ifndef GENERATEOUTPUTS_H_
#define GENERATEOUTPUTS_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "ir/bool/FuncDecl.h"
#include "ClangToIRParser.h"

#include <sys/stat.h>
#include <fstream>
#include <string>

using namespace clang;

namespace Dexter {

class GenerateOutputs : public RecursiveASTVisitor<GenerateOutputs>
{
public:
  GenerateOutputs(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl* f);
  bool VisitStmt(Stmt* s);

protected:
  std::string GetUDFFile (std::set<Dexter::FuncDecl *> udfs);
	std::string GetJsonAnalysis (Dexter::Pipeline * p);
	std::string VarsJson (std::map<std::string, std::set<ValueDecl *>> vars, std::string name, std::set<ValueDecl *> outVars, std::set<ValueDecl *> lVars);
	std::string ConstsJson (std::set<int> int_consts, std::set<double> float_consts);
	std::string BoundsJson (Stmt* loops, std::set<ValueDecl *> outVars);
	std::string toString (Dexter::Type t);
	void GetLoopStmts (Stmt * s, std::vector<Stmt*> & loops);

  Rewriter &rewriter;

  bool debug = false;
};

}

#endif /* GENERATEOUTPUTS_H_ */
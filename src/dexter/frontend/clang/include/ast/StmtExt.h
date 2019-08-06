/*
 * StmtExt.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef STMTEXT_H_
#define STMTEXT_H_

#include "NodeExt.h"

namespace Dexter {

class StmtExt : public NodeExt
{
public:
	StmtExt() { _ub = NULL; _lb = NULL; }

  // For storing loop bounds
	void upperBound(clang::Expr* ub) { _ub = ub; }
  void lowerBound(clang::Expr* lb) { _lb = lb; }
  clang::Expr* upperBound() { return _ub; }
  clang::Expr* lowerBound() { return _lb; }

  // For storing live var information
  void liveVars(std::set<ValueDecl *> lv) { _lv = lv; }
  std::set<ValueDecl *> liveVars() { return _lv; }

protected:
  clang::Expr* _ub;
  clang::Expr* _lb;
  std::set<ValueDecl *> _lv;

public:
  static StmtExt* Get (clang::Stmt* s);

protected:
  static std::map<clang::Stmt*, StmtExt*> exts;
};

}

#endif /* STMTEXT_H_ */
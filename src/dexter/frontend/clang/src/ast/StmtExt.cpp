/*
 * StmtExt.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ast/StmtExt.h"

std::map<Stmt*, Dexter::StmtExt*> Dexter::StmtExt::exts;

Dexter::StmtExt* Dexter::StmtExt::Get (clang::Stmt * s)
{
  if (exts.count(s))
    return exts[s];
  else
    {
      exts[s] = new StmtExt();
      return exts[s];
    }
}
/*
 * DeclExt.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ast/DeclExt.h"

std::map<Decl*, Dexter::DeclExt*> Dexter::DeclExt::exts;

Dexter::DeclExt* Dexter::DeclExt::Get (clang::Decl* d)
{
  if (exts.count(d))
    return exts[d];
  else
    {
      exts[d] = new DeclExt();
      return exts[d];
    }
}
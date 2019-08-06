/*
 * GenerateOutputs.cpp
 *
 *  Created on: Oct 13, 2017
 *      Author: maazsaf
 */

#include "visit/GenerateOutputs.h"

bool Dexter::GenerateOutputs::VisitFunctionDecl (FunctionDecl* f)
{
  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);

  if (!ext->lift())
    return true;

  // Generate directory for output files
  SourceManager &SM = rewriter.getSourceMgr();
  std::string filename = SM.getFileEntryForID(SM.getMainFileID())->getName().str();
  std::string dirName = filename.substr(0, filename.find_last_of(".")) + "_dx_tmp/";
  std::ofstream myfile;

  struct stat info;
  if(stat(dirName.c_str(), &info ) != 0) {
    if (mkdir(dirName.c_str(), 0777) != 0)
      Util::error("Unable to create directory: " + dirName);
  }
  else if( info.st_mode & S_IFDIR )
    {} // File exists and is a directory
  else
    Util::error("Unable to create directory: " + dirName);

  Pipeline* pipeline = Dexter::DeclExt::Get(f)->DAG();
  std::set<Stage*> stages = pipeline->getAllStages();

  std::set<Stage*>::iterator stage;
  for (stage = stages.begin(); stage != stages.end(); ++stage)
  {
    llvm::outs() << "Writing VCs to file for intentional code block `"
                 << f->getNameAsString()
                 << "` (Stage " << (*stage)->getId() << ")\n";

    if ((*stage)->isEmpty())
      continue;

    std::string stage_filename = dirName + f->getNameAsString() + "_stage_" +
      std::to_string((*stage)->getId()) +".prl";

    Dexter::Program * p = (*stage)->getVCs();

    myfile.open (stage_filename);
    myfile << Util::toString(p);
    myfile.flush();
    myfile.close();

    if ((*stage)->getUDFs().size() == 0)
      continue;

    stage_filename = dirName + f->getNameAsString() + "_stage_" +
          std::to_string((*stage)->getId()) +"_udfs.prl";

    myfile.open (filename);
    std::string udfs = GetUDFFile((*stage)->getUDFs());
    myfile << udfs;
    myfile.flush();
    myfile.close();
  }

  // Generate Analysis file
  llvm::outs() << "Writing static analysis results to file for intentional code block `"
               << f->getNameAsString()
               << "`\n\n";

  filename = dirName + f->getNameAsString() + ".json";

  myfile.open (filename);
  std::string analysis = GetJsonAnalysis(pipeline);
  myfile << analysis;
  myfile.flush();
  myfile.close();

  return true;
}

bool Dexter::GenerateOutputs::VisitStmt (Stmt* s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

std::string Dexter::GenerateOutputs::GetUDFFile (std::set<FuncDecl *> udfs)
{
  std::string code = "";

  std::set<FuncDecl*>::iterator it;
  for (it = udfs.begin(); it != udfs.end(); ++it)
    code += Util::toString(*it);

  return code;
}

std::string Dexter::GenerateOutputs::GetJsonAnalysis (Pipeline * pipeline)
{
  std::string json = "{\"Stages\":[";

  std::set<Stage*> stages = pipeline->getAllStages();

  std::set<Stage*>::iterator stageit;
  for (stageit = stages.begin(); stageit != stages.end(); ++stageit)
  {
    Stage* stage = *stageit;

    std::string stage_json = "{";
    stage_json += "\"id\": " + std::to_string(stage->getId()) + ", ";
    stage_json += "\"forks\": "; stage_json += (stage->forks() ? "true" : "false"); stage_json += ", ";
    stage_json += "\"containsLoop\": "; stage_json += (stage->containsLoop() ? "true" : "false"); stage_json += ", ";
    stage_json += "\"isEmpty\": "; stage_json += (stage->isEmpty() ? "true" : "false"); stage_json += ", ";

    if (stage->forks())
    {
      std::map<NamedDecl *, Dexter::Expr *> vars;
      stage_json += "\"cond\": \"" + Util::toString(Dexter::ClangToIRParser::parse(stage->getCond(), vars)) + "\", ";
      stage_json += "\"cons\": " + std::to_string((stage->getCons() == NULL ? -1 : stage->getCons()->getId())) + ", ";
      stage_json += "\"altr\": " + std::to_string((stage->getAltr() == NULL ? -1 : stage->getAltr()->getId())) + ", ";
    }

    stage_json += "\"next\": " + std::to_string((stage->getNextStage() == NULL ? -1 : stage->getNextStage()->getId())) + ", ";


    // Get the loop statement (max 1 loop nest for each stage)
    WhileStmt* loopNest = NULL;
    if (stage->containsLoop())
    {
      std::vector<Stmt*> stmts = stage->getStatements();
      std::vector<Stmt*>::iterator stmt;
      for (stmt = stmts.begin(); stmt != stmts.end(); stmt++) {
        if (isa<WhileStmt>(*stmt)) {
          loopNest = cast<WhileStmt>(*stmt);
          break;
        }
      }
      assert (loopNest != NULL);
    }

    std::set<ValueDecl *> lVars = stage->getLocalVars();
    std::set<ValueDecl *> idxVars = stage->getIndexVars();
    std::set<ValueDecl *> inVars = stage->getInputVars();
    std::set<ValueDecl *> outVars = stage->getOutputVars();
    std::set<ValueDecl *> outArrVars = stage->getOutputArrayVars();

    std::map<std::string, std::set<ValueDecl *>> localVarsMap;
    std::map<std::string, std::set<ValueDecl *>> idxVarsMap;
    std::map<std::string, std::set<ValueDecl *>> inVarsMap;
    std::map<std::string, std::set<ValueDecl *>> outVarsMap;
    std::map<std::string, std::set<ValueDecl *>> outArrVarsMap;

    std::set<ValueDecl  *>::iterator it;

    // Generate local vars map
    for (it = lVars.begin(); it != lVars.end(); ++it)
    {
      QualType t = (*it)->getType();
      std::string var_type = toString(Dexter::ClangToIRParser::toIRType(t));

      if (localVarsMap.find(var_type) == localVarsMap.end())
        localVarsMap[var_type] = std::set<ValueDecl *>();

      localVarsMap[var_type].insert(*it);
    }

    // Generate index vars map
    for (it = idxVars.begin(); it != idxVars.end(); ++it)
    {
      QualType t = (*it)->getType();
      std::string var_type = toString(Dexter::ClangToIRParser::toIRType(t));

      if (idxVarsMap.find(var_type) == idxVarsMap.end())
        idxVarsMap[var_type] = std::set<ValueDecl *>();

      idxVarsMap[var_type].insert(*it);
    }

    // Generate input vars map
    for (it = inVars.begin(); it != inVars.end(); ++it)
    {
      QualType t = (*it)->getType();
      std::string var_type = toString(Dexter::ClangToIRParser::toIRType(t));

      if (inVarsMap.find(var_type) == inVarsMap.end())
        inVarsMap[var_type] = std::set<ValueDecl *>();

      inVarsMap[var_type].insert(*it);
    }

    // Generate output vars map
    for (it = outVars.begin(); it != outVars.end(); ++it)
    {
      QualType t = (*it)->getType();
      std::string var_type = toString(Dexter::ClangToIRParser::toIRType(t));

      if (outVarsMap.find(var_type) == outVarsMap.end())
        outVarsMap[var_type] = std::set<ValueDecl *>();

      outVarsMap[var_type].insert(*it);
    }

    // Generate output vars map
    for (it = outArrVars.begin(); it != outArrVars.end(); ++it)
    {
      if (lVars.find(*it) != lVars.end())
        continue;

      QualType t = (*it)->getType();
      std::string var_type = toString(Dexter::ClangToIRParser::toIRType(t));

      if (outArrVarsMap.find(var_type) == outArrVarsMap.end())
        outArrVarsMap[var_type] = std::set<ValueDecl *>();

      outArrVarsMap[var_type].insert(*it);
    }

    if (stage->isEmpty())
    {
      json += stage_json.substr(0, stage_json.length()-2) + "}, ";
      continue;
    }

    // Code is added just for viz purposes
    std::string code = "";
    std::vector<Stmt*> stmts = stage->getStatements();
    std::vector<Stmt*>::iterator stmt;
    for (stmt = stmts.begin(); stmt != stmts.end(); ++stmt) {
      if (isa<clang::Expr>(*stmt))
        code += Dexter::Util::print(*stmt) + ";\n";
      else
        code += Dexter::Util::print(*stmt) + "\n";
    }

    // Quote string characters
    size_t start_pos = 0;
    while((start_pos = code.find("\"", start_pos)) != std::string::npos) {
      code.replace(start_pos, 1, "\\\"");
      start_pos += 2;
    }
    start_pos = 0;
    while((start_pos = code.find("\n", start_pos)) != std::string::npos) {
      code.replace(start_pos, 1, "\\n");
      start_pos += 2;
    }
    stage_json += "\"code\": \"" + code + "\", ";

    stage_json += "\"analysis\" : {\"Variables\":{" +
                            VarsJson(inVarsMap, "Input", outVars, lVars) + (outVarsMap.empty() ? "" : ", ") +
                            VarsJson(outVarsMap, "Output", outVars, lVars) + (outArrVarsMap.empty() ? "" : ", ") +
                            VarsJson(outArrVarsMap, "OutputArr", outVars, lVars) + (idxVarsMap.empty() ? "" : ", ") +
                            VarsJson(idxVarsMap, "Index", outVars, lVars) + (localVarsMap.empty() ? "" : ", ") +
                            VarsJson(localVarsMap, "Local", outVars, lVars) +
                        "}, \"Constants\":{" +
                            ConstsJson(stage->getIntConsts(), stage->getFloatConsts()) +
                        "}, \"Bounds\":[" +
                            BoundsJson(loopNest, outVars) +
                        "]}";

    json += stage_json + "}, ";
  }

  return json.substr(0, json.length()-2) + "]}";
}

void Dexter::GenerateOutputs::GetLoopStmts (Stmt * s, std::vector<Stmt*> & loops)
{
  if (s == NULL) return;

  if (isa<WhileStmt>(s))
    loops.push_back(s);
  else if (isa<ForStmt>(s))
    loops.push_back(s);

  Stmt::child_iterator it;
  for (it = s->child_begin(); it != s->child_end(); ++it)
    GetLoopStmts(*it, loops);
}

std::string Dexter::GenerateOutputs::VarsJson (std::map<std::string, std::set<ValueDecl *>> vars, std::string name, std::set<ValueDecl *> outVars, std::set<ValueDecl *> lVars)
{
  std::string json = "";
  std::map<std::string, std::set<ValueDecl *>>::iterator it;
  for (it = vars.begin(); it != vars.end(); ++it)
  {
    json += "\"" + name + "<" + it->first + ">\":[";
    std::set<ValueDecl *>::iterator it2;
    for (it2 = it->second.begin(); it2 != it->second.end(); ++it2)
    {
      std::string var_type = (*it2)->getType().getCanonicalType().getAsString();
      std::string var_name = (*it2)->getNameAsString();

      if (name == "Input" && outVars.find(*it2) != outVars.end() && lVars.find(*it2) == lVars.end())
          var_name = var_name + "_init";

      json += "[\"" + var_name + "\", \"" + var_type + "\"], ";
    }
    json = json.substr(0, json.length()-2) + "], ";
  }
  return json.substr(0, json.length()-2);
}

std::string Dexter::GenerateOutputs::ConstsJson (std::set<int> int_consts, std::set<double> float_consts)
{
  std::string json = "\"int\": [";

  std::set<int>::iterator it;
  for (it = int_consts.begin(); it != int_consts.end(); ++it)
  {
    json += "\"" + std::to_string(*it) + "\", ";
  }

  json = (int_consts.size() > 0 ? json.substr(0, json.length()-2) : json) + "], \"float\": [";

  std::set<double>::iterator it2;
  for (it2 = float_consts.begin(); it2 != float_consts.end(); ++it2)
  {
    json += "\"" + std::to_string(*it2) + "\", ";
  }

  return (float_consts.size() > 0 ? json.substr(0, json.length()-2) : json) + "]";
}

std::string Dexter::GenerateOutputs::BoundsJson (Stmt* loopNest, std::set<ValueDecl *> outVars)
{
  std::vector<Stmt*> loops;
  GetLoopStmts(loopNest, loops);

  std::string json = "";

  std::map<NamedDecl *, Dexter::Expr *> vars;
  std::vector<Stmt*>::iterator it;
  for (it = loops.begin(); it != loops.end(); ++it)
  {
    Dexter::StmtExt* ext = Dexter::StmtExt::Get(*it);
    clang::Expr * lb = ext->lowerBound();
    clang::Expr * ub = ext->upperBound();

    ValueDecl* lb_decl = (isa<DeclRefExpr>(lb) ? cast<DeclRefExpr>(lb)->getDecl() : NULL);
    ValueDecl* ub_decl = (isa<DeclRefExpr>(ub) ? cast<DeclRefExpr>(ub)->getDecl() : NULL);

    std::string lb_str = Util::toString(Dexter::ClangToIRParser::parse(lb, vars));
    std::string ub_str = Util::toString(Dexter::ClangToIRParser::parse(ub, vars));

    json += "{";
    json += "\"lb\": \"" + (lb_decl && outVars.find(lb_decl) != outVars.end() ? lb_str+"_init" : lb_str ) + "\"";
    json += ", ";
    json += "\"ub\": \"" + (ub_decl && outVars.find(ub_decl) != outVars.end() ? ub_str+"_init" : ub_str ) + "\"";
    json += "}, ";
  }

  return json.substr(0, json.length()-2);
}

std::string Dexter::GenerateOutputs::toString(Dexter::Type t) {
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/type/Type");
  jmethodID m = e->GetMethodID(c, "toString", "()Ljava/lang/String;");
  jstring tn = (jstring) (e->CallObjectMethod(t.obj(), m));
  return e->GetStringUTFChars(tn, 0);
}
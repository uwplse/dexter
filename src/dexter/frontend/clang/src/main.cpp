#include "clang/Tooling/CommonOptionsParser.h"
#include "clang/Tooling/Tooling.h"

#include "ir/type/TypesFactory.h"
#include "ir/bool/BinaryExpr.h"
#include "ir/bool/UnaryExpr.h"
#include "ir/bool/BoolLitExpr.h"

#include "Util.h"
#include "Scheduler.h"

/**
 * Created by Maaz Ahmad on 6/26/19.
 */

using namespace clang;
using namespace clang::driver;
using namespace clang::tooling;

static llvm::cl::OptionCategory ToolingSampleCategory("Dexter Clang Frontend");

static llvm::cl::extrahelp CommonHelp(CommonOptionsParser::HelpMessage);

// A help message for this specific tool can be added afterwards.
static llvm::cl::extrahelp DexterJarHelp("\n<dxjar> path to Dexter jar file\n");
static llvm::cl::extrahelp DslHelp("\n<dsl> path to DSL implementation in Dexter IR\n");

static llvm::cl::opt<std::string> JarPath("dxjar", llvm::cl::desc("<path to Dexter jar file>"), llvm::cl::Required);
static llvm::cl::opt<std::string> DslPath("dsl", llvm::cl::desc("<path to target dsl in Dexter IR>"), llvm::cl::Optional);

void initTypes()
{
  Dexter::TypesFactory::init();
  Dexter::BinaryExpr::init();
  Dexter::UnaryExpr::init();
  Dexter::BoolLitExpr::init();
}

void loadDsl(std::string dslFilePath)
{
  JNIEnv * e = Dexter::Util::env;
  jclass ts = e->FindClass("dexter/ir/type/TypesFactory");
  jmethodID m = e->GetStaticMethodID(ts, "loadDSL", "(Ljava/lang/String;)V");
  jstring fp = e->NewStringUTF(dslFilePath.c_str());
  e->CallStaticObjectMethod(ts, m, fp);
}

int main (int argc, const char **argv)
{
  CommonOptionsParser op(argc, argv, ToolingSampleCategory);
  ClangTool Tool(op.getCompilations(), op.getSourcePathList());

  // Init JVM
  Dexter::Util::initJVM(JarPath.getValue());
  // Init Builtin Types
  initTypes();
  // Load types from user-DSL to TypesFactory
  loadDsl(DslPath.getValue());

  int r = Tool.run(newFrontendActionFactory<Dexter::SchedulerAction>().get());
  Dexter::Util::shutdownJVM();

  return r;
}
/*
 * Util.cpp
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#include "Util.h"

using namespace clang;

JNIEnv * Dexter::Util::env;
JavaVM * Dexter::Util::jvm;

/*
 * Do this to enable Java8 to support JNI on OSX:
 * https://oliverdowling.com.au/2014/03/28/java-se-8-on-mac-os-x/
 *
 * Edit /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Info.plist and change:
 * <key>JVMCapabilities</key>
   <array>
    <string>CommandLine</string>
    <string>JNI</string>
    <string>BundledApp</string>
   </array>
 */
void Dexter::Util::initJVM (const std::string &dxJarPath)
{
  JavaVMOption options[1]; // A list of options to build a JVM from C++
  JavaVMInitArgs vm_args; // Arguments for the JVM (see below)

  // set options[0] to be "-Djava.class.path=.:" + <dxjar path>
  std::string s = "-Djava.class.path=.:" + dxJarPath;
  std::vector<char> cstr(s.c_str(), s.c_str() + s.size() + 1);
  options[0].optionString = cstr.data();

  llvm::outs() << "Dexter jar path: " << options[0].optionString << "\n";

  //memset(&vm_args, 0, sizeof(vm_args));
  vm_args.version = JNI_VERSION_1_8;
  vm_args.nOptions = 1;
  vm_args.options = options;

  long status = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
  if (status == JNI_ERR)
  {
    llvm::errs() << "Unable to create JVM\n";
    exit(1);
  }
  else
    llvm::outs() << "created jvm: " << jvm << " e " << env << "\n\n";
}

void Dexter::Util::ASSERT (bool cond, std::string msg)
{
  if (!cond)
  {
    llvm::errs() << msg << "\n";
    exit(1);
  }
}

void Dexter::Util::error (std::string msg)
{
  llvm::errs() << msg << "\n";
  exit(1);
}

void Dexter::Util::error (Stmt * s, std::string msg)
{
  llvm::errs() << msg;
  s->dump();
  exit(1);
}

void Dexter::Util::error (const clang::Type * t, std::string msg)
{
  llvm::errs() << msg;
  t->dump();
  exit(1);
}

void Dexter::Util::error (Decl * s, std::string msg)
{
  llvm::errs() << msg;
  s->dump();
  exit(1);
}


void Dexter::Util::error (QualType t, std::string msg)
{
  llvm::errs() << msg;
  t->dump();
  exit(1);
}

void Dexter::Util::print (Dexter::Expr * e)
{
  jclass c = env->FindClass("dexter/ir/Expr");
  jmethodID m = env->GetMethodID(c, "print", "()V");
  env->CallVoidMethod(e->obj(), m);
  return;
}

std::string Dexter::Util::toString (Dexter::Expr * e)
{
  jclass c = env->FindClass("dexter/ir/Expr");
  jmethodID m = env->GetMethodID(c, "toString", "()Ljava/lang/String;");
  jstring str = (jstring)env->CallObjectMethod(e->obj(), m);
  return env->GetStringUTFChars(str, JNI_FALSE);
}

const std::string Dexter::Util::print (Stmt * s)
{
  std::string str;
  llvm::raw_string_ostream ss(str);
  s->printPretty(ss, NULL, PrintingPolicy(LangOptions()));
  ss.flush();
  return str;
}

const std::string Dexter::Util::print (FunctionDecl * f)
{
  std::string str;
  llvm::raw_string_ostream ss(str);
  f->print(ss);
  ss.flush();

  return str;
}

const std::string Dexter::Util::print (jobject p)
{
  JNIEnv * e = env;
  jclass c = e->FindClass("dexter/ir/Util");
  jmethodID m = e->GetStaticMethodID(c, "print", "(Ldexter/ir/Expr;)Ljava/lang/String;");
  jstring jStr = (jstring) e->CallStaticObjectMethod(c, m, p);

  const char *cstr = env->GetStringUTFChars(jStr, NULL);
  std::string str = std::string(cstr);
  env->ReleaseStringUTFChars(jStr, cstr);
  return str;
}

const std::string Dexter::Util::printr (FunctionDecl * f, Rewriter rewriter)
{
  clang::SourceManager& mgr = f->getASTContext().getSourceManager();
  clang::SourceRange range = Util::getSourceRange(f, rewriter);
  StringRef s = clang::Lexer::getSourceText(clang::CharSourceRange::getTokenRange(range), mgr, f->getASTContext().getLangOpts());
  return s.str();
}

const std::string Dexter::Util::printr (CXXRecordDecl * f, Rewriter rewriter)
{
  clang::SourceManager& mgr = f->getASTContext().getSourceManager();
  clang::SourceRange range = Util::getSourceRange(f, rewriter);
  StringRef s = clang::Lexer::getSourceText(clang::CharSourceRange::getTokenRange(range), mgr, f->getASTContext().getLangOpts());
  return s.str();
}

// Built-in source range function does not always return correct locations when macros are involved
const SourceRange Dexter::Util::getSourceRange(Stmt * s, Rewriter rewriter)
{
  SourceLocation st = s->getLocStart();
  SourceLocation end = s->getLocEnd();

  if (st.isMacroID()){
    CharSourceRange expansionRange =
          rewriter.getSourceMgr().getImmediateExpansionRange(st);
    st = expansionRange.getBegin();
  }

  if (end.isMacroID()){
    CharSourceRange expansionRange =
          rewriter.getSourceMgr().getImmediateExpansionRange(end);
    end = expansionRange.getEnd();
  }

  SourceRange lr(st,end);
  return lr;
}

// Built-in source range function does not always return correct locations when macros are involved
const SourceRange Dexter::Util::getSourceRange(Decl * d, Rewriter rewriter)
{
  SourceLocation st = d->getLocStart();
  SourceLocation end = d->getLocEnd();

  if (st.isMacroID()){
    CharSourceRange expansionRange =
          rewriter.getSourceMgr().getImmediateExpansionRange(st);
    st = expansionRange.getBegin();
  }

  if (end.isMacroID()){
    CharSourceRange expansionRange =
          rewriter.getSourceMgr().getImmediateExpansionRange(end);
    end = expansionRange.getEnd();
  }

  SourceRange lr(st,end);
  return lr;
}

Dexter::Expr * Dexter::Util::substitute(Dexter::Expr * base, Dexter::Expr * from, Dexter::Expr * to)
{
  jclass c = env->FindClass("dexter/ir/Util");
  jmethodID m = env->GetStaticMethodID(c, "substitute", "(Ldexter/ir/Expr;Ldexter/ir/Expr;Ldexter/ir/Expr;)Ldexter/ir/Expr;");
  jobject obj = env->CallStaticObjectMethod(c, m, base->obj(), from->obj(), to->obj());
  return new Dexter::Expr(obj);
}

void Dexter::Util::shutdownJVM ()
{
  jvm->DestroyJavaVM();
}
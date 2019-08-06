LLVM_BUILD_PATH = /usr/lib/llvm-7
LLVM_BIN_PATH = $(LLVM_BUILD_PATH)/bin

LLVM_LIBS=core mc MCParser BitReader ProfileData Option
LLVM_INCLUDE_FLAGS= $(shell $(LLVM_BIN_PATH)/llvm-config --cxxflags)
LLVM_BUILD_FLAGS= $(shell $(LLVM_BIN_PATH)/llvm-config --cxxflags --ldflags --libs $(LLVM_LIBS) --system-libs)

CLANG_LIBS = \
	-lclangFrontendTool -lclangFrontend -lclangDriver \
	-lclangSerialization -lclangCodeGen -lclangParse \
	-lclangSema -lclangStaticAnalyzerFrontend \
	-lclangStaticAnalyzerCheckers -lclangStaticAnalyzerCore \
	-lclangAnalysis -lclangARCMigrate -lclangRewrite \
	-lclangEdit -lclangAST -lclangLex -lclangBasic -lclangTooling
CLANG_BUILD_FLAGS = -L$(LLVM_BUILD_PATH)/lib $(CLANG_LIBS)

JVM_HOME = /usr/lib/jvm/java-8-openjdk-amd64
JVM_INCLUDE_FLAGS = -I$(JVM_HOME)/include -I$(JVM_HOME)/include/linux
JVM_BUILD_FLAGS = -L$(JVM_HOME)/jre/lib/amd64/server -ljvm

CXX = $(LLVM_BIN_PATH)/clang++
CXXFLAGS = -fno-rtti -std=c++11 -O0 -g $(LLVM_INCLUDE_FLAGS) $(JVM_INCLUDE_FLAGS) -Iinclude
CFLAGS = $(CXXFLAGS)

SRCS = $(shell find . -name "*.cpp")
OBJS = $(subst src,obj,$(subst .cpp,.o,$(SRCS)))
DWOS = $(subst .cpp,.dwo,$(SRCS))

all: main

main: $(OBJS)
	@mkdir -p bin
	$(CXX) $(OBJS) $(CFLAGS) -o bin/clang_frontend $(LLVM_BUILD_FLAGS) $(JVM_BUILD_FLAGS) $(CLANG_BUILD_FLAGS)

obj/%.o: src/%.cpp
	@mkdir -p $(@D)
	$(CXX) $(CFLAGS) -c -o $@ $<

clean:
	rm -rf bin $(OBJS) $(DWOS) *.log
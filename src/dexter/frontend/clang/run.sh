if [[ -z "${HALIDE_PATH}" ]]; then
  echo "Please set HALIDE_PATH to your Halide installation directory"
  exit 1
fi

DEXTER_JAR_PATH=$1
INPUT_FILE_PATH=$2
CORE_DSL_PATH=DSL.ir
USER_DSL_PATH=$3
DEXTER_INCLUDE_PATH=$4/include
VERBOSITY=$5
MODE=$6

export LD_LIBRARY_PATH=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server

if [ -z "$USER_DSL_PATH" ]; then 
	./bin/clang_frontend \
		-extra-arg-before="-std=c++11" \
		-extra-arg-before="-I$HALIDE_PATH/include"  \
		-extra-arg-before="-I$DEXTER_INCLUDE_PATH"  \
		-dxjar $DEXTER_JAR_PATH \
		-dsl-core $CORE_DSL_PATH \
		-verbosity $VERBOSITY \
		-mode $MODE \
		$INPUT_FILE_PATH
else 
	./bin/clang_frontend \
		-extra-arg-before="-std=c++11" \
		-extra-arg-before="-I$HALIDE_PATH/include"  \
		-extra-arg-before="-I$DEXTER_INCLUDE_PATH"  \
		-dxjar $DEXTER_JAR_PATH \
		-dsl-core $CORE_DSL_PATH \
		-dsl-user $USER_DSL_PATH \
		-verbosity $VERBOSITY \
		-mode $MODE \
		$INPUT_FILE_PATH
fi

exit 0

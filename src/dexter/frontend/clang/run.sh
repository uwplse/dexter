HALIDE_PATH=/home/maaz/Software/Halide
DEXTER_JAR_PATH=../../../../out/artifacts/dexter_jar/dexter.jar
DEXTER_INCLUDE_PATH=$3/include
INPUT_FILE_PATH=$1
CORE_DSL_PATH=DSL.ir
USER_DSL_PATH=$2

export LD_LIBRARY_PATH=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server

if [ -z "$USER_DSL_PATH" ]; then 
	./bin/clang_frontend \
		-extra-arg-before="-std=c++11" \
		-extra-arg-before="-I$HALIDE_PATH/include"  \
		-extra-arg-before="-I$DEXTER_INCLUDE_PATH"  \
		-dxjar $DEXTER_JAR_PATH \
		-dsl-core $CORE_DSL_PATH \
		-verbosity $4 \
		$INPUT_FILE_PATH
else 
	./bin/clang_frontend \
		-extra-arg-before="-std=c++11" \
		-extra-arg-before="-I$HALIDE_PATH/include"  \
		-extra-arg-before="-I$DEXTER_INCLUDE_PATH"  \
		-dxjar $DEXTER_JAR_PATH \
		-dsl-core $CORE_DSL_PATH \
		-dsl-user $USER_DSL_PATH \
		-verbosity $4 \
		$INPUT_FILE_PATH
fi
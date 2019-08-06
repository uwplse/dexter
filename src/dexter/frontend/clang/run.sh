HALIDE_PATH=/home/maaz/Software/Halide
DEXTER_JAR_PATH=../../../../out/artifacts/dexter_jar/dexter.jar
DEXTER_INCLUDE_PATH=include
DSL_PATH=DSL.ir

export LD_LIBRARY_PATH=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server

./bin/clang_frontend \
	-extra-arg-before="-std=c++11" \
	-extra-arg-before="-I$HALIDE_PATH/include"  \
	-extra-arg-before="-Iinclude"  \
	-dxjar $DEXTER_JAR_PATH \
	-dsl $DSL_PATH \
	$1
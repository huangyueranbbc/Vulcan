for p in ./*.proto
do
    ./protoc -I=./ --java_out=../src/main/java/ $p
done
## Refs
- https://www.baeldung.com/jni

## Commands

MacOS version

```shell
# find java home
$ java -XshowSettings:properties -version 2>&1 > /dev/null | grep 'java.home'
java.home = /Library/Java/JavaVirtualMachines/jdk1.8.0_231.jdk/Contents/Home/jre
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_231.jdk/Contents/Home
# compile
g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin github_incodelearning_jni_DemoJNI.cpp -o github_incodelearning_jni_DemoJNI.o
# link and include in shared library
g++ -dynamiclib -o libnative.dylib github_incodelearning_jni_DemoJNI.o -lc
# run below at project root directory InCodeLearning-Java/src/main/java
java -cp . -Djava.library.path=/${NATIVE_SHARED_LIB_FOLDER} github.incodelearning.jni.DemoJNI
```
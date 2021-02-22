## Refs
- https://www.baeldung.com/jni

## Commands

MacOS version

```shell
# find java home
$ java -XshowSettings:properties -version 2>&1 > /dev/null | grep 'java.home'
java.home = /Library/Java/JavaVirtualMachines/jdk1.8.0_231.jdk/Contents/Home/jre
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_231.jdk/Contents/Home
jenv gloabal 1.8.0.231 # set java version
java -version # confirm java 8 version
$ pwd
/Users/<user>/Unix/projects/InCodeLearning-Java/src/main/java/github/incodelearning/jni
javac -h . DemoJNI.java # generates header file github_incodelearning_jni_DemoJNI.h
# compile
g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin github_incodelearning_jni_DemoJNI.cpp -o github_incodelearning_jni_DemoJNI.o
# link and include in shared library
g++ -dynamiclib -o libnative.dylib github_incodelearning_jni_DemoJNI.o -lc
# run below at project root directory InCodeLearning-Java/src/main/java
export NATIVE_SHARED_LIB_FOLDER=/Users/<user>/Unix/projects/InCodeLearning-Java/src/main/java/github/incodelearning/jni
$ java -cp . -Djava.library.path=/${NATIVE_SHARED_LIB_FOLDER} github.incodelearning.jni.DemoJNI
Hello from C++ !!
```

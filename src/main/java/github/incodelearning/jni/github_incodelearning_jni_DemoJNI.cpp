#include "github_incodelearning_jni_DemoJNI.h"
#include <iostream>

/*
 * Class:     github_incodelearning_jni_DemoJNI
 * Method:    sayHello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT void JNICALL   Java_github_incodelearning_jni_DemoJNI_sayHello (JNIEnv* env, jobject thisObject) {
	// std::string hello = "Hello from C++ !!";
    // std::cout << hello << std::endl;
    // return env->NewStringUTF(hello.c_str());
    std::cout << "Hello from C++ !!" << std::endl;
}
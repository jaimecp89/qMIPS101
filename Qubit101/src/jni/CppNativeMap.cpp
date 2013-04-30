#include "CppNativeMap.h"
#include <map>

typedef std::map<jobject, jobject, bool(*)(jobject,jobject)> nativemap;
static JNIEnv* senv;

bool comp(jobject o1, jobject o2){
	static jmethodID comp1;
	jclass ClassicStateClassID;
	int res;

	if(comp1 == NULL){
		ClassicStateClassID = senv->GetObjectClass(o1);
		comp1 = senv->GetMethodID(ClassicStateClassID, "compareTo", "(Ljava/lang/Object;)I");
	}
	
	res = senv->CallIntMethod(o1, comp1, o2);

	return res < 0;
}

JNIEXPORT void JNICALL Java_domain_engine_quantum_maps_NativeMap_init
  (JNIEnv * env, jobject obj)
{
	jclass cls;
	jfieldID id;
	jlong ref;

	bool(*comp_ptr)(jobject, jobject) = comp;
	nativemap* nmap;
	nmap = new nativemap(comp_ptr);
	
	cls = env->GetObjectClass(obj);
	id = env->GetFieldID(cls, "ref", "J");
	ref = env->GetLongField(obj, id);

	env->SetIntField(obj,id,(long)nmap);
	senv = env;
}


JNIEXPORT void JNICALL Java_domain_engine_quantum_maps_NativeMap__1clear
  (JNIEnv *, jobject, jlong ref){

	  nativemap* nmap = (nativemap*)ref;
	  nmap->clear();

}

JNIEXPORT jboolean JNICALL Java_domain_engine_quantum_maps_NativeMap__1containsKey
  (JNIEnv *, jobject, jlong ref, jobject arg0){

	  nativemap* nmap = (nativemap*)ref;
	  return nmap->count(arg0) >= 1;
}

JNIEXPORT jboolean JNICALL Java_domain_engine_quantum_maps_NativeMap__1containsValue
  (JNIEnv *, jobject, jlong ref, jobject arg0){
	  //TODO
	  return NULL;
}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeMap__1entrySet
	(JNIEnv * env, jobject obj, jlong ref){

		jclass hashSetClass;
		jclass nativeMapClass;
		jmethodID cidConst;
		jmethodID cidAdd;
		jmethodID cidCreate;
		jobject hset;

		hashSetClass = env->FindClass("java/util/HashSet");
		nativeMapClass = env->FindClass("domain/engine/quantum/maps/NativeMap");
		
		cidConst = env->GetMethodID(hashSetClass, "<init>", "()V");
		cidAdd = env->GetMethodID(hashSetClass, "add", "(Ljava/lang/Object;)Z");
		cidCreate = env->GetMethodID(nativeMapClass,"createEntry","(Ldomain/engine/quantum/ClassicState;Ldomain/engine/math/Complex;)Ldomain/engine/quantum/maps/NativeMap$Entry;");
		
		hset = env->NewObject(hashSetClass, cidConst);

		nativemap* nmap = (nativemap*)ref;
		nativemap::iterator it;

		for(it = nmap->begin(); it != nmap->end(); it++){
			jobject entry;
			entry =	env->CallObjectMethod(obj, cidCreate, (*it).first, (*it).second);
			env->CallBooleanMethod(hset, cidAdd, entry);
		}

		return hset;
}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeMap__1get
  (JNIEnv * env, jobject, jlong ref, jobject arg0){
	  
	  nativemap* nmap = (nativemap*)ref;
	  jobject fnd;
	  try{
		  fnd = nmap->at(arg0);
	  }catch(...){
		  fnd = NULL;
	  }
	  return fnd;
}

JNIEXPORT jboolean JNICALL Java_domain_engine_quantum_maps_NativeMap__1isEmpty
  (JNIEnv *, jobject, jlong ref){

	  nativemap* nmap = (nativemap*)ref;
	  return nmap->size() == 0;
}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeMap__1keySet
  (JNIEnv *, jobject, jlong){
	  printf("Usando el metodo nativo _keySet");
	  return NULL;
}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeMap__1put
  (JNIEnv * env, jobject, jlong ref, jobject arg0, jobject arg1){

	  nativemap* nmap = (nativemap*)ref;
	  nmap->insert(std::pair<jobject, jobject>(env->NewGlobalRef(arg0), env->NewGlobalRef(arg1)));
	  return NULL;
}

JNIEXPORT void JNICALL Java_domain_engine_quantum_maps_NativeMap__1putAll
  (JNIEnv *, jobject, jlong, jobject){
	  printf("Usando el metodo nativo _putAll");
}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeMap__1remove
  (JNIEnv *, jobject, jlong ref, jobject arg0){

	  nativemap* nmap = (nativemap*)ref;
	  jobject res = nmap->at(arg0);
	  nmap->erase(arg0);
	  return res;
}

JNIEXPORT jint JNICALL Java_domain_engine_quantum_maps_NativeMap__1size
  (JNIEnv *, jobject, jlong ref){

	  nativemap* nmap = (nativemap*)ref;
	  return nmap->size();
}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeMap__1values
  (JNIEnv *, jobject, jlong){
	  printf("Usando el metodo nativo _values");
	  return NULL;
}
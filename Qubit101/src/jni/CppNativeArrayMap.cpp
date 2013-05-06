#include "CppNativeArrayMap.h"
#include "jni.h"
#include <math.h>

struct entry{
	jobject key;
	jobject value;
};

typedef struct{
	entry** elems;
	jsize size;
} arraymap;

int getEntryIndex(JNIEnv * env, jobject key){
	static jfieldID stateid;
	jbyteArray bytes;
	jbyte * nbytes;
	int index = 0;

	if(stateid == NULL){
		stateid = env->GetFieldID(env->GetObjectClass(key), "state", "[B");
	}
	bytes = reinterpret_cast<jbyteArray>(env->GetObjectField(key, stateid));
	nbytes = env->GetByteArrayElements(bytes, NULL);

	for(int i = 0; i < env->GetArrayLength(bytes); i++){
		if(nbytes[i] == 1)
			index += 1 << i;
	}

	return index;
}

jlong init(JNIEnv * env, jobject obj, jobject key){
	jobject state;
	jbyteArray arr;
	jclass classicState;
	jfieldID getState;
	jsize size;
	jclass cls;
	jfieldID id;

	classicState = env->GetObjectClass(key);
	getState = env->GetFieldID(classicState, "state", "[B");
	state = env->GetObjectField(key, getState);

	arr = (jbyteArray)state;

	size = env->GetArrayLength(arr);

	int mapLength = pow(2.0, size);

	static arraymap amap = *(new arraymap);

	amap.elems = new entry*[mapLength];
	amap.size = mapLength;

	for(int i = 0; i < mapLength; i++)
		amap.elems[i] = NULL;

	cls = env->GetObjectClass(obj);
	id = env->GetFieldID(cls, "ref", "J");

	env->SetLongField(obj,id,(jlong)&amap);

	return (jlong)&amap;
}

JNIEXPORT void JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1clear
	(JNIEnv * env, jobject obj, jlong ref){
		jclass cls;
		jfieldID id;

		arraymap* nmap = (arraymap*)ref;
		nmap = NULL;

		cls = env->GetObjectClass(obj);
		id = env->GetFieldID(cls, "ref", "J");
		ref = env->GetLongField(obj, id);

		env->SetIntField(obj,id,0);
}


JNIEXPORT jboolean JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1containsKey
	(JNIEnv * env, jobject, jlong ref, jobject key){
		if(ref == 0) return false;
		return NULL;
}

JNIEXPORT jboolean JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1containsValue
	(JNIEnv *, jobject, jlong ref, jobject){
		if(ref == 0) return false;
		return NULL;
}


JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1entrySet
	(JNIEnv * env, jobject obj, jlong ref){

		jclass hashSetClass;
		jclass nativeMapClass;
		jmethodID cidConst;
		jmethodID cidAdd;
		jmethodID cidCreate;
		jobject hset;

		hashSetClass = env->FindClass("java/util/HashSet");
		nativeMapClass = env->FindClass("domain/engine/quantum/maps/NativeArrayMap");

		cidConst = env->GetMethodID(hashSetClass, "<init>", "()V");
		cidAdd = env->GetMethodID(hashSetClass, "add", "(Ljava/lang/Object;)Z");
		cidCreate = env->GetMethodID(nativeMapClass,"createEntry","(Ldomain/engine/quantum/ClassicState;Ldomain/engine/math/Complex;)Ldomain/engine/quantum/maps/NativeArrayMap$Entry;");

		hset = env->NewObject(hashSetClass, cidConst);

		arraymap amap = *(arraymap*)ref;
		int size = amap.size;

		for(int i = 0; i < size; i++){
			jobject entry;
			if(amap.elems[i] != NULL){
				entry =	env->CallObjectMethod(obj, cidCreate, (*amap.elems[i]).key, (*amap.elems[i]).value);
				env->CallBooleanMethod(hset, cidAdd, entry);
			}
		}
		return hset;

}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1get
	(JNIEnv * env, jobject, jlong ref, jobject key){
		if(ref == 0) return NULL;

		arraymap amap = *(arraymap*)ref;
		int index = getEntryIndex(env, key);
		if(amap.elems[index]==NULL) return NULL;
		return ((*amap.elems[index])).value;
}


JNIEXPORT jboolean JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1isEmpty
	(JNIEnv *, jobject, jlong ref){
		if(ref == 0) return true;
		//TODO
}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1keySet
	(JNIEnv *, jobject, jlong ref){
		printf("Usando el metodo nativo _keySet");
		return NULL;
}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1put
	(JNIEnv * env, jobject obj, jlong ref, jobject key, jobject value){
		jlong auxref = ref;
		jobject res;

		if(auxref == 0) auxref = init(env, obj, key);
		static jfieldID stateid;
		int index = getEntryIndex(env, key);
 
		arraymap amap = *(arraymap*)auxref;

		if(amap.elems[index] == NULL) res = NULL;
		else res = (*amap.elems[index]).value;

		entry* ent = new entry;
		amap.elems[index] = ent;

		(*ent).key = env->NewGlobalRef(key);
		(*ent).value = env->NewGlobalRef(value);

		return env->NewGlobalRef(res);
}

JNIEXPORT void JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1putAll
	(JNIEnv *, jobject, jlong ref, jobject){
		printf("Usando el metodo nativo _putAll");
}


JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1remove
	(JNIEnv * env, jobject, jlong ref, jobject key){
		int index = getEntryIndex(env, key);
		arraymap amap = *(arraymap*)ref;

		jobject res = (*amap.elems[index]).value;
		amap.elems[index] = NULL;
		return res;
}

JNIEXPORT jint JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1size
	(JNIEnv *, jobject, jlong ref){
		if(ref == NULL) return 0;
		arraymap amap = *(arraymap*)ref;
		return amap.size;
}

JNIEXPORT jobject JNICALL Java_domain_engine_quantum_maps_NativeArrayMap__1values
	(JNIEnv *, jobject, jlong ref){
		printf("Usando el metodo nativo _values");
		return NULL;
}

package ru.alexpanchenko.stater.plugin.utils

import com.android.annotations.NonNull
import groovy.transform.TypeChecked
import ru.alexpanchenko.stater.plugin.model.MethodDescriptor
import ru.alexpanchenko.stater.plugin.model.StateType
import ru.alexpanchenko.stater.plugin.visitors.TypesSignatureVisitor
import stater.org.objectweb.asm.signature.SignatureReader

@TypeChecked
class MethodDescriptorUtils {

  static MethodDescriptor getDescriptorByType(StateType type, boolean isGet) {
    switch (type) {
      case StateType.BOOLEAN:
        return new MethodDescriptor(isGet ? Methods.Get.BOOLEAN : Methods.Put.BOOLEAN, Descriptors.BOOLEAN)
      case StateType.BOOLEAN_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.BOOLEAN_ARRAY : Methods.Put.BOOLEAN_ARRAY, Descriptors.BOOLEAN_ARRAY)
      case StateType.BYTE:
        return new MethodDescriptor(isGet ? Methods.Get.BYTE : Methods.Put.BYTE, Descriptors.BYTE)
      case StateType.BYTE_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.BYTE_ARRAY : Methods.Put.BYTE_ARRAY, Descriptors.BYTE_ARRAY)
      case StateType.CHAR:
        return new MethodDescriptor(isGet ? Methods.Get.CHAR : Methods.Put.CHAR, Descriptors.CHAR)
      case StateType.CHAR_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.CHAR_ARRAY : Methods.Put.CHAR_ARRAY, Descriptors.CHAR_ARRAY)
      case StateType.SHORT:
        return new MethodDescriptor(isGet ? Methods.Get.SHORT : Methods.Put.SHORT, Descriptors.SHORT)
      case StateType.SHORT_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.SHORT_ARRAY : Methods.Put.SHORT_ARRAY, Descriptors.SHORT_ARRAY)
      case StateType.INT:
        return new MethodDescriptor(isGet ? Methods.Get.INT : Methods.Put.INT, Descriptors.INT)
      case StateType.INT_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.INT_ARRAY : Methods.Put.INT_ARRAY, Descriptors.INT_ARRAY)
      case StateType.INT_ARRAY_LIST:
        return new MethodDescriptor(isGet ? Methods.Get.INT_ARRAY_LIST : Methods.Put.INT_ARRAY_LIST, Descriptors.ARRAY_LIST)
      case StateType.FLOAT:
        return new MethodDescriptor(isGet ? Methods.Get.FLOAT : Methods.Put.FLOAT, Descriptors.FLOAT)
      case StateType.FLOAT_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.FLOAT_ARRAY : Methods.Put.FLOAT_ARRAY, Descriptors.FLOAT_ARRAY)
      case StateType.LONG:
        return new MethodDescriptor(isGet ? Methods.Get.LONG : Methods.Put.LONG, Descriptors.LONG)
      case StateType.LONG_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.LONG_ARRAY : Methods.Put.LONG_ARRAY, Descriptors.LONG_ARRAY)
      case StateType.DOUBLE:
        return new MethodDescriptor(isGet ? Methods.Get.DOUBLE : Methods.Put.DOUBLE, Descriptors.DOUBLE)
      case StateType.DOUBLE_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.DOUBLE_ARRAY : Methods.Put.DOUBLE_ARRAY, Descriptors.DOUBLE_ARRAY)
      case StateType.STRING:
        return new MethodDescriptor(isGet ? Methods.Get.STRING : Methods.Put.STRING, Descriptors.STRING)
      case StateType.STRING_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.STRING_ARRAY : Methods.Put.STRING_ARRAY, Descriptors.STRING_ARRAY)
      case StateType.STRING_ARRAY_LIST:
        return new MethodDescriptor(isGet ? Methods.Get.STRING_ARRAY_LIST : Methods.Put.STRING_ARRAY_LIST, Descriptors.ARRAY_LIST)
      case StateType.CHAR_SEQUENCE:
        return new MethodDescriptor(isGet ? Methods.Get.CHAR_SEQUENCE : Methods.Put.CHAR_SEQUENCE, Descriptors.CHAR_SEQUENCE)
      case StateType.CHAR_SEQUENCE_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.CHAR_SEQUENCE_ARRAY : Methods.Put.CHAR_SEQUENCE_ARRAY, Descriptors.CHAR_SEQUENCE_ARRAY)
      case StateType.CHAR_SEQUENCE_ARRAY_LIST:
        return new MethodDescriptor(isGet ? Methods.Get.CHAR_SEQUENCE_ARRAY_LIST : Methods.Put.CHAR_SEQUENCE_ARRAY_LIST, Descriptors.ARRAY_LIST)
      case StateType.SERIALIZABLE:
        return new MethodDescriptor(isGet ? Methods.Get.SERIALIZABLE : Methods.Put.SERIALIZABLE, Descriptors.SERIALIZABLE)
      case StateType.PARCELABLE:
        return new MethodDescriptor(isGet ? Methods.Get.PARCELABLE : Methods.Put.PARCELABLE, Descriptors.PARCELABLE)
      case StateType.PARCELABLE_ARRAY:
        return new MethodDescriptor(isGet ? Methods.Get.PARCELABLE_ARRAY : Methods.Put.PARCELABLE_ARRAY, Descriptors.PARCELABLE_ARRAY)
      case StateType.PARCELABLE_ARRAY_LIST:
        return new MethodDescriptor(isGet ? Methods.Get.PARCELABLE_ARRAY_LIST : Methods.Put.PARCELABLE_ARRAY_LIST, Descriptors.ARRAY_LIST)
      case StateType.BUNDLE:
        return new MethodDescriptor(isGet ? Methods.Get.BUNDLE : Methods.Put.BUNDLE, Descriptors.BUNDLE)
      case StateType.IBINDER:
        return new MethodDescriptor(isGet ? Methods.Get.IBINDER : Methods.Put.IBINDER, Descriptors.IBINDER)
      case StateType.CUSTOM:
        return new MethodDescriptor(isGet ? Methods.Get.STRING : Methods.Put.STRING, Descriptors.STRING)
    }
  }

  @NonNull
  static List<String> getSignatureTypes(@NonNull String signature) {
    TypesSignatureVisitor signatureVisitor = new TypesSignatureVisitor()
    new SignatureReader(signature).accept(signatureVisitor)
    return signatureVisitor.types
  }

  static boolean primitiveIsObject(String descriptor) {
    return descriptor == Descriptors.BYTE_OBJ || descriptor == Descriptors.BYTE_OBJ_ARRAY ||
        descriptor == Descriptors.BOOLEAN_OBJ || descriptor == Descriptors.BOOLEAN_OBJ_ARRAY ||
        descriptor == Descriptors.CHAR_OBJ || descriptor == Descriptors.CHAR_OBJ_ARRAY ||
        descriptor == Descriptors.SHORT_OBJ || descriptor == Descriptors.SHORT_OBJ_ARRAY ||
        descriptor == Descriptors.FLOAT_OBJ || descriptor == Descriptors.FLOAT_OBJ_ARRAY ||
        descriptor == Descriptors.INTEGER || descriptor == Descriptors.INTEGER_ARRAY ||
        descriptor == Descriptors.LONG_OBJ || descriptor == Descriptors.LONG_OBJ_ARRAY ||
        descriptor == Descriptors.DOUBLE_OBJ || descriptor == Descriptors.DOUBLE_OBJ_ARRAY
  }
}

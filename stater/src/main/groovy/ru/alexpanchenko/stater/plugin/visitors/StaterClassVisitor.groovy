package ru.alexpanchenko.stater.plugin.visitors

import com.android.annotations.NonNull
import groovy.transform.TypeChecked
import javassist.ClassPool
import ru.alexpanchenko.stater.plugin.StateFieldStorage
import ru.alexpanchenko.stater.plugin.utils.*
import stater.org.objectweb.asm.*

@TypeChecked
class StaterClassVisitor extends ClassVisitor implements Opcodes {

  private boolean needTransform = false
  private boolean hasOnCreate = false
  private boolean hasSavedInstanceState = false
  private String owner
  private String superOwner

  private final ClassPool classPool
  private final StateTypeDeterminator typeDeterminator
  private final StateFieldStorage fieldStorage

  StaterClassVisitor(
      @NonNull ClassVisitor classVisitor,
      @NonNull ClassPool classPool,
      @NonNull StateTypeDeterminator typeDeterminator,
      @NonNull StateFieldStorage fieldStorage
  ) {
    super(Const.ASM_VERSION, classVisitor)
    this.classPool = classPool
    this.typeDeterminator = typeDeterminator
    this.fieldStorage = fieldStorage
  }

  @Override
  void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    this.owner = name
    this.superOwner = superName
    needTransform = ClassHierarchyUtils.containsParent(
        classPool,
        superName,
        Packages.ACTIVITY,
        Packages.ACTIVITY_X_SUPPORT,
        Packages.FRAGMENT_X,
        Packages.FRAGMENT_SUPPORT,
        Packages.FRAGMENT
    )
    super.visit(version, access, name, signature, superName, interfaces)
  }

  @Override
  FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
    FieldVisitor fv = super.visitField(access, name, descriptor, signature, value)
    if (needTransform) {
      return new StaterFieldVisitor(fv, name, descriptor, signature, owner, typeDeterminator, fieldStorage)
    }
    return fv
  }

  @Override
  MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions)
    if (needTransform && name == Methods.ON_CREATE) {
      hasOnCreate = true
      return new OnCreateVisitor(mv, fieldStorage)
    }
    if (needTransform && name == Methods.ON_SAVED_INSTANCE_STATE) {
      hasSavedInstanceState = true
      return new OnSavedInstanceStateVisitor(mv, fieldStorage)
    }
    return mv
  }

  @Override
  void visitEnd() {
    if (needTransform && !hasOnCreate) {
      visitOnCreateMethod()
    }
    if (needTransform && !hasSavedInstanceState) {
      visitOnSaveInstanceStateMethod()
    }
    super.visitEnd()
    fieldStorage.clear()
  }

  private void visitOnCreateMethod() {
    MethodVisitor methodVisitor = cv.visitMethod(
        Opcodes.ACC_PROTECTED, Methods.ON_CREATE, Descriptors.ON_CREATE, null, null
    )
    methodVisitor.visitCode()
    Label l0 = new Label()
    methodVisitor.visitLabel(l0)
    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
    methodVisitor.visitMethodInsn(
        Opcodes.INVOKESPECIAL, superOwner, Methods.ON_CREATE, Descriptors.ON_CREATE, false
    )
    Label l1 = new Label()
    methodVisitor.visitLabel(l1)
    new OnCreateVisitor(methodVisitor, fieldStorage).visitCode()
    Label l2 = new Label()
    methodVisitor.visitLabel(l2)
    methodVisitor.visitInsn(Opcodes.RETURN)
    methodVisitor.visitMaxs(3, 1)
    methodVisitor.visitEnd()
  }

  private void visitOnSaveInstanceStateMethod() {
    MethodVisitor methodVisitor = cv.visitMethod(
        Opcodes.ACC_PROTECTED,
        Methods.ON_SAVED_INSTANCE_STATE,
        Descriptors.ON_SAVED_INSTANCE_STATE,
        null,
        null
    )
    methodVisitor.visitCode()
    Label l0 = new Label()
    methodVisitor.visitLabel(l0)
    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
    methodVisitor.visitMethodInsn(
        Opcodes.INVOKESPECIAL, superOwner,
        Methods.ON_SAVED_INSTANCE_STATE,
        Descriptors.ON_SAVED_INSTANCE_STATE,
        false
    )
    Label l1 = new Label()
    methodVisitor.visitLabel(l1)
    new OnSavedInstanceStateVisitor(methodVisitor, fieldStorage).visitCode()
    Label l2 = new Label()
    methodVisitor.visitLabel(l2)
    methodVisitor.visitInsn(Opcodes.RETURN)
    methodVisitor.visitMaxs(3, 1)
    methodVisitor.visitEnd()
  }
}

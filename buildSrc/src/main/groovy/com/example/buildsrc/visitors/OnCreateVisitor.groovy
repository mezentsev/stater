package com.example.buildsrc.visitors

import groovy.transform.TypeChecked
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import com.example.buildsrc.model.MethodDescriptor
import com.example.buildsrc.model.StateType
import com.example.buildsrc.utils.Const
import com.example.buildsrc.utils.Descriptors
import com.example.buildsrc.utils.MethodDescriptorUtils
import com.example.buildsrc.utils.Types

@TypeChecked
class OnCreateVisitor extends MethodVisitor {

  OnCreateVisitor(MethodVisitor methodVisitor) {
    super(Const.ASM_VERSION, methodVisitor)
  }

  @Override
  void visitCode() {
    mv.visitCode()

    Label l1 = new Label()
    mv.visitVarInsn(Opcodes.ALOAD, 1)
    mv.visitJumpInsn(Opcodes.IFNULL, l1)

    Const.stateFields.each { field ->
      Label label = new Label()
      mv.visitLabel(label)
      mv.visitVarInsn(Opcodes.ALOAD, 0)
      mv.visitVarInsn(Opcodes.ALOAD, 1)
      mv.visitLdcInsn(field.key)

      MethodDescriptor methodDescriptor = MethodDescriptorUtils.getDescriptorByType(field.type, true)
      if (methodDescriptor == null || !methodDescriptor.isValid()) {
        throw new IllegalStateException("StateType for ${field.name} in ${field.owner} is unknown!")
      }
      mv.visitMethodInsn(
          Opcodes.INVOKEVIRTUAL,
          Types.BUNDLE,
          methodDescriptor.method,
          "(${Descriptors.STRING})${methodDescriptor.descriptor}",
          false
      )
      // cast
      if (field.type == StateType.SERIALIZABLE
          || field.type == StateType.PARCELABLE
          || field.type == StateType.PARCELABLE_ARRAY
          || field.type == StateType.IBINDER
      ) {
        mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getType(field.descriptor).internalName)
      }
      mv.visitFieldInsn(Opcodes.PUTFIELD, field.owner, field.name, field.descriptor)
    }
    mv.visitLabel(l1)
  }

}
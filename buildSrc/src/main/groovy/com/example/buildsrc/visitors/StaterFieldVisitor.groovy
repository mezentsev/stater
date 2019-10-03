package com.example.buildsrc.visitors

import com.android.annotations.NonNull
import com.example.buildsrc.model.SaverField
import com.example.buildsrc.model.StateType
import com.example.buildsrc.utils.Const
import com.example.buildsrc.utils.Descriptors
import com.example.buildsrc.utils.StateTypeDeterminator
import groovy.transform.TypeChecked
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.FieldVisitor

@TypeChecked
class StaterFieldVisitor extends FieldVisitor {

  private final String name
  private final String descriptor
  private final String signature
  private final String owner
  private final StateTypeDeterminator typeDeterminator

  StaterFieldVisitor(
      @NonNull FieldVisitor fieldVisitor,
      @NonNull String name,
      @NonNull String descriptor,
      @NonNull String signature,
      @NonNull String owner,
      @NonNull StateTypeDeterminator typeDeterminator
  ) {
    super(Const.ASM_VERSION, fieldVisitor)
    this.name = name
    this.descriptor = descriptor
    this.signature = signature
    this.owner = owner
    this.typeDeterminator = typeDeterminator
  }

  @Override
  AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    AnnotationVisitor av = super.visitAnnotation(descriptor, visible)
    if (descriptor == Descriptors.STATE) {
      StateType type = typeDeterminator.determinate(this.descriptor, this.signature)
      SaverField field = new SaverField(this.name, this.descriptor, this.owner, type)
      Const.stateFields.add(field)
    }
    return av
  }

}

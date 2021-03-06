package ru.alexpanchenko.stater.plugin.visitors;

import org.junit.Before;
import org.junit.Test;

import javassist.ClassPool;
import javassist.NotFoundException;
import ru.alexpanchenko.stater.plugin.StateFieldStorage;
import ru.alexpanchenko.stater.plugin.model.StateType;
import ru.alexpanchenko.stater.plugin.utils.Descriptors;
import ru.alexpanchenko.stater.plugin.utils.StateTypeDeterminator;
import stater.org.objectweb.asm.FieldVisitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class StaterFieldVisitorTest {

  private static final String A_CLASS = "ru/alexpanchenko/stater/plugin/utils/models/A.class";
  private static final String B_CLASS = "ru/alexpanchenko/stater/plugin/utils/models/B.class";
  private static final String B_PACKAGE = B_CLASS.replace(".class", "").replace("/", ".");

  private final ClassPool classPool = ClassPool.getDefault();
  private final StateFieldStorage fieldStorage = new StateFieldStorage();
  private final FieldVisitor fieldVisitor = mock(FieldVisitor.class);
  private String name = "name";
  private String descriptor = "descriptor";
  private String signature = "signature";
  private String owner = "owner";

  @Before
  public void setUp() {
    fieldStorage.clear();
  }

  @Test
  public void testDeterminatorDeterminate() {
    StateTypeDeterminator typeDeterminator = mock(StateTypeDeterminator.class);

    StaterFieldVisitor visitor = new StaterFieldVisitor(
      fieldVisitor, name, descriptor, signature, owner, typeDeterminator, fieldStorage
    );
    visitor.visitAnnotation(Descriptors.STATE, true);
    verify(typeDeterminator).determinate(descriptor, signature);
  }

  @Test
  public void testDeterminatorNotDeterminate() {
    StateTypeDeterminator typeDeterminator = mock(StateTypeDeterminator.class);

    StaterFieldVisitor visitor = new StaterFieldVisitor(
      fieldVisitor, name, descriptor, signature, owner, typeDeterminator, fieldStorage
    );
    visitor.visitAnnotation(Descriptors.STATE + "a", true);
    verify(typeDeterminator, never()).determinate(descriptor, signature);
  }

  @Test(expected = IllegalStateException.class)
  public void testIncorrectType() {
    descriptor = "Lcom/example/Fake";
    signature = null;

    StaterFieldVisitor visitor = new StaterFieldVisitor(
      fieldVisitor, name, descriptor, signature, owner, new StateTypeDeterminator(classPool, false), fieldStorage
    );
    visitor.visitAnnotation(Descriptors.STATE, true);
  }

  @Test
  public void testBoolean() {
    descriptor = Descriptors.BOOLEAN;
    signature = null;

    StaterFieldVisitor visitor = new StaterFieldVisitor(
      fieldVisitor, name, descriptor, signature, owner, new StateTypeDeterminator(classPool, false), fieldStorage
    );
    visitor.visitAnnotation(Descriptors.STATE, true);

    assertFalse(fieldStorage.getFields().isEmpty());
    assertEquals(fieldStorage.getFields().size(), 1);
    assertEquals(fieldStorage.getFields().get(0).type, StateType.BOOLEAN);
  }

  @Test
  public void testSerializable() throws NotFoundException {
    classPool.appendClassPath(A_CLASS);
    classPool.appendClassPath(B_CLASS);
    descriptor = "L" + B_PACKAGE + ";";
    signature = null;

    StaterFieldVisitor visitor = new StaterFieldVisitor(
      fieldVisitor, name, descriptor, signature, owner, new StateTypeDeterminator(classPool, false), fieldStorage
    );
    visitor.visitAnnotation(Descriptors.STATE, true);

    assertEquals(fieldStorage.getFields().get(0).type, StateType.SERIALIZABLE);
  }

}

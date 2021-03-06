package ru.alexpanchenko.stater;

import com.android.build.api.transform.Transform;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.LibraryPlugin;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import ru.alexpanchenko.stater.plugin.StaterPlugin;
import ru.alexpanchenko.stater.plugin.StaterTransform;

import static org.junit.Assert.assertNotNull;

public class StaterPluginTest {

  @Test(expected = GradleException.class)
  public void testApplyToNonAndroidProject() {
    final Project project = ProjectBuilder.builder().build();
    new StaterPlugin().apply(project);
  }

  @Test
  public void testApplyToAndroidProject() {
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(AppPlugin.class);
    StaterPlugin staterPlugin = new StaterPlugin();
    staterPlugin.apply(project);

    BaseExtension androidExtension = (BaseExtension) project.getExtensions().findByName("android");

    assertNotNull(androidExtension);

    Transform staterTransform = androidExtension.getTransforms().stream()
      .filter(transform -> transform instanceof StaterTransform)
      .findFirst()
      .get();

    assertNotNull(staterTransform);
  }

  @Test
  public void testApplyToAndroidLibraryProject() {
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(LibraryPlugin.class);
    StaterPlugin staterPlugin = new StaterPlugin();
    staterPlugin.apply(project);

    BaseExtension androidExtension = (BaseExtension) project.getExtensions().findByName("android");

    assertNotNull(androidExtension);

    Transform staterTransform = androidExtension.getTransforms().stream()
      .filter(transform -> transform instanceof StaterTransform)
      .findFirst()
      .get();

    assertNotNull(staterTransform);
  }
}

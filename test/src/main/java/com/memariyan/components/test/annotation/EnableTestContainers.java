package com.memariyan.components.test.annotation;

import com.memariyan.components.test.config.EnvironmentPropertyRegistrar;
import com.memariyan.components.test.execution.CustomExecutionListener;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.*;

import static org.springframework.test.context.TestExecutionListeners.MergeMode;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnvironmentPropertyRegistrar.class)
@TestExecutionListeners(value = CustomExecutionListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface EnableTestContainers {

	ImageType[] images();

}

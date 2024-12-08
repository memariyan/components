package com.memariyan.components.test.execution;

import com.memariyan.components.test.container.ContainerFactory;
import com.memariyan.components.test.container.ContainersContext;
import com.memariyan.components.test.annotation.EnableTestContainers;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextAnnotationUtils;
import org.springframework.test.context.TestExecutionListener;

import java.util.Arrays;
import java.util.Objects;

public class CustomExecutionListener implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        TestContextAnnotationUtils.AnnotationDescriptor<EnableTestContainers> descriptor = TestContextAnnotationUtils.
                findAnnotationDescriptor(testContext.getTestClass(), EnableTestContainers.class);
        if (Objects.isNull(descriptor)) {
            return;
        }
        EnableTestContainers annotation = descriptor.getAnnotation();
        Arrays.stream(annotation.images()).forEach(type -> new ContainerFactory().create(type, testContext).
                forEach(ContainersContext::addContainer));
        ContainersContext.start();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        ContainersContext.flush();
    }
}

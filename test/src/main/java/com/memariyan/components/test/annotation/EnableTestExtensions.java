package com.memariyan.components.test.annotation;

import com.memariyan.components.test.config.EnvironmentPropertyRegistrar;
import com.memariyan.components.test.extension.impl.WiremockExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({SpringExtension.class, WiremockExtension.class})
@Import(EnvironmentPropertyRegistrar.class)
public @interface EnableTestExtensions {

}

package com.memariyan.components.test.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memariyan.components.test.config.EnvironmentPropertyRegistrar;
import com.memariyan.components.test.extension.impl.WiremockExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ComponentScan(
		basePackageClasses = { ObjectMapper.class},
		includeFilters = {
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ObjectMapper.class),
				@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.memariyan.*.client.*ErrorDecoder")
		}
)
@ImportAutoConfiguration(classes = {FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.memariyan.**.client"})
@ExtendWith({SpringExtension.class, WiremockExtension.class})
@Import(EnvironmentPropertyRegistrar.class)
public @interface FeignTest {

	@AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
	String[] basePackages() default {"com.memariyan.**.client"};

	@AliasFor(annotation = ComponentScan.class, attribute = "excludeFilters")
	ComponentScan.Filter[] excludeFilters() default {};

	@AliasFor(annotation = EnableFeignClients.class, attribute = "clients")
	Class<?>[] clients();

}

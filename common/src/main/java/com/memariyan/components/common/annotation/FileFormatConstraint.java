package com.memariyan.components.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileFormatConstraint implements ConstraintValidator<FileFormat, MultipartFile> {

    private List<String> formats;
    private int minLength;

    @Override
    public void initialize(FileFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        formats = List.of(constraintAnnotation.formats());
        minLength = constraintAnnotation.minLength();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return file.getSize() > minLength && formats.contains(FilenameUtils.getExtension(file.getOriginalFilename()));
    }
}

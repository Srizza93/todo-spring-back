package com.todo.back.converter;

import com.todo.back.model.ERole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<ERole, String> {

    @Override
    public String convertToDatabaseColumn(ERole role) {
        return role.name();
    }

    @Override
    public ERole convertToEntityAttribute(String value) {
        return ERole.valueOf(value);
    }
}

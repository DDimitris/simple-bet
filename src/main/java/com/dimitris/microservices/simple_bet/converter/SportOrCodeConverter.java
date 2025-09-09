package com.dimitris.microservices.simple_bet.converter;

import com.dimitris.microservices.simple_bet.enums.Sport;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SportOrCodeConverter implements AttributeConverter<Sport, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Sport sport) {
        if (sport == null) return null;
        return sport.getCode();
    }

    @Override
    public Sport convertToEntityAttribute(Integer dbCode) {
        if (dbCode == null) return null;
        return Sport.toName(dbCode);
    }
}


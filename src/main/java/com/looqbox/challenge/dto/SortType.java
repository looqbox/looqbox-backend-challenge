package com.looqbox.challenge.dto;

import org.springframework.util.StringUtils;

public enum SortType {

    ALPHABETICAL,
    LENGTH;

    /**
     * Converte uma string para o tipo de ordenação correspondente, ignorando maiúsculas e minúsculas.
     * @param value A string a ser convertida para SortType.
     * @return O tipo de ordenação correspondente ou ALPHABETICAL como padrão.
     */
    public static SortType fromString(String value) {
        if (!StringUtils.hasText(value)) {
            return ALPHABETICAL;
        }

        for (SortType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return ALPHABETICAL;
    }
}

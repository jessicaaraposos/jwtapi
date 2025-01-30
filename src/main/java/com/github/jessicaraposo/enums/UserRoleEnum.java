package com.github.jessicaraposo.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum UserRoleEnum {
    ADMIN, MEMBER, EXTERNAL;

    private static final Set<String> VALID_ROLES = Arrays.stream(UserRoleEnum.values())
            .map(Enum::name) // Converte os valores do enum para String (ex: "ADMIN")
            .collect(Collectors.toSet());

    public static boolean isValidRole(String role) {
        return role != null && VALID_ROLES.contains(role.toUpperCase());
    }

    public static Set<String> getValidRoles() {
        return VALID_ROLES;
    }
}

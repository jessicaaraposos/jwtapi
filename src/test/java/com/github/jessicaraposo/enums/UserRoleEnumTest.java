package com.github.jessicaraposo.enums;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleEnumTest {

    @Test
    void shouldValidateValidRoles() {
        assertTrue(UserRoleEnum.isValidRole("ADMIN"));
        assertTrue(UserRoleEnum.isValidRole("MEMBER"));
        assertTrue(UserRoleEnum.isValidRole("EXTERNAL"));
    }

    @Test
    void shouldInvalidateInvalidRoles() {
        assertFalse(UserRoleEnum.isValidRole("INVALID_ROLE"));
        assertFalse(UserRoleEnum.isValidRole("User"));
        assertFalse(UserRoleEnum.isValidRole(""));
        assertFalse(UserRoleEnum.isValidRole(null));
    }

    @Test
    void shouldBeCaseInsensitive() {
        assertTrue(UserRoleEnum.isValidRole("admin"));
        assertTrue(UserRoleEnum.isValidRole("mEmBeR"));
        assertTrue(UserRoleEnum.isValidRole("EXTERNAL"));
    }

    @Test
    void shouldReturnAllValidRoles() {
        Set<String> validRoles = UserRoleEnum.getValidRoles();
        assertEquals(Set.of("ADMIN", "MEMBER", "EXTERNAL"), validRoles);
    }
}

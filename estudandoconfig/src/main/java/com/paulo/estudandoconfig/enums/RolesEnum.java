package com.paulo.estudandoconfig.enums;

public enum RolesEnum {
admin("admin"),user("user");

    private final String roleString;
    RolesEnum(String roleString) {
        this.roleString = roleString;
    }
    public String getRoleString() {
        return roleString;
    }

    public static RolesEnum fromString(String roleString) {
        for (RolesEnum role : RolesEnum.values()) {
            if (role.roleString.equalsIgnoreCase(roleString)) {
                return role;
            }
        }
        throw new IllegalArgumentException("The role " + roleString+" isnt valid.");
    }
}

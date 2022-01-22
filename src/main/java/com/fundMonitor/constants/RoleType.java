package com.fundMonitor.constants;

public enum RoleType {
    admin("管理员"),
    common("普通员工");
    public String role;

    RoleType(String r) {
        this.role = r;
    }
}

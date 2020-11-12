package br.com.softplan.models.enuns;

import javax.validation.constraints.NotEmpty;

public enum Profiles {
    ADMIN(1, "ROLE_ADMIN"),
    TRIADOR(2, "ROLE_TRIADOR"),
    FINALIZADOR(3, "ROLE_FINALIZADOR");

    private int cod;
    @NotEmpty(message = "Profile may not be empty")
    private String desc;

    Profiles(int cod, String desc) {
        this.cod = cod;
        this.desc = desc;
    }

    public int getCod() {
        return cod;
    }

    public String getDesc() {
        return desc;
    }

    public static Profiles toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (Profiles profiles : Profiles.values()) {
            if (cod.equals(profiles.getCod())) {
                return profiles;
            }
        }
        throw new IllegalArgumentException("Id invalid: " + cod);
    }
}
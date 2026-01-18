package com.goolbitg.api.v1.entity.challengeGroup.enumeration;

public enum Category {
    FOOD("식비"),
    TRAFFIC("교통비"),
    SHOPING("쇼핑"),
    LIVING("생활비"),
    ETC("기타");

    private String koName;

    private Category(String koName) {
        this.koName = koName;
    }

    public String getKoName() {
        return this.koName;
    }

}

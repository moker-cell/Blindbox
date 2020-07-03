package com.toher.common.constants;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/24 17:15
 */
public enum  UploadEnum {
    UUID("uuid"),
    DATE("date"),
    CUSTOM("custom");

    private String typeName;

    UploadEnum(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    /**
     * 根据类型的名称，返回类型的枚举实例。
     * @param typeName 类型名称
     */
    public static UploadEnum fromTypeName(String typeName) {
        for (UploadEnum type : UploadEnum.values()) {
            if (type.getTypeName().equals(typeName)) {
                return type;
            }
        }
        return null;
    }
}

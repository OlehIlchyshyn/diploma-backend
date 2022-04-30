package com.nulp.fetchproductdata.common.enumeration;

public enum Status {
    AVAILABLE,
    OUT_OF_STOCK;

    public static Status getStatus(String status) {
        return switch (status) {
            case "available" -> AVAILABLE;
            case "limited" -> AVAILABLE;
            case "unavailable" -> OUT_OF_STOCK;
            case "waiting_for_supply" -> OUT_OF_STOCK;
            case "out_of_stock" -> OUT_OF_STOCK;
            case "http://schema.org/InStock" -> AVAILABLE;
            case "http://schema.org/OutOfStock" -> OUT_OF_STOCK;
            case "InStock" -> AVAILABLE;
            case "InStoreOnly" -> AVAILABLE;
            case "OutOfStock" -> OUT_OF_STOCK;
            case "hidden" -> OUT_OF_STOCK;
            default -> throw new IllegalStateException("Unexpected status value: " + status);
        };
    }
}

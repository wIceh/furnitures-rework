package me.wiceh.furnitures.permission;

public enum FurnituresPermission {
    FURNITURE("command.furniture"),
    SEARCH("command.furniture.search");

    private final String permission;

    FurnituresPermission(String permission) {
        this.permission = "ice.furnitures." + permission;
    }

    public String getPermission() {
        return permission;
    }
}

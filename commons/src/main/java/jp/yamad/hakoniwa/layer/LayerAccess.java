package jp.yamad.hakoniwa.layer;

/**
 * Represents the access level for each layer in the Hakoniwa system.
 * Hakoniwa is a layered architecture, and each layer has different access permissions.
 * This enum defines the access levels for each layer, which can be used to control
 * the behavior of the system based on the layer's trustworthiness.
 */
public enum LayerAccess {
    /**
     * Indicates that the layer is fully trusted and has unrestricted access to
     * all higher and lower layers in the system.
     * This level is typically assigned to the core layer of the system.
     */
    TRUSTED,

    /**
     * Indicates that the layer is trusted but has limited access to higher layers in the system.
     * Horizontal access and lower layer access are allowed, but sensitive operations may be restricted.
     * This level is typically assigned to layers that are trusted but not part of the core system.
     */
    HORIZONTAL_TRUSTED,

    /**
     * Indicates that the layer is untrusted and has access to lower layers in the system.
     * Any access of other layers will be decided each time based on the security policy.
     */
    UNTRUSTED,

    /**
     * Indicates that the layer is restricted and has no access to higher or lower layers in the system.
     * Any sensitive access or operations will be automatically denied, and the layer is considered untrusted.
     */
    RESTRICTED
}

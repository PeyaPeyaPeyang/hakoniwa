package jp.yamad.hakoniwa.java;

public class ReferenceType implements JavaType {
    public static final Integer

    private final String fullName;

    private final String packageNmae;
    private final String typeName;

    private ReferenceType(String packageNmae, String typeName) {
        this.fullName = packageNmae.isEmpty() ? typeName : packageNmae + "/" + typeName;
        this.packageNmae = packageNmae;
        this.typeName = typeName;
    }

    public static ReferenceType of(String packageNmae, String typeName) {
        return new ReferenceType(packageNmae, typeName);
    }

    public static ReferenceType ofDescriptor(String desc) {
        if (desc.contains(".")) {
            throw new IllegalArgumentException("Invalid class descriptor: " + desc);
        }

        if (desc.startsWith("L") && desc.endsWith(";")) {
            String fullName = desc.substring(1, desc.length() - 1);
            int lastSlashIndex = fullName.lastIndexOf('/');
            String packageName = fullName.substring(0, lastSlashIndex);
            String typeName = fullName.substring(lastSlashIndex + 1);
            return new ReferenceType(packageName, typeName);
        } else {
            throw new IllegalArgumentException("Invalid class descriptor: " + desc);
        }
    }

    public static ReferenceType of(String fullName) {
        String normalised = fullName.replace('.', '/');
        int lastSlashIndex = normalised.lastIndexOf('/');
        String packageName = lastSlashIndex >= 0 ? normalised.substring(0, lastSlashIndex) : "";
        String typeName = lastSlashIndex >= 0 ? normalised.substring(lastSlashIndex + 1) : normalised;
        return new ReferenceType(packageName, typeName);
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public String getTypeDescriptor() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.arrayDimentions; i++) {
            sb.append("[");
        }
        sb.append("L").append(this.fullName).append(";");
        return sb.toString();
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getPackageName() {
        return this.packageNmae;
    }

    public String getTypeName() {
        return this.typeName;
    }
}

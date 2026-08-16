package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.java.ClassMethod;
import jp.yamad.hakoniwa.java.JavaType;
import jp.yamad.hakoniwa.java.ReferenceType;

import java.util.Objects;

/**
 * Specifies the target of sandboxing.
 *
 * <p>Hakoniwa checks targets at method level.
 * A method is identified by its package name, class name, method name,
 * parameter descriptor, and return type descriptor.
 *
 * <p>The general form is:
 * {@code package/Class->method(params)returnType}.
 *
 * <p>Each component may be replaced with {@code *} to match any value.
 * Method parameters and return type may be omitted to match every overload
 * of the specified method.
 *
 * <p>Examples:
 * <ul>
 *   <li>{@code java/lang/System->exit(I)V}</li>
 *   <li>{@code java/lang/System->exit}</li>
 *   <li>{@code java/lang/System->*}</li>
 *   <li>{@code java/lang/*->exit}</li>
 *   <li>{@code java/lang/*->*}</li>
 *   <li>{@code main}</li>
 *   <li>{@code *}</li>
 * </ul>
 */
@SuppressWarnings("StringEquality")
public class HakoniwaTarget {
    private static final String WILDCARD = "*";

    private final String pattern;

    private final String packageName;
    private final String className;
    private final String methodName;
    private final String methodParams;
    private final String methodReturnType;

    public HakoniwaTarget(
            String packageName,
            String className,
            String methodName,
            String methodParams,
            String methodReturnType) {
        this(
                packageName + "/" + className + "->" + methodName,
                packageName,
                className,
                methodName,
                methodParams,
                methodReturnType);
    }

    private HakoniwaTarget(
            String pattern,
            String packageName,
            String className,
            String methodName,
            String methodParams,
            String methodReturnType) {
        this.pattern = pattern;
        this.packageName = canonicalizeWildcard(packageName);
        this.className = canonicalizeWildcard(className);
        this.methodName = canonicalizeWildcard(methodName);
        this.methodParams = canonicalizeWildcard(methodParams);
        this.methodReturnType = canonicalizeWildcard(methodReturnType);
    }

    public boolean matches(ClassMethod method) {
        if (!this.matches(method.getOwner())) {
            return false;
        }

        String methodName = method.getMethod().getName();
        JavaType[] parameterTypes = method.getMethod().getArgsTypes();
        JavaType returnType = method.getMethod().getReturnType();

        if (!matches(this.methodName, methodName)) {
            return false;
        }

        if (this.methodParams != WILDCARD) {
            StringBuilder sb = new StringBuilder();
            for (JavaType paramType : parameterTypes) {
                sb.append(paramType.getDescriptor());
            }
            if (!this.methodParams.contentEquals(sb)) {
                return false;
            }
        }

        if (this.methodReturnType != WILDCARD) {
            String returnTypeDescriptor = returnType.getDescriptor();
            return this.methodReturnType.equals(returnTypeDescriptor);
        }

        return true;
    }

    public boolean matches(ReferenceType clazz) {
        String packageName = clazz.getPackageName();
        String className = clazz.getClassName();

        return matches(this.packageName, packageName)
                && matches(this.className, className);
    }

    public static HakoniwaTarget parse(String pattern) {
        Objects.requireNonNull(pattern, "pattern");

        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }

        String packageName = WILDCARD;
        String className = WILDCARD;
        String methodName = WILDCARD;
        String methodParams = WILDCARD;
        String methodReturnType = WILDCARD;

        int arrowIndex = pattern.indexOf("->");

        if (arrowIndex != -1 && pattern.indexOf("->", arrowIndex + 2) != -1) {
            throw new IllegalArgumentException(
                    "Pattern contains multiple '->': " + pattern);
        }

        if (arrowIndex == -1) {
            /*
             * "*"    -> every method
             * "main" -> every method named "main"
             * "java/lang/System" -> every method in System
             */
            if (pattern.indexOf('/') == -1) {
                methodName = pattern;
            } else {
                Owner owner = parseOwner(pattern);
                packageName = owner.packageName;
                className = owner.className;
            }
        } else {
            String ownerPart = pattern.substring(0, arrowIndex);
            String methodPart = pattern.substring(arrowIndex + 2);

            if (ownerPart.isEmpty()) {
                throw new IllegalArgumentException(
                        "Owner must not be empty: " + pattern);
            }

            if (methodPart.isEmpty()) {
                throw new IllegalArgumentException(
                        "Method must not be empty: " + pattern);
            }

            Owner owner = parseOwner(ownerPart);
            packageName = owner.packageName;
            className = owner.className;

            int paramsStart = methodPart.indexOf('(');

            if (paramsStart == -1) {
                methodName = methodPart;
            } else {
                int paramsEnd = methodPart.indexOf(')', paramsStart + 1);

                if (paramsEnd == -1) {
                    throw new IllegalArgumentException(
                            "Unclosed parameter descriptor: " + pattern);
                }

                methodName = methodPart.substring(0, paramsStart);
                methodParams = methodPart.substring(paramsStart + 1, paramsEnd);
                methodReturnType = methodPart.substring(paramsEnd + 1);

                if (methodName.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Method name must not be empty: " + pattern);
                }

                if (methodReturnType.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Return type must not be empty: " + pattern);
                }
            }
        }

        return new HakoniwaTarget(
                pattern,
                packageName,
                className,
                methodName,
                methodParams,
                methodReturnType);
    }

    private static Owner parseOwner(String owner) {
        int slashIndex = owner.lastIndexOf('/');

        if (slashIndex == -1) {
            return new Owner(WILDCARD, owner);
        }

        String packageName = owner.substring(0, slashIndex);
        String className = owner.substring(slashIndex + 1);

        if (packageName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Package must not be empty: " + owner);
        }

        if (className.isEmpty()) {
            throw new IllegalArgumentException(
                    "Class must not be empty: " + owner);
        }

        return new Owner(packageName, className);
    }

    private static String canonicalizeWildcard(String value) {
        return WILDCARD.equals(value) ? WILDCARD : value;
    }

    public boolean matches(
            String packageName,
            String className,
            String methodName,
            String methodParams,
            String methodReturnType) {
        return matches(this.packageName, packageName)
                && matches(this.className, className)
                && matches(this.methodName, methodName)
                && matches(this.methodParams, methodParams)
                && matches(this.methodReturnType, methodReturnType);
    }

    private static boolean matches(String expected, String actual) {
        return expected == WILDCARD || expected.equals(actual);
    }

    public String getPattern() {
        return this.pattern;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getMethodParams() {
        return this.methodParams;
    }

    public String getMethodReturnType() {
        return this.methodReturnType;
    }

    private static final class Owner {
        private final String packageName;
        private final String className;

        private Owner(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }
    }
}

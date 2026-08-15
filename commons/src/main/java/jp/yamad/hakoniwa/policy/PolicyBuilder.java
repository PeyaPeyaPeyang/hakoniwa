package jp.yamad.hakoniwa.policy;

import jp.yamad.hakoniwa.action.ActionModel;

import java.util.ArrayList;

public class PolicyBuilder {
    private final List<Policy> definitions;

    public PolicyBuilder() {
        this.definitions = new ArrayList<>();
    }

    public void addDefinition(Policy policy) {
        this.definitions.add(policy);
    }

    public Building ofPackage(String packageName) {
        return new Building().packagName(packageName);
    }

    public Building ofClass(String className) {
        return new Building().className(className);
    }

    public Building ofClass(Class<?> clazz) {
        return new Building().className(clazz.getName());
    }

    public Building ofMethod(String methodName) {
        return new Building().methodName(methodName);
    }

    public Building ofMethod(Class<?> clazz, String methodName) {
        return new Building().className(clazz.getName()).methodName(methodName);
    }

    public class Building {
        private String packageNma = "*";
        private String className = "*";
        private String methodName = "*";
        private String methodParameter = "*";
        private String methodReturn = "*";

        private ActionModel<?> model;

        public PolicyBuilder build() {
            HakoniwaTarget target = new HakoniwaTarget(packageNma, className, methodName, methodParameter, methodReturn);
            if (model == null) {
                throw new IllegalStateException("ActionModel is not set");
            }

            Policy policy = new Policy(target, model);
            PolicyBuilder.this.addDefinition(policy);
            return PolicyBuilder.this;
        }

        public Building packagName(String packageNma) {
            this.packageNma = packageNma;
            return this;
        }

        public Building className(String className) {
            this.className = className;
            return this;
        }

        public Building methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Building parameters(String methodParameter) {
            this.methodParameter = methodParameter;
            return this;
        }

        public Building parameters(Class<?>... paramTypes) {
            StringBuilder sb = new StringBuilder();
            for (Class<?> paramType : paramTypes) {
                JavaType type = JavaType.of(paramType);
                sb.append(type.getDescriptor());
            }
            this.methodParameter = sb.toString();
            return this;
        }

        public Building returnType(String methodReturn) {
            this.methodReturn = methodReturn;
            return this;
        }

        public Building returnType(Class<?> returnType) {
            JavaType type = JavaType.of(returnType);
            this.methodReturn = type.getDescriptor();
            return this;
        }
    }
}

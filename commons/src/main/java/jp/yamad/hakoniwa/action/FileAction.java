package jp.yamad.hakoniwa.action;

import java.nio.file.Path;

public abstract class FileAction extends IOAction<FileAction.FileOperation> {
    public static final SecurityTarget TARGET = new SecurityTarget(IOAction.TARGET, "file");

    private final Path path;

    protected FileAction(FileOperation operation, Path path) {
        super(TARGET, operation);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public static class Open extends FileAction {
        public Open(Path path) {
            super(FileOperation.OPEN, path);
        }
    }

    public static class Read extends FileAction {
        public Read(Path path) {
            super(FileOperation.READ, path);
        }
    }

    public enum FileOperation implements Operation {
        OPEN(false),
        READ(false),
        WRITE(true),

        GET_ATTR(false),
        SET_ATTR(true),

        ;

        private final boolean critical;

        FileOperation(boolean critical) {
            this.critical = critical;
        }

        public boolean isCritical() {
            return critical;
        }
    }
}

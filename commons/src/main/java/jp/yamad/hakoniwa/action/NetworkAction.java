package jp.yamad.hakoniwa.action;

import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public abstract class NetworkAction extends IOAction<NetworkAction.NetworkOperation> {
    public static final SecurityTarget TARGET = new SecurityTarget(IOAction.TARGET, "network");

    private final InetAddress address;

    protected NetworkAction(NetworkOperation operation, InetAddress address) {
        super(TARGET, operation);
        this.address = address;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public static abstract class SocketNetworkAction extends NetworkAction {
        private final Socket socket;

        protected SocketNetworkAction(NetworkOperation operation, Socket socket) {
            super(operation, socket.getRemoteSocketAddress());
            this.socket = socket;
        }
    }

    public static class Bind extends SocketNetworkAction {
        public Bind(Socket socket) {
            super(NetworkOperation.BIND, socket);
        }
    }

    public static class Connect extends SocketNetworkAction {
        public Connect(Socket socket) {
            super(NetworkOperation.CONNECT, socket);
        }
    }

    public static class Send extends NetworkAction {
        private final ByteBuffer data;

        public Send(InetAddress address, ByteBuffer data) {
            super(NetworkOperation.SEND, address);
            this.data = data;
        }

        public ByteBuffer getData() {
            return data;
        }
    }

    public static class Receive extends NetworkAction {
        private final ByteBuffer data;

        public Receive(InetAddress address, ByteBuffer data) {
            super(NetworkOperation.RECEIVE, address);
            this.data = data;
        }

        public ByteBuffer getData() {
            return data;
        }
    }

    public enum NetworkOperation implements Operation {
        BIND(false),
        CONNECT(false),
        SEND(true),
        RECEIVE(false),
        ;

        private final boolean critical;

        NetworkOperation(boolean critical) {
            this.critical = critical;
        }

        public boolean isCritical() {
            return critical;
        }
    }
}

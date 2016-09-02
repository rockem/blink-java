package org.rockm.blink.io;

public class MessageConverter {

    private final Object message;

    public MessageConverter(Object message) {
        this.message = message;
    }

    public byte[] convert() {
        byte[] bodyAsBytes;
        if(message instanceof byte[]) {
            bodyAsBytes = (byte[]) message;
        } else {
            bodyAsBytes = message.toString().getBytes();
        }
        return bodyAsBytes;
    }
}

package pro.prieran.misis.gs.hamming;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        final String originalMessage = "Какое счастье, когда есть семья, друзья, когда родные люди поздравляют. Дорогие мои, спасибо большое за внимание и теплые поздравления в этот день! Очень приятно, большое спасибо";

        final HammingEncoder hammingEncoder = new HammingEncoder(48);

        final BitSequence encodedMessage = hammingEncoder.encode(originalMessage);
        final BitSequence encodedMessageWithOneError = hammingEncoder.encodeWithNoise(originalMessage, 1);


        System.out.println("Original: " + originalMessage);
        System.out.println(" Decoded: " + hammingEncoder.decode(encodedMessage));
        System.out.println("  Errors: " + hammingEncoder.decode(encodedMessageWithOneError));
        System.out.println("Restored: " + hammingEncoder.restore(encodedMessageWithOneError));
        System.out.println();
        System.out.println(" Encoded: " + encodedMessage);
        System.out.println("1  error: " + encodedMessageWithOneError);
    }
}

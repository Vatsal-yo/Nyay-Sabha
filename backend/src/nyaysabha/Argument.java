package nyaysabha;

import java.time.Instant;
import java.util.UUID;

public class Argument {

    private final String id = UUID.randomUUID().toString(), side, text, evidence;
    private final int round;
    private final Instant createdAt = Instant.now();

    public Argument(String side, String text, String evidence, int round) {
        this.side = side;
        this.text = text;
        this.evidence = evidence;
        this.round = round;
    }

    public String getId() {
        return id;
    }

    public String getSide() {
        return side;
    }

    public String getText() {
        return text;
    }

    public String getEvidence() {
        return evidence;
    }

    public int getRound() {
        return round;
    }

    public String toJson() {
        return "{\"id\":" + Json.quote(id) + ",\"side\":" + Json.quote(side) + ",\"text\":" + Json.quote(text)
                + ",\"evidence\":" + Json.quote(evidence) + ",\"round\":" + round + ",\"createdAt\":" + Json.quote(createdAt.toString()) + "}";
    }
}

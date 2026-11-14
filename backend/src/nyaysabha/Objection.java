package nyaysabha;

import java.time.Instant;
import java.util.UUID;

public class Objection {

    private final String id = UUID.randomUUID().toString(), side, targetArgumentId, text, ruling, reason;
    private final Instant createdAt = Instant.now();

    public Objection(String side, String targetArgumentId, String text, String ruling, String reason) {
        this.side = side;
        this.targetArgumentId = targetArgumentId;
        this.text = text;
        this.ruling = ruling;
        this.reason = reason;
    }

    public String getSide() {
        return side;
    }

    public String getTargetArgumentId() {
        return targetArgumentId;
    }

    public String getText() {
        return text;
    }

    public String getRuling() {
        return ruling;
    }

    public String getReason() {
        return reason;
    }

    public String toJson() {
        return "{\"id\":" + Json.quote(id) + ",\"side\":" + Json.quote(side) + ",\"targetArgumentId\":" + Json.quote(targetArgumentId)
                + ",\"text\":" + Json.quote(text) + ",\"ruling\":" + Json.quote(ruling) + ",\"reason\":" + Json.quote(reason)
                + ",\"createdAt\":" + Json.quote(createdAt.toString()) + "}";
    }
}

package nyaysabha;

import java.util.*;

public class CaseFile {

    private final String id, title, description, type, difficulty, historicOutcome, sourceUrl, sceneImageUrl;
    private final CaseCategory category;
    private final boolean predefined;
    private final List<String> timeline, evidence, witnesses;

    public CaseFile(String id, String title, String description, CaseCategory category, boolean predefined) {
        this(id, title, description, category, predefined, "FICTIONAL", "INTERMEDIATE", "", "", "", List.of(), List.of(), List.of());
    }

    public CaseFile(String id, String title, String description, CaseCategory category, boolean predefined, String type, String difficulty, String historicOutcome, String sourceUrl, String sceneImageUrl, List<String> timeline, List<String> evidence, List<String> witnesses) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.predefined = predefined;
        this.type = type;
        this.difficulty = difficulty;
        this.historicOutcome = historicOutcome;
        this.sourceUrl = sourceUrl;
        this.sceneImageUrl = sceneImageUrl;
        this.timeline = new ArrayList<>(timeline);
        this.evidence = new ArrayList<>(evidence);
        this.witnesses = new ArrayList<>(witnesses);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public CaseCategory getCategory() {
        return category;
    }

    public boolean isPredefined() {
        return predefined;
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getHistoricOutcome() {
        return historicOutcome;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getSceneImageUrl() {
        return sceneImageUrl;
    }

    public List<String> getTimeline() {
        return timeline;
    }

    public List<String> getEvidence() {
        return evidence;
    }

    public List<String> getWitnesses() {
        return witnesses;
    }

    private String arr(List<String> list) {
        StringJoiner j = new StringJoiner(",", "[", "]");
        for (String s : list) {
            j.add(Json.quote(s));
        
        }return j.toString();
    }

    public String toJson() {
        return "{\"id\":" + Json.quote(id) + ",\"title\":" + Json.quote(title) + ",\"description\":" + Json.quote(description) + ",\"category\":" + Json.quote(category.name()) + ",\"predefined\":" + predefined + ",\"type\":" + Json.quote(type) + ",\"difficulty\":" + Json.quote(difficulty) + ",\"historicOutcome\":" + Json.quote(historicOutcome) + ",\"sourceUrl\":" + Json.quote(sourceUrl) + ",\"sceneImageUrl\":" + Json.quote(sceneImageUrl) + ",\"timeline\":" + arr(timeline) + ",\"evidence\":" + arr(evidence) + ",\"witnesses\":" + arr(witnesses) + "}";
    }
}

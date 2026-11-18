package nyaysabha;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

/** Groq-backed judge using only Java's built-in HttpClient. */
public class GroqJudgeService implements JudgeService {
    private static final String ENDPOINT = "https://api.groq.com/openai/v1/chat/completions";
    private final String apiKey;
    private final String model;
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build();

    public GroqJudgeService(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = (model == null || model.isBlank()) ? "openai/gpt-oss-120b" : model;
    }
    public static boolean isConfigured() { return System.getenv("GROQ_API_KEY") != null && !System.getenv("GROQ_API_KEY").isBlank(); }

    @Override public String evaluateRound(TrialSession trial, int round) {
        return ask("ROUND EVALUATION", trial, "Evaluate ONLY the arguments submitted in round " + round + " and do NOT give a final verdict. Output exactly these short sections, plain text (no markdown tables):\n"
            + "ROUND " + round + " REVIEW\n"
            + "Prosecution: (one sentence on strength)\n"
            + "Defence: (one sentence on strength)\n"
            + "Evidence: (one sentence describing evidence and limitations)\n"
            + "Judge's remark: (one balanced sentence)\n"
            + "Next step: (one practical point each side should address, max 2 short sentences).\n"
            + "Keep the entire response under 120 words. Do not repeat case facts or arguments verbatim. Do not use tables or extra headings. Assess evidence reliability, corroboration, contradictions, and unsupported assertions. Do not reward verbosity. Never invent statements, reports, apologies, messages, evidence, facts, or legal breaches that are not explicitly in the case file or the current-round submissions. The case-file inventory is not proof that an exhibit was actually discussed or authenticated. Evaluate what each side actually said: if an argument is vague (for example, 'X is bad') say it is vague; if a side only says 'no' or gives a bare denial, say it is unsupported. Do not strengthen either side's case for them. Clearly distinguish case background from submissions made by the lawyers.");
    }
    @Override public Objection evaluateObjection(TrialSession trial, String side, String targetId, String text) {
        String answer = ask("OBJECTION RULING", trial, "The " + side + " side objects to argument " + targetId + ". Objection stated: " + text + "\nReturn the first line exactly as SUSTAINED or OVERRULED, then give a reason in no more than 35 words and identify what material the judge will disregard or retain. If the facts do not support a confident ruling, use OVERRULED and clearly say the objection is not established on this record.");
        String ruling = answer.stripLeading().toUpperCase(Locale.ROOT).startsWith("SUSTAINED") ? "SUSTAINED" : "OVERRULED";
        return new Objection(side,targetId,text,ruling,answer);
    }
    @Override public String finalVerdict(TrialSession trial) {
        return ask("FINAL SIMULATED JUDGMENT", trial, "Both sides have rested. Return a concise final judgment using EXACTLY these sections and plain text (no markdown tables):\n"
            + "FINAL JUDGMENT\n"
            + "Issue: (one sentence)\n"
            + "Evidence considered: (up to 3 short bullets; only material actually submitted)\n"
            + "Assessment: (2-3 sentences comparing arguments and evidence, noting key gaps)\n"
            + "Decision: (criminal: CONVICTED or ACQUITTED, only if supported; civil/family: CLAIMANT/RESPONDENT or NO CLEAR FINDING; state the simulated outcome clearly)\n"
            + "Reason: (1-2 sentences explaining the decision)\n"
            + "Legal references: (only verified references if provided; otherwise say Legal citations require verification.)\n"
            + "Keep it under 250 words. Avoid repeating the case summary, arguments, or earlier round reviews. Do not invent facts or citations. For criminal cases, assess the prosecution burden and presume innocence; if the record does not prove guilt beyond reasonable doubt, acquit rather than treating a weak defence as proof. For civil/family disputes, assess the evidence and appropriate simulated civil outcome without importing the criminal burden. For a real-case replay, assess the submitted arguments independently and do not treat the historical outcome as predetermined. This is a simulation, not a real judgment.");
    }

    private String ask(String task, TrialSession t, String instruction) {
        String key = apiKey;
        if (key == null || key.isBlank()) throw new IllegalStateException("Groq is not configured. Set GROQ_API_KEY in the backend environment and restart Java.");
        String system = "You are Nyay Sabha, an educational Indian courtroom simulation judge, not a real judge or lawyer. "
            + "Keep answers VERY concise and follow the requested output structure exactly. Use plain text, short lines, and simple language. No tables, repeated summaries, decorative markdown, or extra sections. Apply a structured legal-style reasoning framework but do not cite unverified statutes. "
            + "Be neutral, careful, and distinguish allegations from established facts. Compare arguments, evidentiary support, reliability, corroboration, contradictions, gaps, and relevance. Treat the case-file evidence list as available exhibits, not automatically proven facts; arguments may refer to exhibits, but submissions do not add new evidence. For historical replay cases, never invent exhibits, witness statements, or events beyond the case record. Do not fabricate evidence, legal rules, section numbers, quotations, case law, or procedural powers. "
            + "IMPORTANT LEGAL CITATION RULE: no verified statute text is supplied in this request. Therefore do not quote or assert any BNS/BNSS/BSA section as verified. If a legal provision may be relevant, write 'LEGAL CITATION REQUIRES VERIFICATION' and identify the legal topic without a section number. BNS concerns offences and punishments; procedure and evidence may instead involve BNSS and BSA; family/civil matters may involve other statutes. Do not force BNS into non-criminal cases. "
            + "This is a simulation only, not legal advice. " + task;
        String user = "CASE CATEGORY: " + t.getCaseFile().getCategory() + "\nCASE TITLE: " + t.getCaseFile().getTitle()
            + "\nCASE FACTS: " + t.getCaseFile().getDescription() + "\nCASE TYPE: " + t.getCaseFile().getType() + "\nDIFFICULTY: " + t.getCaseFile().getDifficulty() + "\nCASE TIMELINE: " + String.join("\n", t.getCaseFile().getTimeline()) + "\nCASE EXHIBITS / EVIDENCE INVENTORY: " + String.join("\n", t.getCaseFile().getEvidence()) + "\nWITNESS INFORMATION: " + String.join("\n", t.getCaseFile().getWitnesses()) + "\nHISTORICAL OUTCOME NOTE: " + t.getCaseFile().getHistoricOutcome() + "\nPLAINTIFF/PROSECUTION: " + t.getProsecutionName()
            + "\nDEFENCE: " + t.getDefenceName() + "\nARGUMENTS:\n" + arguments(t, task.equals("ROUND EVALUATION") ? extractRound(instruction) : -1)
            + "\nOBJECTIONS:\n" + objections(t, task.equals("ROUND EVALUATION") ? extractRound(instruction) : -1) + "\nTASK: " + instruction;
        String body = "{\"model\":" + Json.quote(model) + ",\"temperature\":0.2,\"reasoning_effort\":\"low\",\"max_completion_tokens\":2200,\"messages\":["
            + "{\"role\":\"system\",\"content\":" + Json.quote(system) + "},"
            + "{\"role\":\"user\",\"content\":" + Json.quote(user) + "}]}";
        HttpRequest request = HttpRequest.newBuilder(URI.create(ENDPOINT)).timeout(Duration.ofSeconds(90))
            .header("Authorization", "Bearer " + key).header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() < 200 || response.statusCode() >= 300)
                throw new IllegalStateException("Groq API returned HTTP " + response.statusCode() + ". Check the model, key and account limits.");
            Object parsed = Json.parse(response.body());
            String finishReason = "unknown";
            if (parsed instanceof Map) {
                Map<?,?> root = (Map<?,?>) parsed;
                Object choices = root.get("choices");
                if (choices instanceof List && !((List<?>)choices).isEmpty()) {
                    Object choice = ((List<?>)choices).get(0);
                    if (choice instanceof Map) {
                        Map<?,?> choiceMap = (Map<?,?>) choice;
                        Object finish = choiceMap.get("finish_reason");
                        if (finish != null) finishReason = String.valueOf(finish);
                        Object message = choiceMap.get("message");
                        if (message instanceof Map) {
                            Object content = ((Map<?,?>)message).get("content");
                            if (content instanceof String && !((String)content).isBlank()) return ((String)content).trim();
                        }
                    }
                }
            }
            String preview = response.body().replaceAll("\\s+", " ");
            if (preview.length() > 450) preview = preview.substring(0, 450) + "...";
            throw new IllegalStateException("Groq returned no text for the judge. finish_reason=" + finishReason + ". Response preview: " + preview);
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); throw new IllegalStateException("Groq request was interrupted."); }
          catch (java.io.IOException e) { throw new IllegalStateException("Could not reach Groq. Check internet connectivity."); }
    }
    private int extractRound(String instruction) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("round (\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE).matcher(instruction);
        return m.find() ? Integer.parseInt(m.group(1)) : -1;
    }
    private String arguments(TrialSession t, int onlyRound) {
        StringBuilder b = new StringBuilder();
        for (Argument a : t.getArguments()) {
            if (onlyRound > 0 && a.getRound() != onlyRound) continue;
            b.append("[Round ").append(a.getRound()).append("] ").append(a.getSide())
             .append(": ").append(a.getText()).append(" | Evidence claimed: ")
             .append(a.getEvidence().isBlank() ? "(none stated)" : a.getEvidence()).append("\n");
        }
        return b.length() == 0 ? "(No arguments submitted in this round.)" : b.toString();
    }
    private String objections(TrialSession t, int onlyRound) {
        StringBuilder b = new StringBuilder();
        for (Objection o : t.getObjections()) {
            if (onlyRound > 0) {
                Argument target = t.getArguments().stream()
                    .filter(a -> a.getId().equals(o.getTargetArgumentId())).findFirst().orElse(null);
                if (target == null || target.getRound() != onlyRound) continue;
            }
            b.append(o.getSide()).append(" objected to ").append(o.getTargetArgumentId())
             .append(": ").append(o.getText()).append(" | Ruling: ").append(o.getRuling())
             .append(" | ").append(o.getReason()).append("\n");
        }
        return b.length() == 0 ? "(No relevant objections.)" : b.toString();
    }
}

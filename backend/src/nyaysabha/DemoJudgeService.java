package nyaysabha;

/**
 * Offline placeholder used to test the courtroom flow.
 * This is NOT an AI decision and does NOT issue legal advice.
 * Next milestone: replace with an API-backed JudgeService and verified legal citations.
 */
public class DemoJudgeService implements JudgeService {
    public String evaluateRound(TrialSession trial, int round) {
        return "DEMO FEEDBACK (not an AI ruling): Both sides should connect each factual claim to the case record, "
            + "address the opposing argument, and avoid treating an allegation as proven fact. Round " + round + " recorded.";
    }
    public Objection evaluateObjection(TrialSession trial, String side, String targetId, String text) {
        return new Objection(side,targetId,text,"PENDING_AI",
            "Objection recorded. No validity decision has been made because the AI judge is not connected yet.");
    }
    public String finalVerdict(TrialSession trial) {
        return "DEMO MODE: No winner or legal conclusion has been determined. Connect the AI judge and verified Indian legal reference database before issuing a simulated judgment.";
    }
}
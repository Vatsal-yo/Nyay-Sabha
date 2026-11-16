package nyaysabha;

public interface JudgeService {

    String evaluateRound(TrialSession trial, int round);

    Objection evaluateObjection(TrialSession trial, String side, String targetId, String text);

    String finalVerdict(TrialSession trial);
}

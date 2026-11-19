package nyaysabha;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TrialService {

    private final Map<String, TrialSession> trials = new ConcurrentHashMap<>();
    private final JudgeService judge = GroqJudgeService.isConfigured() ? new GroqJudgeService(System.getenv("GROQ_API_KEY"), System.getenv("GROQ_MODEL")) : new DemoJudgeService();
    private final List<CaseFile> library = new ArrayList<>();
    private static final String SCENE = "https://images.unsplash.com/photo-1560520653-9e0e4c89e5f2?auto=format&fit=crop&w=1200&q=80";

    public TrialService() {
        add("CR-001", "The Missing Evidence", "A late-night shop break-in. A witness saw a person nearby but could not identify them. The investigation record contains no confirmed fingerprints or recovered property.", CaseCategory.CRIMINAL, "FICTIONAL", "EASY", List.of("22:15 — Shop closes and alarm is set", "22:50 — Neighbour reports movement near the rear lane", "07:00 — Owner discovers break-in"), List.of("E1 · Witness says they noticed a figure but distance and lighting limited visibility", "E2 · Shop alarm log shows a short interruption; source of interruption is unknown", "E3 · No recovered property or confirmed forensic match in the file"), List.of("W1 · Neighbour: saw a figure from across the lane, cannot identify", "W2 · Shop owner: confirms missing stock and alarm status"));
        add("CR-002", "The Disputed Phone", "A phone is reported missing after a disagreement. It is found in a shared corridor later that day. Several residents had access; ownership and possession are disputed.", CaseCategory.CRIMINAL, "FICTIONAL", "EASY", List.of("09:00 — Disagreement in apartment lobby", "11:30 — Phone reported missing", "14:00 — Phone found in common corridor"), List.of("E1 · Phone recovered from shared area", "E2 · Purchase receipt submitted, serial number partly obscured", "E3 · No CCTV footage covering the corridor"), List.of("W1 · Resident heard argument but did not see phone taken", "W2 · Building caretaker found phone in corridor"));
        add("CR-003", "The Midnight Break-In", "A jewellery shop is burgled between 10:30 and 11:15 PM. A former employee is accused; the defence says another person had access and the investigation is incomplete.", CaseCategory.CRIMINAL, "FICTIONAL", "INTERMEDIATE", List.of("20:00 — Former employee's shift ends", "22:30 — Shop alarm is armed", "22:50 — Motion alert near rear entrance", "07:10 — Missing jewellery discovered"), List.of("E1 · Partial fingerprint report: inconclusive, no individual attribution", "E2 · CCTV shows helmeted person; face not visible", "E3 · Phone location places device in nearby area with broad accuracy", "E4 · Rear door lock report notes prior maintenance issue", "E5 · Chain-of-custody log has a gap for one item"), List.of("W1 · Guard: saw a person at a distance, uncertain identification", "W2 · Store manager: confirms stock loss and employee access history", "W3 · Technician: explains camera coverage and blind spot"));
        add("CY-001", "The Phishing Transfer", "A small business loses money after an employee receives a convincing payment-change email. The accused contractor disputes authorship and access to the account.", CaseCategory.CYBERCRIME, "FICTIONAL", "INTERMEDIATE", List.of("09:12 — Vendor payment email received", "09:40 — Bank details changed in internal system", "10:05 — Transfer approved", "12:30 — Vendor reports no payment received"), List.of("E1 · Email header extract has incomplete routing information", "E2 · Login record shows shared office IP", "E3 · Bank transfer confirmation", "E4 · Audit log shows a change but user attribution is uncertain"), List.of("W1 · Accounts employee: followed email instructions", "W2 · IT administrator: describes shared credentials", "W3 · Vendor: confirms original bank details"));
        add("FO-001", "The Fingerprint Dispute", "A fingerprint is recovered from a glass at a scene. The expert report states a partial print with limited ridge detail; the parties dispute how much it can establish.", CaseCategory.FORENSIC, "FICTIONAL", "ADVANCED", List.of("18:00 — Gathering begins", "20:15 — Incident reported", "21:00 — Glass collected and sealed", "Next day — Partial print examination completed"), List.of("E1 · Partial latent print image, low clarity", "E2 · Examiner report notes limited comparison detail", "E3 · Collection log has time discrepancy", "E4 · No DNA result included in case record"), List.of("W1 · Scene officer explains collection process", "W2 · Forensic examiner describes limitations and conclusion wording"));
        add("FA-001", "The Custody Dispute", "Separated parents disagree about a child's living arrangements, schooling and routine. Both propose arrangements and present information about stability and welfare.", CaseCategory.FAMILY, "FICTIONAL", "INTERMEDIATE", List.of("Six months ago — Parents separated", "Last month — School attendance concerns raised", "This week — Both propose different schedules"), List.of("E1 · School attendance summary", "E2 · Proposed weekly schedules from both parents", "E3 · Child routine notes, source and completeness disputed"), List.of("W1 · Teacher: describes attendance and classroom routine", "W2 · Relative: describes home routine but has limited direct knowledge"));
        add("FA-002", "Huang Marriage — Hypothetical Property Division", "HYPOTHETICAL EDUCATIONAL CASE — Public reporting says Jensen Huang acknowledged infidelity and apologized to his wife Lori. As of the cited November 2026 reporting, no divorce filing or announced separation was reported. This fictional exercise imagines a later divorce petition and dispute over Nvidia shares, trusts and other assets. It does not claim that Lori Huang has filed for half his fortune. The couple live in California, so this scenario concerns California community-property concepts and is not an application of Indian divorce law.", CaseCategory.FAMILY, "HYPOTHETICAL CASE INSPIRED BY PUBLIC REPORTING", "ADVANCED", List.of("1985 — Jensen and Lori Huang marry", "1993 — Jensen Huang co-founds Nvidia during the marriage", "November 2026 — Jensen publicly acknowledges infidelity and apologizes", "Hypothetical future — One spouse files for divorce and requests an equal division of the community estate"), List.of("E1 · Public reporting of Jensen Huang's statement acknowledging infidelity", "E2 · Nvidia proxy disclosures concerning shares held through family trusts and shared beneficial interests", "E3 · Hypothetical asset schedule distinguishing premarital property, marital acquisitions and trust-held assets", "E4 · No divorce petition, settlement demand or court ruling is included in the public record cited here; players must not treat a 50% claim over all wealth as established"), List.of("W1 · Financial expert: explains how share acquisition dates and trust structures may affect classification", "W2 · Family-law expert: explains California community-property principles", "W3 · Each spouse's position is represented as a hypothetical statement, not an actual quote"), "https://fortune.com/2026/11/14/jensen-huang-divorce-lori-huang-nvidia-fortune-community-property/", SCENE);
        add("FA-003", "ScoutOP and Kaashvi — Hypothetical Relationship Breakdown", "HYPOTHETICAL EDUCATIONAL CASE — News coverage in October 2026 described Kaashvi Hiranandani confronting her partner ScoutOP about his closeness with Love Gill on Bigg Boss 20. ScoutOP described the bond as sibling-like, and Kaashvi later apologized for comments she said were insensitive. Public reporting identifies Kaashvi as ScoutOP's girlfriend/partner, not his wife; it does not establish that they are married or that a divorce has been filed. This fictionalized family-law exercise imagines a couple who are legally married and asks how trust, public conduct and reconciliation might be discussed. It does not assert that these public figures are married or divorcing.", CaseCategory.FAMILY, "HYPOTHETICAL CASE INSPIRED BY PUBLIC REPORTING", "INTERMEDIATE", List.of("Bigg Boss 20 — ScoutOP and Love Gill develop a close on-screen friendship", "October 25, 2026 — Kaashvi confronts ScoutOP about the friendship during Weekend Ka Vaar", "October 26, 2026 — Reports describe Kaashvi apologizing for insensitive remarks", "Hypothetical — A married couple with similar facts seeks advice about separation, reconciliation and family arrangements"), List.of("E1 · News report describing the televised confrontation and each person's stated position", "E2 · News report describing Kaashvi's subsequent apology", "E3 · No marriage certificate, divorce petition or evidence of an actual divorce is part of this scenario", "E4 · Players must distinguish public perception and jealousy from proof of adultery or other legally relevant conduct"), List.of("W1 · Spouse A: hypothetical account of feeling hurt by public conduct", "W2 · Spouse B: hypothetical account that the friendship was platonic and sibling-like", "W3 · Counsellor: discusses communication, boundaries and possible reconciliation; not a witness to the real events"), "https://www.ndtv.com/entertainment/bigg-boss-20-weekend-ka-vaar-scoutop-s-girlfriend-kaashvi-calls-him-out-for-his-bond-with-love-gill-12232328", SCENE);
        add("CI-001", "The Boundary Wall", "Two neighbours dispute a wall's location. Old photographs, informal measurements and different property descriptions conflict.", CaseCategory.CIVIL, "FICTIONAL", "EASY", List.of("2018 — Earlier wall visible in photograph", "2023 — Repairs begin", "2024 — New wall constructed", "Current — Neighbour raises dispute"), List.of("E1 · Old photo with unclear date", "E2 · Hand-drawn measurement sketch", "E3 · Property descriptions differ", "E4 · No licensed survey included"), List.of("W1 · Mason: recalls construction instructions", "W2 · Neighbour: describes previous boundary use"));
        add("EN-001", "The River Discharge", "Residents allege a factory released waste into a river. The factory disputes source attribution and says samples were collected after rainfall.", CaseCategory.ENVIRONMENTAL, "FICTIONAL", "INTERMEDIATE", List.of("06:30 — Residents notice discoloration", "08:00 — Sample collected downstream", "10:00 — Factory denies discharge", "Next day — Lab report issued"), List.of("E1 · Downstream sample report, no upstream comparison", "E2 · Rainfall recorded overnight", "E3 · Factory discharge log supplied by operator", "E4 · Sample collection documentation incomplete"), List.of("W1 · Resident: saw discoloration, not discharge source", "W2 · Lab analyst: explains sample limitations", "W3 · Plant operator: explains process and log"));
        add("CO-001", "The Missing Ledger", "A company alleges a manager manipulated expense records. The manager says approvals were shared and the records were edited during routine reconciliation.", CaseCategory.CORPORATE, "FICTIONAL", "ADVANCED", List.of("Quarter start — Shared approval account created", "Month end — Expense entries edited", "Audit — Irregular totals flagged", "Interview — Manager denies sole access"), List.of("E1 · Spreadsheet audit trail with shared account", "E2 · Approval emails with missing attachments", "E3 · Expense receipts, several unreadable", "E4 · Internal audit summary"), List.of("W1 · Auditor: explains anomaly detection", "W2 · IT staff: confirms shared account access", "W3 · Manager: describes reconciliation process"));
        add("MV-001", "The Junction Collision", "A collision occurs at an intersection. Each driver says the other entered against the signal. A dashcam clip is incomplete and timing is disputed.", CaseCategory.MOTOR_VEHICLE, "FICTIONAL", "INTERMEDIATE", List.of("17:42 — Collision reported", "17:50 — Traffic officer arrives", "18:20 — Dashcam file copied", "Two days later — Repair estimates submitted"), List.of("E1 · Dashcam clip ends before impact", "E2 · Road sketch, not to scale", "E3 · Vehicle damage photos", "E4 · Signal timing log requested but not supplied"), List.of("W1 · Pedestrian: view partly blocked", "W2 · Officer: describes scene on arrival", "W3 · Driver A and Driver B give conflicting accounts"));
        add("CT-001", "The Speech Ban Challenge", "A student group challenges a university restriction on a public event. The university cites safety concerns; the group argues the restriction is broad and inconsistently applied.", CaseCategory.CONSTITUTIONAL, "FICTIONAL", "ADVANCED", List.of("Day 1 — Event application submitted", "Day 3 — Restriction order issued", "Day 4 — Student group appeals internally", "Day 7 — Hearing simulation begins"), List.of("E1 · Restriction order with broad wording", "E2 · Safety assessment summary, no detailed risk matrix", "E3 · Prior event approvals show inconsistent conditions"), List.of("W1 · Student organiser: describes planned event", "W2 · Campus officer: explains safety concerns", "W3 · Administrator: describes decision process"));
        add("REAL-001", "Jessica Lall Case — Replay the Trial", "Historical case replay. Jessica Lall was shot at a Delhi restaurant in April 1999. The trial court acquitted Manu Sharma in 2006; Delhi High Court convicted him in December 2006; the Supreme Court upheld the conviction in April 2010. Use the documented record only; this replay is not a re-creation of the actual judgment.", CaseCategory.CRIMINAL, "REAL CASE REPLAY", "ADVANCED", List.of("29 April 1999 — Shooting at restaurant", "21 February 2006 — Trial court acquittal", "December 2006 — Delhi High Court conviction", "19 April 2010 — Supreme Court upholds conviction"), List.of("Historical record summary: eyewitness testimony and identification", "Historical record summary: witness credibility and hostile witnesses", "Historical record summary: forensic/ballistic evidence and vehicle-related evidence; consult source record for precise scope", "Do not invent additional exhibits or testimony in this replay"), List.of("Historical witnesses are discussed in source reporting and judgments; this game does not simulate any real witness's private knowledge or voice"), "https://api.sci.gov.in/jonew/judis/31518.pdf", "https://images.unsplash.com/photo-1575505586569-646b2ca8b172?auto=format&fit=crop&w=1200&q=80");
        add("REAL-002", "Aarushi–Hemraj Case — Evidence Review", "Historical case replay. A 2013 trial court convicted Rajesh and Nupur Talwar; the Allahabad High Court acquitted them in October 2017, finding the circumstantial evidence insufficient. The acquittal is the outcome of the High Court appeal. This scenario is an educational evidence review, not a re-judgment of real people.", CaseCategory.CRIMINAL, "REAL CASE REPLAY", "ADVANCED", List.of("May 2008 — Aarushi and Hemraj deaths discovered", "November 2013 — Trial court conviction", "12 October 2017 — Allahabad High Court acquittal on appeal"), List.of("Historical record summary: circumstantial evidence", "Historical record summary: investigation gaps and competing inferences", "No invented forensic exhibits; players should argue only from the record summary and cited source"), List.of("Witnesses and statements are not recreated as AI characters in this historical replay"), "https://indianexpress.com/article/india/2008-aarushi-murder-case-allahabad-high-court-acquits-talwars-4886927/", "https://images.unsplash.com/photo-1593113598332-cd288d649433?auto=format&fit=crop&w=1200&q=80");
    }

    private void add(String id, String title, String desc, CaseCategory cat, String type, String diff, List<String> timeline, List<String> evidence, List<String> witnesses) {
        add(id, title, desc, cat, type, diff, timeline, evidence, witnesses, "", SCENE);
    }

    private void add(String id, String title, String desc, CaseCategory cat, String type, String diff, List<String> timeline, List<String> evidence, List<String> witnesses, String source, String image) {
        library.add(new CaseFile(id, title, desc, cat, true, type, diff, type.equals("REAL CASE REPLAY") ? "Historical outcomes are shown in the case record; this simulation does not alter real judgments." : "", source, image, timeline, evidence, witnesses));
    }

    public synchronized String cases(String category) {
        StringJoiner j = new StringJoiner(",", "[", "]");
        for (CaseFile c : library) {
            if (category == null || category.isBlank() || c.getCategory().name().equalsIgnoreCase(category)) {
                j.add(c.toJson());

            }
        }
        return j.toString();
    }

    public String start(Map<String, String> d) {
        String p = required(d, "prosecutionName"), f = required(d, "defenceName");
        CaseFile selected = null;
        String id = d.getOrDefault("caseId", "");
        for (CaseFile c : library) {
            if (c.getId().equals(id)) {
                selected = c;

            }
        }
        if (selected == null) {
            selected = new CaseFile(UUID.randomUUID().toString(), required(d, "caseTitle"), required(d, "caseDescription"), CaseCategory.parse(d.get("category")), false, "CUSTOM", "USER CREATED", "", "", "", lines(d.get("timeline")), lines(d.get("evidenceList")), lines(d.get("witnessList")));
            synchronized (this) {
                library.add(selected);
            }
        }
        TrialSession t = new TrialSession(UUID.randomUUID().toString(), p, f, selected);
        trials.put(t.getId(), t);
        return t.toJson();
    }

    private List<String> lines(String s) {
        if (s == null || s.isBlank()) {
            return List.of();

        }
        List<String> l = new ArrayList<>();
        for (String line : s.split("\\r?\\n")) {
            if (!line.isBlank()) {
                l.add(line.trim());

            }
        }
        return l;
    }

    public String addArgument(Map<String, String> d) {
        TrialSession t = get(d);
        String side = side(d);
        int round = parseRound(d.getOrDefault("round", "1"));
        Argument a = t.addArgument(side, required(d, "text"), d.getOrDefault("evidence", "").trim(), round);
        return "{\"argument\":" + a.toJson() + ",\"trial\":" + t.toJson() + "}";
    }

    public String addObjection(Map<String, String> d) {
        TrialSession t = get(d);
        Objection o = t.addObjection(side(d), required(d, "targetArgumentId"), required(d, "text"), judge);
        return "{\"objection\":" + o.toJson() + ",\"trial\":" + t.toJson() + "}";
    }

    public String evaluateRound(Map<String, String> d) {
        TrialSession t = get(d);
        String r = t.evaluateRound(judge);
        return "{\"remark\":" + Json.quote(r) + ",\"trial\":" + t.toJson() + "}";
    }

    public String rest(Map<String, String> d) {
        TrialSession t = get(d);
        t.rest(side(d));
        return t.toJson();
    }

    public String finish(Map<String, String> d) {
        TrialSession t = get(d);
        t.finish(judge);
        return t.toJson();
    }

    private TrialSession get(Map<String, String> d) {
        TrialSession t = trials.get(required(d, "trialId"));
        if (t == null) {
            throw new IllegalArgumentException("Courtroom not found or server restarted.");

        }
        return t;
    }

    private String side(Map<String, String> d) {
        String s = required(d, "side").toUpperCase();
        if (!s.equals("PROSECUTION") && !s.equals("DEFENCE")) {
            throw new IllegalArgumentException("Side must be PROSECUTION or DEFENCE.");

        }
        return s;
    }

    private int parseRound(String s) {
        try {
            int n = Integer.parseInt(s);
            if (n < 1) {
                throw new Exception();

            }
            return n;
        } catch (Exception e) {
            throw new IllegalArgumentException("Round must be a positive number.");
        }
    }

    private String required(Map<String, String> d, String k) {
        String v = d.get(k);
        if (v == null || v.isBlank()) {
            throw new IllegalArgumentException(k + " is required.");

        }
        return v.trim();
    }
}

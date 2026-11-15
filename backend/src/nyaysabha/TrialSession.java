package nyaysabha;

import java.util.*;

public class TrialSession {
    private final String id, prosecutionName, defenceName;
    private final CaseFile caseFile;
    private final List<Argument> arguments=new ArrayList<>();
    private final List<Objection> objections=new ArrayList<>();
    private final Map<Integer,String> remarks=new TreeMap<>();
    private int currentRound=1;
    private boolean finished=false;
    private final Set<String> restedSides=new HashSet<>();
    private String verdict="";

    public TrialSession(String id,String prosecutionName,String defenceName,CaseFile caseFile){
        this.id=id; this.prosecutionName=prosecutionName; this.defenceName=defenceName; this.caseFile=caseFile;
    }
    public synchronized Argument addArgument(String side,String text,String evidence,int round){
        ensureOpen();
        if(round!=currentRound) throw new IllegalArgumentException("This is not the active round.");
        if(restedSides.contains(side)) throw new IllegalArgumentException("This side has already rested its case.");
        Argument a=new Argument(side,text,evidence,round); arguments.add(a); return a;
    }
    public synchronized Objection addObjection(String side,String targetId,String text,JudgeService judge){
        ensureOpen();
        if(restedSides.contains(side)) throw new IllegalArgumentException("This side has already rested its case.");
        Argument target=arguments.stream().filter(a->a.getId().equals(targetId)).findFirst()
            .orElseThrow(()->new IllegalArgumentException("The challenged argument was not found."));
        if(target.getSide().equals(side)) throw new IllegalArgumentException("You cannot object to your own side's argument.");
        Objection o=judge.evaluateObjection(this,side,targetId,text); objections.add(o); return o;
    }
    public synchronized String evaluateRound(JudgeService judge){
        ensureOpen();
        boolean hasCurrentRoundArguments = arguments.stream().anyMatch(a -> a.getRound() == currentRound);
        if (!hasCurrentRoundArguments) throw new IllegalArgumentException("Submit at least one argument in round " + currentRound + " before asking the judge to evaluate it.");
        String remark=judge.evaluateRound(this,currentRound);
        remarks.put(currentRound,remark);
        currentRound++;
        return remark;
    }
    public synchronized void rest(String side) {
        ensureOpen();
        restedSides.add(side);
    }
    public synchronized void finish(JudgeService judge){
        ensureOpen();
        if (!restedSides.contains("PROSECUTION") || !restedSides.contains("DEFENCE"))
            throw new IllegalArgumentException("Both sides must formally rest their case before the final verdict.");
        verdict=judge.finalVerdict(this); finished=true;
    }
    private void ensureOpen(){ if(finished) throw new IllegalArgumentException("This trial has already finished."); }
    public String getId(){return id;}
    public String getProsecutionName(){return prosecutionName;}
    public String getDefenceName(){return defenceName;}
    public CaseFile getCaseFile(){return caseFile;}
    public synchronized boolean isRested(String side){return restedSides.contains(side);}
    public synchronized boolean areBothRested(){return restedSides.contains("PROSECUTION") && restedSides.contains("DEFENCE");}
    public int getCurrentRound(){return currentRound;}
    public synchronized List<Argument> getArguments(){return Collections.unmodifiableList(new ArrayList<>(arguments));}
    public synchronized List<Objection> getObjections(){return Collections.unmodifiableList(new ArrayList<>(objections));}
    public synchronized String toJson(){
        StringJoiner args=new StringJoiner(",","[","]"); for(Argument a:arguments)args.add(a.toJson());
        StringJoiner obs=new StringJoiner(",","[","]"); for(Objection o:objections)obs.add(o.toJson());
        StringJoiner evals=new StringJoiner(",","[","]");
        for(Map.Entry<Integer,String> e:remarks.entrySet())
            evals.add("{\"round\":"+e.getKey()+",\"remark\":"+Json.quote(e.getValue())+"}");
        return "{\"id\":"+Json.quote(id)+",\"prosecutionName\":"+Json.quote(prosecutionName)
            +",\"defenceName\":"+Json.quote(defenceName)+",\"case\":"+caseFile.toJson()
            +",\"currentRound\":"+currentRound+",\"finished\":"+finished+",\"prosecutionRested\":"+restedSides.contains("PROSECUTION")+",\"defenceRested\":"+restedSides.contains("DEFENCE")+",\"arguments\":"+args
            +",\"objections\":"+obs+",\"evaluations\":"+evals+",\"finalVerdict\":"+Json.quote(verdict)+"}";
    }
}
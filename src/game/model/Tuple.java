package game.model;

public class Tuple<FST, SND> {
    private FST fst;
    private SND snd;
    
    public Tuple (FST fst, SND snd){
        this.fst = fst;
        this.snd = snd;
    }
    
    public FST fst(){
        return this.fst;
    }
    
    public SND snd(){
        return this.snd;
    }
    
    public void set(FST fst, SND snd){
        this.fst = fst;
        this.snd = snd;
    }
    
    public void setFst(FST fst){
        this.fst=fst;
    }
    
    public void setSnd(SND snd){
        this.snd = snd;
    }
    
    @Override
    public boolean equals(Object o){
        if(o.getClass().equals(this.getClass())){
            Tuple temp = (Tuple) o;
            if(temp.fst == this.fst && temp.snd == this.snd){
                return true;
            }
        }
        return false;
    }
}

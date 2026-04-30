package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;

public class TermTuple extends AbstractTermTuple {

    public TermTuple() {
        super();
    }

    public TermTuple(AbstractTerm term, int curPos) {
        this.term = term;
        this.curPos = curPos;
    }

    @Override
    public boolean equals(Object obj) {
//        AbstractTermTuple att = (AbstractTermTuple) obj;
//        if(att.term == this.term && att.freq == this.freq && att.curPos == this.curPos) return true;
//        else return false;

        if (this == obj) return true;
        if (!(obj instanceof TermTuple)) return false;

        TermTuple other = (TermTuple) obj;

        boolean termEqual = (this.term == null && other.term == null) || (this.term != null && this.term.equals(other.term));

        return termEqual && this.freq == other.freq && this.curPos == other.curPos;
    }

    @Override
    public String toString() {
        return "TermTuple(term=" + this.term + ", freq=" + this.freq + ", curPos=" + this.curPos + ")";
    }

}

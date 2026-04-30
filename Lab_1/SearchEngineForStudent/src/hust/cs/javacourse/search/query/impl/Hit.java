package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;

import java.util.Map;

public class Hit extends AbstractHit {
    public Hit() {
        super();
    }

    public Hit(int docId, String docPath) {
        super(docId, docPath);
    }

    public Hit(int docId, String docPath, Map<AbstractTerm, AbstractPosting> termPostingMapping) {
        super(docId, docPath, termPostingMapping);
    }

    @Override
    public int getDocId() {
        return docId;
    }

    @Override
    public String getDocPath() {
        return docPath;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public Map<AbstractTerm, AbstractPosting> getTermPostingMapping() {
        return termPostingMapping;
    }

    @Override
    public String toString() {
        return "docId=" + docId + ", score=" + score + ", docPath=" + docPath + ", content =" + content + ", termPostingMapping=" + termPostingMapping;
    }

    @Override
    public int compareTo(AbstractHit o) {
        return (int) (this.score - o.getScore());
//        int scoreCmp = Double.compare(o.getScore(), this.score);
//        if (scoreCmp != 0) {
//            return scoreCmp;
//        }
//        return Integer.compare(this.docId, o.getDocId());
    }
}

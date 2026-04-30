package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.util.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IndexSearcher extends AbstractIndexSearcher {
    @Override
    public void open(String indexFile) {
        index.load(new File(indexFile));
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractTerm normalized = normalizeTerm(queryTerm);
        AbstractPostingList postingList = index.search(normalized);
        if (postingList == null || postingList.isEmpty()) {
            return new AbstractHit[0];
        }

        List<AbstractHit> hits = new ArrayList<>();
        for (int i = 0; i < postingList.size(); i++) {
            AbstractPosting posting = postingList.get(i);
            Map<AbstractTerm, AbstractPosting> mapping = new LinkedHashMap<>();
            mapping.put(normalized, posting);
            hits.add(new Hit(posting.getDocId(), index.getDocName(posting.getDocId()), mapping));
        }
        sorter.sort(hits);
        return hits.toArray(new AbstractHit[0]);
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        AbstractTerm t1 = normalizeTerm(queryTerm1);
        AbstractTerm t2 = normalizeTerm(queryTerm2);
        AbstractPostingList p1 = index.search(t1);
        AbstractPostingList p2 = index.search(t2);

        Map<Integer, Hit> hitMap = new LinkedHashMap<>();
        if (combine == LogicalCombination.OR) {
            collectOrHits(hitMap, t1, p1);
            collectOrHits(hitMap, t2, p2);
        } else {
            collectAndHits(hitMap, t1, p1, t2, p2);
        }

        List<AbstractHit> hits = new ArrayList<>(hitMap.values());
        sorter.sort(hits);
        return hits.toArray(new AbstractHit[0]);
    }

    private void collectOrHits(Map<Integer, Hit> hitMap, AbstractTerm term, AbstractPostingList postingList) {
        if (postingList == null) {
            return;
        }
        for (int i = 0; i < postingList.size(); i++) {
            AbstractPosting posting = postingList.get(i);
            int docId = posting.getDocId();
            Hit hit = hitMap.get(docId);
            if (hit == null) {
                hit = new Hit(docId, index.getDocName(docId));
                hitMap.put(docId, hit);
            }
            hit.getTermPostingMapping().put(term, posting);
        }
    }

    private void collectAndHits(
            Map<Integer, Hit> hitMap,
            AbstractTerm t1,
            AbstractPostingList p1,
            AbstractTerm t2,
            AbstractPostingList p2
    ) {
        if (p1 == null || p2 == null) {
            return;
        }

        for (int i = 0; i < p1.size(); i++) {
            AbstractPosting posting1 = p1.get(i);
            int docId = posting1.getDocId();
            int idx = p2.indexOf(docId);
            if (idx >= 0) {
                AbstractPosting posting2 = p2.get(idx);
                Map<AbstractTerm, AbstractPosting> mapping = new LinkedHashMap<>();
                mapping.put(t1, posting1);
                mapping.put(t2, posting2);
                hitMap.put(docId, new Hit(docId, index.getDocName(docId), mapping));
            }
        }
    }

    private AbstractTerm normalizeTerm(AbstractTerm term) {
        if (term == null) {
            return null;
        }
        String content = term.getContent();
        if (content == null) {
            return new Term(null);
        }
        if (Config.IGNORE_CASE) {
            return new Term(content.toLowerCase());
        }
        return new Term(content);
    }
}

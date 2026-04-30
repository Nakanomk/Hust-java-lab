package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.StopWords;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StopWordTermTupleFilter extends AbstractTermTupleFilter {
    private static final Set<String> STOP_WORD_SET = new HashSet<>(Arrays.asList(StopWords.STOP_WORDS));

    public StopWordTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple tuple;
        while ((tuple = input.next()) != null) {
            String content = tuple.term == null ? null : tuple.term.getContent();
            if (content == null || !STOP_WORD_SET.contains(content)) {
                return tuple;
            }
        }
        return null;
    }
}

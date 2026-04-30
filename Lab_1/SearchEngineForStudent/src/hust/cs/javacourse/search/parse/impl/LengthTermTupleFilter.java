package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

public class LengthTermTupleFilter extends AbstractTermTupleFilter {

    public LengthTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple tuple;
        while ((tuple = input.next()) != null) {
            String content = tuple.term == null ? null : tuple.term.getContent();
            if (content == null) {
                continue;
            }
            int len = content.length();
            if (len >= Config.TERM_FILTER_MINLENGTH && len <= Config.TERM_FILTER_MAXLENGTH) {
                return tuple;
            }
        }
        return null;
    }
}

package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

import java.util.regex.Pattern;

public class PatternTermTupleFilter extends AbstractTermTupleFilter {
    private final Pattern pattern;

    public PatternTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
        this.pattern = Pattern.compile(Config.TERM_FILTER_PATTERN);
    }

    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple tuple;
        while ((tuple = input.next()) != null) {
            if (tuple.term != null && tuple.term.getContent() != null
                    && pattern.matcher(tuple.term.getContent()).matches()) {
                return tuple;
            }
        }
        return null;
    }
}

package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TermTupleScanner extends AbstractTermTupleScanner {
    private final StringSplitter splitter = new StringSplitter();
    private Iterator<String> currentLineTerms = Collections.emptyIterator();
    private int curPos = 0;

    public TermTupleScanner(BufferedReader input) {
        super(input);
        splitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
    }

    @Override
    public AbstractTermTuple next() {
        try {
            while (true) {
                if (currentLineTerms.hasNext()) {
                    String token = currentLineTerms.next();
                    if (Config.IGNORE_CASE) {
                        token = token.toLowerCase();
                    }
                    TermTuple tuple = new TermTuple(new Term(token), curPos);
                    curPos++;
                    return tuple;
                }

                String line = input.readLine();
                if (line == null) {
                    return null;
                }
                List<String> terms = splitter.splitByRegex(line);
                currentLineTerms = terms.iterator();
            }
        } catch (IOException e) {
            throw new RuntimeException("Read document failed in TermTupleScanner.", e);
        }
    }
}

package com.jiganaut.bonsai.parser.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.RuleVisitor;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class Context implements Tokenizer {

    /**
     * 
     * @author Junji Mikami
     */
    private static record Cache(long lineNumber, long index, String name, String value) {
    }

    /**
     * 
     * @author Junji Mikami
     */
    private static class Position {
        int pos;
        int count;

        void set(int p) {
            this.pos = p;
        }

        int get() {
            return pos;
        }

        int getAndIncrement() {
            return pos++;
        }

        int decrementAndGet() {
            assert 0 < pos;
            return --pos;
        }

        boolean hasRemaining(List<?> list) {
            return pos < list.size();
        }

        boolean hasPrevious() {
            return 0 < pos;
        }

        void mark(boolean flag) {
            if (flag) {
                count++;
            } else if (0 < count) {
                count--;
            }
        }

        boolean isMarked() {
            return 0 < count;
        }
    }

    static final Rule EOF = new Rule() {

        @Override
        public Kind getKind() {
            throw new AssertionError();
        }

        @Override
        public <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
            throw new AssertionError();
        }
    };

    private final Grammar grammar;
    private final Production production;
    private final Tokenizer tokenizer;
    private final Set<Rule> followSet;

    private final List<Cache> cacheList;
    private final Position position;

    Context(Grammar grammar, Tokenizer tokenizer) {
        this(grammar, null, tokenizer, Set.of(EOF), new LinkedList<>(), new Position());
    }

    private Context(
            Grammar grammar,
            Production production,
            Tokenizer tokenizer,
            Set<Rule> followSet,
            List<Cache> cacheList,
            Position position) {
        assert grammar != null;
        assert tokenizer != null;
        assert followSet != null;
        this.grammar = grammar;
        this.production = production;
        this.tokenizer = tokenizer;
        this.followSet = followSet;
        this.cacheList = cacheList;
        this.position = position;
    }

    Context withProduction(Production production) {
        return new Context(
                this.grammar,
                production,
                this.tokenizer,
                this.followSet,
                this.cacheList,
                this.position);
    }

    Context withFollowSet(Set<Rule> followSet) {
        return new Context(
                this.grammar,
                this.production,
                this.tokenizer,
                followSet,
                this.cacheList,
                this.position);
    }

    Grammar grammar() {
        return grammar;
    }

    Production production() {
        return production;
    }

    Set<Rule> followSet() {
        return followSet;
    }

    @Override
    public boolean hasNext() {
        return position.hasRemaining(cacheList) || tokenizer.hasNext();
    }

    @Override
    public boolean hasNextName(String name) {
        if (position.hasRemaining(cacheList)) {
            var next = cacheList.get(position.get());
            return Objects.equals(name, next.name());
        }
        return tokenizer.hasNextName(name);
    }

    @Override
    public boolean hasNextValue(Pattern pattern) {
        if (position.hasRemaining(cacheList)) {
            var next = cacheList.get(position.get());
            return pattern.matcher(next.value()).matches();
        }
        return tokenizer.hasNextValue(pattern);
    }

    @Override
    public boolean hasNextValue(String regex) {
        return hasNextValue(Pattern.compile(regex));
    }

    @Override
    public Token next() {
        if (!position.isMarked() && position.hasPrevious()) {
            cacheList.remove(position.decrementAndGet());
        }
        if (position.hasRemaining(cacheList)) {
            var next = cacheList.get(position.getAndIncrement());
            return new DefaultToken(next.name(), next.value());
        }
        var token = tokenizer.next();
        if (position.isMarked()) {
            var cache = new Cache(
                    tokenizer.getLineNumber(),
                    tokenizer.getIndex(),
                    tokenizer.getName(),
                    tokenizer.getValue());
            cacheList.add(position.getAndIncrement(), cache);
        }
        return token;
    }

    @Override
    public String nextName() {
        if (!position.isMarked() && position.hasPrevious()) {
            cacheList.remove(position.decrementAndGet());
        }
        if (position.hasRemaining(cacheList)) {
            var next = cacheList.get(position.getAndIncrement());
            return next.name();
        }
        var name = tokenizer.nextName();
        if (position.isMarked()) {
            var cache = new Cache(
                    tokenizer.getLineNumber(),
                    tokenizer.getIndex(),
                    tokenizer.getName(),
                    tokenizer.getValue());
            cacheList.add(position.getAndIncrement(), cache);
        }
        return name;
    }

    @Override
    public String nextValue() {
        if (!position.isMarked() && position.hasPrevious()) {
            cacheList.remove(position.decrementAndGet());
        }
        if (position.hasRemaining(cacheList)) {
            var next = cacheList.get(position.getAndIncrement());
            return next.value();
        }
        var value = tokenizer.nextValue();
        if (position.isMarked()) {
            var cache = new Cache(
                    tokenizer.getLineNumber(),
                    tokenizer.getIndex(),
                    tokenizer.getName(),
                    tokenizer.getValue());
            cacheList.add(position.getAndIncrement(), cache);
        }
        return value;
    }

    @Override
    public String getName() {
        if (position.hasPrevious()) {
            var previous = cacheList.get(position.get() - 1);
            return previous.name();
        }
        return tokenizer.getName();
    }

    @Override
    public String getValue() {
        if (position.hasPrevious()) {
            var previous = cacheList.get(position.get() - 1);
            return previous.value();
        }
        return tokenizer.getValue();
    }

    @Override
    public long getLineNumber() {
        if (position.hasPrevious()) {
            var previous = cacheList.get(position.get() - 1);
            return previous.lineNumber();
        }
        return tokenizer.getLineNumber();
    }

    @Override
    public long getIndex() {
        if (position.hasPrevious()) {
            var previous = cacheList.get(position.get() - 1);
            return previous.index();
        }
        return tokenizer.getIndex();
    }

    @Override
    public void close() throws IOException {
        tokenizer.close();
    }

    int mark() {
        position.mark(true);
        return position.get();
    }

    void reset(int position) {
        this.position.set(position);
    }

    void clear() {
        position.mark(false);
    }
}

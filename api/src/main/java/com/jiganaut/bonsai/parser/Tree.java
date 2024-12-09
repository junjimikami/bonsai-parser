package com.jiganaut.bonsai.parser;

import java.util.List;

/**
 *
 * @author Junji Mikami
 */
public interface Tree {

    /**
     * 
     * @author Junji Mikami
     *
     */
    public static enum Kind {
        TERMINAL,
        NON_TERMINAL;

        public boolean isTerminal() {
            return this == TERMINAL;
        }

        public boolean isNonTerminal() {
            return this == NON_TERMINAL;
        }
    }

    /**
     * 
     * @author Junji Mikami
     */
    public static interface Builder {
        public Tree.Builder setName(String name);

        public Tree.Builder setValue(String value);

        public Tree build();
    }

    /**
     * 
     * @return
     */
    public Kind getKind();

    public String getName();

    public String getValue();

    public List<? extends Tree> getSubTrees();

    /**
     * 
     * @param <R>
     * @param <P>
     * @param v
     * @param p
     * @return
     */
    public <R, P> R accept(TreeVisitor<R, P> v, P p);

    public default <R, P> R accept(TreeVisitor<R, P> v) {
        return accept(v, null);
    }

}

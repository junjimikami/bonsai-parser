package com.jiganaut.bonsai.parser;

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
     * @return
     */
    public Kind getKind();

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
    
    public default NonTerminal asNonTerminal() {
        return (NonTerminal) this;
    }
    public default Terminal asTerminal() {
        return (Terminal) this;
    }
}

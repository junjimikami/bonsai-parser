package com.jiganaut.bonsai.parser.spi;

import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.impl.DefaultTreeProvider;

public abstract class TreeProvider {

//    private static final ServiceLoader<TreeProvider> SERVICE_LOADER = ServiceLoader.load(TreeProvider.class);
    private static final TreeProvider DEFAULT_PROVIDER = new DefaultTreeProvider();

    public static TreeProvider load() {
        return DEFAULT_PROVIDER;
//        return SERVICE_LOADER.findFirst()
//                .orElseGet(() -> DEFAULT_PROVIDER);
    }

    public abstract NonTerminalNode.Builder createNonTerminalBuilder();

    public abstract TerminalNode.Builder createTerminalBuilder();

}
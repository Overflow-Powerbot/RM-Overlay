package org.overflow.api.utils.window;

import java.awt.*;

public interface WindowInfo extends Comparable<WindowInfo> {

    boolean valid();

    boolean visible();

    int depth();

    int pid();

    String title();

    Rectangle area();

    @Override
    default int compareTo(WindowInfo o) {
        return Integer.compare(depth(), o.depth());
    }


}



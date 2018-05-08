package org.overflow.api.utils.window.unix;

import org.overflow.api.utils.window.Desktop;
import org.overflow.api.utils.window.WindowInfo;

import java.util.LinkedList;
import java.util.List;

public class UnixDesktop implements Desktop {
    @Override
    public List<WindowInfo> list() {
        //TODO Unix implementation
        return new LinkedList<>();
    }
}

package org.overflow.api.utils.window.mac;

import org.overflow.api.utils.window.Desktop;
import org.overflow.api.utils.window.WindowInfo;

import java.util.LinkedList;
import java.util.List;

public class OSXDesktop implements Desktop {
    @Override
    public List<WindowInfo> list() {
        return new LinkedList<>();
    }
}

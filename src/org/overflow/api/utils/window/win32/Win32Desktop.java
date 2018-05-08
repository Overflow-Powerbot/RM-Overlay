package org.overflow.api.utils.window.win32;

import com.sun.jna.platform.win32.User32;
import org.overflow.api.utils.window.Desktop;
import org.overflow.api.utils.window.WindowInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Win32Desktop implements Desktop {
    @Override
    public List<WindowInfo> list() {
        List<WindowInfo> list = new LinkedList<>();
        AtomicInteger depth = new AtomicInteger(0);
        User32.INSTANCE.EnumWindows((hwnd, pointer) -> {
            WindowInfo info = new Win32WindowInfo(hwnd, depth.getAndIncrement());
            try {
                if (info.valid() && info.visible()) {
                    list.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }, null);
        return list;
    }
}

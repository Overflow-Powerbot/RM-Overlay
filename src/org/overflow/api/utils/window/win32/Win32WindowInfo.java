package org.overflow.api.utils.window.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;
import org.overflow.api.utils.window.WindowInfo;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Win32WindowInfo implements WindowInfo {

    private final WinDef.HWND hwnd;
    private final int depth;

    private final AtomicInteger pid = new AtomicInteger(-1);
    private final AtomicReference<String> title = new AtomicReference<>(null);
    private final AtomicReference<Rectangle> area = new AtomicReference<>(null);

    public Win32WindowInfo(WinDef.HWND hwnd, int depth) {
        this.hwnd = hwnd;
        this.depth = depth;
    }

    public final boolean valid() {
        return this.hwnd != null && User32.INSTANCE.IsWindow(hwnd);
    }

    public final boolean visible() {
        return User32.INSTANCE.IsWindowVisible(hwnd) && !title().equals("");
    }

    public final synchronized int pid() {
        if (pid.get() == -1) {
            IntByReference pid = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);
            this.pid.set(pid.getValue());
        }
        return pid.get();
    }

    @Override
    public final int depth() {
        return depth;
    }

    public final synchronized String title() {
        if (title.get() == null) {
            int length = User32.INSTANCE.GetWindowTextLength(hwnd) + 1;
            char[] buffer = new char[length];
            User32.INSTANCE.GetWindowText(hwnd, buffer, length);
            title.set(Native.toString(buffer));
        }
        return title.get();
    }


    public final synchronized Rectangle area() {
        if (area.get() == null) {
            WinDef.RECT window = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(hwnd, window);
            int fx = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CXFRAME);
            int fy = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CYFRAME);
            int my = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CYCAPTION);
            window.left += fx;
            window.right -= fx;
            window.top += fy + my;
            window.bottom -= fy;
            area.set(window.toRectangle());
        }
        return area.get();
    }

    @Override
    public String toString() {
        return "WindowInfo{" +
                "valid=" + valid() +
                ", visible=" + visible() +
                ", pid=" + pid() +
                ", title='" + title() + '\'' +
                ", area=" + area() +
                '}';
    }


}

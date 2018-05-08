package org.overflow.api.utils;

import org.overflow.api.utils.window.Desktop;
import org.overflow.api.utils.window.mac.OSXDesktop;
import org.overflow.api.utils.window.unix.UnixDesktop;
import org.overflow.api.utils.window.win32.Win32Desktop;

import java.util.LinkedList;

public enum OperatingSystem {
    WINDOWS("win") {
        @Override
        public Desktop desktop() {
            return new Win32Desktop();
        }
    },
    OSX("mac") {
        @Override
        public Desktop desktop() {
            return new OSXDesktop();
        }
    },
    UNIX("nix") {
        @Override
        public Desktop desktop() {
            return new UnixDesktop();
        }
    },
    OTHER("other") {
        @Override
        public Desktop desktop() {
            return LinkedList::new;
        }
    };

    private final String name;

    OperatingSystem(String name) {

        this.name = name;
    }

    public final boolean running() {
        return System.getProperty("os.name").toLowerCase().contains(name);
    }

    public abstract Desktop desktop();

    public static OperatingSystem get() {
        for (OperatingSystem system : values()) {
            if (system.running()) {
                return system;
            }
        }
        return OperatingSystem.OTHER;
    }

}

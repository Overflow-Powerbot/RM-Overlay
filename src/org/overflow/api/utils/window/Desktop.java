package org.overflow.api.utils.window;

import org.overflow.api.utils.OperatingSystem;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public interface Desktop {
    Supplier<Desktop> SUPPLIER = new Supplier<Desktop>() {

        private final AtomicReference<Desktop> ref = new AtomicReference<>();

        @Override
        public synchronized Desktop get() {
            if (ref.get() == null) {
                ref.set(OperatingSystem.get().desktop());
            }
            return ref.get();
        }
    };

    List<WindowInfo> list();
}

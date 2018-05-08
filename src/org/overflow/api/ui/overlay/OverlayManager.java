package org.overflow.api.ui.overlay;

import org.overflow.api.ui.overlay.swing.Overlay;
import org.overflow.api.utils.window.Desktop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class OverlayManager implements ActionListener {

    public static final OverlayManager INSTANCE = new OverlayManager();

    private final Timer update = new Timer(50, this);
    private final Map<Integer, Overlay> overlays = new HashMap<>();
    private final AtomicReference<Desktop> desktop = new AtomicReference<>();

    private OverlayManager() {
    }


    public final void initiate(Desktop desktop) {
        if (!this.update.isRunning() && desktop != null) {
            this.desktop.set(desktop);
            this.update.start();
        }
    }

    public final Overlay getOverlay(int pid) {
        return overlays.computeIfAbsent(pid, integer -> new Overlay());
    }

    public final void removeOverlay(int pid) {
        Overlay overlay = this.overlays.remove(pid);
        if (overlay != null) {
            overlay.visible(false);
            overlay.dispose();
        }
    }

    public final void shutdown() {
        this.update.stop();
        new LinkedList<>(this.overlays.keySet()).forEach(this::removeOverlay);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (overlays.size() > 0) {
            final AtomicBoolean visible = new AtomicBoolean(true);
            List<Overlay> temp = new LinkedList<>(this.overlays.values());
            desktop.get().list().forEach(wi -> {
                if(!wi.title().contains("RuneMate")) {
                    Overlay overlay = this.overlays.get(wi.pid());
                    if (overlay != null) {
                        temp.remove(overlay);
                        if (visible.compareAndSet(true, false)) {
                            overlay.visible(true);
                            overlay.bounds(wi.area());
                            overlay.repaint();
                        } else overlay.visible(false);
                    } else visible.compareAndSet(true, false);
                }
            });
            temp.forEach(overlay -> overlay.visible(false));
        }
    }
}

package org.overflow.api.ui.overlay.swing;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Overlay implements OverlayListener {

    private final JFrame frame;

    private final Object lock = new Object();
    private final AtomicBoolean shutdown = new AtomicBoolean(false);
    private final List<OverlayListener> listeners = new LinkedList<>();

    private boolean visible;
    private Rectangle location;

    public Overlay() {
        this.frame = new JFrame();
        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.frame.setFocusable(false);
        this.frame.setFocusableWindowState(false);
        this.frame.setUndecorated(true);
        this.frame.setBackground(new Color(0, 0, 0, 0));
        this.frame.setLayout(new BorderLayout());
        this.frame.add(new OverlayCanvas(this), BorderLayout.CENTER);
    }

    public final void visible(boolean visible) {
        this.visible = visible;
        this.frame.setVisible(visible);
        this.frame.setAlwaysOnTop(visible);
        this.frame.pack();
    }

    public final void bounds(Rectangle r) {
        if (this.location == null || !this.location.equals(r)) {
            this.location = r;
            this.frame.setLocation(r.x, r.y);
            this.frame.setPreferredSize(r.getSize());
            this.frame.pack();
        }
    }

    public final void repaint() {
        this.frame.repaint();
    }

    public final void dispose() {
        if(this.shutdown.compareAndSet(false,true)) {
            SwingUtilities.invokeLater(frame::dispose);
        }
    }

    public final boolean submit(OverlayListener listener) {
        synchronized (lock) {
            return this.listeners.add(listener);
        }
    }

    public final boolean revoke(OverlayListener listener) {
        synchronized (lock) {
            return this.listeners.remove(listener);
        }
    }

    @Override
    public void render(Graphics2D g) {
        synchronized (this.lock) {
            this.listeners.forEach(l -> l.render(g));
        }
    }

    @Override
    public String toString() {
        return "Overlay{" +
                "visible=" + visible +
                ", location=" + location +
                '}';
    }
}


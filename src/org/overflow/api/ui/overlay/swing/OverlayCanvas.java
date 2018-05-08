package org.overflow.api.ui.overlay.swing;

import javax.swing.*;
import java.awt.*;

public class OverlayCanvas extends JComponent {
    public static  boolean DEBUG_BOUNDS = true;

    private final Overlay overlay;

    public OverlayCanvas(Overlay overlay) {
        this.overlay = overlay;
        this.setOpaque(false);
        this.setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(DEBUG_BOUNDS) {
            g.setColor(Color.GREEN);
            g.drawRect(this.getX(), this.getY(), this.getWidth() - 1, this.getHeight() - 1);
            g.drawLine(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight());
            g.drawLine(this.getX(), this.getY() + this.getHeight(), this.getX() + this.getWidth(), this.getY());
        }
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            overlay.render(g2);
        } finally {
            g2.dispose();
        }
    }
}

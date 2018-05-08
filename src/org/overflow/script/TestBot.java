package org.overflow.script;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.LoopingBot;
import org.overflow.api.ui.overlay.OverlayManager;
import org.overflow.api.ui.overlay.swing.OverlayListener;
import org.overflow.api.utils.window.Desktop;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Tom (Overflow).
 */
public class TestBot extends LoopingBot implements OverlayListener {

    private final AtomicReference<List<Npc>> npcs = new AtomicReference<>();
    private final ExecutorService service = Executors.newSingleThreadExecutor();

    public TestBot() {
    }

    @Override
    public void onStart(String... arguments) {
        super.onStart(arguments);
        OverlayManager.INSTANCE.initiate(Desktop.SUPPLIER.get());
        OverlayManager.INSTANCE.getOverlay(Integer.valueOf(Environment.getRuneScapeProcessId())).submit(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.service.shutdown();
        OverlayManager.INSTANCE.removeOverlay(Integer.valueOf(Environment.getRuneScapeProcessId()));
    }

    @Override
    public void onLoop() {
        if (RuneScape.isLoggedIn()) {
            npcs.set(Npcs.getLoaded().asList());
        }
    }

    @Override
    public void render(Graphics2D g) {
        try {
            long l = System.currentTimeMillis();
            service.submit(() -> {
                if (npcs.get() != null) {
                    new LinkedList<>(npcs.get()).parallelStream().forEach(npc -> {
                        npc.getPosition().render(g);
                    });
                }
            }).get();
            System.out.println("Took: " + (System.currentTimeMillis() - l));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

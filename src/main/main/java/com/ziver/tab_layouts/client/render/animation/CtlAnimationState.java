package com.ziver.tab_layouts.client.render.animation;

public final class CtlAnimationState {
    private long startTimeMillis = -1L;
    private long pausedOffsetMillis = 0L;
    private boolean paused = false;

    public int getFrame(long nowMillis, int frames, int fps) {
        if (frames <= 1) {
            return 0;
        }

        if (fps <= 0) {
            return 0;
        }

        if (startTimeMillis == -1L) {
            startTimeMillis = nowMillis;
        }

        long effectiveTime = paused ? pausedOffsetMillis : nowMillis - startTimeMillis;
        long frameDurationMillis = Math.max(1L, 1000L / fps);

        return (int) ((effectiveTime / frameDurationMillis) % frames);
    }

    public void setPaused(boolean pause, long nowMillis) {
        if (startTimeMillis == -1L) {
            startTimeMillis = nowMillis;
        }

        if (pause && !paused) {
            pausedOffsetMillis = nowMillis - startTimeMillis;
            paused = true;
        } else if (!pause && paused) {
            startTimeMillis = nowMillis - pausedOffsetMillis;
            paused = false;
        }
    }

    public void reset() {
        startTimeMillis = -1L;
        pausedOffsetMillis = 0L;
        paused = false;
    }
}
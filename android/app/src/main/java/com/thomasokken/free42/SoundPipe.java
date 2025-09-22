/*****************************************************************************
 * Free42 -- an HP-42S calculator simulator
 * Copyright (C) 2004-2025  Thomas Okken
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/.
 *****************************************************************************/

package com.thomasokken.free42;

import android.media.SoundPool;

public class SoundPipe extends Thread {
    private SoundPool soundPool;
    private int soundId = -1;

    public SoundPipe(SoundPool soundPool) {
        this.soundPool = soundPool;
        start();
    }

    public void play(int id) {
        synchronized (this) {
            soundId = id;
            notify();
        }
    }

    public void run() {
        int nextId;
        while (!interrupted()) {
            try {
                synchronized (this) {
                    wait();
                    nextId = soundId;
                    soundId = -1;
                }
            } catch (InterruptedException e) {
                break;
            }
            if (nextId != -1) {
                soundPool.play(nextId, 1f, 1f, 0, 0, 1f);
                nextId = -1;
            }
        }
        soundPool.release();
    }
}

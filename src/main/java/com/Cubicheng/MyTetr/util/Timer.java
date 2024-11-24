package com.Cubicheng.MyTetr.util;

class Timer {
	public void restart() {
		pass_time = 0;
		shotted = false;
	}

	public void set_wait_time(float val) {
		wait_time = val;
	}

	public void set_one_shot(boolean flag) {
		one_shot = flag;
	}

	public void set_on_timeout(Function on_timeout) {
		this.on_timeout = on_timeout;
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
	}

	public void on_update(float delta) {
		if (paused) return;
		pass_time += delta;
		if (pass_time >= wait_time) {
			boolean can_shot = (!one_shot || (one_shot && !shotted));
			if (can_shot) {
				on_timeout.function();
				shotted = true;
			}
			pass_time = 0;
		}
	}

	public boolean get_shotted() {
		return shotted;
	}

	float pass_time = 0;
	float wait_time = 0;
	boolean paused = false;
	boolean shotted = false;
	boolean one_shot = false;
	Function on_timeout;
};
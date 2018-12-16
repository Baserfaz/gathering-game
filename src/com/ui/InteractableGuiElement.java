package com.ui;


public abstract class InteractableGuiElement extends GuiElement {

    public abstract void onHover();
    public abstract void onClick();
    public abstract void onUnfocus();

    private Runnable onClickRunnable, onHoverRunnable;

    public InteractableGuiElement(int x, int y, int width, int height,
                                  Runnable onClickRunnable, Runnable onHoverRunnable) {
        super(x, y, width, height);
        this.onClickRunnable = onClickRunnable;
        this.onHoverRunnable = onHoverRunnable;
    }

    public Runnable getOnClickRunnable() {
        return onClickRunnable;
    }
    public void setOnClickRunnable(Runnable onClickRunnable) {
        this.onClickRunnable = onClickRunnable;
    }
    public Runnable getOnHoverRunnable() {
        return onHoverRunnable;
    }
    public void setOnHoverRunnable(Runnable onHoverRunnable) {
        this.onHoverRunnable = onHoverRunnable;
    }
    public void setHovering(boolean b) { this.isHovering = b; }
    public boolean isHovering() { return this.isHovering; }
}

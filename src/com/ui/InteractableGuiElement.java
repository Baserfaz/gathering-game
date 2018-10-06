package com.ui;


import com.enumerations.InteractAction;

public abstract class InteractableGuiElement extends GuiElement {

    public abstract void onHover();
    public abstract void onClick();
    public abstract void onUnfocus();

    public InteractAction onClickAction = InteractAction.NONE;
    public InteractAction onHoverAction = InteractAction.NONE;

    private Runnable onClickRunnable = null;
    private Runnable onHoverRunnable = null;

    public InteractableGuiElement(int x, int y, int width, int height,
            InteractAction onClickAction, InteractAction onHoverAction) {
        super(x, y, width, height);
        
        this.onClickAction = onClickAction;
        this.onHoverAction = onHoverAction;
    }

    public InteractableGuiElement(int x, int y, int width, int height,
                                  Runnable onClickRunnable, Runnable onHoverRunnable) {
        super(x, y, width, height);

        this.onClickRunnable = onClickRunnable;
        this.onHoverRunnable = onHoverRunnable;

    }
    
    // ---- GETTERS & SETTERS ----
    public InteractAction getOnClickAction() { return onClickAction; }
    public void setOnClickAction(InteractAction onClickAction) { this.onClickAction = onClickAction; }
    public InteractAction getOnHoverAction() { return onHoverAction; }
    public void setOnHoverAction(InteractAction onHoverAction) { this.onHoverAction = onHoverAction; }

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

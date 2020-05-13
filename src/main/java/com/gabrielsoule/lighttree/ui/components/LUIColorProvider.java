package com.gabrielsoule.lighttree.ui.components;


import com.gabrielsoule.lighttree.LightTree;

public interface LUIColorProvider {

    public LUIColorProvider bgColor = () -> LightTree.getInstance().newUI.backgroundColor;
    public LUIColorProvider accentColor = () -> LightTree.getInstance().newUI.borderColor;


    public int get();
}

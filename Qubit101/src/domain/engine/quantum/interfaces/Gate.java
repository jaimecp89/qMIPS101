// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 05/10/2011 0:50:13
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Gate.java

package domain.engine.quantum.interfaces;

import domain.engine.quantum.QuantumState;

public interface Gate
{

    public abstract QuantumState operate(QuantumState quantumstate, int i);

    public abstract int getGateId();

    public abstract String getSymbol();
}

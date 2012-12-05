// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 05/10/2011 0:49:30
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   UnitaryGate.java

package domain.engine.quantum.interfaces;

import domain.engine.quantum.QuantumState;

// Referenced classes of package domain.engine.quantum.interfaces:
//            Gate

public interface UnitaryGate
    extends Gate
{

    public abstract QuantumState operate(QuantumState quantumstate, int i, int ai[]);
}
